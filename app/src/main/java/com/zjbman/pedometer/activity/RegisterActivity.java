package com.zjbman.pedometer.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.zjbman.pedometer.R;
import com.zjbman.pedometer.activity.base.BaseActivity;
import com.zjbman.pedometer.app.ActivityManager;
import com.zjbman.pedometer.db.MySQLiteOpenHelper;
import com.zjbman.pedometer.util.StringUtil;
import com.zjbman.pedometer.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;

public class RegisterActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tl_username__register)
    TextInputLayout tl_username__register;
    @Bind(R.id.tl_password__register)
    TextInputLayout tl_password__register;
    @Bind(R.id.tl_password_again_register)
    TextInputLayout tl_password_again_register;
    @Bind(R.id.tl_email_register)
    TextInputLayout tl_email_register;
    @Bind(R.id.tl_qq_register)
    TextInputLayout tl_qq_register;
    @Bind(R.id.btn_login)
    Button btn_login;

    private String username;
    private String password;
    private String passwordAgain;
    private String email;
    private String qq;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_register,null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        /* 设定布局中的toolbar*/
        toolbar.setTitle("欢迎注册");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    protected void setListener() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_login:
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        ActivityManager.getInstance().removeActivity(RegisterActivity.this);
                        break;
                }
                return true;
            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(check()){
                    insertDB();
                }
            }
        });
    }

    private void insertDB() {
        if(StringUtil.isEmpty(email)){
            email = "zjbman@sina.com";
        }
        if(StringUtil.isEmpty(qq)){
            qq = "825303675";
        }

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "insert into user (username,password,email,qq,create_date) values (" +
                "" + username +", " + password +"," + email +"," + qq + "," + format.format(new Date()) + ")";

        try {
            ContentValues cValue = new ContentValues();
            cValue.put("username",username);
            cValue.put("password",password);
            cValue.put("email",email);
            cValue.put("qq",qq);
            cValue.put("create_date",format.format(new Date()));

            database.insert("user",null,cValue);

            ToastUtil.show(this,"注册成功!");
            startActivity(new Intent(this,LoginActivity.class));
            ActivityManager.getInstance().removeActivity(this);
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.show(this,"注册出现未知错误，请重新注册!");
        }
    }

    private boolean check() {
        username = tl_username__register.getEditText().getText().toString();
        password = tl_password__register.getEditText().getText().toString();
        passwordAgain = tl_password_again_register.getEditText().getText().toString();
        email = tl_email_register.getEditText().getText().toString();
        qq = tl_qq_register.getEditText().getText().toString();

        if(StringUtil.isEmpty(username) || StringUtil.isEmpty(password) || StringUtil.isEmpty(passwordAgain)){
            ToastUtil.show(this,"信息填写不完整");
            return false;
        }

        if(!password.equals(passwordAgain)){
            ToastUtil.show(this,"两次密码填写不一致");
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         /* 将toolbar的菜单布局文件实例化*/
        getMenuInflater().inflate(R.menu.register, menu);
        return true;
    }

    /* 要点：toolbar的点击监听分为了两个部分，
   一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
   另一个是它右边的文字（这个是我们自定义的菜单所有的）
   左边的点击事件通过下面的方式*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
             /* 点击toolbar 返回主界面*/
            case android.R.id.home:
                startActivity(new Intent(this,MainActivity.class));
                ActivityManager.getInstance().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRelease() {

    }
}
