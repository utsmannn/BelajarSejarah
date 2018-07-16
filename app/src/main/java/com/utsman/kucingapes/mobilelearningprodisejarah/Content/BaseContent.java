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
                    final String bodyShare = body;
                    final String allShare = titleShare + "\n" + "Oleh: " + subShare + "\n\n" + bodyShare;

                    fabShare = findViewById(R.id.fab_share);
                    fabShare.setOnClickListener(new View.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                        @Override
                        public void onClick(View v) {

                            /*List<Intent> targetedShareIntents = new ArrayList<>();
                            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0);
                            if (!resInfo.isEmpty()) {
                                for (ResolveInfo resolveInfo : resInfo) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    targetedShareIntent.setType("text/plain");
                                    if (packageName.equals("com.whatsapp")) {
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
                            }*/


                            /*List<Intent> shareIntentsLists = new ArrayList<>();
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                            shareIntent.setType("text/plain");
                            List<ResolveInfo> resInfos = getPackageManager().queryIntentActivities(shareIntent, 0);
                            if (!resInfos.isEmpty()) {
                                for (ResolveInfo resInfo : resInfos) {
                                    String packageName = resInfo.activityInfo.packageName;
                                    if (!packageName.toLowerCase().contains("com.whatsapp")) {
                                        Intent intent = new Intent();
                                        intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                                        intent.setAction(Intent.ACTION_SEND);
                                        intent.setType("image/*");
                                        intent.setPackage(packageName);
                                        shareIntentsLists.add(intent);
                                        *//*Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                        Intent intent = new Intent();
                                        intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setType("text/plain");
                                        intent.setPackage(packageName);
                                        shareIntentsLists.add(intent);*//*
                                    }
                                }
                                if (!shareIntentsLists.isEmpty()) {
                                    Intent chooserIntent = Intent.createChooser(shareIntentsLists.remove(0), "Choose app to share");
                                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentsLists.toArray(new Parcelable[]{}));
                                    startActivity(chooserIntent);
                                } else
                                    Log.e("Error", "No Apps can perform your task");

                            }*/

                            List<Intent> targetedShareIntents = new ArrayList<>();
                            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                            //shareIntent.setType("text/plain");
                            shareIntent.setType("text/*");
                            List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(shareIntent, 0);
                            if (!resInfo.isEmpty()) {
                                for (ResolveInfo resolveInfo : resInfo) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    //Intent targetedShareIntent = new Intent(android.content.Intent.ACTION_SEND);
                                    Intent targetedShareIntent = new Intent();
                                    //targetedShareIntent.setType("text/*");
                                    targetedShareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject to be shared");
                                    if (TextUtils.equals(packageName, "com.whatsapp")) {
                                        //targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, bodyShare);
                                        //shareIntent.setType("text/plain");
                                        targetedShareIntent.setAction(Intent.ACTION_VIEW);
                                        Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                        targetedShareIntent.setData(uriUrl);
                                        /*Uri uriUrl = Uri.parse("whatsapp://send?text="+body+"");
                                        targetedShareIntent.putExtra(Intent.ACTION_VIEW, uriUrl);
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(uriUrl);
                                        startActivity(i);*/
                                    } else {
                                        targetedShareIntent.setAction(Intent.ACTION_SEND);
                                        targetedShareIntent.setType("text/plain");
                                        targetedShareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "text message to shared");
                                    }
                                    targetedShareIntent.setPackage(packageName);
                                    targetedShareIntents.add(targetedShareIntent);
                                }
                                Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0), "Select app to share");
                                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[targetedShareIntents.size()]));
                                startActivity(chooserIntent);
                            }

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