package com.utsman.kucingapes.mobilelearningprodisejarah.Favorit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class BaseFavorit extends AppCompatActivity {
    private FirebaseRecyclerAdapter<RcGetter, ItemViewHolder> adapter;

    public EmptyRecyclerView recyclerView_materi;
    public String nameKategori, nameUser;

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
