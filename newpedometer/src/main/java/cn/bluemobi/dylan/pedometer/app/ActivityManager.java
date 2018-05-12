package cn.bluemobi.dylan.pedometer.app;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

/**
 * Activity的管理类
 *
 * Created by zjbman
 * on 2018/3/15.
 */

public class ActivityManager {
    private static ActivityManager manager;
    private static Stack<Activity> stack;

    private ActivityManager(){
        stack = new Stack<>();
    }

    public static ActivityManager getInstance(){
        if(manager == null ){
            synchronized (ActivityManager.class){
                if(manager == null ){
                    manager = new ActivityManager();
                }
            }
        }
        return manager;
    }

    /**
     * 往Activity管理者添加Activity，纳入管理
     */
    public void addActivity(Activity activity){
        if(activity != null){
            stack.add(activity);
        }
    }

    /**
     * 往Activity管理者移除指定的Activity
     * @param activity
     */
    public void removeActivity(Activity activity){
        if(activity != null){
            for(int i = stack.size() - 1; i >= 0;i--){
                if(activity.equals(stack.get(i))){
                    stack.remove(i);
                    activity.finish();
                }
            }
        }
    }

    /**
     * 移除所有的Activity
     */
    public void removeAll(){
        for(int i = stack.size() - 1; i >= 0;i--){
            stack.get(i).finish();
            stack.remove(i);
        }
    }

    /**
     * 获取当前的Activity数量
     * @return
     */
    public int getSize(){
        return stack.size();
    }

    /**
     * 列出所有的Activity
     */
    public void listActivity(){
        Log.e("当前Activity数量为  ", getSize() + " ");
        for(int i = stack.size() - 1; i >= 0;i--){
            Log.e("listActivity ", stack.get(i).getClass().getSimpleName() + "\r\n");
        }
    }
}
