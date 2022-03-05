package com.presenti.app.view

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.presenti.app.R
import java.io.IOException


class ScanQRActivity : AppCompatActivity() {

    val REQUEST_CAMERA_PERMISSION = 7;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qractivity)

        val qrImage = findViewById<ImageView>(R.id.barcode)
        qrImage.setOnClickListener {

            try {
                if (ActivityCompat.checkSelfPermission(
                        this@ScanQRActivity,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    someActivityResultLauncher.launch(
                        Intent(
                            this@ScanQRActivity,
                            MerchantScannerActivity::class.java
                        )
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        this@ScanQRActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        REQUEST_CAMERA_PERMISSION
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        val buttonRemoteLogin = findViewById<Button>(R.id.remote)
        buttonRemoteLogin.setOnClickListener {

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            someActivityResultLauncher.launch(
                Intent(
                    this@ScanQRActivity,
                    MerchantScannerActivity::class.java
                )
            )
        } else if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            showAlertMessage(
                "Presenti",
                "Camera permission needed in order to proceed.You can do it in system settings for Presenti app."
            )
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private var someActivityResultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        val str = result.data?.getStringExtra("barcode_data")
        Log.d("Test", "Barcode kotlin: " + str)
        if (!TextUtils.isEmpty(str))
            startActivity(Intent(this@ScanQRActivity, MarkAttendanceActivity::class.java))
    }

    private fun showAlertMessage(title: String, message: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@ScanQRActivity)
        builder.setMessage(message).setTitle(title)
            .setCancelable(false)
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                dialog.dismiss()

            })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

}