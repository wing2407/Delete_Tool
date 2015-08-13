package cn.com.hewoyi.deletetool.Utils;

import android.app.Application;
import android.content.Context;

/**
 * 创建者: small wai
 * 日期: 2015-07-30
 * 时间: 11:31
 * 内容：本文件可以创建一个全局context
 * 修改者：
 * 修改时间及内容：
 */
public class MyApplication extends Application {

    /**
     * 全局的上下文.
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //全局context
        mContext = getApplicationContext();

        //全局异常捕获
       /* CrashHandler handler = CrashHandler.getInstance();
        //在Appliction里面设置我们的异常处理器为UncaughtExceptionHandler处理器
        handler.init(getApplicationContext());*/

       /* UncaughtException mUncaughtException = UncaughtException.getInstance();
        mUncaughtException.init();
        //mUncaughtException.setContext(this);*/
    }

    /**
     * 获取Context.
     *
     * @return
     */
    public static Context getContext() {
        return mContext;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}