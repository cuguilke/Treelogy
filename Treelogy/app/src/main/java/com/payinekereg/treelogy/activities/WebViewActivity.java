package com.payinekereg.treelogy.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;

import com.payinekereg.treelogy.R;
import com.payinekereg.treelogy.constructors.ListTreesConstructor;
import com.payinekereg.treelogy.tabs.ListTreesTAB;

import static com.payinekereg.treelogy.constants.MyConstants.ID;

import static com.payinekereg.treelogy.constants.Constants_English.eLOADING;
/**
 * Created by Emre on 3/11/2016.
 */
public class WebViewActivity extends AppCompatActivity {

    private WebView webView                     ;
    private ListTreesConstructor tree           ;
    private ProgressDialog mProgressDialog      ;

    private ImageView imageView                 ;
    private boolean swapImage  = false          ;

    private Button more                         ;
    private final boolean lang = EntranceActivity.lang;

    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        tree = ListTreesTAB.filteredModelList.get(getIntent().getExtras().getInt(ID));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(tree.getTreeName());
        collapsingToolbar.setExpandedTitleColor(Color.LTGRAY);

        imageView   = (ImageView)   findViewById(R.id.webViewImage);
        more        = (Button)      findViewById(R.id.webView_more);
        webView     = (WebView)     findViewById(R.id.webView);

        imageView.setBackgroundResource(tree.getLeaf());

        connectionMethod();

        more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                connectionMethod();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (swapImage)
                        imageView.setBackgroundResource(tree.getLeaf());
                    else
                        imageView.setBackgroundResource(tree.getTree());

                    swapImage= !swapImage;
                }
            });
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if(!mProgressDialog.isShowing())
                mProgressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if(mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }
    }
    public void onBackPressed()
    {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    private boolean connectionCheck()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();

        return nInfo != null && nInfo.isConnected();
    }

    private void connectionMethod()
    {
        if(connectionCheck())
        {
            more.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            CustomWebViewClient webViewClient = new CustomWebViewClient();

            String Url = "";
            if(lang)
                Url = "https://en.wikipedia.org/wiki/" + tree.getTreeName();
            else
                Url = "https://tr.wikipedia.org/wiki/" + tree.getTreeName();


            mProgressDialog = new ProgressDialog(this);
            if(lang)
                mProgressDialog.setMessage(eLOADING);
            else
                mProgressDialog.setMessage("Yükleniyor...");

            mProgressDialog.setCancelable(false);

            webView = (WebView) findViewById(R.id.webView);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.getSettings().setSupportZoom(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(webViewClient);
            webView.loadUrl(Url);
        }
        else
        {
            more.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
        }
    }
    @Override
    public void onPause() {

        finish();
        super.onPause();
    }
}
