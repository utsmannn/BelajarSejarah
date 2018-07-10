package com.utsman.kucingapes.mobilelearningprodisejarah.OldClass;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utsman.kucingapes.mobilelearningprodisejarah.About;
import com.utsman.kucingapes.mobilelearningprodisejarah.AdapterCategory;
import com.utsman.kucingapes.mobilelearningprodisejarah.Brosur;
import com.utsman.kucingapes.mobilelearningprodisejarah.ListContent;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;
import com.utsman.kucingapes.mobilelearningprodisejarah.ModelCategory;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAwal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<ModelCategory> modelCategoryList = new ArrayList<>();
    private AdapterCategory adapterCategory;
    private DrawerLayout drawer;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private String nameUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerMain = findViewById(R.id.main_list);
        adapterCategory = new AdapterCategory(modelCategoryList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerMain.setLayoutManager(layoutManager);
        recyclerMain.setItemAnimator(new DefaultItemAnimator());
        recyclerMain.addItemDecoration(new MarginDecoration(15, MarginDecoration.VERTICAL));
        recyclerMain.setAdapter(adapterCategory);
        preData();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void preData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("kat");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    String title = ds.child("titleCat").getValue(String.class);
                    String imgUrl = ds.child("imgCat").getValue(String.class);
                    String name = ds.child("nameCat").getValue(String.class);
                    Integer id = ds.child("idCat").getValue(Integer.class);
                    addData(title, imgUrl, id, name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addData(String title, String imgUrl, Integer id, String name) {
        ModelCategory modelCategory = new ModelCategory(title, imgUrl, id, name);
        modelCategoryList.add(modelCategory);
        adapterCategory.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        setupSearch(searchView);
        return true;
    }

    private void setupSearch(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            Intent intent = new Intent(MainActivityAwal.this, ListContent.class);
            intent.putExtra("cat", "favorit");
            startActivity(intent);

        } else if (id == R.id.nav_profil) {
            startActivity(new Intent(MainActivityAwal.this, Brosur.class));

        } else if (id == R.id.nav_web) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://fkip.uhamka.ac.id/pendidikan-sejarah"));
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivityAwal.this, About.class));

        } else if (id == R.id.nav_exit) {
            //logOut();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
