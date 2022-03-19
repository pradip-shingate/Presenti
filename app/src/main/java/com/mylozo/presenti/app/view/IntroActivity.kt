package com.mylozo.presenti.app.view

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mylozo.presenti.app.R
import com.mylozo.presenti.app.model.EmployeePrefs
import com.mylozo.presenti.app.model.EmployeeRepository
import com.mylozo.presenti.app.model.Utility
import java.util.*


class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_intro)

        val txtView = findViewById<TextView>(R.id.text_next)
        txtView.setOnClickListener {
            val employee = EmployeePrefs.getEmployeeDetails(this@IntroActivity)
            if (employee.data?.empAutoId!! > 0 && !TextUtils.isEmpty(employee.data?.empMobileNo)) {
                EmployeeRepository.employee = EmployeePrefs.getEmployeeDetails(this@IntroActivity)
                EmployeeRepository.business = EmployeePrefs.getBusinessDetails(this@IntroActivity)
                startActivity(Intent(this@IntroActivity, ScanQRActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                finish()
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Utility.loadLocale(this)
        setContentView(R.layout.activity_intro)
    }
}