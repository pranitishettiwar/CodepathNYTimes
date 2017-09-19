package com.codepath.nytimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import com.codepath.nytimessearch.R;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String url = getIntent().getStringExtra("url");

        WebView webview = (WebView) findViewById(R.id.wvArticle);

//        webview.setWebViewClient(new WebViewClient() {
//                                     @Override
//                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                                         view.loadUrl(url);
//                                         return true;
//                                     }
//                                 }
//
//        );
        webview.loadUrl(url);
    }

}
