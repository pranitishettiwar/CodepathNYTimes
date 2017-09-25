package com.codepath.nytimessearch.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.model.Article;

import org.parceler.Parcels;

public class ArticleActivity extends AppCompatActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Unwrap content
        Article article = (Article) Parcels.unwrap(getIntent().getParcelableExtra("article"));

        WebView webview = (WebView) findViewById(R.id.wvArticle);
        if (isNetworkAvailable()) {

            // Configure related browser settings
            webview.getSettings().setLoadsImagesAutomatically(true);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            // Zoom out if the content width is greater than the width of the viewport
            webview.getSettings().setLoadWithOverviewMode(true);

            webview.getSettings().setSupportZoom(true);
            webview.getSettings().setBuiltInZoomControls(true); // allow pinch to zoom

            // Configure the client to use when opening URLs
            webview.setWebViewClient(new MyBrowser());
            // Load the initial URL
            webview.loadUrl(article.getWebUrl());

        } else {
            webview.loadUrl(article.getWebUrl());
            Toast.makeText(ArticleActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);

        MenuItem searchItem = menu.findItem(R.id.action_share);
        return super.onCreateOptionsMenu(menu);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    // Manages the behavior when URLs are loaded
    private class MyBrowser extends WebViewClient {
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }
    }

}
