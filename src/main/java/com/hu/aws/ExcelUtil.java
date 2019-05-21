package com.hu.aws;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author hutiantian
 * @Date 2018/11/20 15:17:37
 */
public class ExcelUtil {

    /*
     * 生成excel文件
     * */
    public static void ReportList(String sheetName,String top, List<String> header, List<List<String>> body, OutputStream out) throws Exception {

        //新建excel报表
        HSSFWorkbook excel = new HSSFWorkbook();
        //添加一个sheet
        HSSFSheet hssfSheet = excel.createSheet(sheetName);
        //生成样式
        HSSFCellStyle style1 = excel.createCellStyle();
        HSSFCellStyle style2 = excel.createCellStyle();

        //设置默认单元格高度
        hssfSheet.setDefaultRowHeight((short) 450);

        //设置背景色
        style1.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//设置为灰色
        style1.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style1.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);

        // 指定当单元格内容显示不下时自动换行
        style2.setWrapText(true);

        // 生成一个字体
        HSSFFont font1 = excel.createFont();
//        font1.setColor(HSSFColor.OLIVE_GREEN.index);
//        font1.setFontName("宋体"); //设置字体
//        font1.setFontHeightInPoints((short) 11); //设置字号
        font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//粗体显示

        // 生成一个字体
        HSSFFont font2 = excel.createFont();

        // 把字体应用到当前的样式
        style1.setFont(font1);
        style2.setFont(font2);

        //设置居中
        style1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style1.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中
        style2.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 居左
        style2.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直居中

        //设置边框
        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框


        HSSFRow Row = hssfSheet.createRow(0);
        //创建单元格
        HSSFCell Cell = Row.createCell(0);
        //设置单元格的值
        Cell.setCellValue(top);
        Cell.setCellStyle(style1);
//        hssfSheet.setColumnWidth(0, 5000);
        CellRangeAddress region1 = new CellRangeAddress(0, 0, (short) 0, (short) body.get(0).size()-1);
        hssfSheet.addMergedRegion(region1);
        // 设置表头
        HSSFRow firstRow = hssfSheet.createRow(1);
        for (int columnNum = 0; columnNum < header.size(); columnNum++) {
            //创建单元格
            HSSFCell hssfCell = firstRow.createCell(columnNum);
            hssfCell.setCellStyle(style2);
            //设置单元格的值
            hssfCell.setCellValue(header.size() < columnNum ? "-" : header.get(columnNum));
            //设置宽度
            hssfSheet.setColumnWidth(columnNum, 5000); //第一个参数代表列id(从0开始),第2个参数代表宽度值
        }

        // 设置主体数据
        for (int rowNum = 0; rowNum < body.size(); rowNum++) {
            //往excel表格创建一行，excel的行号是从0开始的
            HSSFRow hssfRow = hssfSheet.createRow(rowNum + 2);
            List<String> data = body.get(rowNum);
            for (int columnNum = 0; columnNum < data.size(); columnNum++) {
                //创建单元格
                HSSFCell hssfCell = hssfRow.createCell(columnNum);
                hssfCell.setCellStyle(style2);

                //设置单元格的值
                hssfCell.setCellValue(data.size() < columnNum ? "-" : data.get(columnNum));
            }
        }
        excel.write(out);
    }

}
