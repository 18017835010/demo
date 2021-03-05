package com.unspay.util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * User: ji.chen
 * Date: 2021/3/4
 * Time: 4:42 下午
 * Description: No Description
 */
public class ExcelUtil {


     /**
      * 获取指定列
      * @param xlsPath
      * @param sheetIndex
      */
     public static void getSheetContents(String xlsPath,int sheetIndex) {
          Workbook wb = null;
          System.out.println("---------------获取指定sheet文本----------------");
          try {
               InputStream is = new FileInputStream(xlsPath);
               wb = Workbook.getWorkbook(is);
               Sheet sheet = wb.getSheet(sheetIndex);

               for (int row = 0; row < sheet.getRows(); row++) {
                    Cell[] rowCells = sheet.getRow(row);
                    for (int column = 0; column < rowCells.length; column++){
                         String str = rowCells[column].getContents();
                         if (StringUtils.isBlank(str)) {
                              continue;
                         }
                         if(column == rowCells.length - 1){
                              System.out.print(str);
                              break;
                         }
                         System.out.print(str+",");
                    }
                    System.out.println();
               }
          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     /**
      * 获取指定列
      * @param xlsPath
      * @param sheetIndex
      * @param column
      */
     public static void getRowContents(String xlsPath,int sheetIndex,int column) {
          Workbook wb = null;
          System.out.println("---------------获取指定列文本----------------");
          try {
               InputStream is = new FileInputStream(xlsPath);
               wb = Workbook.getWorkbook(is);
               Sheet sheet = wb.getSheet(sheetIndex);

               for (int row = 0; row < sheet.getRows(); row++) {
                    Cell[] cells = sheet.getRow(row);
                    if(cells.length <= column){
                         System.out.print(",");
                         continue;
                    }
                    String str = cells[column].getContents();
                    if (StringUtils.isBlank(str)) {
                         System.out.print(",");
                         continue;
                    }
                    if(row == cells.length - 1){
                         System.out.print(str);
                         break;
                    }
                    System.out.print(str+",");
//                    String idCardNo = NoahAESHelper.decrypt(str, "user.user.id_card_no");
//                    System.out.println("明文："+idCardNo);
//                    System.out.println(1 == IdCardNoUtil.getGenderIntByIdCard(idCardNo) ? "MALE":"FEMALE");
//                    System.out.println(idCardNo.substring(0, 6));
//                    System.out.println(IdCardNoUtil.getBirthMonthByIdCard(idCardNo));
               }
               System.out.println();
          } catch (Exception e) {
               e.printStackTrace();
          }
     }


     /**
      * 获取指定行
      * @param xlsPath
      * @param sheetIndex
      * @param row
      */
     public static void getColumnContents(String xlsPath,int sheetIndex,int row) {
          Workbook wb = null;
          System.out.println("---------------获取指定行----------------");
          try {
               InputStream is = new FileInputStream(xlsPath);
               wb = Workbook.getWorkbook(is);
               Sheet sheet = wb.getSheet(sheetIndex);

               //去除第一行表头
               for (int column = 0; column < sheet.getColumns(); column++) {
                    Cell[] cells = sheet.getColumn(column);
                    if(cells.length <= row){
                         System.out.print(",");
                         continue;
                    }
                    String str = cells[row].getContents();
                    if (StringUtils.isBlank(str)) {
                         System.out.print(",");
                         continue;
                    }
                    if(column == cells.length - 1){
                         System.out.print(str);
                         break;
                    }
                    System.out.print(str+",");
//                    String idCardNo = NoahAESHelper.decrypt(str, "user.user.id_card_no");
//                    System.out.println("明文："+idCardNo);
//                    System.out.println(1 == IdCardNoUtil.getGenderIntByIdCard(idCardNo) ? "MALE":"FEMALE");
//                    System.out.println(idCardNo.substring(0, 6));
//                    System.out.println(IdCardNoUtil.getBirthMonthByIdCard(idCardNo));
               }
               System.out.println();
          } catch (Exception e) {
               e.printStackTrace();
          }
     }

     /**
      * 获取指定单元格
      * @param xlsPath
      * @param sheetIndex
      * @param row
      * @param column
      */
     public static void getContents(String xlsPath,int sheetIndex,int row,int column) {
          Workbook wb = null;

          System.out.println("---------------获取指定单元格文本----------------");
          try {
               InputStream is = new FileInputStream(xlsPath);
               wb = Workbook.getWorkbook(is);
               Sheet sheet = wb.getSheet(sheetIndex);
               Cell[] cells = sheet.getRow(row);
               String str = cells[column].getContents();
               if (StringUtils.isBlank(str)) {
                    return;
               }
               System.out.println(str);
          } catch (Exception e) {
               e.printStackTrace();
          }
     }
}
