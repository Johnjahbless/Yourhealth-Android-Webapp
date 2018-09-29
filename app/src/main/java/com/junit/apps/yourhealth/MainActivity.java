package com.junit.apps.yourhealth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    WebView webview;
    SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout layout;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_back:
                    if (webview.canGoBack()) {
                        webview.goBack();
                    }
                    return true;
                case R.id.navigation_home:
                    webview.loadUrl("https://yourhealth-d2683.firebaseapp.com");
                    return true;
                case R.id.navigation_forward:
                    if (webview.canGoForward()) {
                        webview.goForward();
                    }
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        webview = (WebView) findViewById(R.id.wv_test);

        layout = (RelativeLayout) findViewById(R.id.layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                (android.R.color.black));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webview.loadUrl("javascript:window.location.reload( true )");

            }
        });

        WebSettings webSettings = webview.getSettings();
        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setEnableSmoothTransition(true);
        webview.getSettings().setJavaScriptEnabled(true);


        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("https://yourhealth-d2683.firebaseapp.com");
    }

    public class WebViewClient extends
            android.webkit.WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            swipeRefreshLayout.setRefreshing(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            swipeRefreshLayout.setRefreshing(false);
            super.onPageFinished(view, url);

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("yourhealth-d2683.firebaseapp.com")) {
                view.loadUrl(url);

            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
            }
            return true;
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            layout.setVisibility(View.VISIBLE);
            webview.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            finish();
        }
        return false;
    }
        @Override
    public void onBackPressed() {
            if (webview.canGoBack()) {
                webview.goBack();
            } else {
                super.onBackPressed();
            }
        }
    }
