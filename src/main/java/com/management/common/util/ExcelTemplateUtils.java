package com.management.common.util;

/**
 * @description: Excel 工具类
 * @Auther: zyf
 * @Date: 2019-09-22
 **/

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExcelTemplateUtils {

    /**
     * 设置 excel 基本信息
     *
     * @param workbook
     * @param sheetName
     * @param sheetIndex
     * @return
     */
    static public Sheet createSheet(Workbook workbook, String sheetName, int sheetIndex) {
        workbook.createSheet(sheetName);
        return workbook.getSheetAt(sheetIndex);
    }

    /**
     * 设置表格基本样式
     *
     * @param book
     * @return
     */
    static public CellStyle setDefaultCellStyle(Workbook book) {
        CellStyle cellStyle = book.createCellStyle();
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
        cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        return cellStyle;
    }

    /**
     * 获得合并单元格的值
     *
     * @param cellRangeAddress
     * @param sheet
     * @return
     */
    static public String getCellRangeAddressValue(Sheet sheet, CellRangeAddress cellRangeAddress) {
        return getCellValue(sheet, cellRangeAddress.getFirstRow(), cellRangeAddress.getFirstColumn());
    }


    /**
     * string 转 double
     *
     * @param string
     * @return
     */
    static public Double toDouble(String string) {
        if (string.isEmpty())
            return null;
        return Double.parseDouble(string);
    }

    /**
     * string 转 integer
     *
     * @param string
     * @return
     */
    static public Integer toInteger(String string) {
        return Objects.requireNonNull(toDouble(string)).intValue();
    }

    /**
     * 获得所有 合并单元格，并进行排序
     *
     * @param
     * @return
     */
    static public List<CellRangeAddress> getCellRangeAddressList(Sheet sheet) {
        return IntStream
                .range(0, sheet.getNumMergedRegions())
                .mapToObj(sheet::getMergedRegion)
                .sorted((a, b) -> {
                    if (a.getFirstRow() == b.getFirstRow())
                        return a.getFirstColumn() - b.getFirstColumn();
                    return a.getFirstRow() - b.getFirstRow();
                }).collect(Collectors.toList());
    }

    /**
     * 获得指定单元格的数据
     *
     * @param sheet
     * @param row
     * @param column
     * @return
     */
    static public String getCellValue(Sheet sheet, int row, int column) {
        sheet.getRow(row).getCell(column).setCellType(Cell.CELL_TYPE_STRING);
        return sheet.getRow(row).getCell(column).toString();
    }

}
