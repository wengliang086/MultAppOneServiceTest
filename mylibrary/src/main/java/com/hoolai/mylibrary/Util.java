package com.hoolai.mylibrary;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/3/31.
 */

public class Util {

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        TreeMap<String, String> map = new TreeMap<String, String>();
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            map.put(mName, mName);
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        for (String m : map.values()) {
            Log.e("ServiceName:", m);
        }
        return isWork;
    }

}
