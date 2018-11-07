package com.example.zx.webviewwxdemo;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * *讲URL分解，属性名与值按照键值对的形式存入urlElementsMap中
 * https://test03.api.firstp2plocal.com/deal/confirm?id=8693&code=&token=0d48f74dd215dca4e10e0952af88cf8d58230b46e1af9f5c952f334d3efac17b&signature=9EB761AE9CA96AE3C2235C607FEEB13D
 */
public class UrlUtil {
    MathUrlUtil mathUtil;
    //URL地址项
    private String mUrlAddress;
    //存储url中的参数项

    private String mUrl;

    /**
     * 需要忽略的parameters
     */
    private List<String> ignoreParams;

    private LinkedHashMap<String, String> urlElementsMap;


    public void setIgnoreParams(List<String> ignoreParams) {
        this.ignoreParams = ignoreParams;
    }

    public UrlUtil(String url) {
        mathUtil = new MathUrlUtil();
        mUrl = url;
        setUrl();
    }

    public UrlUtil() {
        mathUtil = new MathUrlUtil();
    }

    public List<String> getIgnoreParams() {
        if (ignoreParams == null) {
            ignoreParams = new ArrayList<String>();
            ignoreParams.add("token");
            ignoreParams.add("signature");
            ignoreParams.add("timestamp");
            ignoreParams.add("sessionId");
            ignoreParams.add("site_id");
        }
        return ignoreParams;
    }

    public void setUrl() {
        if (StringUtil.isNull(mUrl)) {
            Log.i("","Error! url is empty");
            return;
        }
        doSplitOperatUrl();
    }

    /**
     * 做一次url拆解
     */
    private void doSplitOperatUrl() {
        //如果Url中不含有"？"
        if (!mUrl.contains("?")) {
            mUrlAddress = mUrl;
        } else {
            //将“？”前的部分存入mUrlAddress作为地址项
            mUrlAddress = mUrl.substring(0, mUrl.indexOf("?"));
        }

        //只有当urladdress是我们的地址时,才会带上token,siteid等参数
        if (mathUtil.isOurUrl(mUrlAddress)) {
            decomposeUrlIntoMap();
        }
    }


    //将URL分解
    private void decomposeUrlIntoMap() {
        getUrlElementsMap().clear();
        Uri myUri = Uri.parse(mUrl);
        Set<String> paramNames = myUri.getQueryParameterNames();
        Iterator<String> paramIterator = paramNames.iterator();
        while (paramIterator.hasNext()) {

            String paramName = paramIterator.next();
            String paramValue = queryParameterWithoutDecode(paramName, myUri);
//                String paramValue = myUri.getQueryParameter(paramName);


            boolean nBoolNeedAddParam = false;
            boolean nBoolIsOurUrlAddress = mathUtil.isOurUrl(mUrlAddress);
            //如果不是我们的地址,则所有参数默认都添加进map(即使第三方地址带了token)
            if (!nBoolIsOurUrlAddress) nBoolNeedAddParam = true;
                //如果是我们的地址,则只有不在忽略列表里面的参数才添加进map(因为最后geturl的时候会默认在末尾统一加上)
            else if (nBoolIsOurUrlAddress && !getIgnoreParams().contains(paramName)) nBoolNeedAddParam = true;

            if (nBoolNeedAddParam) {
                getUrlElementsMap().put(paramName, paramValue);
            }
        }
    }

    /**
     * 生成URL
     *
     * @param
     * @return
     */
    public String getUrl() {
        if (mUrlAddress == null) {
            return null;
        } else {
            //只有确定是我们自己的地址的时候才会添加这些必须参数,否则不添加
            if (mathUtil.isOurUrl(mUrlAddress)) {
                //将地址项 mUrlAddress存入resultUrlSb
                StringBuilder resultUrlSb = new StringBuilder(mUrlAddress);
                if (getUrlElementsMap().size() > 0) {
                    resultUrlSb.append("?");
                    //对urlElementsMap中的参数项进行遍历
                    Iterator<String> it = getUrlElementsMap().keySet().iterator();
                    while (it.hasNext()) {
                        String keyTemp = it.next();
                        resultUrlSb.append(keyTemp + "=" + getUrlElementsMap().get(keyTemp));
                        //如果是最后一个参数了,则不在末尾添加&符号了
                        if (it.hasNext()) resultUrlSb.append("&");
                    }
                    resultUrlSb.append("&" + resultUrlSb.toString());
                } else {
                    resultUrlSb.append("?" + resultUrlSb.toString());
                }
                return resultUrlSb.toString();
            } else {
                return mUrl;
            }
        }
    }

//    /**
//     * 添加token
//     */
//    private void addToken() {
//        //如果用户已经登录，url中加入token项
//        if (UserInfoUtil.getUserinfo().isLogin()) {
//            int tktype = mathUtil.getTokenType(mUrlAddress);
//            if (tktype == MathUrlUtil.TKTYPE_GENERAL) {
//                getUrlElementsMap().put("token", UserInfoUtil.getUserinfo().getToken());
//            } else if (tktype == MathUrlUtil.TKTYPE_SESSIONID) {
//                getUrlElementsMap().put("sessionId", UserInfoUtil.getUserinfo().getToken());
//            }
//        }
//    }

//    /**
//     * 添加一些其他的必要参数
//     */
//    private void addOtherParam() {
//        getUrlElementsMap().put("site_id", LocalParamUtil.getSiteID());
//        getUrlElementsMap().put("timestamp", CommonUtil.getSystemCurrentTime() + "");
//        /**
//         *
//         * 如果红包币开关开启则添加特殊参数
//         *
//         * 由于刚开始只考虑到了分享红包的页面.
//         * 但是实际测试中发现投资确认页也存在需要传入此参数的场景(投资时 含红包XXX元 需改成 网信币 )
//         * 为避免日后上线还会有其他页面因没有想到而导致疏漏,遂将此参数加入基础属性中.
//         * 本着:多一个参数没啥影响,但少一个参数可能就要发版的原则吧>_<
//         *
//         */
//        if (OnLineParamUtil.getParamWXB()) {
//            /**
//             * 由于一个url在加载时会被拆成map,在添加完必要参数后再会合成URL
//             * 如果服务端有意在加载的时候就修改此值(虽然想不到什么适合场景,但稳妥起见,此功能保留...)
//             * 也是可以支持的,APP不会强制将wxb赋成true,只有在url里面没有此字段时才会加上.
//             */
//            if (!getUrlElementsMap().containsKey("wxb")) {
//                getUrlElementsMap().put("wxb", "true");
//            }
//        }
//    }

//    /**
//     * 添加必须要的参数
//     */
//    private void addNecessaryParams() {
//        addToken();
//        addOtherParam();
//    }

//    /**
//     * 获取sign字符串
//     *
//     * @param url
//     * @return
//     */
//    private String getSignature(String url) {
//        StringBuilder tempSb = new StringBuilder();
//        if (mathUtil.getSecretType(url) == MathUrlUtil.SECRETTYPE_OAPI) {
//            HashMap<String, String> map = CommonUtil.urlToHashmap(url);
//            tempSb.append("sign=" + SignForAuthorize.getSignature(MobileApplication.myApplication, map, Constant.OAPICLIENTSECRET));
//        } else if (mathUtil.getSecretType(url) == MathUrlUtil.SECRETTYPE_STOCK) {
//            HashMap<String, String> map = CommonUtil.urlToHashmap(url);
//            tempSb.append("signature=" + SignForAuthorize.getSignature(MobileApplication.myApplication, map, Constant.STOCKCLIENTSECRET));
//        } else {
//            tempSb.append("signature=" + SignUtil.getSignature(MobileApplication.myApplication, url));
//        }
//        return tempSb.toString();
//    }

    /**
     * 获取URL中的键值对hashmap
     *
     * @return
     */
    public LinkedHashMap<String, String> getUrlElementsMap() {
        if (urlElementsMap == null) urlElementsMap = new LinkedHashMap<>();
        return urlElementsMap;
    }


    /**
     * 由于uri不能被继承,所以把源码抠出来,做了一个不urldecode的获取方法
     *
     * @param key
     * @param uri
     * @return
     */
    public static String queryParameterWithoutDecode(String key, Uri uri) {
        if (uri.isOpaque()) {
            throw new UnsupportedOperationException("This isn't a hierarchical URI.");
        }
        if (key == null) {
            throw new NullPointerException("key");
        }

        final String query = uri.getEncodedQuery();
        if (query == null) {
            return null;
        }

        final String encodedKey = uri.encode(key, null);
        final int length = query.length();
        int start = 0;
        do {
            int nextAmpersand = query.indexOf('&', start);
            int end = nextAmpersand != -1 ? nextAmpersand : length;

            int separator = query.indexOf('=', start);
            if (separator > end || separator == -1) {
                separator = end;
            }

            if (separator - start == encodedKey.length() && query.regionMatches(start, encodedKey, 0, encodedKey.length())) {
                if (separator == end) {
                    return "";
                } else {
                    String encodedValue = query.substring(separator + 1, end);
                    return encodedValue;
                }
            }

            // Move start to end of name.
            if (nextAmpersand != -1) {
                start = nextAmpersand + 1;
            } else {
                break;
            }
        } while (true);
        return null;
    }

}
