package com.utsman.kucingapes.mobilelearningprodisejarah.Activity;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class ListContent extends AppCompatActivity {
    private List<ModelContentList> lists = new ArrayList<>();
    private AdapterContentList adapterContentList;
    private RecyclerView recyclerView;

    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_content);

        recyclerView = findViewById(R.id.content_list);
        adapterContentList = new AdapterContentList(lists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MarginDecoration(15,
                MarginDecoration.VERTICAL));
        recyclerView.setAdapter(adapterContentList);
        preData();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("cat");

            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(category);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterContentList.notifyDataSetChanged();
            }
        }, 500);
    }

    private void preData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("data-md");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue(String.class);
                    String imgUrl = ds.child("img").getValue(String.class);
                    String cat = ds.child("cat").getValue(String.class);
                    String body = ds.child("body").getValue(String.class);
                    Integer id = ds.child("id").getValue(int.class);
                    addData(title, imgUrl, cat, body, id);

                    final List<ModelContentList> filterList = filter(lists, category);
                    adapterContentList.setFilter(filterList);

                }

                ProgressBar progressBar = findViewById(R.id.progbar);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private List<ModelContentList> filter(List<ModelContentList> models, String query) {
        query = query.toLowerCase();
        final List<ModelContentList> filteredModelList = new ArrayList<>();
        for (ModelContentList model : models) {
            final String text = model.getCat().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void addData(String title, String imgUrl, String cat, String body, Integer id) {
        ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
        lists.add(contentList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
