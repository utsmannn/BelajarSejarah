package com.utsman.kucingapes.mobilelearningprodisejarah;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Brosur extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brosur);
        WebView brosur = findViewById(R.id.brosur);
        WebSettings settings = brosur.getSettings();
        settings.setJavaScriptEnabled(true);
        brosur.loadUrl("https://kucingapp.000webhostapp.com/brosur");
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
