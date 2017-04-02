package com.hoolai.multapp2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.hoolai.mylibrary.CommonSuperActivity;
import com.hoolai.mylibrary.HoolaiPushService;

public class MainActivity extends CommonSuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Log.d(HoolaiPushService.TAG, getString(R.string.app_name) + " process id is " + Process.myPid());

//        HoolaiPushService.init(this);

//        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HoolaiPushService.start(MainActivity.this);
//            }
//        });

        try {
            //访问其他应用中的Preference
            Context otherAppsContext  = createPackageContext("com.hoolai.service.test", Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences share = otherAppsContext.getSharedPreferences("wujay", Context.MODE_WORLD_READABLE);
            String name = share.getString("appName", "未找到");
            Toast.makeText(this, "name=" + name, Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(HoolaiPushService.TAG, e.getMessage(), e);
        }
    }
}
