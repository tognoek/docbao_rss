package com.example.docbao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TrangDocBao extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_doc_bao);
        WebView webview = (WebView) findViewById(R.id.wv);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        webview.loadUrl(link);
        webview.setWebViewClient(new WebViewClient());
    }
}