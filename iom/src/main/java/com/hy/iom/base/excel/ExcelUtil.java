package com.hy.iom.base.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
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
public class ExcelUtil {

    public CellStyle getDatacellStyle(Workbook hWorkbook) {
        CellStyle cellStyle = getCellStyle(hWorkbook, (short) 220);//设置单元格格式
        DataFormat format = hWorkbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        return cellStyle;
    }

    public List<Field> getFields(Class className){
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
        return fieldList;
    }

    /**
     * .
     * 生成07格式的excel对象 使用流方式防止内存溢出
     *
     * @param hWorkbook  execl对象
     * @param list       队列
     * @param title      字段名
     * @param className  类名
     * @param exportType 指定类型
     * @throws Exception 异常
     */
    void exportExcel(Workbook hWorkbook, List list, String title, Class className, Integer exportType, String sheetName) throws Exception {
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
        cellStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        //solid 填充  foreground  前景色
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 将上面获得的样式对象给对应单元格
        hCell.setCellStyle(cellStyle);
        //设置标题行
        hCell.setCellValue(title);

        if (fieldList.isEmpty()) {
            return;
        }

        // 创建第二列，列名
        hRow = hSheet.createRow(rowindex++);
        cellStyle = getCellStyle(hWorkbook, (short) 240);
        //设置背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        createTitle(exportType, fieldList, columnsize, hSheet, hRow, cellStyle);
        hSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnsize - 1));

        if (list == null || list.isEmpty()) {
            return;
        }
        //生成数据
        cellStyle = getCellStyle(hWorkbook, (short) 220);// 设置单元格格式
        DataFormat format = hWorkbook.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("@"));
        dealCreateRow(list, fieldList, columnsize, rowindex, hSheet, cellStyle);

        // 固定标题（前一个参数代表列，后一个参数单表行）
        hSheet.createFreezePane(0, 1);
        // 固定列名（前一个参数代表列，后一个参数单表行）
        hSheet.createFreezePane(0, 2);
    }

    public void dealCreateRow(List list, List<Field> fieldList, int columnsize, int rowindex, Sheet hSheet,
                               CellStyle cellStyle) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        for (Object model : list) {
            rowindex = getRowindex(fieldList, columnsize, rowindex, hSheet, cellStyle, model);
        }
    }

    public int getRowindex(List<Field> fieldList, int columnsize, int rowindex, Sheet hSheet, CellStyle cellStyle, Object model) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Row hRow;
        Cell hCell;
        hRow = hSheet.createRow(rowindex++);
        Class clazz = model.getClass();
        for (int i = 0; i < columnsize; i++) {
            Field field = fieldList.get(i);
            String methodName = "get" + field.getName().substring(0, 1).toUpperCase()
                + field.getName().substring(1);
            Method method = clazz.getMethod(methodName);
            try {
                Object result = method.invoke(model);
                hCell = hRow.createCell((short) i);
                if (result != null) {
                    if (result instanceof Enum) {
                        try {
                            Enum eResult = (Enum) result;
                            Field vField = eResult.getClass().getField(eResult.name());
                            if (vField != null) {
                                ExcelEnumAnnotation excelEnumAnnotation = vField.getAnnotation(ExcelEnumAnnotation.class);
                                if (excelEnumAnnotation != null) {
                                    result = excelEnumAnnotation.name();
                                }
                            }
                        } catch (Exception ignore) {
                        }
                    } else if (result instanceof Date) {
                        JsonFormat jsonFormat = field.getAnnotation(JsonFormat.class);
                        if (jsonFormat != null) {
                            String pattern = jsonFormat.pattern();
                            result = new SimpleDateFormat(pattern).format(result);
                        } else {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            result = format.format(result);
                        }
                    }
                    hCell.setCellValue(new XSSFRichTextString(result.toString()));
                } else {
                    hCell.setCellValue(new XSSFRichTextString("-"));
                }
                hCell.setCellStyle(cellStyle);
            } catch (IllegalArgumentException e) {
                log.error("生成excel出错", e);
            }
        }
        return rowindex;
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
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        font = hWorkbook.createFont();
        font.setFontHeight(fontHeight);
        font.setBoldweight((short) 500);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return cellStyle;
    }


    /**
     * 导出Excel
     *
     * @param titleName 报表名称
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @return 对象
     */
    Workbook exportExcel(String titleName, String sheetName, String[] title, String[][] values) {

        Workbook workbook = new XSSFWorkbook();

        int rowindex = 0;
        // 创建一个HSSFSheet对象（excll的表单）
        Sheet xSheet = workbook.createSheet(sheetName);
        // 创建行（excel的行）
        Row hRow = xSheet.createRow(rowindex++);
        //设置行高度
        hRow.setHeight((short) 380);
        // 创建单元格（从0开始）
        Cell hCell = hRow.createCell((short) 0);
        //样式对象
        CellStyle cellStyle = getCellStyle(workbook, (short) 300);
        //设置背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.LIME.getIndex());
        //solid 填充  foreground  前景色
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        // 将上面获得的样式对象给对应单元格
        hCell.setCellStyle(cellStyle);
        //设置标题行
        hCell.setCellValue(titleName);

        if (title.length == 0) {
            return workbook;
        }

        //创建第二行，代表列名
        hRow = xSheet.createRow(rowindex);
        cellStyle = getCellStyle(workbook, (short) 300);
        //设置背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        //solid 填充  foreground  前景色
        cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
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

        CellStyle textStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));

        //创建内容
        for (int i = 0; i < values.length; i++) {
            hRow = xSheet.createRow(i + 2);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                Cell rowCell = hRow.createCell(j);
                rowCell.setCellStyle(textStyle);
                rowCell.setCellValue(values[i][j]);
            }
        }

        // 固定标题（前一个参数代表列，后一个参数单表行）
        xSheet.createFreezePane(0, 1);
        // 固定列名（前一个参数代表列，后一个参数单表行）
        xSheet.createFreezePane(0, 2);
        return workbook;
    }
}
