package com.qianfeng.Sdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogSdk {
    // 定义日志警示
    private static final Logger logger = Logger.getGlobal();
    //定义常量
    private static final String ver = "1.0";
    private static final String platformname = "java_server";
    private static final String chargeSuccess = "e_cs";
    private static final String sdkName = "java_sdk";
    private static final String requestUrl = "http://192.168.216.111:80/";
    private static final String chargeRefund = "e_cr";
    /**支付事件
     *  // 支付成功返回true 失败返回flase
     * @param mid
     * @param oid
     * @return
     */
    public static boolean chargeSuccess(String mid, String oid,String flag) {
        if (isEmpty(mid)||isEmpty(oid)){
            logger.log(Level.WARNING,"mid or oid is null ");
            return  false;
        }
        try {
            // umi oid 肯定不为空
            Map<String,String> data =new HashMap<String,String>();
            if (isEmpty(flag)||flag.equals("1")){
                data.put("en",chargeSuccess);
            }else if (flag.equals("2")){
                data.put("en",chargeRefund);
            }
            data.put("pl",platformname );
            data.put("sdk",sdkName);
            data.put("c_time",System.currentTimeMillis()+"");
            data.put("ver",ver);
            data.put("u_mid",mid);
            data.put("oid",oid);
            // 构造最终请求的url
            String url = buildUrl(data);
            // 将URl加入队列中。
            SendUrl.addUrlToQueue(url);
          String json = "{\"code\":200,data:{\"isSuccess\":true}}";
            return  true;
        } catch (Exception e) {
            throw new RuntimeException("请求支付成功的事件失败");
        }
    }
    // 封装url
    private static String buildUrl(Map<String, String> data)  {
        if (data.isEmpty()){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(requestUrl).append("?");
            for (Map.Entry<String,String> en:data.entrySet())
                if (!isEmpty(en.getKey())){
                sb.append(en.getKey()).append("=").
                        append(URLEncoder.encode(en.getValue(),"UTF-8"))
                        .append("&");
                }
        } catch (UnsupportedEncodingException e) {
            logger.log(Level.WARNING,"value 的编码异常");
        }
        return  sb.toString().substring(0,sb.length()-1);
    }
    // 判断字符串是否为空，为空 返回true
    public static boolean isEmpty(String input){
        return  input==null  || input.trim().equals("")||input.trim().length()==0 ?true:false;
    }
    public static boolean isNotEmpty(String input){
        return  !isEmpty(input);
    }
}


