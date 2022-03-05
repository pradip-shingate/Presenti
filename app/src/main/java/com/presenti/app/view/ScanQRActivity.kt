package com.presenti.app.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.presenti.app.R
import com.presenti.app.barcodescanner.MerchantScannerActivity


class ScanQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qractivity)

        val qrImage = findViewById<ImageView>(R.id.barcode)
        qrImage.setOnClickListener {
            someActivityResultLauncher.launch(
                Intent(
                    this@ScanQRActivity,
                    MerchantScannerActivity::class.java
                )
            )
        }

        val buttonRemoteLogin = findViewById<Button>(R.id.remote)
        buttonRemoteLogin.setOnClickListener {

        }
    }

    private var someActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val str = result.data?.getStringExtra("barcode_data")
        Log.d("Test", "Barcode kotlin: " + str)
        if (!TextUtils.isEmpty(str))
            startActivity(Intent(this@ScanQRActivity, MarkAttendanceActivity::class.java))
    }

}