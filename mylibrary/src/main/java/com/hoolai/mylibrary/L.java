package com.hoolai.mylibrary;

import android.content.Context;
import android.util.Log;

/**
 * Created by phoenix on 2017/4/2.
 */

public class L {

    public static void e(String msg) {
        Log.e(HoolaiPushService.TAG, msg);
    }

    public static void e(Context context, String msg) {
        Log.e(HoolaiPushService.TAG, context.getString(R.string.app_name) + " " + msg);
    }
}
