package com.utsman.kucingapes.mobilelearningprodisejarah.Favorit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.utsman.kucingapes.mobilelearningprodisejarah.Content.ContentActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.EmptyRecyclerView;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;

public class ListFavorit extends AppCompatActivity {
    private FirebaseRecyclerAdapter<RcGetter, ItemViewHolder> adapter;

    EmptyRecyclerView recyclerView_materi;
    String parentKategori, kategori, nameKategori, nameUser;
    int idCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_materi);
        recyclerView_materi = findViewById(R.id.recycler_materi);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            nameUser = user.getDisplayName();
            if (nameUser != null) {
                nameUser = nameUser.replaceAll("\\s", "-");
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(nameKategori);

        setupDatabase();

    }

    private void setupDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("data");
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
                //final String kat = String.valueOf(holder.getTvSubtitleListMateri().getText());
                final String kat = model.getTitleCat();
                holder.setTvTitleListMateri(model.getTitle());
                holder.setTvSubtitleListMateri(model.getBody());
                holder.setImgMateri(getBaseContext(), model.getImg());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //int id = model.getId();
                        Intent intent = new Intent(ListFavorit.this, ContentActivity.class);
                        intent.putExtra("img", model.getImg());
                        intent.putExtra("body", model.getBody());
                        intent.putExtra("kategori", kat);
                        intent.putExtra("title", model.getTitle());
                        //intent.putExtra("id", id);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
