package com.qianfeng.common.DealWithFile;

import com.qianfeng.etl.util.IpParserUtil;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * 需求：两个100G文件 进行统计分析到一份数据中（表格式，字段相同），使用java分析合并，内存16G+1T主机
 *1、实现一个文件分割器，把一个大文件，分割成若干个小文件（文件名为：原文件名+原扩展名+编号+.dat）
 * 2、分割后的文件进行合并。  注意，以字节流，大小进行切分的时候，数据连接部，会出现数据失真。
 * 最佳结局方案：以行为单位读取分割，抽取数据
 */
public class BigFileSplitAndMerge {
    private static final Logger logger = Logger.getLogger(BigFileSplitAndMerge.class);
    /**
     * 文件分割：
     * src：源文件路径
     * fileSize分割后每个文件大小，单位为MB
     * dest目标文件路径
     */
    public static void splitWithFile(String src,int fileSize,String dest ){
        //判断路径文件是否有必要切分
        if ("".equals(src) || src == null || fileSize == 0 || "".equals(dest)) {
                logger.error("please check your source of path is legal or the split fileSize？");
        }
        //获取源文件
        File srcfile = new File(src);
        //源文件大小
        Long srcSize = srcfile.length();
        //分割后目标文件大小
        Long destSize = Long.valueOf(1024*fileSize);
        //分割后的文件数量
        int number = (int) (srcSize/destSize);
        number = srcSize%destSize==0?number:(number+1);
        //切出文件名
        String fileName = src.substring(src.lastIndexOf("\\"));
        /**
         * 字节流进行读取操作
         */
        InputStream in = null; //输入字节流
        BufferedInputStream bis = null; // 输入缓冲流
        byte[] bytes = new byte[1024];//以M为单位读取文件
        OutputStream out=null;
        BufferedOutputStream bos=null;

        try {
            in = new FileInputStream(srcfile);
            bis = new BufferedInputStream(in);
            for (int i= 0 ;i<number;i++){
                int len = -1; //每次读取的长度值
                String destName = dest+File.separator+fileName+"_"+i+".txt";
                out  = new FileOutputStream(destName);
                  bos = new BufferedOutputStream(out);
                int count = 0 ;
                while((len=bis.read(bytes))!=-1){
                bos.write(bytes,0,len);
                count+=len;
                if (count>=destSize)
                    break;
                }
                //关闭输出流之前，要先调用flush方法强制缓冲区中的内容输出，并清空缓冲区
                bos.flush();
            }
            bis.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bos!=null)bos.close();
                if (out!=null)out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 数据合并：拼接文件路径时，文件的根路径很重要哈
     * @param args destPath 目标目录  srcPath 源文件目录
     */
    public static void merge(String destPath,String ... srcPath){
        if (destPath==null||"".equals(destPath)||srcPath==null){
            logger.error("Path of sourcce or destPath  is not exist！");
        }
        for (String string:srcPath){
            if ("".equals(string)||string==null)
                logger.error("source of path is null ");
        }
        // 合并后的文件名
        String name  = srcPath[0].substring(srcPath[0].lastIndexOf("\\"));
        String destName = name.substring(0,name.lastIndexOf("_"));
        destPath =destPath+destName;//合并后的文件路径
        File destFile=new File(destPath);//合并后的文件
        OutputStream out = null;
        BufferedOutputStream bos = null;
        BufferedInputStream bis =null;
        InputStream in =null;
        try {
            out = new FileOutputStream(destFile);
            bos = new BufferedOutputStream(out);
            for (String src :srcPath){
                File srcFile = new File(src);
                  in = new FileInputStream(srcFile);
                  bis = new BufferedInputStream(in);
                byte [] bytes = new byte[1024*1024];
                int len = -1 ;
                while((len=bis.read(bytes))!=-1){
                    bos.write(bytes,0,len);
                }
              bos.flush();
            }
            bis.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bos!=null)bos.close();
                if (out!=null)out.close();
            } catch (IOException e) {
                logger.warn("outputstream  is not close");
            }
        }
    }

    /**
     * 以行为单位进行读取，分割字符
     */
    public static void ReadOfLine(String src,String destPath){
        File file = new File(src);
        BufferedReader reader = null;
        String temp = null;
        int line  = 1;
        try {
            reader = new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
             String string = getNewString(temp);
             saveRecordInFile(string,destPath);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //保存到文件
    private static void saveRecordInFile(String newStr,String destPath) {
        File record = new File(destPath); // 保存结果文件
        FileWriter writer = null;
        try {


            if (!record.exists()){
                // 文件不存在则新建
                File dir = new File(record.getParent());
                dir.mkdirs();
                record.createNewFile();
            }
            writer = new FileWriter(record,true);//追加写入文件。
            writer.write(newStr);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer!=null) try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //切分行字段
    // 以空格等正则分割字符串重新组合字符串或字段
    private static String getNewString(String temp) {
        String str1="";
        String str2="";
        String str3="";
        //String []arrayStr=temp.split("\\s+");
        String []arrayStr=temp.split("\\^A");
        str1="\n\t\t"+arrayStr[0];
//        str2="\t"+arrayStr[1];
//        str3="\t"+arrayStr[2];
        return str1;
    }
    // 保存行字符到文件
    


    public static void main(String[] args) {
        /**
         * 切割测试
         */
                String src = "E:\\BC-11.1533008182809.log";
              //  String src = "D:\\项目工具\\千锋项目\\项目课件\\12etl01\\qqwry.dat";

//                int fileSize = 1;
//                String dest = "E:\\split";
//                System.out.println("切割开始");
//                splitWithFile(src,fileSize,dest);
//                System.out.println("successed");
        /**
         * 合并测试
         */
            String destPath = "E:\\split";
            String[] srcPaths = {
                    "E:\\split\\BC-11.1533008182809.log_0.txt",
                    "E:\\split\\BC-11.1533008182809.log_1.txt",
                    "E:\\split\\BC-11.1533008182809.log_2.txt",
                    "E:\\split\\BC-11.1533008182809.log_3.txt",
                    "E:\\split\\BC-11.1533008182809.log_4.txt"
            };
//            System.out.println("合并开始");
//            merge(destPath,srcPaths);
//            System.out.println("合并结束");
        /**
         * 行读取测试
         */
        String srcFile = "E:\\BC-11.1533008182809.log";
        String destFile ="E:\\RecordLine.txt";
        System.out.println("行读取分析开始");
        ReadOfLine(srcFile,destFile);
        System.out.println("行读取分析结束");
    }


}
