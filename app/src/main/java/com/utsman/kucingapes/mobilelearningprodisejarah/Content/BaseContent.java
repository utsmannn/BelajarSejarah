package com.utsman.kucingapes.mobilelearningprodisejarah.Content;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.util.ArrayList;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.ExternalStyleSheet;

public class BaseContent extends AppCompatActivity {
    public int id;
    public String nameUser, body, img, title, kategori;
    public String sign = "Aplikasi Belajar Sejarah NKRI oleh\nProdi Sejarah UHAMKA\n" +
            "Unduh di\n" +
            "https://play.google.com/store/apps/details?id=com.utsman.kucingapes.mobilelearningprodisejarah" +
            "\n--------------\n";
    public MenuItem fav, favFill;
    public CoordinatorLayout coordinatorLayout;
    public MarkdownView markdownView;
    public FloatingActionButton fabShare;

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
            body = body.replaceAll("[~]+[\\s]", "\n\n");
            body = body.replaceAll("[~]", "\n\n");
            body = body.replaceAll("[$]", "\n");
        }

        markdownView.addStyleSheet(ExternalStyleSheet.fromAsset("style.css", null));
        markdownView.loadMarkdown(body);
        settings.setDefaultFontSize(medium);

        markdownView.setVisibility(View.VISIBLE);
    }

    public void setDataMateri() {
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("data-md").child(String.valueOf(id));
        mRef.keepSynced(true);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                body = dataSnapshot.child("body").getValue(String.class);
                img = dataSnapshot.child("img").getValue(String.class);
                title = dataSnapshot.child("title").getValue(String.class);
                kategori = dataSnapshot.child("cat").getValue(String.class);

                textJudul.setText(title);
                textSubtitle.setText(kategori);

                Picasso.get().load(img).into(imgHeader);

                setupBody();
                setupShare(title, kategori, body);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public void setDataOpini() {
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference().child("opini").child(String.valueOf(id));
        mRef.keepSynced(true);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                body = dataSnapshot.child("body").getValue(String.class);
                img = dataSnapshot.child("img").getValue(String.class);
                title = dataSnapshot.child("title").getValue(String.class);
                kategori = dataSnapshot.child("author").getValue(String.class);

                if (kategori != null) {
                    kategori = kategori.replaceAll("[$]", "\n");

                    textJudul.setText(title);
                    textSubtitle.setText(kategori);

                    Picasso.get().load(img).into(imgHeader);

                    setupBody();
                    String subtile = "Oleh: "+kategori;
                    setupShare(title, subtile, body);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupShare(final String title, final String subtile, final String body) {
        int count = body.length();
        String bodyMax = body;

        if (count > 4000) {
            int max = count-3000;
            bodyMax = removeLastChar(body, max);
            bodyMax = bodyMax+"..."+"(teks terlalu panjang)";
        }

        final String finalBodyMax = bodyMax;

        fabShare = findViewById(R.id.fab_share);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Intent> targetedShareIntents = new ArrayList<>();
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/*");
                List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0);
                if (!resInfo.isEmpty()) {
                    for (ResolveInfo resolveInfo : resInfo) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        Intent targetedShareIntent = new Intent();
                        targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                        if (TextUtils.equals(packageName, "com.whatsapp")) {
                            String titleBold = "*"+title+"*";
                            String bodyItalic = body.replaceAll("[*]", "");
                            String allShare = sign+"\n"+titleBold+"\n"+subtile+"\n\n"+ bodyItalic;
                            targetedShareIntent.setAction(Intent.ACTION_VIEW);
                            Uri uriUrl = Uri.parse("whatsapp://send?text="+allShare+"");
                            targetedShareIntent.setData(uriUrl);

                        } else {
                            String allShare = sign+"\n"+title+"\n"+subtile+"\n\n"+ finalBodyMax;
                            targetedShareIntent.setAction(Intent.ACTION_SEND);
                            targetedShareIntent.setType("text/plain");
                            targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, allShare);
                        }
                        targetedShareIntent.setPackage(packageName);
                        targetedShareIntents.add(targetedShareIntent);
                    }
                    Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Bagikan");
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
                    startActivity(chooserIntent);
                }
            }
        });
    }

    private static String removeLastChar(String str, int all) {
        return str.substring(0, str.length() - all);
    }

}