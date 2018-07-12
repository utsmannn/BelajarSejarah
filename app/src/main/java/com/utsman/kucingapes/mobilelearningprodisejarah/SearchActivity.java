package com.utsman.kucingapes.mobilelearningprodisejarah;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterSearch;
import com.utsman.kucingapes.mobilelearningprodisejarah.Main.MainActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<ModelContentList> lists = new ArrayList<>();
    private List<ModelContentList> listOp = new ArrayList<>();
    private AdapterContentList adapterContentList;
    private AdapterSearch adapterOpiniList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setupSearchMateri();
        setupSearchOpini();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterContentList.notifyDataSetChanged();
                adapterOpiniList.notifyDataSetChanged();
            }
        }, 1000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<ModelContentList> filterList = filter(lists, newText);
                final List<ModelContentList> filterOp = filter(listOp, newText);

                adapterContentList.setFilterSearch(filterList);
                adapterOpiniList.setFilterSearch(filterOp);
                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onBackPressed();
                return true;
            }
        });

        return true;
    }

    private List<ModelContentList> filter(List<ModelContentList> models, String query) {
        query = query.toLowerCase();
        final List<ModelContentList> filteredModelList = new ArrayList<>();
        for (ModelContentList model : models) {
            final String text = model.getTitle().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    /*private void setupSearchMateri(final String newText) {
        RecyclerView recyclerView = findViewById(R.id.list_search_materi);
        recyclerView.setNestedScrollingEnabled(false);
        adapterContentList = new AdapterContentList(lists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MarginDecoration(15,
                MarginDecoration.VERTICAL));
        recyclerView.setAdapter(adapterContentList);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("data-md");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue(String.class);
                    String imgUrl = ds.child("img").getValue(String.class);
                    String cat = ds.child("cat").getValue(String.class);
                    String body = ds.child("body").getValue(String.class);
                    Integer id = ds.child("id").getValue(int.class);
                    addData(title, imgUrl, cat, body, id);

                    final List<ModelContentList> filterList = filter(lists, newText);
                    adapterContentList.setFilter(filterList);

                }
            }

            private void addData(String title, String imgUrl, String cat, String body, Integer id) {
                ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
                lists.add(contentList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // adapterContentList.notifyDataSetChanged();
            }
        }, 500);
    }


    private void setupSearchOpini(final String newText) {
        RecyclerView recyclerView = findViewById(R.id.list_search_opini);
        recyclerView.setNestedScrollingEnabled(false);
        adapterOpiniList = new AdapterSearch(listOp);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MarginDecoration(15,
                MarginDecoration.VERTICAL));
        recyclerView.setAdapter(adapterOpiniList);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("opini");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue(String.class);
                    String imgUrl = ds.child("img").getValue(String.class);
                    String cat = ds.child("cat").getValue(String.class);
                    String body = ds.child("author").getValue(String.class);
                    Integer id = ds.child("id").getValue(int.class);
                    addData(title, imgUrl, cat, body, id);

                    final List<ModelContentList> filterList = filter(lists, newText);
                    adapterContentList.setFilter(filterList);

                }
            }

            private void addData(String title, String imgUrl, String cat, String body, Integer id) {
                ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
                listOp.add(contentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               // adapterOpiniList.notifyDataSetChanged();
            }
        }, 500);
    }*/

    private void setupSearchMateri() {
        RecyclerView recyclerView = findViewById(R.id.list_search_materi);
        recyclerView.setNestedScrollingEnabled(false);
        adapterContentList = new AdapterContentList(lists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MarginDecoration(15,
                MarginDecoration.VERTICAL));
        recyclerView.setAdapter(adapterContentList);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("data-md");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue(String.class);
                    String imgUrl = ds.child("img").getValue(String.class);
                    String cat = ds.child("cat").getValue(String.class);
                    String body = ds.child("body").getValue(String.class);
                    Integer id = ds.child("id").getValue(int.class);
                    addData(title, imgUrl, cat, body, id);

                    /*final List<ModelContentList> filterList = filter(lists, newText);
                    adapterContentList.setFilter(filterList);*/

                }
            }

            private void addData(String title, String imgUrl, String cat, String body, Integer id) {
                ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
                lists.add(contentList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // adapterContentList.notifyDataSetChanged();
            }
        }, 500);
    }

    private void setupSearchOpini() {
        RecyclerView recyclerView = findViewById(R.id.list_search_opini);
        recyclerView.setNestedScrollingEnabled(false);
        adapterOpiniList = new AdapterSearch(listOp);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MarginDecoration(15,
                MarginDecoration.VERTICAL));
        recyclerView.setAdapter(adapterOpiniList);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("opini");
        myRef.keepSynced(true);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String title = ds.child("title").getValue(String.class);
                    String imgUrl = ds.child("img").getValue(String.class);
                    String cat = ds.child("cat").getValue(String.class);
                    String body = ds.child("author").getValue(String.class);
                    Integer id = ds.child("id").getValue(int.class);
                    addData(title, imgUrl, cat, body, id);

                 /*   final List<ModelContentList> filterList = filter(lists, newText);
                    adapterContentList.setFilter(filterList);
*/
                }
            }

            private void addData(String title, String imgUrl, String cat, String body, Integer id) {
                ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
                listOp.add(contentList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // adapterOpiniList.notifyDataSetChanged();
            }
        }, 500);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
