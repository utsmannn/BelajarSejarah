package com.utsman.kucingapes.mobilelearningprodisejarah.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.utsman.kucingapes.mobilelearningprodisejarah.About;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterOpiniList;
import com.utsman.kucingapes.mobilelearningprodisejarah.Adapter.AdapterSearch;
import com.utsman.kucingapes.mobilelearningprodisejarah.Brosur;
import com.utsman.kucingapes.mobilelearningprodisejarah.Favorit.ListFavorit;
import com.utsman.kucingapes.mobilelearningprodisejarah.Favorit.ListOpiniFavorit;
import com.utsman.kucingapes.mobilelearningprodisejarah.Fragment.MateriFragment;
import com.utsman.kucingapes.mobilelearningprodisejarah.Fragment.OpiniFragment;
import com.utsman.kucingapes.mobilelearningprodisejarah.Model.ModelContentList;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;
import com.utsman.kucingapes.mobilelearningprodisejarah.RcConfig.MarginDecoration;
import com.utsman.kucingapes.mobilelearningprodisejarah.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FirebaseUser user;
    private NavigationView navigationView;
    private ProgressDialog mProgressDialog;

    private List<ModelContentList> lists = new ArrayList<>();
    private AdapterOpiniList adapterOpiniList;
    public static MainActivity mainActivity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainActivity = this;

        FirebaseAuth auth = FirebaseAuth.getInstance();
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

        /*adapterOpiniList = new AdapterOpiniList(lists);
        if (adapterOpiniList.getItemCount() == 0) {
            showProgressDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //adapterOpiniList.refresh();
                    viewPager.invalidate();
                    hideProgressDialog();
                }
            }, 3000);
        }*/

        //new Handler()

        /*showProgressDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        }, 1000);*/
    }

    /*@Override
    protected void onResume() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("start", false);
        if(!previouslyStarted) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("start", Boolean.TRUE);
            edit.apply();
            recreate();
        }
        super.onResume();
    }*/

    private void setupViewPager(ViewPager viewPager) {
        adapterOpiniList = new AdapterOpiniList(lists);
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MateriFragment(), "Materi");
        adapter.addFragment(new OpiniFragment(), "Opini");
        viewPager.setAdapter(adapter);

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    Toast.makeText(getApplicationContext(), "das", Toast.LENGTH_SHORT).show();
                    adapterOpiniList.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/

        /*Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int state = bundle.getInt("state");
            viewPager.setCurrentItem(state);
        }*/
        /*adapterOpiniList = new AdapterOpiniList(listBaseOp);
        adapterOpiniList.notifyDataSetChanged();*/
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }  else {
            this.moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.main, menu);*/
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.activity_search) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }
        return super.onOptionsItemSelected(item);
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

        Picasso.get().load(url_header).into(imgHeaderDrawer);
        Picasso.get().load(user.getPhotoUrl()).transform(new CropCircleTransformation()).into(imgProfil);

        profilName.setText(user.getDisplayName());
        profilEmail.setText(user.getEmail());

    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Memuat...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void searchActivity(MenuItem item) {
        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
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
