package com.hoolai.mylibrary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Log.d(HoolaiPushService.TAG, getString(R.string.app_name) + " process id is " + Process.myPid());
    }
}
