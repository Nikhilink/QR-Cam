package com.librarymanagement.qrcam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnScanforstudent;
    Button btnScanforbook;
    Button config;
    Button otherscan;

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnScanforstudent = (Button) findViewById(R.id.scanforstudent);
        this.btnScanforbook = (Button) findViewById(R.id.scanforbook);
        this.otherscan = (Button) findViewById(R.id.scanothercodes);
        this.config = (Button) findViewById(R.id.configure);
        this.config.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, Configure.class));
            }
        });
        this.btnScanforstudent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, StudentBarcode.class));
            }
        });
        this.btnScanforbook.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, BookBarcode.class));
            }
        });
        this.otherscan.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,ScanOther.class));
            }
        });
    }
}
