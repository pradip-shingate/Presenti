package com.mylozo.presenti.app.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mylozo.presenti.app.R
import com.mylozo.presenti.app.model.EmployeePrefs
import com.mylozo.presenti.app.model.EmployeeRepository

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            if (!EmployeePrefs.getValid(this)) {
                startActivity(Intent(this@SplashScreenActivity, IntroActivity::class.java))
            } else {
                EmployeeRepository.employee = EmployeePrefs.getEmployeeDetails(this@SplashScreenActivity)
                EmployeeRepository.business = EmployeePrefs.getBusinessDetails(this@SplashScreenActivity)
                startActivity(Intent(this@SplashScreenActivity, ScanQRActivity::class.java))
            }
            finish()
        }, 1500)
    }
}