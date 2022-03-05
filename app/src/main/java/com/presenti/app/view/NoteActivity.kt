package com.presenti.app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.presenti.app.R

class NoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)
        val logOut=findViewById<ImageView>(R.id.log_out)
        val language=findViewById<ImageView>(R.id.language)
        language.layoutParams=logOut.layoutParams
        logOut.visibility= View.GONE
    }
}