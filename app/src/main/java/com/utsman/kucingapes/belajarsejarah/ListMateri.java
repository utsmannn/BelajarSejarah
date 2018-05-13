package com.utsman.kucingapes.belajarsejarah;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import java.util.HashMap;
import java.util.Map;

public class ListMateri extends AppCompatActivity {
    private FirebaseRecyclerAdapter<RcGetter, ItemViewHolder> adapter;

    RecyclerView recyclerView_materi;
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
        setupDatabase();

        TextView textView = findViewById(R.id.id_text);
        String id = String.valueOf(idCat);
        textView.setText(id);
    }

    private void setupDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("data");
        databaseReference.keepSynced(true);
        recyclerView_materi.hasFixedSize();
        recyclerView_materi.setLayoutManager(new LinearLayoutManager(this));

        Query query;
        if (kategori.equals("favorit")) {
            query = databaseReference.orderByChild("userfavorit/"+nameUser).equalTo("true");
        } else {
            query = databaseReference.orderByChild("cat").equalTo(kategori);
        }

        FirebaseRecyclerOptions recyclerOptions = new FirebaseRecyclerOptions.Builder<RcGetter>()
                .setQuery(query, RcGetter.class).build();

        adapter = new FirebaseRecyclerAdapter<RcGetter, ItemViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull final ItemViewHolder holder, int position, @NonNull final RcGetter model) {
                holder.setTvTitleListMateri(model.getTitle());
                holder.setImgMateri(getBaseContext(), model.getImg());
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = model.getId();
                        Intent intent = new Intent(ListMateri.this, ContentActivity.class);
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
        // startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
