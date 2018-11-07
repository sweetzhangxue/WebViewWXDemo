package com.example.zx.webviewwxdemo;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MyWebView extends WebView{

    /**
     * 是否加载过url
     * true:加载过
     * false:没加载过
     */
    private boolean isHaveLoaded = false;
    Context mContext;

    public MyWebView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        getSettings().setDomStorageEnabled(true);
        getSettings().setAppCacheEnabled(true);
        getSettings().setJavaScriptEnabled(true);
        //一般人对WebView的加速，都是建议先用webView.getSettings().setBlockNetworkImage(true); 将图片下载阻塞，然后在浏览器的OnPageFinished事件中设置webView.getSettings().setBlockNetworkImage(false); 通过图片的延迟载入，让网页能更快地显示。
        getSettings().setBlockNetworkImage(false);
        //设置自适应屏幕，两者合用
        getSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        getSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        getSettings().setSavePassword(false);
        getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        String usragent=getSettings().getUserAgentString()+" "+"gzAndroid/"+CommonUtil.getAppVersionName(mContext);
        getSettings().setUserAgentString(usragent);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            CookieManager.getInstance().acceptThirdPartyCookies(this);
        }
    }
    /**
     * 加载一个页面
     * @param url
     */
    public void loadPage(String url){
        UrlUtil util=new UrlUtil(url);
        loadPage(util);
    }

    public void loadPage(UrlUtil util){
        String uri=util.getUrl();
        loadUrl(uri);
        setHaveLoaded(true);
    }


    /**
     * 以post方式加载一个页面
     * @param url
     * @param bytes
     */
    public void postPage(String url,byte[] bytes){
        UrlUtil util=new UrlUtil(url);
        postPage(util,bytes);
    }

    public void postPage(UrlUtil util,byte[] bytes){
        String uri=util.getUrl();
        postUrl(uri,bytes);
        setHaveLoaded(true);
    }

    @Override
    public void postUrl(String url, byte[] postData) {
        super.postUrl(url, postData);
    }

    /**
     * 只单纯的加载一个url,不加入head和token以及sign
     * @param url
     */
    public void loadPageWithoutTokenAndHead(String url){
        loadUrl(url);
    }


    public boolean isHaveLoaded() {
        return isHaveLoaded;
    }

    public void setHaveLoaded(boolean haveLoaded) {
        isHaveLoaded = haveLoaded;
    }
}
