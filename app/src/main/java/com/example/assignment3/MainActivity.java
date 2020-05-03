package com.example.assignment3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    final int Permission_request_code = 5555;
    String message = "";
    int result = PackageManager.PERMISSION_DENIED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadManager manager=(DownloadManager) getSystemService(this.DOWNLOAD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission_request_code);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Permission_request_code);

        }
            String json = "";
            InputStream is = getResources().openRawResource(R.raw.data);
            try {
                byte[] data = new byte[is.available()];
                while (is.read(data) != -1) {
//nothing needs to be done.
                }
                json = new String(data);
            } catch (IOException e) {

                e.printStackTrace();
            }

                RecyclerView rv;
                rv = findViewById(R.id.recyclerView);
                LinearLayoutManager llm = new LinearLayoutManager(this);
                rv.setLayoutManager(llm);
                RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(this,json, manager);
                rv.setAdapter(rvAdapter);
        }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case Permission_request_code:
                for (int i = 0 ;i < grantResults.length; i++){
                    Toast.makeText(this, permissions[i] + " has been " + (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "Granted" : "Denied"), Toast.LENGTH_SHORT).show();
        }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}


