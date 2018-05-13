package com.utsman.kucingapes.belajarsejarah;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private FirebaseRecyclerAdapter<RcGetter, ItemViewHolder> adapter;
    private DiscreteScrollView recyclerViewCategory;
    private RecyclerView recyclerViewMateriSearch;
    private LinearLayout layout_category;
    private SearchView searchView;
    private String nameUser;
    private int value;
    private boolean kluar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerViewCategory = findViewById(R.id.recycler_category);
        recyclerViewMateriSearch = findViewById(R.id.recycler_materi_search);
        layout_category = findViewById(R.id.parent_category);
        recyclerViewCategory.setVisibility(View.VISIBLE);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            nameUser = user.getDisplayName();
            if (nameUser != null) {
                nameUser = nameUser.replaceAll("\\s", "-");
            }
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setupDatabase();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    int idCat = bundle.getInt("idCat");
                    recyclerViewCategory.smoothScrollToPosition(idCat);
                }
            }
        }, 100);
    }

    private void setupDatabase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("kat");
        databaseReference.keepSynced(true);

        Query query = databaseReference.orderByKey();
        FirebaseRecyclerOptions recyclerOptions = new FirebaseRecyclerOptions.Builder<RcGetter>()
                .setQuery(query, RcGetter.class).build();

        adapter = new FirebaseRecyclerAdapter<RcGetter, ItemViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull final RcGetter model) {
                holder.setTvTitleCat(model.getTitleCat());
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ListMateri.class);
                        intent.putExtra("kategori", model.getNameCat());
                        intent.putExtra("nameKategori", model.getTitleCat());
                        intent.putExtra("idCat", model.getIdCat());
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_category, parent, false);
                return new ItemViewHolder(view);
            }
        };

        recyclerViewCategory.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        recyclerViewCategory.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            layout_category.setVisibility(View.VISIBLE);
            recyclerViewMateriSearch.setVisibility(View.GONE);
        } if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } if (kluar) {
            super.onBackPressed();
            finish();
        }

        this.kluar = true;
        Toast.makeText(this, "Ketuk dua kali untuk keluar aplikasi", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                kluar = false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewMateriSearch.setVisibility(View.VISIBLE);
                layout_category.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.onActionViewCollapsed();
                recyclerViewMateriSearch.setVisibility(View.GONE);
                layout_category.setVisibility(View.VISIBLE);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final DatabaseReference databaseMateri = FirebaseDatabase.getInstance().getReference()
                .child("data");

        databaseMateri.keepSynced(true);
        recyclerViewMateriSearch.hasFixedSize();
        recyclerViewMateriSearch.setLayoutManager(new LinearLayoutManager(this));
        if (newText.length() > 0) {
            newText = newText.substring(0, 1).toUpperCase() + newText.substring(1).toLowerCase();
        } if (newText.length() == 0) {
            newText = "gak ada";
        }

        Query query = databaseMateri.orderByChild("title").startAt(newText).endAt(newText+"\uf8ff");
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
                        Intent intent = new Intent(MainActivity.this, ContentActivity.class);
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

        recyclerViewMateriSearch.setAdapter(adapter);
        adapter.startListening();
        return true;
    }
}
