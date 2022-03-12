package com.presenti.app.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import com.presenti.app.R
import com.presenti.app.model.EmployeePrefs
import com.presenti.app.model.EmployeeRepository
import com.presenti.app.model.NetworkHelper
import com.presenti.app.model.UserDetails
import com.presenti.app.presenter.NetworkResponseListener
import java.io.IOException


class ScanQRActivity : AppCompatActivity(), NetworkResponseListener {

    val REQUEST_CAMERA_PERMISSION = 700;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qractivity)

        val qrImage = findViewById<ImageView>(R.id.barcode)
        qrImage.setOnClickListener {

            try {
                if (ActivityCompat.checkSelfPermission(
                        this@ScanQRActivity,
                        Manifest.permission.CAMERA
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
            EmployeePrefs.deleteEmployeeDetails(this@ScanQRActivity)
            finish()
        }

        val buttonRemoteLogin = findViewById<Button>(R.id.remote)
        buttonRemoteLogin.setOnClickListener {
            startActivity(Intent(this@ScanQRActivity, MarkAttendanceActivity::class.java))
        }

        findViewById<TextView>(R.id.user_name).text =
            "Hi, " + EmployeeRepository.employee.data?.empName
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            someActivityResultLauncher.launch(
                Intent(
                    this@ScanQRActivity,
                    MerchantScannerActivity::class.java
                )
            )
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showAlertMessage(
                "Presenti",
                "Camera permission needed in order to proceed.You can do it in system settings for Presenti app."
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
                NetworkHelper().validateUser(
                    "http://api.presenti.lo-yo.in/api/Business/GetBusinessIdByQRCode?BusinessQRCodeId=$id",
                    this
                )
            }
        }
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
            if (o is UserDetails && EmployeeRepository.employee.data?.businessId == o.data) {
                startActivity(Intent(this@ScanQRActivity, MarkAttendanceActivity::class.java))
            } else {
                showSnackBar("Invalid user.")
            }
        }
    }

    override fun onNetworkFailure(o: Object?) {
        showSnackBar("Network error.Please try again.")
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }
}