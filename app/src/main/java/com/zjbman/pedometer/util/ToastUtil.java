package com.zjbman.pedometer.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zjbman
 * on 2018/4/3.
 */

public class ToastUtil {
    public static void show(Context context,String content){
        Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }
}
