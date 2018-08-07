package com.qianfeng.Sdk;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 用来发送http 请求已经构建好的url
 */
public class SendUrl {
    // 警告日志等级打印对象
    private static final Logger logger = Logger.getGlobal();

    //定义存储url】的队列
    private static final BlockingQueue<String> queue = new LinkedBlockingDeque<String>();
    // 创建单例对象
    private static SendUrl sendUrl = null;

    // 私有构造器
    private SendUrl() {
    }
    // 共有的获取该类的实例的方法
    public static SendUrl getInstance() {
        if (sendUrl == null) {
            synchronized (SendUrl.class) {//防止两个线程同时来获取
                if (sendUrl == null) {
                    sendUrl = new SendUrl();
                    //创建一个线程
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //TODO
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            SendUrl.sendUrl.sendUrl();
                        }
                    });
                // 将线程启动
                    //如果挂载启动，
                //    th.setDaemon(true);//建议服务器运行时后台启动。
                    th.start();
                }

            }
        }
        return sendUrl;
    }

    /**
     * 将url添加自己的发送url的队列中
     */
public static void addUrlToQueue(String url) {
    try {
        getInstance().queue.put(url);
        //    getInstance().queue.add(url);
    } catch (Exception e) {
        logger.log(Level.WARNING, "添加url到队列异常");
    }
}
// 循环队列中的url进行发送
    public static void sendUrl(){
    while(true){
        try {
            String url =  queue.take();
            HttpUtil.requesUrl(url);// 发送
        } catch (InterruptedException e) {
            logger.log(Level.WARNING, "获取队列中url异常");
        }
    }
    }
    /**
     * 用于发送url的工具类
     */
    public static class HttpUtil {
        /**
         * 发送url
         */
        public static void requesUrl(String url) {
            HttpURLConnection coon = null;
            InputStream is = null;
            //构建url、
            try {
                URL url1 = new URL(url);
                //获取conn
                coon = (HttpURLConnection) url1.openConnection();

                //为conn设置属性
                coon.setRequestMethod("GET");
                coon.setConnectTimeout(5000);
                coon.setReadTimeout(5000);
                // 真正的发送
                is = coon.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                coon.disconnect();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }
}



