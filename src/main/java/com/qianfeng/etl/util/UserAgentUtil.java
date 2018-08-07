package com.qianfeng.etl.util;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 用于浏览器代理对象的解析
 */
public class UserAgentUtil {

    // define the  loger class
    private static final Logger logger = Logger.getLogger(UserAgentUtil.class);
    // 浏览器代理对象内容对象   封装有浏览器版本，驱动等信息
    UserAgentInfo info = new UserAgentInfo();

    // 获取uasparser对象 静态代码块
    private  static   UASparser uaSparser = null ;
    static {
        try {
            uaSparser =new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            logger.error("获取uasparser对象失败",e);
        }
    }

    // 解析浏览器对象
    public UserAgentInfo parserUserAgent(String userAgent){

        if (StringUtils.isEmpty(userAgent)){
            return  null ;

        }
        try {
          //  UserAgentInfo ua = uaSparser.parse(userAgent);
            cz.mallat.uasparser.UserAgentInfo ua = uaSparser.parse(userAgent);
            if (ua!=null){
                info.setBrowserName(ua.getUaFamily());
                info.setBrowserVersion(ua.getBrowserVersionInfo());
                info.setOsName(ua.getOsFamily());
                info.setOsVersion(ua.getOsName());
            }

        } catch (IOException e) {
            logger.error("浏览器useragent解析异常",e);
        }
        return  info;
    }
    public static class UserAgentInfo{
     private String browserName;
     private String osVersion;
     private String browserVersion;
     private String osName;

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        @Override
        public String toString() {
            return "UserAgentInfo{" +
                    "browserName='" + browserName + '\'' +
                    ", osVersion='" + osVersion + '\'' +
                    ", browserVersion='" + browserVersion + '\'' +
                    ", osName='" + osName + '\'' +
                    '}';
        }
    }

}
