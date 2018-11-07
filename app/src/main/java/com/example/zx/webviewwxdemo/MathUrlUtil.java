package com.example.zx.webviewwxdemo;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MathUrlUtil {
    /**
     * token类型:
     * TKTYPE_GENERAL:普通的token
     * TKTYPE_SESSIONID:session
     * TKTYPE_STOCKSESSIONID:股票特有的sessionid
     */
    public static int TKTYPE_GENERAL = 1, TKTYPE_SESSIONID = 2, TKTYPE_STOCKSESSIONID = 3;

    /**
     * 签名使用的盐
     * SECRETTYPE_GENERAL:通用盐
     * SECRETTYPE_OAPI:oAPI盐
     * SECRETTYPE_STOCK:股票特有的盐
     */
    public static int SECRETTYPE_GENERAL = 1, SECRETTYPE_OAPI = 2, SECRETTYPE_STOCK = 3;
    //我们的URL通配字符
    private ArrayList<String> array_oururlMaths;

    //OAPI地址的通配
    private ArrayList<String> array_OAPIMaths;

    //FUNDAPI地址的通配
    private ArrayList<String> array_FundMaths;

    //Stock地址的通配
    private ArrayList<String> array_StockMaths;

    //Stock地址的通配
    private ArrayList<String> array_EventMaths;


    public ArrayList<String> getArray_oururlMaths() {
        if (array_oururlMaths == null) {
            //这些域下都是我们服务器的地址
            array_oururlMaths = new ArrayList<>();
            array_oururlMaths.add("(.*?).firstp2p.com(.*?)");
            array_oururlMaths.add("(.*?).firstp2plocal.com(.*?)");
            array_oururlMaths.add("(.*?).ncfgroup.com(.*?)");
            array_oururlMaths.add("(.*?).wangxinlicai.com(.*?)");

//            /**
//             * 测试环境下可能会有很多特殊的域
//             * 这种域正式环境下又不会真实存在的,就可以添加到这个里面
//             */
//
//            if (TesterUtil.DEBUGSTATE == TesterUtil.DEBUG.TEST) {
//                array_oururlMaths.add("http://10.20.69.216:8081(.*?)");
//            }
        }
        return array_oururlMaths;
    }


    public ArrayList<String> getArray_OAPIMaths() {
        if (array_OAPIMaths == null) {
            array_OAPIMaths = new ArrayList<>();
            array_OAPIMaths.add("(.*?)oapi.(.*?)");

//            /**
//             * 测试环境下可能会有很多特殊的域
//             * 这种域正式环境下又不会真实存在的,就可以添加到这个里面
//             */
//
//            if (TesterUtil.DEBUGSTATE == TesterUtil.DEBUG.TEST) {
//                array_OAPIMaths.add("(.*?)api.open(.*?)");
//            }
        }
        return array_OAPIMaths;
    }

    public ArrayList<String> getArray_FundMaths() {
        if (array_FundMaths == null) {
            array_FundMaths = new ArrayList<>();
            array_FundMaths.add("(.*?)fundapi.(.*?)");
        }

        return array_FundMaths;
    }

    public ArrayList<String> getArray_StockMaths() {
        if (array_StockMaths == null) {
            array_StockMaths = new ArrayList<>();
            array_StockMaths.add("(.*?)stockaccount.(.*?)");
            array_StockMaths.add("(.*?)stocktrade.(.*?)");
            array_StockMaths.add("(.*?)stockmarket.(.*?)");
        }
        return array_StockMaths;
    }

    public ArrayList<String> getArray_EventMaths() {
        if (array_EventMaths == null) {
            array_EventMaths = new ArrayList<>();
            array_EventMaths.add("(.*?)event.(.*?)");
        }
        return array_EventMaths;
    }


    /**
     * 判断一个url是否符合一个通配规则
     *
     * @param url        待检验的url
     * @param mathsLists 通配符集合
     * @return true:说明url在对应的通配集合内,false:url不在对应的通配集合内
     */
    public boolean isMaths(String url, ArrayList<String> mathsLists) {
        boolean ismaths = false;
        for (int i = 0; i < mathsLists.size(); i++) {
            ismaths = urlMath(url, mathsLists.get(i));
            if (ismaths) return true;
        }
        return ismaths;
    }

    /**
     * 某一个url的通配
     *
     * @param url
     * @param math
     * @return
     */
    public boolean urlMath(String url, String math) {
        return Pattern.compile(math).matcher(url).matches();
    }


    /**
     * 查看一个url是否与constantUrl的https和http中的一个相匹配
     *
     * @param url
     * @param constantUrl
     * @return
     */
    public boolean mathHttpsAndHttp(String url, String constantUrl) {
        String httpurl = constantUrl.replaceAll("https:", "http:");
        return (url.contains(constantUrl) || url.contains(httpurl));
    }

    /**
     * 是否是我们自己本站的域下的地址
     *
     * @param url
     * @return
     */
    public boolean isOurUrl(String url) {
        return isMaths(url, getArray_oururlMaths());
    }


}
