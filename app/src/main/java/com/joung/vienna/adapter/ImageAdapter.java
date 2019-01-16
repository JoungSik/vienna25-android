package com.joung.vienna.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.joung.vienna.R;
import com.squareup.picasso.Picasso;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private String[] images = new String[]{
            "image_1", "image_2", "image_3"
    };

    private Context mContext;

    public ImageAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image)
        ImageView mImageView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(int position) {
            String image = images[position];

            // String[] textImages = mContext.getResources().getStringArray(R.array.text_images);
            int resourceID = mContext.getResources().getIdentifier(image, "drawable",
                    mContext.getPackageName());

            Picasso.get().load(resourceID).into(mImageView);
        }
    }
}
