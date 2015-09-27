package com.ttn.googlehackathon.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ttn.googlehackathon.R;

/**
 * Project: <b>GoogleHackathon</b><br/>
 * Created by: Akhilesh Dhar Dubey on 27/9/15.<br/>
 * Email id: akhilesh.dubey@tothenew.com<br/>
 * Skype id: akhileshdubey91
 */
public class AboutAppActivity extends ActionBarActivity {

    String url;
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        initViews();
    }

    protected void initViews() {

        url = getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.setWebViewClient(new WebBrowserClient());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.loadUrl(url);
        WebChromeClient webChromeClient = new WebChromeClient();
        webView.setWebChromeClient(webChromeClient);

    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    private class WebBrowserClient extends WebViewClient {
        ProgressDialog progressDialog;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (progressDialog == null)
                progressDialog = new ProgressDialog(AboutAppActivity.this);
            progressDialog.show();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (progressDialog != null)
                progressDialog.dismiss();
            progressDialog = null;
            super.onPageFinished(view, url);
        }

    }

}