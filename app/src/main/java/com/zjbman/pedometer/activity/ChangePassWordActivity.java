package com.zjbman.pedometer.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.zjbman.pedometer.R;
import com.zjbman.pedometer.activity.base.BaseActivity;
import com.zjbman.pedometer.app.ActivityManager;
import com.zjbman.pedometer.db.MySQLiteOpenHelper;
import com.zjbman.pedometer.util.SharedpreferencesUtil;
import com.zjbman.pedometer.util.StringUtil;
import com.zjbman.pedometer.util.ToastUtil;

import java.util.Map;

import butterknife.Bind;

public class ChangePassWordActivity extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_save)
    Button btn_save;
    @Bind(R.id.tl_old)
    TextInputLayout tl_old;
    @Bind(R.id.tl_password)
    TextInputLayout tl_password;
    @Bind(R.id.tl_password_again)
    TextInputLayout tl_password_again;

    private String oldPassword;
    private String newPassword;
    private String newPasswordAgain;

    private String username;
    private String password;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_change_pass_word, null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
         /* 设定布局中的toolbar*/
        toolbar.setTitle("修改密码");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Map<String, String> user = SharedpreferencesUtil.getInstance().getUser(this);
        username = user.get("username");
        password = user.get("password");
    }

    @Override
    protected void setListener() {


        tl_password_again.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals(tl_password.getEditText().getText().toString())) {
                    tl_password_again.setHint("两次输入的密码不一致");
                } else {
                    tl_password_again.setHint("密码通过");
                }
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check()) {

                    updateDB();

                    /** 成功或失败都清空密码,避免用户连续点击注册按钮而一直请求服务器*/
                    tl_old.getEditText().setText("");
                    tl_password.getEditText().setText("");
                    tl_password_again.getEditText().setText("");
                    tl_password_again.setHint("请跟上面输入的密码保持一致,必填");

                }
            }
        });
    }

    private void updateDB() {
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
        SQLiteDatabase database = helper.getWritableDatabase();

        boolean isPass = true;
        Cursor cursor = database.query("user", null, "username=?", new String[]{username}, null, null, null);
        while (cursor.moveToNext()) {
            String pw = cursor.getString(cursor.getColumnIndex("password"));
            if(!pw.equals(oldPassword)){
                ToastUtil.show(this,"旧密码不正确！");
                isPass = false;
                break;
            }
        }
        if(isPass) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put("password", newPassword);
                database.update("user", contentValues, "username=?", new String[]{username});

                ToastUtil.show(this, "成功修改密码！");
                SharedpreferencesUtil.getInstance().cacheUser(this,username,newPassword);
//                startActivity(new Intent(this, MainActivity.class));
                ActivityManager.getInstance().removeActivity(this);
            } catch (Exception e) {
                ToastUtil.show(this, "修改密码失败！");
                e.printStackTrace();
            }
        }
    }


    private boolean check() {
        oldPassword = tl_old.getEditText().getText().toString();
        newPassword = tl_password.getEditText().getText().toString();
        newPasswordAgain = tl_password_again.getEditText().getText().toString();


        if (StringUtil.isEmpty(oldPassword) || StringUtil.isEmpty(newPassword) || StringUtil.isEmpty(newPasswordAgain)) {
            ToastUtil.show(ChangePassWordActivity.this, "请填写完整信息");
            return false;
        }

        if (!newPassword.equals(newPasswordAgain)) {
            ToastUtil.show(ChangePassWordActivity.this, "两次输入的新密码不一致");
            return false;
        }

        return true;
    }

    /* 要点：toolbar的点击监听分为了两个部分，
      一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
      另一个是它右边的文字（这个是我们自定义的菜单所有的）
      左边的点击事件通过下面的方式 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
             /* 点击toolbar 返回主界面*/
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("position",4);
                startActivity(intent);
                ActivityManager.getInstance().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRelease() {

    }
}
