package com.utsman.kucingapes.belajarsejarah;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


class ItemViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public Button button;

    ItemViewHolder(View itemView) {
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

    public void setImgMateri(Context context, String img) {
        ImageView imgMateri = view.findViewById(R.id.img_materi);
        Glide.with(context).load(img).into(imgMateri);
    }
}
