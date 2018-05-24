package com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public RelativeLayout button;

    public ItemViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        button = view.findViewById(R.id.btn_belajar);
    }

    public void setTvTitleCat(String titleCat) {
        TextView tvTitleCat = view.findViewById(R.id.title_cat);
        tvTitleCat.setText(titleCat);
    }

    public void setTvTitleListMateri(String title) {
        TextView tvTitleListMateri = view.findViewById(R.id.title_list_materi);
        tvTitleListMateri.setText(title);
    }

    public void setTvSubtitleListMateri (String body) {
        TextView tvSubtitleListMateri = view.findViewById(R.id.subtitle_list_materi);
        tvSubtitleListMateri.setText(body);
    }

    public void setImgMateri(Context context, String img) {
        ImageView imgMateri = view.findViewById(R.id.img_materi);
        Glide.with(context)
                .load(img)
                .into(imgMateri);
    }

    public void setImgCat (Context context, String imgCat) {
        ImageView imgCategory = view.findViewById(R.id.img_cat);
        Glide.with(context)
                .load(imgCat)
                .into(imgCategory);
    }
}
