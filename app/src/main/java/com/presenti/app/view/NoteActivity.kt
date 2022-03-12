package com.presenti.app.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.presenti.app.R
import com.presenti.app.model.EmployeeRepository
import com.presenti.app.model.NetworkHelper
import com.presenti.app.presenter.NetworkResponseListener
import org.json.JSONObject

class NoteActivity : AppCompatActivity(), NetworkResponseListener {
    var currentLatitude = 0.0
    var currentLongitude = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val logOut = findViewById<ImageView>(R.id.log_out)
        val language = findViewById<ImageView>(R.id.language)
        language.layoutParams = logOut.layoutParams
        logOut.visibility = View.GONE
        findViewById<Button>(R.id.submit).setOnClickListener {
            findViewById<Button>(R.id.submit).isEnabled=false
            findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE
            val customerName = findViewById<EditText>(R.id.PersonName).text.toString()
            val customerPhone = findViewById<EditText>(R.id.Phone).text.toString()
            var customerNote = findViewById<EditText>(R.id.MultiLine).text.toString()
            if (customerName.isEmpty() || customerPhone.isEmpty()) {
                showSnackBar("Please enter customer name and phone number.")
            } else {
                val json: JSONObject = JSONObject()
                json.put("CustomerName", customerName)
                    .put("CustomerMobileNumber", customerPhone)
                    .put("Notes", customerNote)
                    .put("BusinessId", EmployeeRepository.employee.data?.businessId)
                    .put("EmpLatitude", currentLatitude)
                    .put("EmpLongitude", currentLongitude)

                Thread {
                    NetworkHelper().insertNote(
                        "http://api.presenti.lo-yo.in/api/PresentiLog/InsertPresentiLogNote",
                        json.toString(), this
                    )
                }.start()
            }
        }
    }

    private fun showSnackBar(message: String) {
        runOnUiThread {
            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            findViewById<Button>(R.id.submit).isEnabled=true
            val snackBar = Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG)
            snackBar.show()
        }
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

    override fun onNetworkSuccess(o: Object?) {
        showSnackBar("Note updated.")
    }

    override fun onNetworkFailure(o: Object?) {
        showSnackBar("Unable to update note.Please check your internet connection.")
    }
}