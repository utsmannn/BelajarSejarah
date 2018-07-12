package com.utsman.kucingapes.mobilelearningprodisejarah.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.utsman.kucingapes.mobilelearningprodisejarah.Activity.ListContent;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelCategory;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.Holder>{
    private List<ModelCategory> modelCategoryList;
    private Context context;

    public AdapterCategory(List<ModelCategory> modelCategoryList) {
        this.modelCategoryList = modelCategoryList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_row, parent, false);
        context = parent.getContext();
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        final ModelCategory modelCategory = modelCategoryList.get(position);
        holder.tvTitle.setText(modelCategory.getTitleCat());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListContent.class);
                intent.putExtra("cat", modelCategory.getTitleCat());
                context.startActivity(intent);
            }
        });

        Picasso.get().load(modelCategory.getImgCat()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return modelCategoryList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView img;
        public CardView cardView;
        public Holder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.title_cat_row);
            img = itemView.findViewById(R.id.img_row);
            cardView = itemView.findViewById(R.id.card_row);
        }
    }
}
