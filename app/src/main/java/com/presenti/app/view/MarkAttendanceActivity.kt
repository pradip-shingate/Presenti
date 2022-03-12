package com.presenti.app.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.presenti.app.R
import com.presenti.app.model.EmployeeRepository
import com.presenti.app.model.NetworkHelper
import com.presenti.app.model.UserDetails
import com.presenti.app.model.UserPresentiDetail
import com.presenti.app.presenter.NetworkResponseListener
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MarkAttendanceActivity : AppCompatActivity(), NetworkResponseListener {

    val REQUEST_LOCATION_PERMISSION = 701
    var currentLatitude = 0.0
    var currentLongitude = 0.0
    var currentTime = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_attendance)
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
            currentTime = getFormattedDate() ?: ""
            val json: JSONObject = JSONObject()
            json.put("EmpAutoId", EmployeeRepository.employee.data?.empAutoId)
                .put("BusinessId", EmployeeRepository.employee.data?.businessId)
                .put("InTime", currentTime)
                .put("EmpLatitude", currentLatitude)
                .put("EmpLongitude", currentLongitude)
            NetworkHelper().insertInOutLogs(
                "http://api.presenti.lo-yo.in/api/PresentiLog/InsertPresentiInLog",
                json.toString(),
                this
            )
        }

        findViewById<Button>(R.id.logout).setOnClickListener{
            currentTime = getFormattedDate() ?: ""
            val json: JSONObject = JSONObject()
            json.put("EmpAutoId", EmployeeRepository.employee.data?.empAutoId)
                .put("BusinessId", EmployeeRepository.employee.data?.businessId)
                .put("OutTime", currentTime)
                .put("EmpLatitude", currentLatitude)
                .put("EmpLongitude", currentLongitude)
            NetworkHelper().insertInOutLogs(
                "http://api.presenti.lo-yo.in/api/PresentiLog/InsertPresentiOutLog",
                json.toString(),OutLog()
            )
        }

        try {
            if (ActivityCompat.checkSelfPermission(
                    this@MarkAttendanceActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                getLastKnownLocation()
                NetworkHelper().getUserPresentiDetails(
                    "http://api.presenti.lo-yo.in/api/PresentiLog/GetPresentiLogByEmpAutoId?empAutoId=873&empBusinessId=1",
                    this@MarkAttendanceActivity
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MarkAttendanceActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastKnownLocation()
            NetworkHelper().getUserPresentiDetails(
                "http://api.presenti.lo-yo.in/api/PresentiLog/GetPresentiLogByEmpAutoId?empAutoId=873&empBusinessId=1",
                this@MarkAttendanceActivity
            )
        } else if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showAlertMessage(
                "Presenti",
                "Location permission needed in order to proceed.You can do it in system settings for Presenti app."
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
                    "Last IN Time " + getFormattedDate(o.data?.inTime)
                findViewById<TextView>(R.id.last_out_time).text =
                    "Last OUT Time " + getFormattedDate(o.data?.outTime)
            }
        } else if (o is UserDetails && !o.isError) {
            runOnUiThread {
                findViewById<TextView>(R.id.last_in_time).text =
                    "Last IN Time " + getFormattedDate(currentTime)
            }
        }
    }

    override fun onNetworkFailure(o: Object?) {
        showSnackBar("Unable to fetch user details.Please check your internet connection.")
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

            val fmtOut = SimpleDateFormat("dd-MM-yyyy 'at' hh:mm:ss")
            return fmtOut.format(date)
        } catch (e: Exception) {
        }
        return null
    }

    private fun getFormattedDate(): String? {
        try {
            val fmtOut = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
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

    inner class OutLog:NetworkResponseListener
    {
        override fun onNetworkSuccess(o: Object?) {
            if (o is UserDetails && !o.isError) {
                runOnUiThread {
                    findViewById<TextView>(R.id.last_out_time).text =
                        "Last OUT Time " + getFormattedDate(currentTime)
                }
            }
        }

        override fun onNetworkFailure(o: Object?) {
            onNetworkFailure(o)
        }

    }
}