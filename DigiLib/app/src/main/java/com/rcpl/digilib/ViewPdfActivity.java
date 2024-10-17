package com.rcpl.digilib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class ViewPdfActivity extends AppCompatActivity {
    WebView webView1;
    ProgressBar progressbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();



        webView1 = (WebView)findViewById(R.id.webview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new Callback());
        //String filename ="http://www3.nd.edu/~cpoellab/teaching/cse40816/android_tutorial.pdf";
        String filename = bundle.getString("URL VALUE");
        webView1.loadUrl("http://docs.google.com/gview?embedded=true&url=" + filename);
        //    webview.loadUrl("https://firebasestorage.googleapis.com/v0/b/mycloud-541bb.appspot.com/o/cprogramming_tutorial.pdf?alt=media&token=d5798e38-c9bd-4c0e-9124-84dc7ec40127");
        webView1.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                progressbar.setVisibility(View.GONE);

            }
        });





    }


    private class Callback extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // return super.shouldOverrideUrlLoading(view, request);
            return (false);
        }

       /* @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }*/
    }
}

