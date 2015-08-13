package cn.com.hewoyi.deletetool.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * copy的服务器交互代码
 */
import cn.com.hewoyi.deletetool.Service.WorkService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, WorkService.class);
        context.startService(service);
    }
}
