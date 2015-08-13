package cn.com.hewoyi.deletetool.Utils;

/**
 * copy的服务器交互代码
 */


import android.content.SharedPreferences;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.Date;

public class LogThread extends Thread {
    private String uri;
    private SharedPreferences sp;

    public LogThread(String url, SharedPreferences sp) {
        this.uri = url;
        this.sp = sp;
    }

    public void run() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(this.uri);
            client.execute(request);

            if (sp != null) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("LAST_SEND_TIME", new Date().getTime());
                editor.commit();
            }
        } catch (Exception e) {
        }
    }
}
