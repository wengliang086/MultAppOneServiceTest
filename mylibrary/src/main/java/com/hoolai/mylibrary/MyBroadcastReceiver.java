package com.hoolai.mylibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/3/31.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    public static final String Msg_Action = "android.intent.action.MyBroadcastReceiver.Msg";
    public static final String Sync_Action = "android.intent.action.MyBroadcastReceiver.Aysn";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(HoolaiPushService.TAG, "MyBroadcastReceiver" + " process id is " + Process.myPid());
        if (intent.getAction().equals(Msg_Action)) {
            String msg = intent.getExtras().getString("msg");
            Toast.makeText(context, "msg=" + msg, Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(context, NotificationActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if (intent.getAction().equals(Sync_Action)) {
            ArrayList<String> msgs = intent.getExtras().getStringArrayList("set");
            for (String msg : msgs) {
                PrefUtil.getInstance().addPackageName(msg);
            }
        }
    }
}
