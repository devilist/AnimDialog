package com.devilist.animdialog;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by zengp on 2017/12/8.
 */

public class CircleAnimDialog extends BaseAnimDialog {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setEnterAnimDuration(1500);
            setExitAnimDuration(600);
        }
    }

    @Override
    public void doEnterAnim(View contentView, long animDuration) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // circle
            Animator circeAnimator = ViewAnimationUtils.createCircularReveal(
                    contentView, contentView.getWidth() / 2, contentView.getHeight() / 2,
                    0, 3 * contentView.getHeight() / 2);
            circeAnimator.setInterpolator(new DecelerateInterpolator());
            circeAnimator.setDuration(animDuration);
            circeAnimator.start();
        } else {
            super.doEnterAnim(contentView, animDuration);
        }
    }

    @Override
    public void doExitAnim(View contentView, long animDuration) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // dim
            ValueAnimator dimAnimator = ValueAnimator.ofFloat(0.7f, 0);
            dimAnimator.setDuration(animDuration - 100 < 0 ? animDuration : animDuration - 100);
            dimAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = (float) animation.getAnimatedValue();
                    if (null != getDialog() && null != getDialog().getWindow())
                        getDialog().getWindow().setDimAmount(offset);
                }
            });
            dimAnimator.start();
            // circle
            Animator circeAnimator = ViewAnimationUtils.createCircularReveal(
                    contentView, contentView.getWidth() / 2, contentView.getHeight() / 2,
                    3 * contentView.getHeight() / 2, 0);
            circeAnimator.setInterpolator(new DecelerateInterpolator());
            circeAnimator.setDuration(animDuration);
            circeAnimator.start();
        } else {
            super.doExitAnim(contentView, animDuration);
        }
    }
}
