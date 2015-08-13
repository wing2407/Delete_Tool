package cn.com.hewoyi.deletetool.Utils;

import java.util.List;

import cn.com.hewoyi.deletetool.Model.AppInfo;

/**
 * Copyright ©2015,HEWOYI,All Rights Reserved
 * 广州市和沃易网络科技有限公司 版权所有
 * 内容描述：比较两个List的增量，配合白名单获取真正需要的list
 * 创建者: small wai
 * 日期: 2015-07-30
 * 时间: 15:52
 * 修改者：
 * 修改时间及内容：
 */
public class UtilCompareList {

    private static List<String> whiteList;
    private static List<AppInfo> localList;

    /**
     * 较有效率地比较两个List的异同
     * 如果运行速度慢或者过于耗资源则需要更换更好的方法
     *  @param whitelist 白名单white (String类型)
     * @param listlocal 将要比较的本地List （AppInfo泛型）
     */
    public static List<AppInfo> compareList(List<String> whitelist, List<AppInfo> listlocal) {
        //时间测试
        //long currTime = System.currentTimeMillis();

        //参数实例化
        whiteList = whitelist;
        localList = listlocal;

        for (int i = 0; i < listlocal.size(); i++) {
            AppInfo app = listlocal.get(i);
            if(whitelist.contains(app.getPackageName())){
                listlocal.remove(app);
            }
        }


//        for (AppInfo list : listlocal){
//            String packageName = list.getPackageName();
//            if(whitelist.contains(packageName)){
//                listlocal.remove(list);
//            }
//            for (String ls : whitelist){
//                if (packageName == ls)
//                {
//                    listlocal.remove(list);
//                }
//            }
//        }
/*
        //导出package包名String列表list
        List<String> localPackageList = new ArrayList<String>() ;
        for (i=0;i<localList.size();i++){
        localPackageList.add(localList.get(i).getPackageName());
        }

        Map<String, Integer> targetMap = new HashMap<String, Integer>();
        //将白名单whiteList放入Map中，并将其value设为1
        for (String u : whiteList) {
            targetMap.put(u, 1);
        }
        //将本地列表localPackageList放入Map中，如果存在则将其value设置为2，如果不存在则将其value设为-1
        for (String u : localPackageList) {
            boolean isExisted = targetMap.containsKey(u);
            if (isExisted) {
                targetMap.put(u, 2);
            } else {
                targetMap.put(u, -1);
            }
        }
        //long middleTime = System.currentTimeMillis();
        //List<String> removeList = new ArrayList<String>();

        //经过筛选后的最终list
        List<String> finalList = new ArrayList<String>();

        finalList.contains("");


        for (Map.Entry<String, Integer> entry : targetMap.entrySet()) {
            if (entry.getValue() == -1) {
                finalList.add(entry.getKey());
            }
        }*/

        //得到最终list返回
        return listlocal;

 /*
        //时间测试
        System.out.println("当前Map大小："+targetMap.size());
        System.out.println("删除数据大小："+ removeList.size());
        System.out.println("新增数据大小: " + addList.size());
        long endTime = System.currentTimeMillis();
        System.out.println("抽取前所用时间:"+(middleTime-currTime)+"毫秒");
        System.out.println("方法所用时间:"+(endTime-currTime)+"毫秒");
*/
    }
}  