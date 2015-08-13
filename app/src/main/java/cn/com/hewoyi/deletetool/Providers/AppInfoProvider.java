package cn.com.hewoyi.deletetool.Providers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import cn.com.hewoyi.deletetool.Model.AppInfo;
import cn.com.hewoyi.deletetool.Utils.UtilCompareList;
import cn.com.hewoyi.deletetool.Utils.UtilForXml;

/**
 * 创建者: small wai
 * 日期: 2015-07-29
 * 时间: 15:55
 * 内容：获取本地的应用程序信息列表
 * 修改者：
 * 修改时间及内容：
 */
public class AppInfoProvider {

    private PackageManager packageManager;
    //获取一个包管理器
    public AppInfoProvider(Context context){
        packageManager = context.getPackageManager();

    }
    /**
     *获取系统中所有应用信息，
     *并将应用软件信息保存到list列表中。
     **/
    public List<AppInfo> getAllApps(){


        List<AppInfo> localList = new ArrayList<AppInfo>();
        AppInfo myAppInfo;
        //获取到所有安装了的应用程序的信息，包括那些卸载了的，但没有清除数据的应用程序
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for(PackageInfo info:packageInfos){
            myAppInfo = new AppInfo();
            //拿到包名
            String packageName = info.packageName;
             //拿到应用程序的信息
            ApplicationInfo appInfo = info.applicationInfo;
            //拿到应用程序的图标
            Drawable icon = appInfo.loadIcon(packageManager);
            //拿到应用程序的大小
            //long codesize = packageStats.codeSize;
            //Log.i("info", "-->"+codesize);
            //拿到应用程序的程序名
            String appName = appInfo.loadLabel(packageManager).toString();
            myAppInfo.setPackageName(packageName);
            myAppInfo.setAppName(appName);
            myAppInfo.setIcon(icon);

            //打印调试信息
            //Log.d("info", " + " + appName + " : " + packageName + "*");

            if(filterApp(appInfo)){
                //false为非系统应用
                myAppInfo.setSystemApp(false);
            }else{
                //true为系统应用
                myAppInfo.setSystemApp(true);
            }
            localList.add(myAppInfo);
        }

        //读取xml加载白名单list
        UtilForXml utilForXml = new UtilForXml();
        //调用筛选方法筛选localList列表内容
        UtilCompareList utilCompareList = new UtilCompareList();
        //经筛选后的最终展示列表list
        List<AppInfo> list = new ArrayList<AppInfo>();
        list = utilCompareList.compareList(utilForXml.queryFromXml(),localList) ;
        //返回AppInfo泛型列表
        return list;
    }

    /**
     *判断某一个应用程序是不是系统的应用程序，
     *如果是返回true，否则返回false。
     */
    public boolean filterApp(ApplicationInfo info){
        //有些系统应用是可以更新的，如果用户自己下载了一个系统的应用来更新了原来的，它还是系统应用，这个就是判断这种情况的
        if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0){
            return true;
        }else if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0){//判断是不是系统应用
            return true;
        }
        return false;
    }


}  
