package com.management.common.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

public class ExcelTitleUtil {

    public void createStudentTitle(HSSFSheet sheet) {
        String[] strs = {"学号", "姓名", "班级", "性别", "年龄", "身份证号", "入学日期", "住址"};
        setTitle(sheet, 8, strs);
    }

    public void createCourseTitle(HSSFSheet sheet) {
        String[] strs = {"课程编号", "课程名称", "课程描述", "课程时间", "教师名称", "教师编号", "状态"};
        setTitle(sheet, 7, strs);
    }

    public void createScoreTitle(HSSFSheet sheet) {
        String[] strs = {"学号", "姓名", "课程编号", "课程名称", "考试时间", "考试成绩", "状态"};
        setTitle(sheet, 7, strs);
    }

    public void createClassTitle(HSSFSheet sheet) {
        String[] strs = {"班级名称", "班级描述", "年级", "班主任姓名", "班主任编号"};
        setTitle(sheet, 5, strs);
    }

    private void setTitle(HSSFSheet sheet, int num, String[] strs) {
        HSSFRow row = sheet.createRow(0);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        for (int i = 0; i < num; i++) {
            sheet.setColumnWidth(i, 15 * 256);
        }

        HSSFCell cell;
        int j = 0;
        for (String str : strs) {
            cell = row.createCell(j);
            cell.setCellValue(str);
            j++;
        }
    }
}
