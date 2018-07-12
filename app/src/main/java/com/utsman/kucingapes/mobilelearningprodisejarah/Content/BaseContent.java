package com.utsman.kucingapes.mobilelearningprodisejarah.Content;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.ExternalStyleSheet;

public class BaseContent extends AppCompatActivity {
    public int id;
    public String nameUser, body, img, title, kategori;
    public MenuItem fav, favFill;
    public CoordinatorLayout coordinatorLayout;
    public MarkdownView markdownView;
    public WebSettings settings;
    public ImageView imgHeader;
    public TextView textJudul, textSubtitle;
    public int small = 12;
    public int medium = 15;
    public int large = 18;

    public FirebaseDatabase database;
    public DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgHeader = findViewById(R.id.imgHeader);
        coordinatorLayout = findViewById(R.id.coordinator);
        textJudul = findViewById(R.id.title_colapse);
        textSubtitle = findViewById(R.id.subtitle_detail);

        markdownView = findViewById(R.id.markdown_view);
        settings = markdownView.getSettings();

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
    }

    public void setupBody() {
        if (body != null) {
            body = body.replaceAll("[~]", "\n\n");
            body = body.replaceAll("[$]", "\n");
        }

        markdownView.addStyleSheet(ExternalStyleSheet.fromAsset("style.css", null));
        markdownView.loadMarkdown(body);
        settings.setDefaultFontSize(medium);
    }

    public void setDataMateri() {
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("data-md").child(String.valueOf(id));
        mRef.keepSynced(true);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                body = dataSnapshot.child("body").getValue(String.class);
                img = dataSnapshot.child("img").getValue(String.class);
                title = dataSnapshot.child("title").getValue(String.class);
                kategori = dataSnapshot.child("cat").getValue(String.class);

                textJudul.setText(title);
                textSubtitle.setText(kategori);

                Picasso.get().load(img).into(imgHeader);

                setupBody();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setDataOpini() {
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("opini").child(String.valueOf(id));
        mRef.keepSynced(true);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                body = dataSnapshot.child("body").getValue(String.class);
                img = dataSnapshot.child("img").getValue(String.class);
                title = dataSnapshot.child("title").getValue(String.class);
                kategori = dataSnapshot.child("author").getValue(String.class);

                if (kategori != null) {
                    kategori = kategori.replaceAll("[$]", "\n");
                }

                textJudul.setText(title);
                textSubtitle.setText(kategori);

                Picasso.get().load(img).into(imgHeader);

                setupBody();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}