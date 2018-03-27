package com.devilist.animd.circle_anim_dialog;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.devilist.animd.R;
import com.devilist.animdialog.CircleAnimDialog;

/**
 * 选择咨询弹窗
 * Created by zengp on 2017/12/21.
 */

public class CircleDialog extends CircleAnimDialog {


    public static CircleDialog newInstance() {

        Bundle args = new Bundle();
        CircleDialog fragment = new CircleDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.getAttributes().dimAmount = 0;
        View contentView = inflater.inflate(R.layout.dialog_circle_anim_dialog, container, false);
        return contentView;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(mScreenWidth, mScreenHeight);
    }

    @Override
    protected void initView() {

    }
}

