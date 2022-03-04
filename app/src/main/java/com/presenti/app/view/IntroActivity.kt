package com.presenti.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.presenti.app.R
import okhttp3.OkHttpClient

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val txtView=findViewById<TextView>(R.id.text_next)
        txtView.setOnClickListener {
            startActivity(Intent(this@IntroActivity, LoginActivity::class.java))
            finish()
        }
    }
}