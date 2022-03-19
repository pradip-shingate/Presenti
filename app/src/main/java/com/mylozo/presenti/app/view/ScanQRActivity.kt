package com.mylozo.presenti.app.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.mylozo.presenti.app.R
import com.mylozo.presenti.app.model.*
import com.mylozo.presenti.app.presenter.NetworkResponseListener
import java.io.IOException


class ScanQRActivity : AppCompatActivity(), NetworkResponseListener {

    val REQUEST_CAMERA_PERMISSION = 700;
    val REQUEST_LOCATION_PERMISSION = 701

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qractivity)
        initUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission()
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showAlertMessage(
                "Presenti",
                resources.getString(R.string.alertCamera)
            )
        } else if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            someActivityResultLauncher.launch(
                Intent(
                    this@ScanQRActivity,
                    MerchantScannerActivity::class.java
                )
            )

        } else if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showAlertMessage(
                "Presenti",
                resources.getString(R.string.alertLocation)
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private var someActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val str: String? = result.data?.getStringExtra("barcode_data")
        Log.d("Test", "Barcode kotlin: $str")

        str?.let {
            val id = Uri.parse(it).getQueryParameter("BusinessQRCodeId")
            if (!TextUtils.isEmpty(id) && TextUtils.isDigitsOnly(id) && Integer.parseInt(id) > 0) {
                runOnUiThread { findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE }
                NetworkHelper().validateUser(
                    "http://api.presenti.lo-yo.in/api/Business/GetBusinessIdByQRCode?BusinessQRCodeId=$id",
                    this
                )
            }
        }
    }


    private var markAttendanceActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val str = result.data?.getBooleanExtra("isSuccess", false)
        val type = result.data?.getStringExtra("type")

        if (str == true && type.equals("Log In"))
            showSnackBar(resources.getString(R.string.login) + " " + resources.getString(R.string.snackSuccessful))
        else if (str == true && type.equals("Log Out"))
            showSnackBar(resources.getString(R.string.logout) + " " + resources.getString(R.string.snackSuccessful))
    }

    private var localeActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        onConfigurationChanged(resources.configuration)
    }

    private fun showAlertMessage(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@ScanQRActivity)
        builder.setMessage(message).setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()

            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onNetworkSuccess(o: Object?) {
        o?.let {
            runOnUiThread {
                if (o is UserDetails && EmployeeRepository.employee.data?.businessId == o.data) {
                    markAttendanceActivityResultLauncher.launch(
                        Intent(
                            this@ScanQRActivity,
                            MarkAttendanceActivity::class.java
                        )
                    )
                } else {
                    showSnackBar(resources.getString(R.string.snackInvalidUser))
                }
                findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            }
        }
    }

    override fun onNetworkFailure(o: Object?) {
        runOnUiThread { findViewById<ProgressBar>(R.id.progress).visibility = View.GONE }
        showSnackBar(resources.getString(R.string.snackNetworkError))
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG)
        snackBar.anchorView = findViewById(R.id.user_name)
        snackBar.show()
    }

    private fun checkLocationPermission() {
        if (EmployeeRepository.business.data?.attendanceLogTypeId == 2) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        this@ScanQRActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    someActivityResultLauncher.launch(
                        Intent(
                            this@ScanQRActivity,
                            MerchantScannerActivity::class.java
                        )
                    )

                } else {
                    ActivityCompat.requestPermissions(
                        this@ScanQRActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            someActivityResultLauncher.launch(
                Intent(
                    this@ScanQRActivity,
                    MerchantScannerActivity::class.java
                )
            )
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_scan_qractivity)
        initUI()
    }

    private fun initUI() {
        findViewById<TextView>(R.id.company_name).text =
            EmployeeRepository.business.data?.businessName

        val qrImage = findViewById<ImageView>(R.id.barcode)
        qrImage.setOnClickListener {

            try {
                if (ActivityCompat.checkSelfPermission(
                        this@ScanQRActivity,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    checkLocationPermission()
                } else {
                    ActivityCompat.requestPermissions(
                        this@ScanQRActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA_PERMISSION
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val buttonLogOut = findViewById<ImageView>(R.id.log_out)
        buttonLogOut.setOnClickListener {
            EmployeePrefs.deleteAllDetails(this@ScanQRActivity)
            finish()
        }

        val buttonRemoteLogin = findViewById<Button>(R.id.remote)
        buttonRemoteLogin.isEnabled =
            EmployeeRepository.business.data?.isBusinessLocationDetect != 1
        buttonRemoteLogin.setOnClickListener {
            markAttendanceActivityResultLauncher.launch(
                Intent(
                    this@ScanQRActivity,
                    MarkAttendanceActivity::class.java
                )
            )
        }

        findViewById<TextView>(R.id.user_name).text =
            "Hi, " + EmployeeRepository.employee.data?.empName

        findViewById<ImageView>(R.id.language).setOnClickListener {
            localeActivityResultLauncher.launch(Intent(this, LanguageSelectorActivity::class.java))
        }
    }
}