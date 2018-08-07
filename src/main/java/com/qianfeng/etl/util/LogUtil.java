package com.qianfeng.etl.util;

import com.qianfeng.common.EventLogsConstant ;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 将采集的日志一行一行的解析成key-value 便于存储
 */
public class LogUtil {
    private static final Logger logger  = Logger.getLogger(LogUtil.class);
    /**
     * 192.168.216.111^A1532576375.965^A192.168.216.111^A
     * /index.html?ver=1.0&u_mid=123&en=e_cr&c_time=1532576375614&
     * ip:192.168.216.111
     * s_time:1532576375.965
     * ver:1.0
     *
     * @param log
     * @return
     */

    public Map parserLog(String log){
        //定义一个map集合用户存储  解析日志：info
        Map<String,String> info = new ConcurrentHashMap<String,String>();
        if (StringUtils.isNotEmpty(log)){
            String [] fields = log.split(EventLogsConstant.COLUMN_SEPARTOR);
            if (fields.length==4){
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_IP,fields[0]);
               info.put(EventLogsConstant.EVENT_COLUMN_NAME_SERVER_TIME, fields[1].replaceAll("\\.",""));
                int index =  fields[3].indexOf("?");
                if (index>0) {
                    String params = fields[3].substring(index + 1);
                    handleParams(info, params);// 将解析的字段值追加到info
                    handleIp(info); // 解析ip   存放ip信息
                    handleUserAgent(info);// 解析userAgent   存放浏览器 kv信息
                }
            }
        }
      return  info;
    }
// 解析userAgent
    private void handleUserAgent(Map<String, String> info) {
        if (info.containsKey(EventLogsConstant.EVENT_COLUMN_NAME_USERAGENT)){
            UserAgentUtil.UserAgentInfo userAgent = new UserAgentUtil().parserUserAgent(info.get(EventLogsConstant.EVENT_COLUMN_NAME_USERAGENT));
            if (userAgent!=null){
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_BROWSER_NAME,userAgent.getBrowserName());
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_BROWSER_VERSION,userAgent.getBrowserVersion());
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_OS_NAME,userAgent.getOsName());
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_OS_VERSION,userAgent.getOsVersion());

            }

        }
    }

    // 利用工具类解析 ip
    private void handleIp(Map<String, String> info) {
        if (info.containsKey(EventLogsConstant.EVENT_COLUMN_NAME_IP)){
            IpParserUtil.RegionInfo region = new IpParserUtil().parserIp(info.get(EventLogsConstant.EVENT_COLUMN_NAME_IP));
            if (region!=null){
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_COUNTRY,region.getCountry());
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_PROVINCE,region.getProvince());
                info.put(EventLogsConstant.EVENT_COLUMN_NAME_CITY,region.getCity());

            }
        }
    }
// 将参数列表 的k-v 存入到info中
    private void handleParams(Map<String, String> info, String params) {
    if (StringUtils.isNotEmpty(params)){
        String[] paramkvs = params.split("&");
        try {
        for (String paramkv :paramkvs){
            String kvs[] = paramkv.split("=");
            String k = kvs[0];

            String v = URLDecoder.decode(kvs[1],"UTF-8");
               if (StringUtils.isNotEmpty(k)){
                   info.put(k,v);
               }

        }
        } catch (UnsupportedEncodingException e) {
            logger.warn("参数处理异常",e);
        }
    }


    }


}
