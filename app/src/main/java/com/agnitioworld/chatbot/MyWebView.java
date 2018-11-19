package com.agnitioworld.chatbot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class MyWebView extends AppCompatActivity {
WebView webView;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web_view);
        webView=findViewById(R.id.webview);
        progressBar=findViewById(R.id.progress);
        webView.loadUrl(getIntent().getStringExtra(Utils.Intents.LINKURL));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            { progressBar.setProgress(progress);
                if(progress == 100){
                    progressBar.setVisibility(View.INVISIBLE);
                    getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF);
                }
            }
        });


//        webview.loadUrl("http://www.google.com");
//
//        webView.setWebViewClient(new MyClient());
    }
//    class MyClient extends WebViewClient {
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
//            Log.e("webviewpage","   started");
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            Log.e("webviewpage","   finished");
//        }
//
//        @Override
//        public void onPageCommitVisible(WebView view, String url) {
//            super.onPageCommitVisible(view, url);
//            Log.e("webviewpage","   commit");
//        }
//        //        @Override
////        public void onProgressChanged(WebView view, int newProgress) {
////            Log.e("progress---->",""+newProgress);
////        }
//
//    }
}
