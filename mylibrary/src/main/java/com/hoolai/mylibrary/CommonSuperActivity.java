package com.hoolai.mylibrary;

import android.os.Process;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Set;

public class CommonSuperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_super);
        Log.d(HoolaiPushService.TAG, getString(R.string.app_name) + " process id is " + Process.myPid());

        HoolaiPushService.init(this);
        HoolaiPushService.start(this);

        findViewById(R.id.btn_start_service).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HoolaiPushService.start(CommonSuperActivity.this);
            }
        });
        findViewById(R.id.btn_get_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<String> set = PrefUtil.getInstance().getAll();
                ((TextView) findViewById(R.id.tv_msg)).setText(set.toString());
            }
        });
    }
}
