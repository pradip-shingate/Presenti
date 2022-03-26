package com.mylozo.presenti.app.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.mylozo.presenti.app.R
import com.mylozo.presenti.app.model.EmployeeRepository
import com.mylozo.presenti.app.model.NetworkHelper
import com.mylozo.presenti.app.model.Utility
import com.mylozo.presenti.app.presenter.NetworkResponseListener
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity(), NetworkResponseListener {
    var currentLatitude = 0.0
    var currentLongitude = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_note)
        initUI()
    }

    private fun showSnackBar(message: String) {
        runOnUiThread {
            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            findViewById<Button>(R.id.submit).isEnabled = true
            val snackBar = Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG)
            snackBar.anchorView = findViewById(R.id.phone)
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
//        showSnackBar(resources.getString(R.string.snackNoteUpdate))

        runOnUiThread {
            val intent = Intent()
            intent.putExtra("noteUpdated", true)
            setResult(480, intent)
            finish()
        }
    }

    override fun onNetworkFailure(o: Object?) {
        showSnackBar(resources.getString(R.string.snackNetworkError))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_note)
        initUI()
    }

    private var localeActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        onConfigurationChanged(resources.configuration)
    }

    private fun initUI() {
        val logOut = findViewById<ImageView>(R.id.log_out)
        val language = findViewById<ImageView>(R.id.language)
        language.layoutParams = logOut.layoutParams
        logOut.visibility = View.GONE
        findViewById<Button>(R.id.submit).setOnClickListener {
            findViewById<Button>(R.id.submit).isEnabled = false
            findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE
            val customerName = findViewById<EditText>(R.id.PersonName).text.toString()
            val customerPhone = findViewById<EditText>(R.id.phone).text.toString()
            var customerNote = findViewById<EditText>(R.id.MultiLine).text.toString()
            if (customerName.isEmpty() || customerPhone.isEmpty()) {
                showSnackBar(resources.getString(R.string.snackCustomerName))
            } else if (customerPhone.length < 10) {
                showSnackBar(resources.getString(R.string.snackCustomerphone))
            } else {
                val json: JSONObject = JSONObject()
                json.put("CustomerName", customerName)
                    .put("CustomerMobileNumber", customerPhone)
                    .put("Notes", customerNote)
                    .put("BusinessId", EmployeeRepository.employee.data?.businessId)
                    .put("CreatedBy", EmployeeRepository.employee.data?.empMobileNo)
                    .put("CreatedOnStr",getFormattedDate() )
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

        findViewById<ImageView>(R.id.language).setOnClickListener {
            localeActivityResultLauncher.launch(Intent(this, LanguageSelectorActivity::class.java))
        }
    }

    private fun getFormattedDate(): String? {
        try {
            val fmtOut = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH)
            return fmtOut.format(Date(System.currentTimeMillis()))
        } catch (e: Exception) {
        }
        return ""
    }
}