package com.hoolai.mylibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/3/31.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getExtras().getString("msg");
        Toast.makeText(context, "msg=" + msg, Toast.LENGTH_LONG).show();
        context.startActivity(new Intent(context, NotificationActivity.class));
    }
}
