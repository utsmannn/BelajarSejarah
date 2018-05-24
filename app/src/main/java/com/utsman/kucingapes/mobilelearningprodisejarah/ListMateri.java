package com.utsman.kucingapes.mobilelearningprodisejarah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.EmptyRecyclerView;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.ItemViewHolder;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.RcGetter;

public class ListMateri extends AppCompatActivity {
    private FirebaseRecyclerAdapter<RcGetter, ItemViewHolder> adapter;

    EmptyRecyclerView recyclerView_materi;
    String parentKategori, kategori, nameKategori, nameUser;
    int idCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_materi);
        recyclerView_materi = findViewById(R.id.recycler_materi);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            parentKategori = bundle.getString("child");
            kategori = bundle.getString("kategori");
            nameKategori = bundle.getString("nameKategori");
            idCat = bundle.getInt("idCat");
        }

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

        Query query;
        if (kategori.equals("favorit")) {
            query = databaseReference.orderByChild("userfavorit/"+nameUser).equalTo("true");
            getSupportActionBar().setTitle("Favorit");
        } else {
            query = databaseReference.orderByChild("cat").equalTo(kategori);
        }

        FirebaseRecyclerOptions recyclerOptions = new FirebaseRecyclerOptions.Builder<RcGetter>()
                .setQuery(query, RcGetter.class).build();

        adapter = new FirebaseRecyclerAdapter<RcGetter, ItemViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final RcGetter model) {
                holder.setTvTitleListMateri(model.getTitle());
                holder.setTvSubtitleListMateri(model.getBody());
                holder.setImgMateri(getBaseContext(), model.getImg());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = model.getId();
                        Intent intent = new Intent(ListMateri.this, ContentActivity.class);
                        intent.putExtra("img", model.getImg());
                        intent.putExtra("body", model.getBody());
                        intent.putExtra("kategori", nameKategori);
                        intent.putExtra("title", model.getTitle());
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_list_materi, parent, false);
                return new ItemViewHolder(view);
            }
        };

        recyclerView_materi.setEmptyView(empty);
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
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idCat", idCat);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
