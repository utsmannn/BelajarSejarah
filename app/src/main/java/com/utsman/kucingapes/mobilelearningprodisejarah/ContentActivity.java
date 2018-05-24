package com.utsman.kucingapes.mobilelearningprodisejarah;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContentActivity extends AppCompatActivity {
    private int id;
    private String nameUser, body, img, title, kategori;
    private MenuItem fav, favFill;
    private CoordinatorLayout coordinatorLayout;
    private DocumentView textView, textViewMedium, textViewLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            body = bundle.getString("body");
            img = bundle.getString("img");
            title = bundle.getString("title");
            kategori = bundle.getString("kategori");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imgHeader = findViewById(R.id.imgHeader);
        RelativeLayout offset = findViewById(R.id.offset);
        coordinatorLayout = findViewById(R.id.coordinator);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        TextView textJudul = findViewById(R.id.title_colapse);
        TextView textSubtitle = findViewById(R.id.subtitle_detail);

        textView = findViewById(R.id.body);
        textViewMedium = findViewById(R.id.body_medium);
        textViewLarge = findViewById(R.id.body_besar);

        Glide.with(this)
                .load(img)
                .into(imgHeader);

        textJudul.setText(title);
        textSubtitle.setText(kategori);

        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setCollapsedTitleTextColor(
                ContextCompat.getColor(this, R.color.text));
        collapsingToolbarLayout.setExpandedTitleColor(
                ContextCompat.getColor(this, R.color.trans));


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            nameUser = user.getDisplayName();
            if (nameUser != null) {
                nameUser = nameUser.replaceAll("\\s", "-");
            }
        }
        setupBody();
    }

    private void setupBody() {
        if (body != null) {
            body = body.replaceAll("[~]", "\n\n");
            body = body.replaceAll("[$]", "\n");
        }

        textView.setText(body);
        textViewMedium.setText(body);
        textViewLarge.setText(body);

        /*Bypass bypass = new Bypass(this);
        textView.setText(bypass.markdownToSpannable(body,
                new BypassGlideImageGetter(textView, Glide.with(this))));*/
    }

    public void tesfav(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("data").child(String.valueOf(id)).child("userfavorit/"+nameUser);
        myRef.setValue("true");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        fav = menu.findItem(R.id.action_favorite);
        favFill = menu.findItem(R.id.action_favorite_fill);

        fav.setVisible(false);
        favFill.setVisible(false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("data").child(String.valueOf(id));
        mRef.keepSynced(true);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("userfavorit/"+nameUser)) {
                    String statusFav = dataSnapshot.child("userfavorit/"+nameUser).getValue(String.class);

                    if (statusFav != null) {
                        if (statusFav.equals("false")) {
                            fav.setVisible(true);
                            favFill.setVisible(false);
                        } if (statusFav.equals("true")) {
                            fav.setVisible(false);
                            favFill.setVisible(true);
                        }
                    }
                } else {
                    fav.setVisible(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String y = "true";
        String n = "false";

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mRef = database.getReference().child("data").child(String.valueOf(id));

        switch (item.getItemId()) {
            case R.id.action_favorite:
                Snackbar favSnack = Snackbar.make(coordinatorLayout, "Ditambahkan ke Favorit", Snackbar.LENGTH_LONG);
                favSnack.show();
                mRef.child("userfavorit/"+nameUser).setValue(y);
                return true;
            case R.id.action_favorite_fill:
                Snackbar favSnackRem = Snackbar.make(coordinatorLayout, "Dihapus dari Favorit", Snackbar.LENGTH_LONG);
                favSnackRem.show();
                mRef.child("userfavorit/"+nameUser).setValue(n);
                return true;
            case R.id.text_kecil:
                textView.setVisibility(View.VISIBLE);
                textViewMedium.setVisibility(View.GONE);
                textViewLarge.setVisibility(View.GONE);
                return true;
            case R.id.text_sedang:
                textView.setVisibility(View.GONE);
                textViewMedium.setVisibility(View.VISIBLE);
                textViewLarge.setVisibility(View.GONE);
                return true;
            case R.id.text_besar:
                textView.setVisibility(View.GONE);
                textViewMedium.setVisibility(View.GONE);
                textViewLarge.setVisibility(View.VISIBLE);
                return true;
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
