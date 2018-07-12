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
import com.utsman.kucingapes.mobilelearningprodisejarah.Content.OpiniActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.Holder> {
    private List<ModelContentList> lists;
    private Context context;

    public AdapterSearch(List<ModelContentList> lists) {
        this.lists = lists;
    }

    @NonNull
    @Override
    public AdapterSearch.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        context = parent.getContext();
        return new AdapterSearch.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterSearch.Holder holder, int position) {
        final ModelContentList contentList = lists.get(position);
        holder.title.setText(contentList.getTitle());
        holder.subtitle.setText(contentList.getBody());
        holder.author.setText(contentList.getCat());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpiniActivity.class);
                intent.putExtra("id", contentList.getId());
                context.startActivity(intent);
            }
        });

        Picasso.get().load(contentList.getImgUrl()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public void clear() {
        int size = lists.size();
        notifyItemRangeRemoved(0, size);
        //notifyDataSetChanged();
    }

    public void setFilter(List<ModelContentList> filterModels) {
        lists = new ArrayList<>();
        lists.addAll(filterModels);
    }

    public void setFilterSearch(List<ModelContentList> filterModels) {
        lists = new ArrayList<>();
        lists.addAll(filterModels);
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