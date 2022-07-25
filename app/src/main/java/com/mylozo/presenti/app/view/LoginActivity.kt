package com.mylozo.presenti.app.view

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.material.snackbar.Snackbar
import com.mylozo.presenti.app.R
import com.mylozo.presenti.app.model.*
import com.mylozo.presenti.app.presenter.NetworkResponseListener


class LoginActivity : AppCompatActivity(), NetworkResponseListener {

    private val resolveHint = 700
    private lateinit var editText: EditText
    private lateinit var loginButton: Button
    private var phoneNumber: String? = null
    private val REQUEST_PHONE_STATE_PERMISSION = 1200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_login)

        editText = findViewById(R.id.editTextPhone)
        editText.showSoftInputOnFocus = false
        editText.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> getPhoneNumbers()
            }
            v?.onTouchEvent(event) ?: true
        }

        loginButton = findViewById(R.id.button_continue)
        loginButton.setOnClickListener {
            findViewById<Button>(R.id.button_continue).isEnabled = false
            findViewById<ProgressBar>(R.id.progress).visibility = View.VISIBLE
            var phone = ""
            phoneNumber?.let { value ->
                phone = if (value.startsWith("+91")) {
                    value.substring(3, value.length)
                } else if (value.length == 12 && value.startsWith("91")) {
                    value.substring(2, value.length)
                } else if (value.length == 11 && value.startsWith("0")) {
                    value.substring(1, value.length)
                } else {
                    value
                }
            }

            Thread {
                NetworkHelper().validateUser(
                    resources.getString(R.string.base_url) + "Business/GetBusinessIdByMobileNumber?MobileNumber=$phone",
                    this
                )
            }.start()

        }

        val contactUs = findViewById<TextView>(R.id.contact_us)
        contactUs.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            resolveHint ->
                // Obtain the phone number from the result
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val credential = data.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                    phoneNumber = credential?.id
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        editText.setText(phoneNumber)
                        findViewById<Button>(R.id.button_continue).isEnabled = true
                    } else {
                        getPhoneNumberByVoiceMail()
                    }
                } else {
                    getPhoneNumberByVoiceMail()
                }
        }
    }

    private fun getPhoneNumbers() {

        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .build()
        val credentialsClient = Credentials.getClient(this)
        val intent = credentialsClient.getHintPickerIntent(hintRequest)
        startIntentSenderForResult(
            intent.intentSender,
            resolveHint,
            null, 0, 0, 0
        )
    }

    private fun getPhoneNumberByVoiceMail() {
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                telephonyManager.line1Number?.let {
                    phoneNumber = it
                }
                if (!TextUtils.isEmpty(phoneNumber)) {
                    editText.setText(phoneNumber)
                    findViewById<Button>(R.id.button_continue).isEnabled = true
                } else {
                    telephonyManager.voiceMailNumber?.let {
                        phoneNumber = it
                    }
                    if (!TextUtils.isEmpty(phoneNumber)) {
                        editText.setText(phoneNumber)
                        findViewById<Button>(R.id.button_continue).isEnabled = true
                    } else {
                        showSnackBar(resources.getString(R.string.snackSimError))
                        findViewById<Button>(R.id.button_continue).isEnabled = false
                    }
                }
            } else {
                showAlertMessageForPermissions(
                    "Presenti",
                    "In order to read phone number we will need read phone state permission."
                );
            }
        } catch (e: Exception) {
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PHONE_STATE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getPhoneNumberByVoiceMail();
        } else if (requestCode == REQUEST_PHONE_STATE_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            findViewById<Button>(R.id.button_continue).isEnabled = false
            showAlertMessage(
                "Presenti",
                "In order to read phone number we will need read phone state permission.Please go in settings and grant the permission for phone and restart the app."
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun showAlertMessageForPermissions(
        title: String,
        message: String
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
        builder.setMessage(message).setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()


                ActivityCompat.requestPermissions(
                    this@LoginActivity,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    REQUEST_PHONE_STATE_PERMISSION
                )
            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun showAlertMessage(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@LoginActivity)
        builder.setMessage(message).setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()

            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    override fun onNetworkSuccess(o: Object?) {
        o?.let { it ->
            if (it is UserDetails) {
                if (!it?.isError && it?.data > 0) {
                    var phone = ""
                    phoneNumber?.let { value ->
                        phone = if (value.startsWith("+91")) {
                            value.substring(3, value.length)
                        } else if (value.length == 12 && value.startsWith("91")) {
                            value.substring(2, value.length)
                        } else if (value.length == 11 && value.startsWith("0")) {
                            value.substring(1, value.length)
                        } else {
                            value
                        }
                    }

                    NetworkHelper().getUserDetails(
                        "http://api.presenti.lo-yo.in/api/Employee/GetEmployeeDetailByMobNo?MobileNumber=$phone&BusinessId=${it?.data}",
                        this
                    )
                } else {
                    showSnackBar(resources.getString(R.string.snackMobileError))
                }

            } else if (it is EmployeeDetails) {

                if (!it?.isError) {
                    EmployeeRepository.employee = it
                    EmployeePrefs.saveEmployeeDetails(this@LoginActivity, it)
                    NetworkHelper().getBusinessDetails(
                        "http://api.presenti.lo-yo.in/api/Business/GetBusinessData?BusinessId=${it.data.businessId}",
                        this@LoginActivity
                    )
                } else {
                    showSnackBar(resources.getString(R.string.snackInvalidEmployee))
                }

            } else if (it is BusinessDetails) {

                if (!it?.isError) {
                    EmployeeRepository.business = it
                    EmployeePrefs.saveBusinessDetails(this@LoginActivity, it)
                    EmployeePrefs.setValid(this@LoginActivity, true)
                    runOnUiThread {
                        findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
                        startActivity(Intent(this, ScanQRActivity::class.java))
                        finish()
                    }
                } else {
                    showSnackBar(resources.getString(R.string.snackInvalidBusiness))
                }

            }
        }
    }

    override fun onNetworkFailure(o: Object?) {
        runOnUiThread {
            findViewById<Button>(R.id.button_continue).isEnabled = true
            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            showSnackBar(resources.getString(R.string.snackNetworkError))
        }
    }

    private fun showSnackBar(message: String) {
        runOnUiThread {
            findViewById<Button>(R.id.button_continue).isEnabled = true
            findViewById<ProgressBar>(R.id.progress).visibility = View.GONE
            val snackBar = Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG)
            snackBar.show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_intro)
    }

}