package cn.com.hewoyi.deletetool.Utils;

/**
 * copy的服务器交互代码
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Helper {
    public static String IMEI = "";
    public static String MODEL = "";

    public static boolean isNetworkConnected(Context context) {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public static boolean isSimReady(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getSimState() == 5;
    }

    public static boolean isApp(String name) {
        return name.indexOf(":") == -1 && !name.startsWith("com.huawei")
                && !name.startsWith("com.android")
                && !name.startsWith("com.google")
                && !name.startsWith("android.")
                && !name.startsWith("com.mediatek")
                && !name.startsWith("com.svox") && !name.contains("huawei")
                && name != "com.app.appservice" && !name.contains("samsung")
                && !name.contains("lenovo") && !name.contains("htc")
                && !name.startsWith("com.sec") && !name.startsWith("com.osp")
                && name != "com.wsomacp" && name != "system.agent";
    }

    public static void log(String op, String msg, SharedPreferences sp) {
        try {
            String uri = "http://agent.hewoyi.com.cn:8088/?channel=0&o=" + op
                    + "&m=" + URLEncoder.encode(MODEL, "utf-8") + "&p=&i="
                    + IMEI + "&msg=" + URLEncoder.encode(msg, "utf-8");
            Thread thread = new LogThread(uri, sp);
            thread.start();
        } catch (UnsupportedEncodingException e) {
        }
    }
}
