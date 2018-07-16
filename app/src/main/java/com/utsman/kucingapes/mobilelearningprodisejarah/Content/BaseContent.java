package com.utsman.kucingapes.mobilelearningprodisejarah.Content;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;

import br.tiagohm.markdownview.MarkdownView;
import br.tiagohm.markdownview.css.ExternalStyleSheet;

public class BaseContent extends AppCompatActivity {
    public int id;
    public String nameUser, body, img, title, kategori;
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
            body = body.replaceAll("[~]", "\n\n");
            body = body.replaceAll("[$]", "\n");
        }

        markdownView.addStyleSheet(ExternalStyleSheet.fromAsset("style.css", null));
        markdownView.loadMarkdown(body);
        settings.setDefaultFontSize(medium);

        ProgressBar progressBar = findViewById(R.id.progbar);
        //progressBar.setVisibility(View.GONE);
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

                String titleShare = title;
                String subShare = kategori;
                String bodyShare = body;
                final String allShare = titleShare + "\n" + subShare + "\n\n" + bodyShare;

                fabShare = findViewById(R.id.fab_share);
                fabShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, allShare);
                        startActivity(Intent.createChooser(sharingIntent, "Bagikan"));
                    }
                });
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

                    String titleShare = title;
                    String subShare = kategori;
                    String bodyShare = body;
                    final String allShare = titleShare + "\n" + "Oleh: " + subShare + "\n\n" + bodyShare;

                    fabShare = findViewById(R.id.fab_share);
                    fabShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            //Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                            /*Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                            startActivity(launchBrowser);*/
                            //startActivity(sharingIntent);

                            List<Intent> targetedShareIntents = new ArrayList<>();
                            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0);
                            if (!resInfo.isEmpty()) {
                                for (ResolveInfo resolveInfo : resInfo) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    targetedShareIntent.setType("text/plain");
                                    if (TextUtils.equals(packageName, "com.whatsapp")) {
                                        /*Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                        targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);*/
                                        //startActivity(targetedShareIntent);
                                        Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                        startActivity(launchBrowser);
                                    } else {
                                        targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "text message to shared");
                                    }
                                    targetedShareIntent.setPackage(packageName);
                                    targetedShareIntents.add(targetedShareIntent);
                                }
                                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
                                startActivity(chooserIntent);
                            }

                            /*Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            //sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, allShare);
                            //startActivity(Intent.createChooser(sharingIntent, "Bagikan"));
                            Intent chose = new Intent(Intent.createChooser(sharingIntent, "bagikan"));
                            //if (chose.resolveActivity(getPackageName().equals("com.whatsapp")))
                            //if (chose.resolveActivityInfo()
                            ResolveInfo info = info.activityInfo.packageName.equals("com.whatsapp");
                            if (sharingIntent.resolveActivityInfo("")


                            final List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities (sharingIntent, 0);
                            for (ResolveInfo info: resolveInfos) {
                                if (info.activityInfo.packageName.equals("com.whatsapp")) {
                                    Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, uriUrl);
                                    startActivity(sharingIntent);
                                } else {
                                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                                    //startActivity(sharingIntent);
                                    startActivity(Intent.createChooser(sharingIntent, "Bagikan"));
                                }
                            }

                            //startActivity(sharingIntent);


                            //if (sharingIntent.resolveActivityInfo(getPackageManager())
                            //startActivity(Intent.createChooser(sharingIntent, "Bagikan"));

                            // convert intentList to array
                            *//*LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

                            openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
                            startActivity(openInChooser);*//*

                            *//*Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                            startActivity(launchBrowser);*//*

                            *//*final Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_TEXT,"text");

                            final List<ResolveInfo> activities = getPackageManager().queryIntentActivities (i, 0);

                            for (ResolveInfo info : activities) {
                                if (info.activityInfo.packageName.equals("com.whatsapp")) {

                                    Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                    startActivity(launchBrowser);
                                } else {
                                    i.setPackage(info.activityInfo.packageName);
                                    startActivity(i);
                                }

                            }*//*

                           *//* AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setTitle("Complete Action using...");
                            builder.setItems(appNames.toArray(new CharSequence[appNames.size()]), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    ResolveInfo info = activities.get(item);
                                    if (info.activityInfo.packageName.equals("com.whatsapp")) {

                                        Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                                        startActivity(launchBrowser);
                                    }

                                    // start the selected activity
                                    i.setPackage(info.activityInfo.packageName);
                                    startActivity(i);
                                }
                            });*/
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}