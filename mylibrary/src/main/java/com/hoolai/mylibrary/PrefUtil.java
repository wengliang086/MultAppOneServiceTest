package com.hoolai.mylibrary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by phoenix on 2017/4/2.
 * 存储APP信息
 * 通过广播同步信息
 */

public class PrefUtil {

    public static final String SP_NAME = "sp_name";
    public static final String DATA_NMAE = "data_name";
    private static PrefUtil mInstance;

    private SharedPreferences sp;
    private Context mContext;
    private Set<String> packageSet = new HashSet<>();

    private PrefUtil() {
    }

    public static void init(Context context) {
        mInstance = new PrefUtil();
        mInstance.mContext = context;
        mInstance.sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        mInstance.addPackageName(context.getPackageName());
    }

    public static PrefUtil getInstance() {
        return mInstance;
    }

    private void reload() {
        packageSet = sp.getStringSet(DATA_NMAE, packageSet);
    }

    private void save() {
        sp.edit().putStringSet(DATA_NMAE, packageSet).apply();
    }

    public Set<String> getAll() {
        return packageSet;
    }

    public void addPackageName(String packageName) {
        if (packageName.startsWith("package:")) {
            packageName = packageName.substring(8);
        }
        packageSet.add(packageName);
        save();
    }

    public void deletePackageName(String packageName) {
        if (packageName.startsWith("package:")) {
            packageName = packageName.substring(8);
        }
        packageSet.remove(packageName);
        save();
    }

    public void sync() {
        Intent intent = new Intent();
        intent.setAction(MyBroadcastReceiver.Sync_Action);
        intent.putStringArrayListExtra("set", new ArrayList<String>(getAll()));
        mContext.sendBroadcast(intent);
        L.e(mContext, "发送同步广播：" + getAll().toString());
    }
}
