package com.presenti.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.presenti.app.R

class MarkAttendanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_attendance)
        val note=findViewById<Button>(R.id.note)
        note.setOnClickListener{
            startActivity(Intent(this@MarkAttendanceActivity,NoteActivity::class.java))
        }

        val logOut=findViewById<ImageView>(R.id.log_out)
        val language=findViewById<ImageView>(R.id.language)
        language.layoutParams=logOut.layoutParams
        logOut.visibility= View.GONE
    }
}