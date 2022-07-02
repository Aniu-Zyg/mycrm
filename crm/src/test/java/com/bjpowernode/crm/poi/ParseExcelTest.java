package com.bjpowernode.crm.poi;

import com.bjpowernode.crm.commons.utils.HSSFUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ParseExcelTest {
    public static void main(String[] args) throws Exception {
        // 根据excel文件生成HSSFWorkbook对象，封装了excel文件的所有信息
        InputStream is = new FileInputStream("D:\\Intellij IDEA\\crm\\serverDir\\studentList.xls");
        HSSFWorkbook wb = new HSSFWorkbook(is);
        // 根据wb生成HSSFSheet对象，封装了一页的所有信息
        HSSFSheet sheet = wb.getSheetAt(0); // 页的下标，从0开始，依次增加
        // 根据sheet生成HSSFRow对象，封装了一行的所有信息
        HSSFRow row = null;
        HSSFCell cell = null;
        for (int i = 0;i <= sheet.getLastRowNum();i ++){ // sheet.getLastRowNum()：最后一行的下标
            row = sheet.getRow(i); // 行的下标，从0开始，依次增加

            for (int j = 0;j < row.getLastCellNum();j ++){ // row.getLastCellNum()：最后一列的下标+1
                // 根据row生成HSSFCell对象，封装了一列的所有信息
                cell = row.getCell(j); // 列的下标，从0开始，依次增加

                // 获取列中的数据
                System.out.print(HSSFUtils.getCellValueForStr(cell) + " ");
            }

            // 每一行中的所有列都打印完，换行
            System.out.println();
        }

    }

}
