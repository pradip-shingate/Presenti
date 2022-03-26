package com.mylozo.presenti.app.view

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
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
                if (value.startsWith("+91")) {
                    phone = value.substring(3, value.length)
                }
            }

            Thread {
                NetworkHelper().validateUser(
                    resources.getString(R.string.base_url) + "Business/GetBusinessIdByMobileNumber?MobileNumber=$phone",
                    this
                )
            }.start()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            resolveHint ->
                // Obtain the phone number from the result
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val credential = data.getParcelableExtra<Credential>(Credential.EXTRA_KEY)
                    phoneNumber = credential?.id
                    editText.setText(phoneNumber)
                    findViewById<Button>(R.id.button_continue).isEnabled = true
                } else {
                    showSnackBar(resources.getString(R.string.snackSimError))
                    findViewById<Button>(R.id.button_continue).isEnabled = false
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

    override fun onNetworkSuccess(o: Object?) {
        o?.let { it ->
            if (it is UserDetails) {
                if (!it?.isError && it?.data > 0) {
                    var phone = ""
                    phoneNumber?.let { value ->
                        if (value.startsWith("+91")) {
                            phone = value.substring(3, value.length)
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