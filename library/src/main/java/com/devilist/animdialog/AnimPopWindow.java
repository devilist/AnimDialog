/*
 * Copyright  2017  zengpu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.devilist.animdialog;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.PopupWindow;

/**
 * popwindow with custom enter and exit anim
 * Created by zengp on 2017/9/1.
 */

public class AnimPopWindow extends PopupWindow implements IAnimCreator, Runnable {

    private Context mContext;
    private long mEnterAnimDuration = 300, mExitAnimDuration = 300;
    // scale anim start position (x y). use for default anims or any custom scale anims if needed
    private float mScaleAnimStartPosX = 0.5f, mScaleAnimStartPosY = 0.5f;

    public AnimPopWindow(Context context, View contentView) {
        this(context, contentView, 0.5f, 0.5f);
    }

    public AnimPopWindow(Context context, View contentView, float scaleAnimStartPosX, float scaleAnimStartPosY) {
        super(context);
        mContext = context;
        mScaleAnimStartPosX = scaleAnimStartPosX;
        mScaleAnimStartPosY = scaleAnimStartPosY;
        // must set mScreenWidth and height here
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setContentView(contentView);
    }

    public void setEnterAnimDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Anim duration cannot be negative");
        }
        this.mEnterAnimDuration = duration;
    }

    public void setExitAnimDuration(long duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Anim duration cannot be negative");
        }
        this.mExitAnimDuration = duration;
    }

    @Override
    public void doEnterAnim(View contentView, long animDuration) {
        // default enter anim;
        // you can custom the anim whatever you like by overriding this method
        ScaleAnimation scaleAnimation = new ScaleAnimation(0F, 1.0F, 0F, 1.0F, Animation.RELATIVE_TO_PARENT,
                mScaleAnimStartPosX, Animation.RELATIVE_TO_PARENT, mScaleAnimStartPosY);
        scaleAnimation.setDuration(animDuration);
        contentView.startAnimation(scaleAnimation);

        final Window window = ((Activity) mContext).getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        ValueAnimator dimAnimator = ValueAnimator.ofFloat(1f, 0.3f);
        dimAnimator.setDuration(animDuration - 100 < 0 ? animDuration : animDuration - 100);
        dimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float offset = (float) animation.getAnimatedValue();
                lp.alpha = offset;
                window.setAttributes(lp);
            }
        });
        dimAnimator.start();

    }

    @Override
    public void doExitAnim(View contentView, long animDuration) {
        // default exit anim;
        // you can custom the anim whatever you like by overriding this method
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0F, 0.0F, 1.0F, 0.0F, Animation.RELATIVE_TO_PARENT,
                mScaleAnimStartPosX, Animation.RELATIVE_TO_PARENT, mScaleAnimStartPosY);
        scaleAnimation.setDuration(animDuration);
        contentView.startAnimation(scaleAnimation);

        final Window window = ((Activity) mContext).getWindow();
        ValueAnimator dimAnimator = ValueAnimator.ofFloat(0.3f, 1f);
        dimAnimator.setDuration(animDuration - 100 < 0 ? animDuration : animDuration - 100);
        dimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float offset = (float) animation.getAnimatedValue();
                if (null != window) {
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.alpha = offset;
                    window.setAttributes(lp);
                }
            }
        });
        dimAnimator.start();
    }

    @Override
    final public void setContentView(View contentView) {
        doEnterAnim(contentView, mEnterAnimDuration);
        super.setContentView(contentView);
    }

    @Override
    final public void dismiss() {
        doExitAnim(getContentView(), mExitAnimDuration);
        getContentView().postDelayed(this, mExitAnimDuration);
    }

    @Override
    final public void run() {
        super.dismiss();
    }

}
