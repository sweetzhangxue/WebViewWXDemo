package com.example.zx.webviewwxdemo;

import android.os.Bundle;
import com.example.zx.webviewwxdemo.webview.BaseWebViewActivity;

public class DetailLoadUrlActivity extends BaseWebViewActivity {

    private MyWebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_load_url);

        myWebView = findViewById(R.id.webView_detail_load_url);
        myWebView.loadUrl("https://shop.guazi.com/");
    }

    @Override
    public MyWebView registerWebView() {
        return myWebView;
    }
}
