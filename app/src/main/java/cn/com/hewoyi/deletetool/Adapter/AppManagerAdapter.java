package cn.com.hewoyi.deletetool.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.com.hewoyi.deletetool.Model.AppInfo;
import cn.com.hewoyi.deletetool.R;

/**
 * Copyright ?2015,HEWOYI,All Rights Reserved
 * 广州市和沃易网络科技有限公司 版权所有
 * 内容描述：
 * 创建者: small wai
 * 日期: 2015-08-01
 * 时间: 14:46
 * 修改者：
 * 修改时间及内容：
 */
public class AppManagerAdapter extends BaseAdapter
{
    private Context mContext;
    private ListView lvApp;
    private List<AppInfo> listApp = null;

    public AppManagerAdapter(Context context,ListView listView, List<AppInfo> list){
        mContext = context;
        listApp = list;
        lvApp = listView;
    }

    @Override
    public int getCount()
    {
        return listApp.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public Object getItem(int position)
    {
        return listApp.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        AppInfo info = listApp.get(position);
        if(convertView == null)
        {
            View view = View.inflate(mContext, R.layout.app_manager_item, null);
            AppManagerViews views = new AppManagerViews();
            views.iv_app_icon = (ImageView) view.findViewById(R.id.iv_app_manager_icon);
            views.tv_app_name = (TextView) view.findViewById(R.id.tv_app_manager_name);
            views.cb_app_select = (CheckBox)view.findViewById(R.id.item_home_checked);
            views.iv_app_icon.setImageDrawable(info.getIcon());
            views.tv_app_name.setText(info.getAppName());
            view.setTag(views);
            return view;
        }
        else {
            AppManagerViews views = (AppManagerViews) convertView.getTag();
            views.iv_app_icon.setImageDrawable(info.getIcon());
            views.tv_app_name.setText(info.getAppName());
            updateChecked(position, views.cb_app_select);
            return convertView;
        }
    }

    public void updateChecked(int position, CheckBox select){
        if(lvApp.isItemChecked(position)){
            select.setChecked(true);
        } else {
            select.setChecked(false);
        }

    }
    /**
     * 用来优化listview的类
     * */
    private class AppManagerViews
    {
        ImageView iv_app_icon;
        TextView tv_app_name;
        CheckBox cb_app_select;
    }

}
