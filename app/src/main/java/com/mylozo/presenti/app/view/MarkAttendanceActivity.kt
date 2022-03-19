package com.mylozo.presenti.app.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.mylozo.presenti.app.R
import com.mylozo.presenti.app.model.*
import com.mylozo.presenti.app.presenter.NetworkResponseListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class MarkAttendanceActivity : AppCompatActivity(), NetworkResponseListener {

    var currentLatitude = 0.0
    var currentLongitude = 0.0
    var currentTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_mark_attendance)
        initUI()
    }

    private fun showAlertMessage(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MarkAttendanceActivity)
        builder.setMessage(message).setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()

            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onNetworkSuccess(o: Object?) {
        if (o is UserPresentiDetail && !o.isError) {
            runOnUiThread {
                findViewById<TextView>(R.id.last_in_time).text =
                    resources.getString(R.string.lastInTime)+" " + getFormattedDate(o.data?.inTime)
                findViewById<TextView>(R.id.last_out_time).text =
                    resources.getString(R.string.lastOutTime)+" " + getFormattedDate(o.data?.outTime)
            }
        } else if (o is UserDetails && !o.isError) {
            runOnUiThread {
                findViewById<TextView>(R.id.last_in_time).text =
                    resources.getString(R.string.lastInTime)+" " + getFormattedDate(currentTime)

                val intent=Intent()
                intent.putExtra("isSuccess",true)
                intent.putExtra("type","Log In")
                setResult(7,intent)
                onBackPressed()
            }
        }

        runOnUiThread {
            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
        }
    }

    override fun onNetworkFailure(o: Object?) {
        runOnUiThread {
            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
        }
        showSnackBar(resources.getString(R.string.snackNetworkError))
    }

    private fun showSnackBar(message: String) {
        runOnUiThread {
            val snackBar = Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG)
            snackBar.show()
        }
    }

    private fun getFormattedDate(str: String?): String? {
        try {
            val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
            val date: Date = fmt.parse(str)

            val fmtOut = SimpleDateFormat("dd-MM-yyyy 'at' hh:mm:ss aaa", Locale.ENGLISH)
            return fmtOut.format(date)
        } catch (e: Exception) {
        }
        return null
    }

    private fun getFormattedDate(): String? {
        try {
            val fmtOut = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
            return fmtOut.format(Date(System.currentTimeMillis()))
        } catch (e: Exception) {
        }
        return ""
    }

    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation() {
        if (isPermissionGranted()) {
            try {
                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            currentLatitude = location.latitude
                            currentLongitude = location.longitude
                        }
                    }
            } catch (e: Exception) {
                currentLatitude = 0.0
                currentLongitude = 0.0
            }
        }
    }

    inner class OutLog : NetworkResponseListener {
        override fun onNetworkSuccess(o: Object?) {
            if (o is UserDetails && !o.isError) {
                runOnUiThread {
                    findViewById<TextView>(R.id.last_out_time).text =
                        resources.getString(R.string.lastOutTime)+" " + getFormattedDate(currentTime)
                }
            }

            runOnUiThread {
                findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
                val intent=Intent()
                intent.putExtra("isSuccess",true)
                intent.putExtra("type","Log Out")
                setResult(7,intent)
                onBackPressed()
            }
        }

        override fun onNetworkFailure(o: Object?) {
            this@MarkAttendanceActivity.onNetworkFailure(o)
        }

    }

    private var localeActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        onConfigurationChanged(resources.configuration)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_mark_attendance)
        initUI()
    }

    @SuppressLint("MissingPermission")
    private fun doGeofence(isLogin: Boolean) {
        if (isPermissionGranted()) {
            try {
                val fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(this@MarkAttendanceActivity)
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        // Got last known location. In some rare situations this can be null.

                        val businessLocation = Location(LocationManager.GPS_PROVIDER)
                        businessLocation.latitude =
                            EmployeeRepository.business.data?.businessLatitude!!
                        businessLocation.longitude =
                            EmployeeRepository.business.data?.businessLongitude!!

                        location?.let {
                            val distance = businessLocation.distanceTo(it)
                            if (distance <= EmployeeRepository.business.data?.locationRangeLimit!!) {
                                if (isLogin) {
                                    doLogin()
                                } else {
                                    doLogOut()
                                }
                            } else {
                                showSnackBar(resources.getString(R.string.snackOutOFBusiness))
                            }
                        } ?: showSnackBar(resources.getString(R.string.snackLocationError))
                    }
                    .addOnFailureListener {
                        showSnackBar(resources.getString(R.string.snackLocationError))
                    }
            } catch (e: Exception) {
                showSnackBar(resources.getString(R.string.snackdistanceError))
            }
        }
    }

    private fun doLogin() {
        currentTime = getFormattedDate() ?: ""
        val json: JSONObject = JSONObject()
        json.put(
            "EmpAutoId",
            EmployeeRepository.employee.data?.empAutoId
        )
            .put(
                "BusinessId",
                EmployeeRepository.employee.data?.businessId
            )
            .put("InTime", currentTime)
            .put("EmpLatitude", currentLatitude)
            .put("EmpLongitude", currentLongitude)

        findViewById<ProgressBar>(R.id.progress).visibility =
            View.VISIBLE



        Thread {
            NetworkHelper().insertInOutLogs(
                "http://api.presenti.lo-yo.in/api/PresentiLog/InsertPresentiInLog",
                json.toString(),
                this
            )
        }.start()
    }

    private fun doLogOut() {
        currentTime = getFormattedDate() ?: ""
        val json: JSONObject = JSONObject()
        json.put(
            "EmpAutoId",
            EmployeeRepository.employee.data?.empAutoId
        )
            .put(
                "BusinessId",
                EmployeeRepository.employee.data?.businessId
            )
            .put("OutTime", currentTime)
            .put("EmpLatitude", currentLatitude)
            .put("EmpLongitude", currentLongitude)

        findViewById<ProgressBar>(R.id.progress).visibility =
            View.VISIBLE

        Thread {
            NetworkHelper().insertInOutLogs(
                "http://api.presenti.lo-yo.in/api/PresentiLog/InsertPresentiOutLog",
                json.toString(), OutLog()
            )
        }.start()
    }

    private fun initUI()
    {
        findViewById<TextView>(R.id.company_name).text =
            EmployeeRepository.business.data?.businessName

        val note = findViewById<Button>(R.id.note)
        note.setOnClickListener {
            startActivity(Intent(this@MarkAttendanceActivity, NoteActivity::class.java))
        }

        val logOut = findViewById<ImageView>(R.id.log_out)
        val language = findViewById<ImageView>(R.id.language)
        language.layoutParams = logOut.layoutParams
        logOut.visibility = View.GONE

        findViewById<TextView>(R.id.user_name).text =
            "Hi, " + EmployeeRepository.employee.data?.empName

        findViewById<Button>(R.id.login).setOnClickListener {
            if (EmployeeRepository.business.data?.isBusinessLocationDetect == 1)
                doGeofence(true)
            else
                doLogin()
        }

        findViewById<Button>(R.id.logout).setOnClickListener {
            if (EmployeeRepository.business.data?.isBusinessLocationDetect == 1)
                doGeofence(false)
            else
                doLogOut()
        }
        getLastKnownLocation()
        findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE
        Thread {
            NetworkHelper().getUserPresentiDetails(
                "http://api.presenti.lo-yo.in/api/PresentiLog/GetPresentiLogByEmpAutoId?empAutoId=${EmployeeRepository.employee.data.empAutoId}&empBusinessId=${EmployeeRepository.employee.data.businessId}",
                this@MarkAttendanceActivity
            )
        }.start()

        findViewById<ImageView>(R.id.language).setOnClickListener{
            localeActivityResultLauncher.launch(Intent(this,LanguageSelectorActivity::class.java))
        }
    }
}