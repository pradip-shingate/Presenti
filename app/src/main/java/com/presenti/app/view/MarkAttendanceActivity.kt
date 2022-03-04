package com.presenti.app.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.presenti.app.R

class MarkAttendanceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mark_attendance)
        val note=findViewById<Button>(R.id.note)
        note.setOnClickListener{
            startActivity(Intent(this@MarkAttendanceActivity,NoteActivity::class.java))
        }
    }
}