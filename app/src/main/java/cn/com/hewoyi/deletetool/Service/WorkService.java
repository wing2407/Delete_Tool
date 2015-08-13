package cn.com.hewoyi.deletetool.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import java.util.Date;

import cn.com.hewoyi.deletetool.Utils.Helper;

/**
 * copy的服务器交互代码
 */
public class WorkService extends Service {

//    public static final String ALARM_SIGN = "android.intent.action.MC_ALARM";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate() {
        try {
            TelephonyManager tm = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Helper.IMEI = tm.getDeviceId().toUpperCase();
            Helper.MODEL = Build.MODEL;
        } catch (Exception e) {
        }

//        AlarmManager manager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//        Intent service = new Intent(getApplicationContext(), WorkService.class);
//        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, service, PendingIntent.FLAG_UPDATE_CURRENT);
//        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 10 * 1000, pendingIntent);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 一分钟执行一次服务，加个最后发送时间判断，一个小时发送一次。
        if (needSend()) {
            if (Helper.isNetworkConnected(this.getApplicationContext())) {
                SharedPreferences sp = getSharedPreferences("work", Context.MODE_PRIVATE);
                Helper.log("arrive", "", sp);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public boolean needSend() {
        boolean result = false;
        long cha = new Date().getTime() - readLastSendTime();
        if (cha > 0) {
            if (cha * 1.0 / (1000 * 60) >= 30) {
                result = true;
            }
        }
        return result;
    }

    public long readLastSendTime() {
        SharedPreferences sp = getSharedPreferences("work", Context.MODE_PRIVATE);
        return sp.getLong("LAST_SEND_TIME", 0);
    }

    @Override
    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, WorkService.class);
        this.startService(localIntent);
    }

}
