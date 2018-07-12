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

import com.bumptech.glide.Glide;
import com.utsman.kucingapes.mobilelearningprodisejarah.Content.ContentActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterContentList extends RecyclerView.Adapter<AdapterContentList.Holder> {
    private List<ModelContentList> lists;
    private Context context;
    private int pos;

    public AdapterContentList(List<ModelContentList> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        context = parent.getContext();
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        pos = position;
        final ModelContentList contentList = lists.get(position);
        holder.title.setText(contentList.getTitle());
        holder.subtitle.setText(contentList.getBody());
        holder.author.setText(contentList.getCat());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ContentActivity.class);
                intent.putExtra("body", contentList.getBody());
                intent.putExtra("img", contentList.getImgUrl());
                intent.putExtra("title", contentList.getTitle());
                intent.putExtra("kategori", contentList.getCat());
                intent.putExtra("id", contentList.getId());
                context.startActivity(intent);
            }
        });

        Glide.with(context)
                .load(contentList.getImgUrl())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void setFilter(List<ModelContentList> filterModels) {
        lists = new ArrayList<>();
        lists.addAll(filterModels);
        //notifyDataSetChanged();
    }

    public void clear() {
        int size = lists.size();
        notifyItemRangeRemoved(0, size);
        notifyDataSetChanged();
    }

    public void setFilterSearch(List<ModelContentList> filterModels) {
        lists = new ArrayList<>();
        lists.addAll(filterModels);

        /*int size = lists.size();
        lists.clear();
        notifyItemRangeRemoved(0, size);*/
        notifyDataSetChanged();
    }

    public class Holder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView img;
        public TextView title, author;
        public TextView subtitle;
        public Holder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_list_row);
            img = itemView.findViewById(R.id.img_cont);
            title = itemView.findViewById(R.id.title_list);
            author = itemView.findViewById(R.id.author);
            subtitle = itemView.findViewById(R.id.body_sub);
            author.setVisibility(View.GONE);
        }
    }
}