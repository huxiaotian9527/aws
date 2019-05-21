package com.hu.aws;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @Author hutiantian
 * @Date 2019/5/20 16:38:47
 */
@Slf4j
public class AWSQuery {

    public static String url = "https://www.amazon.de/dp/";

    public static void main(String[] args) throws Exception {
        log.info("开始生成aws数据文件...");
        //获取需要处理的商品集合
        List<String> goodsList = getList();
        List<List<String>> excelList = new ArrayList<>();
        CountDownLatch countDownLatch = new CountDownLatch(goodsList.size());
        for (String str : goodsList) {
            new Thread(() -> {
                log.info("正在处理商品 " + str);
                List<String> strList;
                while ((strList = toList(str)) == null) {
                    try {
                        Thread.sleep(800);
                        log.info("重新处理商品 " + str);
                    }catch (Exception e){

                    }
                }
                log.info("商品 " + str+" 获取排行成功！");
                synchronized (AWSQuery.class) {
                    excelList.add(strList);
                }
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        int max = 0;
        //取最长的一列，其它补空
        for (List<String> list : excelList) {
            if (list.size() > max) {
                max = list.size();
            }
        }
        for (List<String> list : excelList) {
            int size = list.size();
            if (size < max) {
                for (int i = 0; i < max - size; i++) {
                    list.add("");
                }
            }
        }
        generateExcel(excelList);
        log.info("aws数据文件生成成功O(∩_∩)O");
        Thread.sleep(1500);
    }

    /**
     * 根据商品获取排行
     */
    private static List<String> toList(String goods) {
        List<String> strList = new ArrayList<>();
        try {
            Map<String, String> header = new HashMap<>();
            header.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            header.put("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
            header.put("Referer", "http://www.baidu.com");
            Document doc = Jsoup.parse(HttpsUtil.doGet(url + goods, header).trim());
            Element salesRank = doc.getElementById("SalesRank");  //选择器的形式
            String[] strings = salesRank.text().split("Nr.");
            strList.add(goods);
            for (int i = 1; i < strings.length; i++) {
                //把这种排名转换 19.883 in Garten (Siehe Top 100 in Garten)
                //成 Sport & Freizeit + 8.215
                String rank = strings[i];
                String str1 = rank.split("in")[0].trim();
                String str2 = rank.replace(str1 + " in", "").trim();
                if (str2.contains("(")) {
                    str2 = str2.split("\\(")[0].trim();
                }
                strList.add(str2);
                strList.add(str1);
            }
        } catch (Exception e) {
            log.info("商品 "+goods+" 处理失败！");
            return null;
        }
        //jsoup解析dom
        return strList;
    }


    /**
     * 从文件中读取需要获取信息的商品id
     */
    public static List<String> getList() throws Exception {
        //获取运行环境jre目录
        String jrePath = getJrePath();
        String filePath = jrePath.replace("jre","")+"config.txt";
        BufferedReader bf = new BufferedReader(new FileReader(filePath));
        String str;
        List<String> list = new ArrayList<>();
        while ((str = bf.readLine()) != null) {
            list.add(str);
        }
        return list;
    }

    /**
     * 生成excel
     */
    private static void generateExcel(List<List<String>> list) throws Exception {
        String jrePath = getJrePath();
        List<String> headList = new ArrayList<>();
        headList.add("ASIN");
        headList.add("大类名称");
        headList.add("大类排名");
        int num = (list.get(0).size() - 3) / 2;
        for (int i = 1; i < num + 1; i++) {
            headList.add("小类名称" + i);
            headList.add("小类排名" + i);
        }
        String sDate = com.hu.aws.DateUtil.toString(new Date(), DateFormatEnum.YEAR_MONTH_DAY_HH_MM_SS);
        String filename = "D:\\AWS商品信息" + sDate + ".xls";
        OutputStream out = new FileOutputStream(filename); // 输出目的地
        ExcelUtil.ReportList("aws rank", "AWS商品信息", headList, list, out);
    }


    /**
     * 获取运行时候jre目录
     */
    private static String getJrePath(){
        return System.getProperty("java.home");
    }
}
