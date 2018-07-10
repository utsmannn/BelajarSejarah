package com.utsman.kucingapes.mobilelearningprodisejarah.Favorit;

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
    public TextView tvSubtitleListMateri;

    public ItemViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        button = view.findViewById(R.id.btn_belajar);
    }

    public TextView getTvSubtitleListMateri() {
        return tvSubtitleListMateri;
    }

    /*old*/
    public void setTvTitleCat(String titleCat) {
        TextView tvTitleCat = view.findViewById(R.id.title_cat);
        tvTitleCat.setText(titleCat);
    }

    public void setTvTitleListMateri(String title) {
        TextView tvTitleListMateri = view.findViewById(R.id.title_list);
        tvTitleListMateri.setText(title);
    }

    public void setTvSubtitleListMateri (String body) {
        tvSubtitleListMateri = view.findViewById(R.id.body_sub);
        tvSubtitleListMateri.setText(body);
    }

    public void setImgMateri(Context context, String img) {
        ImageView imgMateri = view.findViewById(R.id.img_cont);
        Glide.with(context)
                .load(img)
                .into(imgMateri);
    }




    /*old*/
    public void setImgCat (Context context, String imgCat) {
        ImageView imgCategory = view.findViewById(R.id.img_cat);
        Glide.with(context)
                .load(imgCat)
                .into(imgCategory);
    }
}
