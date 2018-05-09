package com.zjbman.pedometer.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zjbman.pedometer.R;
import com.zjbman.pedometer.activity.base.BaseActivity;
import com.zjbman.pedometer.app.ActivityManager;
import com.zjbman.pedometer.util.SharedpreferencesUtil;

import java.util.Map;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    @Bind(R.id.tv_plan)
    TextView tv_plan;

    @Bind(R.id.tv_record)
    TextView tv_record;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Bind(R.id.nv_main_navigation)
    NavigationView navigationView;

    @Override
    public View setContentView() {
        return View.inflate(this, R.layout.activity_main, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        /* 设定布局中的toolbar*/
        toolbar.setTitle("计步器");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setLogo(R.mipmap.logo);

        /* 设置toolbar的左边图标不是“<-”而是“亖”*/
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

    }

    @Override
    protected void setListener() {
        tv_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlanActivity.class));
            }
        });

        tv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RecordActivity.class));
            }
        });

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
                        startActivity(new Intent(MainActivity.this,PlanActivity.class));
                        break;
                    case "查看历史记录":
                        startActivity(new Intent(MainActivity.this,RecordActivity.class));
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


        /* 侧滑菜单 头部布局的监听*/
        View headerView = navigationView.getHeaderView(0);
        ImageView iv_icon = headerView.findViewById(R.id.iv_icon);
        TextView tv_name = headerView.findViewById(R.id.tv_name);

        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(this);
        tv_name.setText(user.get("username"));

        iv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.show(MainActivity.this, "点击了头像");
            }
        });
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


        /*  要点：toolbar的点击监听分为了两个部分，
        * 下面监听右边部分*/
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
//                        ToastUtil.show(MainActivity.this, "点击了share");
                        break;
                }
                return true;
            }
        });
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* 将toolbar的菜单布局文件实例化*/
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /* 要点：toolbar的点击监听分为了两个部分，
    一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
    另一个是它右边的文字（这个是我们自定义的菜单所有的）
    左边的点击事件通过下面的方式*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /* 点击toolbar的左边图标 弹出侧滑菜单*/
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRelease() {

    }
}
