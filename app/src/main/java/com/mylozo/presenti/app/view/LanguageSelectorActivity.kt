package com.mylozo.presenti.app.view

import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.mylozo.presenti.app.R
import com.mylozo.presenti.app.model.EmployeePrefs

class LanguageSelectorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selector)
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        when (EmployeePrefs.getLanguage(this)) {
            "en" -> radioGroup.check(R.id.english)
            "mr" -> radioGroup.check(R.id.marathi)
            "hi" -> radioGroup.check(R.id.hindi)
            else -> {
                radioGroup.check(R.id.english)
            }
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.english -> EmployeePrefs.setLanguage(this, "en")
                R.id.marathi -> EmployeePrefs.setLanguage(this, "mr")
                R.id.hindi -> EmployeePrefs.setLanguage(this, "hi")
            }
        }
    }
}