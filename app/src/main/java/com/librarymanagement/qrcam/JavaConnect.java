package com.librarymanagement.qrcam;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class JavaConnect  extends AsyncTask<String, Void, Void>{
    DataOutputStream dos;
    public Context mcon;
    PrintWriter pw;
    Socket s;
    public Void doInBackground(String... voids) {
        String message = voids[0];
        try {
            this.s = new Socket(voids[1], 6666);
            this.pw = new PrintWriter(this.s.getOutputStream());
            this.pw.write(message);
            this.pw.flush();
            this.pw.close();
            this.s.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(e.toString(), e.getMessage(), e);
        }
        return null;
    }
}
