package com.presenti.app.view

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.presenti.app.R
import java.io.IOException


class ScanQRActivity : AppCompatActivity() {

    private var surfaceView: SurfaceView? = null
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    private val REQUEST_CAMERA_PERMISSION = 201

    //This class provides methods to play DTMF tones
    private var toneGen1: ToneGenerator? = null
    private var barcodeData: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qractivity)
        toneGen1 = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        surfaceView = findViewById(R.id.surface_view)
        initialiseDetectorsAndSources()
    }


    private fun initialiseDetectorsAndSources() {
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(400, 400)
            .setAutoFocusEnabled(true) //you should add this feature
            .build();

        surfaceView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            this@ScanQRActivity,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraSource.start(holder)
                    } else {
                        val permission = arrayOf(Manifest.permission.CAMERA)
                        ActivityCompat.requestPermissions(
                            this@ScanQRActivity,
                            permission,
                            REQUEST_CAMERA_PERMISSION
                        );
                    }

                } catch (e: IOException) {
                    e.printStackTrace();
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes: SparseArray<Barcode>? = detections?.detectedItems;
                if(barcodes?.size()!=0)
                {

                    val data = barcodes?.valueAt(0)?.displayValue
                    toneGen1?.startTone(ToneGenerator.TONE_CDMA_PIP, 150)
//                    if (barcodes?.valueAt(0).email != null) {
//                        barcodeText.removeCallbacks(null);
//                        barcodeData = barcodes.valueAt(0).email.address;
//                        barcodeText.setText(barcodeData);
//                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//                    } else {
//
//                        barcodeData = barcodes.valueAt(0).displayValue;
//                        barcodeText.setText(barcodeData);
//                        toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//
//                    }
                }
            }
        } )
    }

//    private void initialiseDetectorsAndSources1() {
//
//        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
//
//        barcodeDetector = new BarcodeDetector.Builder(this)
//            .setBarcodeFormats(Barcode.ALL_FORMATS)
//            .build();
//
//        cameraSource = new CameraSource.Builder(this, barcodeDetector)
//        .setRequestedPreviewSize(1920, 1080)
//            .setAutoFocusEnabled(true) //you should add this feature
//            .build();
//
//        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//            @Override
//            public void surfaceCreated(SurfaceHolder holder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                        cameraSource.start(surfaceView.getHolder());
//                    } else {
//                        ActivityCompat.requestPermissions(MainActivity.this, new
//                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//            @Override
//            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            }
//
//            @Override
//            public void surfaceDestroyed(SurfaceHolder holder) {
//                cameraSource.stop();
//            }
//        });
//
//
//        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
//            @Override
//            public void release() {
//                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void receiveDetections(Detector.Detections<Barcode> detections) {
//                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
//                if (barcodes.size() != 0) {
//
//
//                    barcodeText.post(new Runnable() {
//
//                        @Override
//                        public void run() {
//
//                            if (barcodes.valueAt(0).email != null) {
//                                barcodeText.removeCallbacks(null);
//                                barcodeData = barcodes.valueAt(0).email.address;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//                            } else {
//
//                                barcodeData = barcodes.valueAt(0).displayValue;
//                                barcodeText.setText(barcodeData);
//                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
//
//                            }
//                        }
//                    });
//
//                }
//            }
//        });
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        getSupportActionBar().hide();
//        cameraSource.release();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getSupportActionBar().hide();
//        initialiseDetectorsAndSources();
//    }
//
//}

}