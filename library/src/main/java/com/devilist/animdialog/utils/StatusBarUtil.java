package com.devilist.animdialog.utils;

import android.content.Context;

import java.lang.reflect.Field;

/**
 * Created by zengp on 2018/3/9.
 */

public class StatusBarUtil {

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {
            sbar = 0;
            e1.printStackTrace();
        }
        return sbar;
    }
}
