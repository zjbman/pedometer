package cn.bluemobi.dylan.pedometer.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import java.util.List;
import java.util.Map;

import butterknife.Bind;
import cn.bluemobi.dylan.pedometer.R;
import cn.bluemobi.dylan.pedometer.activity.base.BaseActivity;
import cn.bluemobi.dylan.pedometer.app.ActivityManager;
import cn.bluemobi.dylan.pedometer.step.bean.UserData;
import cn.bluemobi.dylan.pedometer.step.utils.DbUtils;
import cn.bluemobi.dylan.pedometer.util.SharedpreferencesUtil;
import cn.bluemobi.dylan.pedometer.util.StringUtil;
import cn.bluemobi.dylan.pedometer.util.ToastUtil;

public class LoginActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tl_username)
    TextInputLayout tl_username;
    @Bind(R.id.tl_password)
    TextInputLayout tl_password;
    @Bind(R.id.btn_login)
    Button btn_login;

    private String username;
    private String password;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_login, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        /* 设定布局中的toolbar*/
        toolbar.setTitle("欢迎登录");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(this);
        if (!StringUtil.isEmpty(user.get("username"))) {
            username = user.get("username");
            tl_username.getEditText().setText(username);
        }
        if (!StringUtil.isEmpty(user.get("password"))) {
            password = user.get("password");
            tl_password.getEditText().setText(password);
            queryDB();
        }
    }

    @Override
    protected void setListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_register:
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                        ActivityManager.getInstance().removeActivity(LoginActivity.this);
                        break;
                }
                return true;
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {
                    queryDB();
                }
            }
        });
    }

    private void queryDB() {
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "user");
        }
        List<UserData> userDataList =DbUtils.getQueryAll(UserData.class);
        boolean isLogin = false;
        if(userDataList != null && userDataList.size() > 0) {
            for (UserData userData : userDataList ) {
                String username1 = userData.getUsername();
                String password1 = userData.getPassword();

                if (username.equals(username1)) {
                    if (password.equals(password1)) {
                        ToastUtil.show(this, "登录成功！");
                        cacheUser();
                        startActivity(new Intent(this, MainActivity.class));
                        ActivityManager.getInstance().removeActivity(this);
                        isLogin = true;
                        break;
                    }
                }
            }
            if (!isLogin) {
                ToastUtil.show(this, "账号或密码不正确！");
            }
        }
    }

    private void cacheUser() {
        SharedpreferencesUtil.getInstance().cacheUser(this, username, password);
    }

    private boolean check() {
        username = tl_username.getEditText().getText().toString();
        password = tl_password.getEditText().getText().toString();

        if (!StringUtil.isEmpty(username) && !StringUtil.isEmpty(password)) {
            return true;
        } else {
            ToastUtil.show(this, "请输入完整信息");
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         /* 将toolbar的菜单布局文件实例化*/
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    /* 要点：toolbar的点击监听分为了两个部分，
       一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
       另一个是它右边的文字（这个是我们自定义的菜单所有的）
       左边的点击事件通过下面的方式*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
             /* 点击toolbar 返回主界面*/
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class));
                ActivityManager.getInstance().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRelease() {

    }
}
