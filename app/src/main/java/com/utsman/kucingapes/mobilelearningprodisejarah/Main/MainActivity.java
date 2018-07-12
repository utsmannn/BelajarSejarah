package com.utsman.kucingapes.mobilelearningprodisejarah.Main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utsman.kucingapes.mobilelearningprodisejarah.About;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.Brosur;
import com.utsman.kucingapes.mobilelearningprodisejarah.Favorit.ListFavorit;
import com.utsman.kucingapes.mobilelearningprodisejarah.Favorit.ListOpiniFavorit;
import com.utsman.kucingapes.mobilelearningprodisejarah.Fragment.MateriFragment;
import com.utsman.kucingapes.mobilelearningprodisejarah.Fragment.OpiniFragment;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseUser user;
    private FirebaseAuth auth;
    private NavigationView navigationView;
    private RecyclerView searchList;
    private SearchView searchView;

    private List<ModelContentList> lists = new ArrayList<>();
    private AdapterContentList adapterSearchList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        viewPager = findViewById(R.id.viewpager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupDrawerAccount();
        setupViewPager(viewPager);
        setupRecyclerSearch();
        setupDataSearch();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MateriFragment(), "Materi");
        adapter.addFragment(new OpiniFragment(), "Opini");
        viewPager.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int state = bundle.getInt("state");
            viewPager.setCurrentItem(state);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } if (viewPager.getVisibility() == View.GONE && tabLayout.getVisibility() == View.GONE) {
            viewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);
            searchList.setVisibility(View.GONE);
            searchView.setIconified(true);
            adapterSearchList.clear();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                searchList.setVisibility(View.VISIBLE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                viewPager.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.VISIBLE);
                searchList.setVisibility(View.GONE);
                adapterSearchList.clear();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_fav) {
            startActivity(new Intent(getApplicationContext(), ListFavorit.class));

        } else if (id == R.id.nav_fav_op) {
            startActivity(new Intent(getApplicationContext(), ListOpiniFavorit.class));

        } else if (id == R.id.nav_profil) {
            startActivity(new Intent(this, Brosur.class));

        } else if (id == R.id.nav_web) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://fkip.uhamka.ac.id/pendidikan-sejarah"));
            startActivity(intent);

        } else if (id == R.id.nav_about) {
            startActivity(new Intent(this, About.class));

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
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
    private void setupRecyclerSearch() {
        searchList = findViewById(R.id.list_search_materi);
        adapterSearchList = new AdapterContentList(lists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        searchList.setLayoutManager(layoutManager);
        searchList.addItemDecoration(new MarginDecoration(15, MarginDecoration.VERTICAL));
        searchList.setAdapter(adapterSearchList);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //adapterSearchList.notifyDataSetChanged();
            }
        }, 500);

        findViewById(R.id.tess).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterSearchList.clear();
            }
        });

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        /*String newText = query;
        setupDataSearch(newText);*/
        //setupDataSearch(query);
        //adapterSearchList.notifyDataSetChanged();
        //adapterSearchList.clear();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        /*int size = lists.size();
        lists.clear();
        adapterSearchList.notifyItemRangeRemoved(0, size);
        searchList.setVisibility(View.VISIBLE);*/
        //adapterSearchList.notifyDataSetChanged();
        final List<ModelContentList> filterList = filter(lists, newText);
        adapterSearchList.setFilterSearch(filterList);
        return true;
    }

    private void setupDataSearch() {
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
                    adapterSearchList.setFilterSearch(filterList);*/
                    //adapterSearchList.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*private void setupDataSearch(final String newText) {

    }*/

    private void addData(String title, String imgUrl, String cat, String body, Integer id) {
        ModelContentList contentList = new ModelContentList(title, imgUrl, cat, body, id);
        lists.add(contentList);
        //adapterSearchList.notifyDataSetChanged();
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> stringList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }


        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            stringList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            //return super.getPageTitle(position);
            return stringList.get(position);
        }
    }
}
