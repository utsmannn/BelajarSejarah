package com.utsman.kucingapes.mobilelearningprodisejarah.Favorit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public RelativeLayout button;
    public TextView tvSubtitleListMateri;

    public ItemViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void setTvTitleListMateri(String title) {
        TextView tvTitleListMateri = view.findViewById(R.id.title_list);
        tvTitleListMateri.setText(title);
    }

    public void setTvSubtitleListMateri (String body) {
        tvSubtitleListMateri = view.findViewById(R.id.body_sub);
        tvSubtitleListMateri.setText(body);
    }

    public void setImgMateri(String img) {
        ImageView imgMateri = view.findViewById(R.id.img_cont);
        Picasso.get().load(img).into(imgMateri);
    }


}
