package cn.com.hewoyi.deletetool.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.com.hewoyi.deletetool.Model.AppInfo;

/**
 * Copyright ?1999-2015,HEWOYI,All Rights Reserved
 * 广州市和沃易网络科技有限公司 版权所有
 * 创建者: small wai
 * 日期: 2015-08-02
 * 时间: 02:45
 * 修改者：
 * 修改时间及内容：
 */

public class DeleteTask extends AsyncTask<List<AppInfo>, String, Boolean> {

    ProgressDialog progressDialog;
    Context context;

    //自定义Finish监听器
    public interface OnFinishListen {
        void FinishListen(Boolean done);
    }

    OnFinishListen mOnFinishListen = null;

    public void setOnFinishListen(OnFinishListen e) {
        mOnFinishListen = e;
    }

    public DeleteTask(Context mContext) {
        context = mContext;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("警告提示");
        progressDialog.setMessage("删除中....请不要进行其他操作！！！");
        progressDialog.setCancelable(false);
        progressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        //没有root就Toast提示
        if (this.RootPath().isEmpty()) {
            Toast.makeText(context, "获取root权限失败", Toast.LENGTH_LONG).show();
        }

        //关闭进度框
        progressDialog.dismiss();
        //执行finish监听器
        if (mOnFinishListen != null) {
            mOnFinishListen.FinishListen(true);
        }
        super.onPostExecute(aBoolean);
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Toast.makeText(context, "删除" + values[0] + "成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(List<AppInfo>... params) {
        //list数据参数传递
        List<AppInfo> deleteList = params[0];
        //判断是否已经root
//        Boolean isRoot = isRoot();
        for (int i = 0; i < deleteList.size(); i++) {
            Log.d("deletelist", deleteList.get(i).getPackageName());
            if (deleteList.get(i).isSystemApp()) {
                //Log.d("deletelist", deleteList.get(i).getPackageName());
                //删除系统App
                deleteSystemApp(deleteList.get(i).getPackageName(), deleteList.get(i).getAppName());
            } else {
                //删除用户App
                deleteUserApp(deleteList.get(i).getPackageName(), deleteList.get(i).getAppName());
            }
        }
        return null;
    }


    //删除系统App
    private void deleteSystemApp(String packageName, String appName) {
        String sp = this.RootPath();
        if (!sp.isEmpty()) {
            try {
                //执行root权限
                Process process = Runtime.getRuntime().exec(sp);
                DataOutputStream os = new DataOutputStream(process.getOutputStream());

                //获取路径并格式化掉package：
                Process pro_path = Runtime.getRuntime().exec("pm path " + packageName);
                String path = inputStreamToString(pro_path.getInputStream()).substring(8);

                Log.d("deleteli", path);

                //停掉应用，挂载system目录，删除文件，disable禁用
                os.writeBytes("am force-stop " + packageName + "\n");
                os.writeBytes("mount -o remount,rw /system\n");
                os.writeBytes("rm -rf " + path + "\n");
                os.writeBytes("pm disable " + packageName + "\n");

                os.flush();
                os.close();

                publishProgress(appName);

            } catch (IOException e) {
                //Log.d("deletelist", e.getMessage());
                e.printStackTrace();
            }

        }
    }

    //删除用户App
    private void deleteUserApp(String packageName, String appName) {

        String sp = this.RootPath();
        if (!sp.isEmpty()) {
            try {
                Process process = Runtime.getRuntime().exec(sp);
                DataOutputStream os = new DataOutputStream(process.getOutputStream());
                os.writeBytes("pm uninstall " + packageName + "\n");
                os.flush();
                os.close();
            } catch (IOException e) {
                //Log.d("deleteapp", e.getMessage());
                e.printStackTrace();
            }
            publishProgress(appName);
        } else {
            Uri packageUri = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
            context.startActivity(uninstallIntent);
        }
    }

    //inputStream转换为String
    private static String inputStreamToString(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        return baos.toString();
    }

    //判断机器 Android是否已经root，即是否获取root权限
//    public synchronized boolean isRoot() {
//        Process process = null;
//        DataOutputStream os = null;
//        try {
//            process = Runtime.getRuntime().exec("/system/bin/su");
//            os = new DataOutputStream(process.getOutputStream());
//            os.writeBytes("exit\n");
//            os.flush();
//            int exitValue = process.waitFor();
//            if (exitValue == 0) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (Exception e) {
//            Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "
//                    + e.getMessage());
//            return false;
//        } finally {
//            try {
//                if (os != null) {
//                    os.close();
//                }
//                process.destroy();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public String RootPath() {
        String rp = "";
        String[] ps = new String[]{"/system/bin/sb", "/system/etc/sb", "/system/bin/su", "/system/xbin/su"};
        for (String p : ps) {
            File file = new File(p);
            if (file.exists()) {
                rp = p;
                break;
            }
        }
        return rp;
    }

}
