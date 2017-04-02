package com.hoolai.service.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hoolai.mylibrary.HoolaiPushService;

public class MainActivity extends AppCompatActivity {

    //允许其他应用访问
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("wujay", Context.MODE_WORLD_READABLE);
        sharedPreferences.edit().putString("appName", getString(R.string.app_name)).apply();
        Log.d(HoolaiPushService.TAG, getString(R.string.app_name) + " process id is " + Process.myPid());

        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HoolaiPushService.class);
                startService(intent);
            }
        });
    }
}
