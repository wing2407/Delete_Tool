package cn.com.hewoyi.deletetool.Activeties;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.hewoyi.deletetool.Adapter.AppManagerAdapter;
import cn.com.hewoyi.deletetool.Model.AppInfo;
import cn.com.hewoyi.deletetool.Providers.AppInfoProvider;
import cn.com.hewoyi.deletetool.R;
import cn.com.hewoyi.deletetool.Service.WorkService;
import cn.com.hewoyi.deletetool.Utils.DeleteTask;

/**
 * Copyright ?2015,HEWOYI,All Rights Reserved
 * 广州市和沃易网络科技有限公司 版权所有
 * 创建者: small wai
 * 内容：主界面Activity
 * 修改者：
 * 修改时间及内容：
 */
public class HomeActivity extends ActionBarActivity {

    private static final int GET_ALL_APP_FINISH = 1;//加载列表
    private static final int GET_CHECKED_NUM = 2;//更新button上的数目
    private static final int DELETE_DONE = 3;//删除完毕，重新加载列表的标志

    private ListView lv_app_manager;//应用信息列表
    private LinearLayout ll_app_manager_progress; //进度条
    private AppInfoProvider provider;//获取App列表的对象
    private AppManagerAdapter adapter;
    private List<AppInfo> list;

    private List<AppInfo> delete_list = new ArrayList<AppInfo>();//删除列表
    private Button btn_home_delete;
    private Boolean firstEnter = true;//判断首次加载列表

    private Toolbar toolbar;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GET_ALL_APP_FINISH:
                    //进度条设置为不可见
                    ll_app_manager_progress.setVisibility(View.GONE);
                    adapter = new AppManagerAdapter(HomeActivity.this, lv_app_manager, list);
                    lv_app_manager.setAdapter(adapter);
                    lv_app_manager.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    break;
                case GET_CHECKED_NUM:
                    btn_home_delete.setText("删除(已选中" + msg.arg1 + "项)");
                    break;
                case DELETE_DONE:
                    btn_home_delete.setText("删  除");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //实例化
        lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
        ll_app_manager_progress = (LinearLayout) findViewById(R.id.ll_app_manager_progress);
        btn_home_delete = (Button) findViewById(R.id.btn_home_delete);

        //标题栏toolbar
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle("");//设置Toolbar标题
        //toolbar.setSubtitle("  应用删除工具");
        toolbar.setTitleTextColor(Color.parseColor("#000000")); //设置标题颜色
        //toolbar.setSubtitleTextColor(Color.parseColor("#999999"));
        //toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        //判断是否弹出警告框
        if (firstEnter) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("风险提示")
                    .setMessage("本工具提供删除功能，操作不当可能引起系统崩溃，请谨慎操作！")
                    .setCancelable(false)
                    .setNegativeButton("进入", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }

                    })
                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //退出
                            finish();
                        }
                    }).create().show();
        }

        // 启动 Service
        Intent serviceIntent = new Intent(this, WorkService.class);
        startService(serviceIntent);
    }


    @Override
    protected void onStart() {
        super.onStart();

        ll_app_manager_progress.setVisibility(View.VISIBLE);

        /**
         * //开一个线程用于完成对所有应用程序信息的搜索
         * 当搜索完成之后，就把一个成功的消息发送给Handler，
         * 然后handler把搜索到的数据设置进入listview里面  .
         * */
        new Thread() {
            public void run() {
                //加入looper才能使用全局context
                Looper.prepare();
                provider = new AppInfoProvider(HomeActivity.this);
                //获取应用列表
                list = provider.getAllApps();
                Message msg = new Message();
                msg.what = GET_ALL_APP_FINISH;
                handler.sendMessage(msg);
                Looper.loop();

            }
        }.start();

    }

    @Override
    protected void onResume() {
        super.onResume();

        lv_app_manager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //实时更新button选择的项目数量
                Message msg = new Message();
                msg.what = GET_CHECKED_NUM;
                msg.arg1 = lv_app_manager.getCheckedItemCount();
                handler.sendMessage(msg);

                //更新checkBox
                adapter.notifyDataSetChanged();

               /* //LogCat调试
                for (int i = 0; i < lv_app_manager.getCheckedItemIds().length; i++) {
                    Log.d("checked", lv_app_manager.getCheckedItemIds()[i] + "");
                }*/
            }
        });

        btn_home_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //弹出删除确认对话框，确认则执行删除
                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("警告")
                        .setMessage("删除操作有可能引起系统崩溃，是否继续删除选中的 " + lv_app_manager.getCheckedItemCount() + " 个应用？")
                        .setCancelable(true)
                        .setPositiveButton("取消.", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //不操作
                            }
                        })
                        .setNegativeButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //循环获取被选中的list item加入最后的删除list
                                int length = lv_app_manager.getCheckedItemIds().length;
                                for (int i = 0; i < length; i++) {
                                    delete_list.add(list.get((int) lv_app_manager.getCheckedItemIds()[i]));
                                    //Log.d("System", delete_list.get(i).getPackageName());
                                }

                                //执行异步任务：删除操作
                                DeleteTask task = new DeleteTask(HomeActivity.this);
                                task.execute(delete_list);

                                //自定义任务完成Finished监听器
                                task.setOnFinishListen(new DeleteTask.OnFinishListen() {
                                    @Override
                                    public void FinishListen(Boolean done) {
                                        //重新加载列表
                                        reload();
                                    }
                                });
                            }
                        }).create().show();

            }
        });
    }

    //重新加载列表
    private void reload() {

        // list.clear();

        list.removeAll(delete_list);
        delete_list.clear();
        adapter.notifyDataSetChanged();
        lv_app_manager.removeAllViewsInLayout();
        lv_app_manager.clearChoices();

        //重置删除按钮的显示数量
        Message msg = new Message();
        msg.what = DELETE_DONE;
        handler.sendMessage(msg);

        firstEnter = false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //重置删除按钮的显示数量
        Message msg = new Message();
        msg.what = DELETE_DONE;
        handler.sendMessage(msg);

        firstEnter = false;
    }
}
