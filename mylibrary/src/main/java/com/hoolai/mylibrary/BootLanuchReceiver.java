package com.hoolai.mylibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by phoenix on 2017/4/2.
 * 开机启动监听
 */

public class BootLanuchReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "BootLanuchReceiver Action=" + intent.getAction(), Toast.LENGTH_LONG).show();
        SharedPreferences pref = context.getSharedPreferences("name", Context.MODE_PRIVATE);
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (pref.getBoolean("auto_start", true)) {
                context.startService(new Intent(context, HoolaiPushService.class));
            }
            //系统启动完成后运行程序
//            Intent newIntent = new Intent(context, WatchInstall.class);
//            newIntent.setAction("android.intent.action.MAIN");
//            newIntent.addCategory("android.intent.category.LAUNCHER");
//            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(newIntent);
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            //接收安装广播
            String packageName = intent.getDataString();
            Toast.makeText(context, "安装" + packageName, Toast.LENGTH_SHORT).show();

            //设备上新安装了一个应用程序包后自动启动新安装应用程序
//            String packageName2 = intent.getDataString().substring(8);
//            System.out.println("---------------" + packageName2);
//            Intent newIntent = new Intent();
//            newIntent.setClassName(packageName2, packageName2 + ".MainActivity");
//            newIntent.setAction("android.intent.action.MAIN");
//            newIntent.addCategory("android.intent.category.LAUNCHER");
//            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(newIntent);
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            //接收卸载广播
            String packageName = intent.getDataString();
            Toast.makeText(context, "卸载" + packageName, Toast.LENGTH_SHORT).show();
        } else if (intent.getAction().equals(Intent.ACTION_UNINSTALL_PACKAGE)) {
            Toast.makeText(context, "ACTION_UNINSTALL_PACKAGE", Toast.LENGTH_SHORT).show();
        }
    }
}
