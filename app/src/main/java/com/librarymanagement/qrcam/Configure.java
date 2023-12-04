package com.librarymanagement.qrcam;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Configure extends AppCompatActivity  {
    String IP;
    Button config;
    DatabaseHelper databaseHelper;
    EditText ip;
    boolean zalaka;

    /* Access modifiers changed, original: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipconfigure);
        initViews();
    }

    public void initViews() {
        this.databaseHelper = new DatabaseHelper(this);
        this.config = (Button) findViewById(R.id.ipaddbtn);
        this.ip = (EditText) findViewById(R.id.ipadd);
        this.config.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Configure.this.clicked();
            }
        });
    }

    public void clicked() {
        this.IP = this.ip.getText().toString();
        Cursor res = this.databaseHelper.checkifdata();
        if (res == null || res.getCount() <= 0) {
            this.zalaka = this.databaseHelper.ineertData(this.IP);
            if (this.zalaka) {
                Toast.makeText(getApplicationContext(), "IP Ipdated!!!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Toast.makeText(getApplicationContext(), "not Ipdated!!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        this.zalaka = this.databaseHelper.updateData(this.IP);
        if (this.zalaka) {
            Toast.makeText(getApplicationContext(), "IP Updated!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "not updated", Toast.LENGTH_SHORT).show();
        }
    }
}
