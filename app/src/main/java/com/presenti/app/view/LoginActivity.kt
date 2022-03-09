package com.presenti.app.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.material.snackbar.Snackbar
import com.presenti.app.R
import com.presenti.app.model.EmployeeDetails
import com.presenti.app.model.EmployeeRepository
import com.presenti.app.model.NetworkHelper
import com.presenti.app.model.UserDetails
import com.presenti.app.presenter.NetworkResponseListener


class LoginActivity : AppCompatActivity(), NetworkResponseListener {

    private val resolveHint = 700
    private lateinit var editText: EditText
    private lateinit var loginButton: Button
    private var phoneNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            NetworkHelper().validateUser(
                resources.getString(R.string.base_url) + "Business/GetBusinessIdByMobileNumber?MobileNumber=$phoneNumber",
                this
            )
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
        o?.let {
            if (it is UserDetails) {
                if (!it?.isError) {
                    NetworkHelper().getUserDetails(
                        "http://api.presenti.lo-yo.in/api/Employee/GetEmployeeDetailByMobNo?MobileNumber=9766934468&BusinessId=1",
                        this
                    )
                } else {
                    showSnackBar("Mobile number may not be registered with us. Please contact admin.")
                }

            } else if (it is EmployeeDetails) {
                runOnUiThread {
                    if (!it?.isError) {
                        EmployeeRepository.employee = it
                        startActivity(Intent(this, ScanQRActivity::class.java))
                        finish()
                    } else {
                        showSnackBar("Invalid employee. Please contact admin.")
                    }
                }
            }
        }
    }

    override fun onNetworkFailure(o: Object?) {
        runOnUiThread {
            showSnackBar("Network error.Please check your internet connection and try again.")
        }
    }

    private fun showSnackBar(message: String) {
        val snackBar = Snackbar.make(findViewById(R.id.rootView), message, Snackbar.LENGTH_LONG)
        snackBar.show()
    }

}