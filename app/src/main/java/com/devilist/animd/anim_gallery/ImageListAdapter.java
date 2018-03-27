package com.devilist.animd.anim_gallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.devilist.animd.R;

import java.util.List;

/**
 * Created by zengp on 2017/7/8.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private int mScreenWidth;
    private int mSpanCount = 3;
    private List<String> data;
    private Context context;

    public ImageListAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_image_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        setContentViewSize(holder.itemView);
        int id = context.getResources().getIdentifier(data.get(position),
                "string", context.getPackageName());
        holder.item.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private void setContentViewSize(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params.width != mScreenWidth / mSpanCount) {
            params.width = params.height = mScreenWidth / mSpanCount;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = (ImageView) itemView.findViewById(R.id.iv_item_photo);
        }
    }
}
