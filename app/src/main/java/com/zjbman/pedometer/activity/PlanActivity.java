package com.zjbman.pedometer.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zjbman.pedometer.R;
import com.zjbman.pedometer.activity.base.BaseActivity;
import com.zjbman.pedometer.app.ActivityManager;
import com.zjbman.pedometer.step.utils.SharedPreferencesUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;

public class PlanActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_save)
    Button btn_save;
    @Bind(R.id.tv_remind_time)
    TextView tv_remind_time;
    @Bind(R.id.cb_remind)
    CheckBox cb_remind;
    @Bind(R.id.tv_step_number)
    TextView tv_step_number;

    private SharedPreferencesUtils sp;

    private String walk_qty;
    private String remind;
    private String achieveTime;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_plan,null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        initToolbar();
        initData();
    }

    /**
     * 获取锻炼计划
     */
    public void initData() {
        sp = new SharedPreferencesUtils(this);
        String planWalk_QTY = (String) sp.getParam("planWalk_QTY", "7000");
        String remind = (String) sp.getParam("remind", "1");
        String achieveTime = (String) sp.getParam("achieveTime", "20:00");
        if (!planWalk_QTY.isEmpty()) {
            if ("0".equals(planWalk_QTY)) {
                tv_step_number.setText("7000");
            } else {
                tv_step_number.setText(planWalk_QTY);
            }
        }
        if (!remind.isEmpty()) {
            if ("0".equals(remind)) {
                cb_remind.setChecked(false);
            } else if ("1".equals(remind)) {
                cb_remind.setChecked(true);
            }
        }

        if (!achieveTime.isEmpty()) {
            tv_remind_time.setText(achieveTime);
        }

    }

    private void initToolbar() {
        /* 设定布局中的toolbar*/
        toolbar.setTitle("锻炼计划");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setListener() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        tv_remind_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog1();
            }
        });
    }

    private void save() {
        walk_qty = tv_step_number.getText().toString().trim();
        if (cb_remind.isChecked()) {
            remind = "1";
        } else {
            remind = "0";
        }
        achieveTime = tv_remind_time.getText().toString().trim();
        if (walk_qty.isEmpty() || "0".equals(walk_qty)) {
            sp.setParam("planWalk_QTY", "7000");
        } else {
            sp.setParam("planWalk_QTY", walk_qty);
        }
        sp.setParam("remind", remind);

        if (achieveTime.isEmpty()) {
            sp.setParam("achieveTime", "21:00");
            this.achieveTime = "21:00";
        } else {
            sp.setParam("achieveTime", achieveTime);
        }
        ActivityManager.getInstance().removeActivity(this);
    }


    private void showTimeDialog1() {
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        final DateFormat df = new SimpleDateFormat("HH:mm");
//        Date date = null;
//        try {
//            date = df.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (null != date) {
//            calendar.setTime(date);
//        }
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String remaintime = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                Date date = null;
                try {
                    date = df.parse(remaintime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (null != date) {
                    calendar.setTime(date);
                }
                tv_remind_time.setText(df.format(date));
            }
        }, hour, minute, true).show();
    }

    @Override
    protected void onRelease() {

    }


    /**
     * 要点：toolbar的点击监听分为了两个部分，
     * 一个是它左边的图标（这是系统自动生成的，如果它前面还有activity，图标是<—，id是系统分配的android.R.id.home） ；
     * 另一个是它右边的文字（这个是我们自定义的菜单所有的）
     * 左边的点击事件通过下面的方式
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityManager.getInstance().removeActivity(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
