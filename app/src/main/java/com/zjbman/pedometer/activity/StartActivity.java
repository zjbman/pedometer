package com.zjbman.pedometer.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.zjbman.pedometer.R;
import com.zjbman.pedometer.activity.base.BaseActivity;
import com.zjbman.pedometer.app.ActivityManager;

import butterknife.Bind;

public class StartActivity extends BaseActivity {

    @Bind(R.id.relativelayout)
    RelativeLayout rootView;

    @Override
    protected View setContentView() {
        return View.inflate(this, R.layout.activity_start,null);
    }

    @Override
    protected Activity bindActivity() {
        return this;
    }

    @Override
    protected void initView() {
        StartAnimation();
    }

    /**
     * 开始动画
     */
    private void StartAnimation() {
        AlphaAnimation animation = new AlphaAnimation(0.0f,1.0f);
        animation.setDuration(2000);
        rootView.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //动画结束，跳转到MainActivity
                StartActivity.this.startActivity(new Intent(StartActivity.this,LoginActivity.class));
                ActivityManager.getInstance().removeActivity(StartActivity.this);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void onRelease() {
        ActivityManager.getInstance().listActivity();
    }
}
