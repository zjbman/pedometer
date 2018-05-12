package cn.bluemobi.dylan.pedometer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bluemobi.dylan.pedometer.R;
import cn.bluemobi.dylan.pedometer.app.ActivityManager;
import cn.bluemobi.dylan.pedometer.step.UpdateUiCallBack;
import cn.bluemobi.dylan.pedometer.step.service.StepService;
import cn.bluemobi.dylan.pedometer.step.utils.SharedPreferencesUtils;
import cn.bluemobi.dylan.pedometer.util.SharedpreferencesUtil;
import cn.bluemobi.dylan.pedometer.view.StepArcView;

/**
 * 记步主页
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_data;//查看历史步数
    private StepArcView cc;//自定义view
    private TextView tv_set;//设置锻炼计划
    private TextView tv_isSupport;
    private SharedPreferencesUtils sp;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nv_main_navigation)
    NavigationView navigationView;

    private void assignViews() {
        tv_data = (TextView) findViewById(R.id.tv_data);
        cc = (StepArcView) findViewById(R.id.cc);
        tv_set = (TextView) findViewById(R.id.tv_set);
        tv_isSupport = (TextView) findViewById(R.id.tv_isSupport);


        ButterKnife.bind(this);

        /* 设定布局中的toolbar*/
        toolbar.setTitle("计步器");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* 设置toolbar的左边图标不是“<-”而是“亖”*/
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        initData();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
          /* 侧滑菜单 头部布局的监听*/
        View headerView = navigationView.getHeaderView(0);
        ImageView iv_icon = headerView.findViewById(R.id.iv_icon);
        TextView tv_name = headerView.findViewById(R.id.tv_name);


        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(this);
        tv_name.setTextColor(getResources().getColor(R.color.white));
        tv_name.setText(user.get("username"));

    }

    private void addListener() {
        tv_set.setOnClickListener(this);
        tv_data.setOnClickListener(this);

                /* 侧滑菜单 菜单选项的监听*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                item.setChecked(true);//设置选中状态
                String title = item.getTitle().toString();

                switch (title) {
                    case "修改密码":
                        startActivity(new Intent(MainActivity.this,ChangePassWordActivity.class));
                        break;
                    case "设置锻炼计划":
                        startActivity(new Intent(MainActivity.this,SetPlanActivity.class));
                        break;
                    case "查看历史记录":
                        startActivity(new Intent(MainActivity.this,HistoryActivity.class));
                        break;
                    case "退出登录":
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        SharedpreferencesUtil.getInstance().cacheUser(MainActivity.this,"","");
                        ActivityManager.getInstance().removeActivity(MainActivity.this);
                        break;
                    default:
                        break;
                }

//                ToastUtil.show(MainActivity.this, title);

                /* 关闭导航菜单*/
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    private void initData() {
        /* 侧滑菜单 头部布局的监听*/
        View headerView = navigationView.getHeaderView(0);
        ImageView iv_icon = headerView.findViewById(R.id.iv_icon);
        TextView tv_name = headerView.findViewById(R.id.tv_name);

        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(this);
        tv_name.setTextColor(getResources().getColor(R.color.white));
        tv_name.setText(user.get("username"));


        sp = new SharedPreferencesUtils(this);
        //获取用户设置的计划锻炼步数，没有设置过的话默认7000
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        //设置当前步数为0
        cc.setCurrentCount(Integer.parseInt(planWalk_QTY), 0);
        tv_isSupport.setText("计步中...");
        setupService();
    }


    private boolean isBind = false;

    /**
     * 开启计步服务
     */
    private void setupService() {
        Intent intent = new Intent(this, StepService.class);
        isBind = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    /**
     * 用于查询应用服务（application Service）的状态的一种interface，
     * 更详细的信息可以参考Service 和 context.bindService()中的描述，
     * 和许多来自系统的回调方式一样，ServiceConnection的方法都是进程的主线程中调用的。
     */
    ServiceConnection conn = new ServiceConnection() {
        /**
         * 在建立起于Service的连接时会调用该方法，目前Android是通过IBind机制实现与服务的连接。
         * @param name 实际所连接到的Service组件名称
         * @param service 服务的通信信道的IBind，可以通过Service访问对应服务
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepService stepService = ((StepService.StepBinder) service).getService();
            //设置初始化数据
            String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
            cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepService.getStepCount());

            //设置步数监听回调
            stepService.registerCallback(new UpdateUiCallBack() {
                @Override
                public void updateUi(int stepCount) {
                    String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
                    cc.setCurrentCount(Integer.parseInt(planWalk_QTY), stepCount);
                }
            });
        }

        /**
         * 当与Service之间的连接丢失的时候会调用该方法，
         * 这种情况经常发生在Service所在的进程崩溃或者被Kill的时候调用，
         * 此方法不会移除与Service的连接，当服务重新启动的时候仍然会调用 onServiceConnected()。
         * @param name 丢失连接的组件名称
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_set:
                startActivity(new Intent(this, SetPlanActivity.class));
                break;
            case R.id.tv_data:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBind) {
            this.unbindService(conn);
        }
    }

    /*
   * 使用toolbar：
   * 1.首先需要引入v7支持包：compile 'com.android.support:appcompat-v7:26.+'
   * 2.在布局文件中使用toolbar
   * 3.在对应的Activity的java代码中实例化一个toolbar的菜单布局文件，该菜单布局文件将成功映射到当前activity的toolbar上
   * 4.toolbar的监听点击事件分为两个部分，一个是左边由系统自动生成id的android.R.id.home，一个是右边由我们自己在菜单布局文件中设置的id
   * 5.左边的监听事件通过重写activity的onOptionsItemSelected实现
   * 6.右边的监听事件通过toolbar.setOnMenuItemClickListener
   * */
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        /* 将toolbar的菜单布局文件实例化*/
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    /* 要点：toolbar的点击监听分为了两个部分，
    一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
    另一个是它右边的文字（这个是我们自定义的菜单所有的）
    左边的点击事件通过下面的方式*/
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            /* 点击toolbar的左边图标 弹出侧滑菜单*/
//            case android.R.id.home:
//                drawerLayout.openDrawer(GravityCompat.START);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
