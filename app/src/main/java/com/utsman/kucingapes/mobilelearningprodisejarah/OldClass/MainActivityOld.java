package com.utsman.kucingapes.mobilelearningprodisejarah.OldClass;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.utsman.kucingapes.mobilelearningprodisejarah.About;
import com.utsman.kucingapes.mobilelearningprodisejarah.Brosur;
import com.utsman.kucingapes.mobilelearningprodisejarah.Content.ContentActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.Favorit.ListFavorit;
import com.utsman.kucingapes.mobilelearningprodisejarah.LoginActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;
import com.utsman.kucingapes.mobilelearningprodisejarah.Favorit.ItemViewHolder;
import com.utsman.kucingapes.mobilelearningprodisejarah.Favorit.RcGetter;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

public class MainActivityOld extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private FirebaseRecyclerAdapter<RcGetter, ItemViewHolder> adapter;
    private DiscreteScrollView recyclerViewCategory;
    private RecyclerView recyclerViewMateriSearch;
    private LinearLayout layout_category;
    private SearchView searchView;
    private String nameUser;
    private DrawerLayout drawer;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private NavigationView navigationView;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean kluar = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        recyclerViewCategory = findViewById(R.id.recycler_category);
        recyclerViewMateriSearch = findViewById(R.id.recycler_materi_search);
        layout_category = findViewById(R.id.parent_category);

        recyclerViewCategory.setVisibility(View.VISIBLE);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            nameUser = user.getDisplayName();
            if (nameUser != null) {
                nameUser = nameUser.replaceAll("\\s", "-");
            }
        }

        navigationView = findViewById(R.id.nav_view);
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

        setupDrawerAccount();
        setupWelcomeCard();
    }

    @SuppressLint("SetTextI18n")
    private void setupWelcomeCard() {
        ImageView fkipLogo = findViewById(R.id.fkiplogo);
        ImageView appLogo = findViewById(R.id.applogo);
        TextView textWelcome = findViewById(R.id.welcome);

        Glide.with(this)
                .load(R.drawable.fkiplogo)
                .into(fkipLogo);

        Glide.with(this)
                .load(R.drawable.logo)
                .into(appLogo);

        textWelcome.setText("Selamat datang" + " " + user.getDisplayName() + "!");
    }

    private void setupDrawerAccount() {
        View header = navigationView.getHeaderView(0);

        ImageView imgHeaderDrawer = header.findViewById(R.id.img_header_bg);
        ImageView imgProfil = header.findViewById(R.id.image_profil);
        TextView profilName = header.findViewById(R.id.name_profil);
        TextView profilEmail = header.findViewById(R.id.email_profil);
        String url_header = "http://2.bp.blogspot.com/_vv1kmR7iUVM/TJOKIE70iZI/AAAAAAAAAB4/yAdbCCM82Ew/s1600/800px-Indonesia_declaration_of_independence_17_August_1945.jpg";

        Glide.with(this)
                .load(url_header)
                .into(imgHeaderDrawer);

        Glide.with(this)
                .load(user.getPhotoUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(imgProfil);

        profilName.setText(user.getDisplayName());
        profilEmail.setText(user.getEmail());

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
                holder.setImgCat(getBaseContext(), model.getImgCat());
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), ListFavorit.class);
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
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (kluar) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            Intent intent = new Intent(MainActivityOld.this, ListFavorit.class);
            intent.putExtra("kategori", "favorit");
            startActivity(intent);

        } else if (id == R.id.nav_profil) {
            startActivity(new Intent(MainActivityOld.this, Brosur.class));

        } else if (id == R.id.nav_web) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://fkip.uhamka.ac.id/pendidikan-sejarah"));
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(MainActivityOld.this, About.class));

        } else if (id == R.id.nav_exit) {
            logOut();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(MainActivityOld.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(MainActivityOld.this);
        }

        builder.setTitle("Keluar?")
                .setMessage("Anda masuk sebagai " + user.getDisplayName() + ", ingin keluar?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        auth.signOut();
                        mGoogleSignInClient.revokeAccess()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(MainActivityOld.this, LoginActivity.class);
                                        intent.putExtra("out", "out");
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
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
        }
        if (newText.length() == 0) {
            newText = "gak ada";
        }

        Query query = databaseMateri.orderByChild("title").startAt(newText).endAt(newText + "\uf8ff");
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
                        Intent intent = new Intent(MainActivityOld.this, ContentActivity.class);
                        intent.putExtra("img", model.getImg());
                        intent.putExtra("body", model.getBody());
                        intent.putExtra("kategori", model.getNameCat());
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

        recyclerViewMateriSearch.setAdapter(adapter);
        adapter.startListening();
        return true;
    }
}
