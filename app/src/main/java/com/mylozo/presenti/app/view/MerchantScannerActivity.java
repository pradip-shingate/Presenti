package com.mylozo.presenti.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.mylozo.presenti.app.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Prakhar on 5/16/2016.
 */
public class MerchantScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout relativeLayout = new RelativeLayout(this);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 50, 30);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);


        Button switchCamera = new Button(this); //declare a button in layout for camera change option
        switchCamera.setText(getResources().getString(R.string.switchCamera));
        switchCamera.setLayoutParams(params);
        switchCamera.setBackgroundResource(R.drawable.button_style);
        switchCamera.setPadding(15, 0, 15, 0);
        relativeLayout.addView(switchCamera);
        final int i = getFrontCameraId();
        if (i == -1) {
            switchCamera.setVisibility(View.GONE);
        }


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        relativeLayout.addView(mScannerView);

        setContentView(relativeLayout);
        final int[] id = {0};
        switchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView.stopCamera();
                if (id[0] % 2 == 0) {
                    mScannerView.startCamera(i);
                } else {
                    mScannerView.startCamera();
                }
                id[0]++;
            }
        });

        mScannerView.setResultHandler(this);// Register ourselves as a handler for scan results.

        mScannerView.startCamera();         // Start camera


    }

    @SuppressLint("NewApi")
    int getFrontCameraId() {
        if (Build.VERSION.SDK_INT < 22) {
            Camera.CameraInfo ci = new Camera.CameraInfo();
            for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.getCameraInfo(i, ci);
                if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
            }
        } else {
            try {
                CameraManager cManager = (CameraManager) getApplicationContext()
                        .getSystemService(Context.CAMERA_SERVICE);
                String[] cameraId = cManager.getCameraIdList();
                for (int j = 0; j < cameraId.length; j++) {
                    CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId[j]);
                    int cOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (cOrientation == CameraCharacteristics.LENS_FACING_FRONT)
                        return Integer.parseInt(cameraId[j]);
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        return -1; // No front-facing camera found
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

        String txt = rawResult.getText();
        Log.d("barcode", "Barcode :" + txt);
        Intent intent = new Intent();
        intent.putExtra("barcode_data", txt);
        setResult(007, intent);
        finish();
    }
}
