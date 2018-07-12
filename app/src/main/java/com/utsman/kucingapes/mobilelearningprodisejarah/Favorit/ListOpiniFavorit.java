package com.utsman.kucingapes.mobilelearningprodisejarah.Favorit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.utsman.kucingapes.mobilelearningprodisejarah.Content.OpiniActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;

public class ListOpiniFavorit extends BaseFavorit {
    private FirebaseRecyclerAdapter<RcGetter, ItemViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDatabase();

    }

    private void setupDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("opini");
        databaseReference.keepSynced(true);
        TextView empty = findViewById(R.id.empty);
        recyclerView_materi.hasFixedSize();
        recyclerView_materi.setLayoutManager(new LinearLayoutManager(this));

        Query query = databaseReference.orderByChild("userfavorit/"+nameUser).equalTo("true");

        FirebaseRecyclerOptions recyclerOptions = new FirebaseRecyclerOptions.Builder<RcGetter>()
                .setQuery(query, RcGetter.class).build();

        adapter = new FirebaseRecyclerAdapter<RcGetter, ItemViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final RcGetter model) {
                holder.setTvTitleListMateri(model.getTitle());
                holder.setTvSubtitleListMateri(model.getBody());
                holder.setImgMateri(model.getImg());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), OpiniActivity.class);
                        intent.putExtra("id", model.getId());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_row, parent, false);
                return new ItemViewHolder(view);
            }
        };

        ProgressBar progressBar = findViewById(R.id.progbar);
        progressBar.setVisibility(View.GONE);
        recyclerView_materi.setVisibility(View.VISIBLE);

        recyclerView_materi.setEmptyView(empty);
        recyclerView_materi.addItemDecoration(new MarginDecoration(15, MarginDecoration.VERTICAL));
        recyclerView_materi.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
