package com.devilist.animd.anim_gallery;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;


import com.devilist.animd.R;
import com.devilist.animdialog.BaseAnimDialog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zengp on 2017/11/3.
 */

public class GalleryDialog extends BaseAnimDialog implements
        GalleryPreviewAdapter.OnItemClickListener,
        RecyclerViewPager.OnPageSelectListener {

    private TextView tv_title;
    private RecyclerViewPager viewPager;
    private GalleryPreviewAdapter adapter;
    private List<String> photoList = new ArrayList<>();
    private int currentPosition = 1;
    ArrayList<int[]> locations = new ArrayList<>();
    private int width, height;

    public static GalleryDialog newInstance(List<String> photoList, int currentPosition,
                                            ArrayList<int[]> locations) {
        Bundle args = new Bundle();
        GalleryDialog fragment = new GalleryDialog(photoList, currentPosition, locations);
        fragment.setArguments(args);
        return fragment;
    }

    public static GalleryDialog newInstance(List<String> photoList, int position) {
        Bundle args = new Bundle();
        GalleryDialog fragment = new GalleryDialog(photoList, position);
        fragment.setArguments(args);
        return fragment;
    }

    private GalleryDialog(List<String> photoList, int position) {
        this(photoList, position, null);
    }

    private GalleryDialog(List<String> photoList, int currentPosition, ArrayList<int[]> locations) {
        this.photoList = photoList;
        this.currentPosition = currentPosition;
        this.locations = locations;
        setEnterAnimDuration(500);
        setExitAnimDuration(400);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Point realPoint = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getActivity().getWindowManager().getDefaultDisplay().getRealSize(realPoint);
        } else {
            getActivity().getWindowManager().getDefaultDisplay().getSize(realPoint);
        }
        width = realPoint.x;
        height = realPoint.y;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Window window = getDialog().getWindow();
        getDialog().getWindow().setDimAmount(0);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = inflater.inflate(R.layout.dialog_gallery, container, false);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(width, height);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            getView().findViewById(R.id.iv_statusbar).setVisibility(View.GONE);
        }
        tv_title = getView().findViewById(R.id.tv_title);
        viewPager = getView().findViewById(R.id.rvp_list);
        adapter = new GalleryPreviewAdapter(getContext(), R.layout.item_photowall, photoList);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageSelectListener(this);
        adapter.setOnItemClickListener(this);
        viewPager.setCurrentPage(currentPosition - 1);
        tv_title.setText(currentPosition + " / " + photoList.size());
        getView().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        dismiss();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset) {
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position + 1;
        tv_title.setText(currentPosition + " / " + photoList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void doEnterAnim(final View contentView, long animDuration) {
        if (null != locations && locations.size() > 0) {
            final int[] startLoc = locations.get(currentPosition - 1);
            final ImageView child = viewPager.findViewHolderForLayoutPosition(currentPosition - 1)
                    .itemView.findViewById(R.id.iv_item_photo);
            final ImageView.ScaleType oriScype = child.getScaleType();
            final int[] endLoc = ViewLocationUtils.findViewLocation(child);
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float p = (float) animation.getAnimatedValue();
                    float p0 = 0;
                    if (child.getDrawable() != null) {
                        // 修正位置
                        float ratio = width * 1f / child.getDrawable().getIntrinsicWidth();
//                        int endW = child.getDrawable().getIntrinsicWidth();
//                        int endH = child.getDrawable().getIntrinsicHeight();
                        int endW = width;
                        int endH = (int) (child.getDrawable().getIntrinsicHeight() * ratio);
                        if (endH != endLoc[3] || endW != endLoc[2]) {
                            // refresh location
                            endLoc[0] = (endLoc[2] - endW) / 2;
                            endLoc[1] = (endLoc[3] - endH) / 2;
                            endLoc[2] = endW;
                            endLoc[3] = endH;
                            p0 = p;
                        }
                        if (p0 == 1) p0 = 0;
                        // y= (p - p0) * (end - start) / (1 - p0) + start
                        float currentLeft = (p - p0) * (endLoc[0] - startLoc[0]) / (1 - p0) + startLoc[0];
                        float currentTop = (p - p0) * (endLoc[1] - startLoc[1]) / (1 - p0) + startLoc[1];
                        float currentWidth = (p - p0) * (endLoc[2] - startLoc[2]) / (1 - p0) + startLoc[2];
                        float currentHeight = (p - p0) * (endLoc[3] - startLoc[3]) / (1 - p0) + startLoc[3];
                        child.getLayoutParams().width = (int) currentWidth;
                        child.getLayoutParams().height = (int) currentHeight;
                        child.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        child.requestLayout();
                        child.setTranslationX(currentLeft);
                        child.setTranslationY(currentTop);
                    }
                    contentView.setBackgroundColor(((int) (p * p * 0xff)) << 24);
                    child.setEnabled(p >= 0.99);
                    if (p >= 1) {
                        child.getLayoutParams().width = width;
                        child.getLayoutParams().height = height;
                        child.setScaleType(oriScype);
                        child.setImageDrawable(child.getDrawable());
                        child.requestLayout();
                        child.setTranslationX(0);
                        child.setTranslationY(0);
                    }
                }
            });
            animator.setDuration(animDuration);
            animator.start();
        } else
            super.doEnterAnim(contentView, animDuration);
    }

    @Override
    public void doExitAnim(final View contentView, long animDuration) {
        if (null != locations && locations.size() > 0) {
            final int[] endLocation;
            if (currentPosition <= locations.size())
                endLocation = locations.get(currentPosition - 1);
            else {
                endLocation = new int[4];
                endLocation[0] = locations.get(locations.size() - 1)[0] + locations.get(locations.size() - 1)[2];
                endLocation[1] = locations.get(locations.size() - 1)[1] + locations.get(locations.size() - 1)[3];
                endLocation[2] = 0;
                endLocation[3] = 0;
            }
            final ImageView child = viewPager.getChildAt(0).findViewById(R.id.iv_item_photo);
            final int[] startLocation = ViewLocationUtils.findViewLocation(child);
            // 修正位置
            float ratio = width * 1f / child.getDrawable().getIntrinsicWidth();
            int startW = width;
            int startH = (int) (child.getDrawable().getIntrinsicHeight() * ratio);
            startLocation[0] = (startLocation[2] - startW) / 2;
            startLocation[1] = (startLocation[3] - startH) / 2;
            startLocation[2] = startW;
            startLocation[3] = startH;
            ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float p = (float) animation.getAnimatedValue();
                    float currentLeft = p * (endLocation[0] - startLocation[0]) + startLocation[0];
                    float currentTop = p * (endLocation[1] - startLocation[1]) + startLocation[1];
                    float currentWidth = p * (endLocation[2] - startLocation[2]) + startLocation[2];
                    float currentHeight = p * (endLocation[3] - startLocation[3]) + startLocation[3];
                    child.getLayoutParams().width = (int) currentWidth;
                    child.getLayoutParams().height = (int) currentHeight;
                    child.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    child.requestLayout();
                    child.setTranslationX(currentLeft);
                    child.setTranslationY(currentTop);
                    child.setEnabled(false);
                    if (null != contentView)
                        contentView.setBackgroundColor(((int) ((1 - p) * 0xff)) << 24);
                }
            });
            animator.setDuration(animDuration);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.start();
        } else
            super.doExitAnim(contentView, animDuration);
    }
}
