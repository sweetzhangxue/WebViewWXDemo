package com.example.zx.webviewwxdemo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.example.zx.webviewwxdemo.webview.BaseWebViewActivity;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends BaseWebViewActivity {

    private MyWebView webView;
    private String url;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        webView.addJavascriptInterface(new JS(),"android");

        final String baseUtl = "http://www.runoob.com/";
//        String baseUtl = "http://www.runoob.com/images";
//        url = "file:///android_asset/html/local_test.html";
        try {
            InputStream inputStream = getAssets().open("local_test.html");
            int lenght = inputStream.available();
            byte[] buffer = new byte[lenght];
            inputStream.read(buffer);
            data = new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            data = "这里是两个图片是相对路径<img src='/images/logo.png'/> <p/> ";
        }
        webView.loadDataWithBaseURL(baseUtl, data, "text/html", "utf-8", null);

        webView.setWebViewClient(new WebViewClient(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                StringBuffer stringBuffer = new StringBuffer(baseUtl);
                stringBuffer.append("start");
                if (stringBuffer.toString().equals(request.getUrl().toString())) {
//                    Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    class JS{
        @JavascriptInterface
        public void get(String p){
            if ("1".equals(p)){
                startActivity(new Intent( MainActivity.this,DetailActivity.class));
            }else if ("2".equals(p)){
                startActivity(new Intent( MainActivity.this,DetailLoadUrlActivity.class));
            }
            Toast.makeText(MainActivity.this,"====="+p,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public MyWebView registerWebView() {
        return webView;
    }


}
