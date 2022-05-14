package com.example.tooset_test02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SearchUrl extends AppCompatActivity {

    WebView webView;
    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_url);

        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());


        link = getIntent().getStringExtra("link");

        webView.loadUrl(link);
    }
}