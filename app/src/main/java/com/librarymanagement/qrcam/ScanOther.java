package com.librarymanagement.qrcam;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector.Detections;
import com.google.android.gms.vision.Detector.Processor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.barcode.BarcodeDetector.Builder;
import java.io.IOException;
import android.content.ClipboardManager;

public class ScanOther extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    Switch flash;
    String intentData = BuildConfig.FLAVOR;
    Button launchUrl;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;

    /* Access modifiers changed, original: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_scan);
        initViews();
    }

    private void initViews() {
        this.txtBarcodeValue = (TextView) findViewById(R.id.nobardet);
        this.surfaceView = (SurfaceView) findViewById(R.id.surface);
        this.launchUrl = (Button) findViewById(R.id.cpytxt);
        this.launchUrl.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String txttocpy = intentData.toString();
               clipboardManager.setText(txttocpy);
                if(!"".equals(intentData))
                {
                    Toast.makeText(getApplicationContext(), "Text Copied To Clipboard", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Nothing to copy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Barcode scanner started!!!", Toast.LENGTH_SHORT).show();
        this.barcodeDetector = new Builder(this).setBarcodeFormats(0).build();
        this.cameraSource = new CameraSource.Builder(this, this.barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();
        this.surfaceView.getHolder().addCallback(new Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScanOther.this, "android.permission.CAMERA") == 0) {
                        ScanOther.this.cameraSource.start(ScanOther.this.surfaceView.getHolder());
                        return;
                    }
                    ActivityCompat.requestPermissions(ScanOther.this, new String[]{"android.permission.CAMERA"}, ScanOther.REQUEST_CAMERA_PERMISSION);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                ScanOther.this.cameraSource.stop();
            }
        });
        this.barcodeDetector.setProcessor(new Processor<Barcode>() {
            public void release() {
                Toast.makeText(ScanOther.this.getApplicationContext(), "Barcode Scanner stopped!!!", Toast.LENGTH_SHORT).show();
            }

            public void receiveDetections(Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    ScanOther.this.txtBarcodeValue.post(new Runnable() {
                        public void run() {
                            ScanOther.this.intentData = ((Barcode) barcodes.valueAt(0)).displayValue;
                            ScanOther.this.txtBarcodeValue.setText(ScanOther.this.intentData);
                        }
                    });
                }
            }
        });
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        super.onPause();
        this.cameraSource.release();
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
