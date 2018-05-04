package com.zjbman.pedometer.activity;

import android.app.Activity;
import android.view.View;

import com.zjbman.pedometer.R;
import com.zjbman.pedometer.activity.base.BaseActivity;

public class RecordActivity extends BaseActivity {


    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_record,null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void onRelease() {

    }
}
