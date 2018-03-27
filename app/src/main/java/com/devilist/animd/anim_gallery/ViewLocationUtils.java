package com.devilist.animd.anim_gallery;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.GridView;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by zengp on 2017/11/3.
 */

public class ViewLocationUtils {

    public static int[] findViewLocation(View view) {
        final int[] location = new int[2];
        view.getLocationOnScreen(location);
        int statusBarH = getStatusBarHeightCompat(view.getContext());
        return new int[]{location[0], location[1] - statusBarH, view.getWidth(), view.getHeight()};
    }

    public static ArrayList<int[]> findItemViewLocations(RecyclerView parent) {
        if (parent.getAdapter() == null) return null;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (null == layoutManager) return null;
        int statusBarH = getStatusBarHeightCompat(parent.getContext());
        int totalCount = parent.getAdapter().getItemCount();
        int firstVisiblePosition = 0, lastVisiblePosition = 0;
        ArrayList<int[]> locations = new ArrayList<>();
        if (layoutManager instanceof LinearLayoutManager) {
            firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

        }
        // 前面不可见的，位置居中
        if (firstVisiblePosition > 0) {
            View child = parent.getChildAt(0);
            final int[] location = new int[2];
            child.getLocationOnScreen(location);
            for (int k = 0; k < firstVisiblePosition; k++) {
                locations.add(new int[]{location[0], location[1] - statusBarH, 0, 0});
            }
        }
        // 中间可见的
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final int[] location = new int[2];
            child.getLocationOnScreen(location);
            locations.add(new int[]{location[0], location[1] - statusBarH, child.getWidth(), child.getHeight()});
        }
        // 后面不可见的，居中
        if (lastVisiblePosition < totalCount-1) {
            final int[] loc = locations.get(locations.size() - 1);
            locations.add(new int[]{loc[0] + loc[2], loc[1] + loc[3] - statusBarH, 0, 0});
        }
        return locations;
    }

    public static ArrayList<int[]> findItemViewLocations(GridView parent) {
        int statusBarH = getStatusBarHeightCompat(parent.getContext());
        ArrayList<int[]> locations = new ArrayList<>();
        // 前面不可见的，位置居中
        int firstVisiblePosition = parent.getFirstVisiblePosition();
        if (firstVisiblePosition > 0) {
            View child = parent.getChildAt(0);
            final int[] location = new int[2];
            child.getLocationOnScreen(location);
            for (int k = 0; k < firstVisiblePosition; k++) {
                locations.add(new int[]{parent.getWidth() / 2, location[1] - statusBarH, 0, 0});
            }
        }
        // 中间可见的
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            final int[] location = new int[2];
            child.getLocationOnScreen(location);
            locations.add(new int[]{location[0], location[1] - statusBarH, child.getWidth(), child.getHeight()});
        }
        // 后面不可见的，居中
        int lastVisiblePosition = parent.getLastVisiblePosition();
        if (lastVisiblePosition < parent.getAdapter().getCount() - 1) {
            final int[] loc = locations.get(locations.size() - 1);
            locations.add(new int[]{parent.getWidth() / 2, loc[1] + loc[3] - statusBarH, 0, 0});
        }
        return locations;
    }

    private static int getStatusBarHeightCompat(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getStatusBarHeight(context);
        } else return 0;
    }

    /**
     * 状态栏高度
     *
     * @param context
     * @return
     */
    private static int getStatusBarHeight(Context context) {
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
