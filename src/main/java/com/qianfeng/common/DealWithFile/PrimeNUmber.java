package com.qianfeng.common.DealWithFile;

/**
 * 判断是否是素数、遍历0- n 之间的素数
 */
public class PrimeNUmber {
    public static void checkPrimeNumber(int n) {

        for (int i = 2; i <= n; i++) // 1不是素数，所以直接从2开始循环
        {
            int j = 2;
            while (i % j != 0) {
                j++; // 测试2至i的数字是否能被i整除，如不能就自加
            }
            if (j == i)
            //当有被整除的数字时，判断它是不是自身,若是，则说明是素数
            {
                System.out.println(i); // 如果是就打印出数字

                // BigFileSplitAndMerge.saveRecordInFile(i+"\t","//"); 保存到文件
            }
        }
    }

    public static void main(String[] args) {
        checkPrimeNumber(100);
    }
}