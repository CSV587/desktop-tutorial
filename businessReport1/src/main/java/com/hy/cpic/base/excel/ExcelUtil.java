package com.hy.cpic.base.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 11:27 2018/8/23
 * @ Description ：excel工具类
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Slf4j
class ExcelUtil {

    /**
     * .
     * 生成07格式的excel对象 使用流方式防止内存溢出
     *
     * @param hWorkbook  execl对象
     * @param list       队列
     * @param title      字段名
     * @param className  类名
     * @param exportType 指定类型
     * @return Workbook
     */
    Workbook exportExcel(Workbook hWorkbook, List list, String title, Class className, Integer exportType, String sheetName) {
        // 获取属性
        List<Field> fields = new ArrayList<>();
        while (className != null) {
            //当父类为null的时候说明到达了最上层的父类(Object类).
            fields.addAll(Arrays.asList(className.getDeclaredFields()));
            className = className.getSuperclass();
            //得到父类,然后赋给自己
        }
        List<Field> fieldList = new ArrayList<>();
        for (Field fie : fields) {
            if (fie.isAnnotationPresent(ExcelAnnotation.class)) {
                fieldList.add(fie);
            }
        }
        // 按照id进行排序
        fieldList.sort(Comparator.comparingInt(f -> f.getAnnotation(ExcelAnnotation.class).id()));

        int columnsize = fieldList.size(), rowindex = 0;
        // 创建一个HSSFWorbook对象
        if (hWorkbook == null) {
            hWorkbook = new XSSFWorkbook();
        }
        // 创建一个HSSFSheet对象（sheet页）
        Sheet hSheet;
        if (sheetName == null) {
            hSheet = hWorkbook.createSheet();
        } else {
            hSheet = hWorkbook.createSheet(sheetName);
        }
        // 创建第一行(此行作为头)
        Row hRow = hSheet.createRow(rowindex++);
        hRow.setHeight((short) 380);
        // 创建单元格（第一（0）个）
        Cell hCell = hRow.createCell((short) 0);
        // 设置样式
        CellStyle cellStyle = getCellStyle(hWorkbook, (short) 300);
        //设置背景颜色
        cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
        //solid 填充  foreground  前景色
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 将上面获得的样式对象给对应单元格
        hCell.setCellStyle(cellStyle);
        //设置标题行
        hCell.setCellValue(title);

        if (fieldList.isEmpty()) {
            return hWorkbook;
        }

        // 创建第二列，列名
        hRow = hSheet.createRow(rowindex++);
        cellStyle = getCellStyle(hWorkbook, (short) 240);
        //设置背景颜色
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        createTitle(exportType, fieldList, columnsize, hSheet, hRow, cellStyle);
        hSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnsize - 1));

        if (list == null || list.isEmpty()) {
            return hWorkbook;
        }
        //生成数据
        cellStyle = getCellStyle(hWorkbook, (short) 220);// 设置单元格格式
        dealCreateRow(list, fieldList, columnsize, rowindex, hSheet, cellStyle);

        // 固定标题（前一个参数代表列，后一个参数单表行）
        hSheet.createFreezePane(0, 1);
        // 固定列名（前一个参数代表列，后一个参数单表行）
        hSheet.createFreezePane(0, 2);
        return hWorkbook;
    }

    /**
     * .
     * 生成07格式的excel对象 使用流方式防止内存溢出
     *
     * @param list       队列
     * @param title      字段名
     * @param className  类名
     * @param exportType 指定类型
     * @return Workbook
     * @throws Exception 异常
     */
    Workbook exportExcel(List list, String title, Class className, Integer exportType) throws Exception {
        return exportExcel(null, list, title, className, exportType, null);
    }

    private void dealCreateRow(List list, List<Field> fieldList, int columnsize, int rowindex, Sheet hSheet,
                               CellStyle cellStyle) {
        Row hRow;
        Cell hCell;
        for (Object model : list) {
            hRow = hSheet.createRow(rowindex++);
            for (int i = 0; i < columnsize; i++) {
                hCell = hRow.createCell((short) i);
                Field field = fieldList.get(i);
                String result = getObjValue(model, field);
                hCell.setCellValue(new XSSFRichTextString(result));
                hCell.setCellStyle(cellStyle);
            }
        }
    }

    private Object getEnumValue(Object result)
        throws NoSuchFieldException {
        Enum eResult = (Enum) result;
        Field vField = eResult.getClass().getField(eResult.name());
        if (vField != null) {
            ExcelEnumAnnotation excelEnumAnnotation = vField.getAnnotation(ExcelEnumAnnotation.class);
            if (excelEnumAnnotation != null) {
                result = excelEnumAnnotation.name();
            }
        }
        return result;
    }

    private Object getDataValue(Object result, Field field) {
        JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
        if (jsonFormat != null) {
            String pattern = jsonFormat.pattern();
            result = new SimpleDateFormat(pattern).format(result);
        } else {
            result = result.toString();
        }
        return result;
    }

    private String getObjValue(final Object model,
                               final Field field) {
        try {
            Class clazz = model.getClass();
            String methodName = "get" + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
            Method method = clazz.getMethod(methodName);
            Object result = method.invoke(model);
            if (result != null) {
                if (result instanceof Enum) {
                    result = getEnumValue(result);
                } else if (result instanceof Date) {
                    result = getDataValue(result, field);
                }
                return result.toString();
            } else {
                return "-";
            }
        } catch (NoSuchMethodException |
            IllegalAccessException |
            InvocationTargetException |
            NoSuchFieldException e) {
            return "-";
        }
    }

    /**
     * .
     * 生成列名
     *
     * @param exportType 模板编号
     * @param fieldList  列名
     * @param columnsize 列数
     * @param hSheet     行
     * @param hRow       列
     * @param cellStyle  单元格格式
     */
    private void createTitle(Integer exportType, List<Field> fieldList, int columnsize, Sheet hSheet, Row hRow,
                             CellStyle cellStyle) {
        Cell hCell;
        for (int i = 0; i < columnsize; i++) {
            Field field = fieldList.get(i);
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                // 获取该字段的注解对象
                ExcelAnnotation anno = field.getAnnotation(ExcelAnnotation.class);
                hCell = hRow.createCell((short) i);
                String colName = field.getAnnotation(ExcelAnnotation.class).name().length > exportType
                    ? field.getAnnotation(ExcelAnnotation.class).name()[exportType]
                    : field.getAnnotation(ExcelAnnotation.class).name()[0];
                hCell.setCellValue(colName);
                hCell.setCellStyle(cellStyle);
                hSheet.setColumnWidth((short) i, (short) anno.width());
            }
        }
    }

    /**
     * 功能 :设置excel表格默认样式
     *
     * @param hWorkbook  需导出Excel数据
     * @param fontHeight 字体粗度
     * @return 单行数据
     */
    private CellStyle getCellStyle(Workbook hWorkbook, short fontHeight) {
        CellStyle cellStyle;
        Font font;
        cellStyle = hWorkbook.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
        cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
        cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        font = hWorkbook.createFont();
        font.setFontHeight(fontHeight);
        font.setBoldweight((short) 500);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return cellStyle;
    }


    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param workbook  Workbook对象
     * @return 对象
     */
    Workbook exportExcel(String sheetName, String[] title, String[][] values, Workbook workbook) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (workbook == null) {
            workbook = new XSSFWorkbook();
        }

        int rowindex = 0;
        // 创建一个HSSFSheet对象（excll的表单）
        Sheet xSheet = workbook.createSheet();
        // 创建行（excel的行）
        Row hRow = xSheet.createRow(rowindex++);
        //设置行高度
        hRow.setHeight((short) 380);
        // 创建单元格（从0开始）
        Cell hCell = hRow.createCell((short) 0);
        //样式对象
        CellStyle cellStyle = getCellStyle(workbook, (short) 300);
        //设置背景颜色
        cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
        //solid 填充  foreground  前景色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        // 将上面获得的样式对象给对应单元格
        hCell.setCellStyle(cellStyle);
        //设置标题行
        hCell.setCellValue(sheetName);

        if (title.length == 0) {
            return workbook;
        }

        //创建第二行，代表列名
        hRow = xSheet.createRow(rowindex++);
        cellStyle = getCellStyle(workbook, (short) 300);
        //设置背景颜色
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        //solid 填充  foreground  前景色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        for (int i = 0; i < title.length; i++) {
            String name = title[i];
            hCell = hRow.createCell((short) i);
            hCell.setCellValue(name);
            hCell.setCellStyle(cellStyle);
            xSheet.setColumnWidth((short) i, (short) 10000);
        }
        xSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, title.length - 1));

        if (values.length == 0) {
            return workbook;
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            hRow = xSheet.createRow(i + 2);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                hRow.createCell(j).setCellValue(values[i][j]);
            }
        }


        // 固定标题（前一个参数代表列，后一个参数单表行）
        xSheet.createFreezePane(0, 1);
        // 固定列名（前一个参数代表列，后一个参数单表行）
        xSheet.createFreezePane(0, 2);
        return workbook;
    }
}
