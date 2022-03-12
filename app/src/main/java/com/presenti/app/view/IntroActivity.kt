package com.presenti.app.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.presenti.app.R
import com.presenti.app.model.EmployeePrefs
import com.presenti.app.model.EmployeeRepository

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val txtView = findViewById<TextView>(R.id.text_next)
        txtView.setOnClickListener {
            val employee = EmployeePrefs.getEmployeeDetails(this@IntroActivity)
            if (employee.data?.empAutoId!! > 0 && !TextUtils.isEmpty(employee.data?.empMobileNo)) {
                EmployeeRepository.employee=EmployeePrefs.getEmployeeDetails(this@IntroActivity)
                startActivity(Intent(this@IntroActivity, ScanQRActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
                finish()
            }
        }
    }
}