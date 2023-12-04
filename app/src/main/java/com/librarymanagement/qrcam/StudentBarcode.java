package com.librarymanagement.qrcam;

import android.database.Cursor;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
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
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StudentBarcode extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private BarcodeDetector barcodeDetector;
    Camera cam = null;
    private CameraSource cameraSource;
    DatabaseHelper databaseHelper;
    DataOutputStream dos;
    Switch flash;
    public String intentData = BuildConfig.FLAVOR;
    Boolean isflashon = Boolean.valueOf(false);
    String msg;
    Socket s;
    Button send;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcode_student);
        initViews();
    }

    private void initViews() {
        this.txtBarcodeValue = (TextView) findViewById(R.id.nobardet);
        this.surfaceView = (SurfaceView) findViewById(R.id.surface);
        this.send = (Button) findViewById(R.id.sendmsg);
        this.databaseHelper = new DatabaseHelper(this);
    }

    public void send(View v) {
        JavaConnect sm = new JavaConnect();
        String msg = this.intentData;
        Cursor res = this.databaseHelper.readIP();
        StringBuffer stringBuffer = new StringBuffer();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                stringBuffer.append(res.getString(0));
            }
            String ip = stringBuffer.toString();
            sm.execute(new String[]{msg, ip});
        }
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Barcode scanner started!!!", Toast.LENGTH_SHORT).show();
        this.barcodeDetector = new Builder(this).setBarcodeFormats(0).build();
        this.cameraSource = new CameraSource.Builder(this, this.barcodeDetector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();
        this.surfaceView.getHolder().addCallback(new Callback() {
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(StudentBarcode.this, "android.permission.CAMERA") == 0) {
                        StudentBarcode.this.cameraSource.start(StudentBarcode.this.surfaceView.getHolder());
                        return;
                    }
                    ActivityCompat.requestPermissions(StudentBarcode.this, new String[]{"android.permission.CAMERA"}, StudentBarcode.REQUEST_CAMERA_PERMISSION);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            public void surfaceDestroyed(SurfaceHolder holder) {
                StudentBarcode.this.cameraSource.stop();
            }
        });
        this.barcodeDetector.setProcessor(new Processor<Barcode>() {
            public void release() {
                Toast.makeText(StudentBarcode.this.getApplicationContext(), "Barcode Scanner stopped!!!", Toast.LENGTH_SHORT).show();
            }

            public void receiveDetections(Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    StudentBarcode.this.txtBarcodeValue.post(new Runnable() {
                        public void run() {
                            StudentBarcode.this.intentData = ((Barcode) barcodes.valueAt(0)).displayValue;
                            if (StudentBarcode.this.intentData.length() == 7) {
                                StudentBarcode.this.txtBarcodeValue.setText(StudentBarcode.this.intentData);
                            } else {
                                StudentBarcode.this.txtBarcodeValue.setText("No Barcode Detected");
                            }
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
