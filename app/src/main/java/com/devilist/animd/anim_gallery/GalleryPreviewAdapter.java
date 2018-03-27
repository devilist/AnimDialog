package com.devilist.animd.anim_gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.devilist.animd.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 照片墙
 * Created by zengpu on 2016/11/11.
 */
public class GalleryPreviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> data = new ArrayList<>();
    private int layoutResId;
    private OnItemClickListener mOnItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView item;
        private OnItemClickListener mListener;

        public ViewHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            mListener = listener;
            item = (ImageView) itemView.findViewById(R.id.iv_item_photo);
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != mListener)
                mListener.onItemClick(v, getLayoutPosition());
        }
    }

    public GalleryPreviewAdapter(Context context, int layoutResId, List<String> data) {
        this.context = context;
        this.data = data;
        this.layoutResId = layoutResId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(layoutResId, parent, false);
        return new ViewHolder(v, mOnItemClickListener);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mViewHolder = (ViewHolder) holder;
        mViewHolder.setIsRecyclable(false);
        final ImageView itemView = mViewHolder.item;
        int id = context.getResources().getIdentifier(data.get(position),
                "string", context.getPackageName());
        itemView.setImageResource(id);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
}