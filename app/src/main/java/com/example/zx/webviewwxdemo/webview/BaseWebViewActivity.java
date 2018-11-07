package com.example.zx.webviewwxdemo.webview;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.CookieSyncManager;
import com.example.zx.webviewwxdemo.MyWebView;
import java.io.File;


public abstract class BaseWebViewActivity extends Activity{

    private String JSCallback;

    private File cameraCache;
    /**
     * 进行H5跳转的时候,某些场景需要再次回到次页面时,此页面要刷新
     * 如:强制评测.
     * 所以这里有一个状态,当这个状态为true的时候,在onstart生命周期则会reload一次webview
     */
    private boolean isNeedGoBackRefresh = false;

    /**
     * 是否不需要关闭按钮，默认需要
     */
    private boolean noCloseBtn = false;

    /**
     * 通知H5本页面已经触发返回
     * 注:某些场景下,如页面A跳转到页面B,然后再回来的时候要触发A的刷新.
     * 如果直接使用页面刷新,Android则会产生一级历史页面.
     * 所以有些时候需要使用js来做前端刷新,此时就会用到这个方法
     */
    private boolean isNeedCallJsFunction = false;


    public void setJSCallback(String JSCallback) {
        this.JSCallback = JSCallback;
    }

    public String getJSCallback() {
        return JSCallback;
    }

    public void setCameraCache(File cameraCache) {
        this.cameraCache = cameraCache;
    }

    public void setNoCloseBtn(boolean noCloseBtn) {
        this.noCloseBtn = noCloseBtn;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CookieSyncManager.createInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isNeedGoBackRefresh && !subclassIndividualControlGobackRefresh()){
            if (registerWebView() != null){
                registerWebView().reload();
            }
            setNeedGoBackRefresh(false);
        }
        if (isNeedCallJsFunction){
            isNeedCallJsFunction = false;
            String jsfunction = "gobackjsfunc";
            registerWebView().loadUrl("javascript:"+jsfunction+"()");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isNeedCallJsFunction = true;
    }

    protected void goBack(){
        if (registerWebView().canGoBack()){
            registerWebView().goBack();
        }else {
            finish();
        }

    }

    //注册webview
    public abstract MyWebView registerWebView();

    /**
     * 某些特殊场景需要子类继承此方法进行独立刷新控制如:
     * 投资确认页点击选择投资券-->选完投资券后返回投资确认页,投资确认页的刷新
     * 当时有种case,即用户已经将投资金额填到输入框了,这个时候选完券之后返回,如果直接刷新的话
     * 输入的金额则会丢失,所以在选券的时候scheme会把填入的金额返给APP,
     * APP进行独立保存,在页面重新返回的时候使用[原先的url+独立保存的参数]对该页面进行刷新
     * <p/>
     * 而如果直接使用父类控制webview刷新,则会丢失这些[独立保存的参数]
     * <p/>
     * 所以就需要这个方法了.
     * 当这个方法返回false:则说明,父类如果发现isNeedGoBackRefresh==true,则可以直接刷新子类的webview
     * 如果返回true:则说明,子类自己会处理刷新,父类无需刷新.
     *
     * @return
     */
    public boolean subclassIndividualControlGobackRefresh() {
        return false;
    }

    /**
     * 子类是否需要独立控制用户点击back按键的处理
     * <p/>
     * 此方法默认返回false.
     * 当返回false的时候,用户按下物理back按键
     * webviewActivity页面会先判断当前的webview是否有history
     * 如果有history则会先回到上一级的history
     * 直到没有history了,才会关闭此webviewActivity页面
     * <p/>
     * 而如果返回true,则没有这个控制,默认自动finish此页面
     * <p/>
     * 有一些场景比较特殊.需要子类继承此方法并返回true
     * 例如投资确认页.在选择完一张投资券以后,需要重新load一遍url以加载上这个投资券
     * 此时就会产生一级history.
     * 如果直接使用默认的方式,则在投资确认页点back就回不去了,会一直刷上一级的history直到history刷没
     * 所以此时就要继承此方法返回true
     *
     * @return
     */
    public boolean subclassIndividualControlPressBack() {
        return false;
    }

    public void setNeedGoBackRefresh(boolean needGoBackRefresh) {
        isNeedGoBackRefresh = needGoBackRefresh;
    }
}

