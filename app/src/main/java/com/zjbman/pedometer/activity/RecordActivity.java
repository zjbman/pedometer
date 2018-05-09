package com.zjbman.pedometer.activity;

import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.zjbman.pedometer.R;
import com.zjbman.pedometer.activity.base.BaseActivity;
import com.zjbman.pedometer.adapter.CommonAdapter;
import com.zjbman.pedometer.adapter.CommonViewHolder;
import com.zjbman.pedometer.app.ActivityManager;
import com.zjbman.pedometer.step.bean.StepData;
import com.zjbman.pedometer.step.utils.DbUtils;

import java.util.List;

import butterknife.Bind;

public class RecordActivity extends BaseActivity {

    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

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
        initToolbar();
        initData();
    }

    private void initToolbar() {
        /* 设定布局中的toolbar*/
        toolbar.setTitle("历史记录");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        setEmptyView(listView);
        if(DbUtils.getLiteOrm()==null){
            DbUtils.createDb(this, "jingzhi");
        }
        List<StepData> stepDatas =DbUtils.getQueryAll(StepData.class);
        Logger.d("stepDatas="+stepDatas);
        listView.setAdapter(new CommonAdapter<StepData>(this,stepDatas,R.layout.item) {
            @Override
            protected void convertView(View item, StepData stepData) {
                TextView tv_date= CommonViewHolder.get(item,R.id.tv_date);
                TextView tv_step= CommonViewHolder.get(item,R.id.tv_step);
                tv_date.setText(stepData.getToday());
                tv_step.setText(stepData.getStep()+"步");
            }
        });
    }

    protected <T extends View> T setEmptyView(ListView listView) {
        TextView emptyView = new TextView(this);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyView.setText("暂无数据！");
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        emptyView.setVisibility(View.GONE);
        ((ViewGroup) listView.getParent()).addView(emptyView);
        listView.setEmptyView(emptyView);
        return (T) emptyView;
    }

    @Override
    protected void setListener() {

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
