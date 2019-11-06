package com.hy.base.excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hy.base.utils.FieldUtil;
import com.hy.error.ErrorUtil;
import com.hy.util.DateFormat;
import com.hy.util.DateUtil;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public final class ExcelUtil {

    /**
     * .
     * 工具类隐藏构造函数
     */
    private ExcelUtil() {

    }

    /**
     * .
     * 单元格高度
     */
    private static final short HEIGHT_LENGTH = 380;

    /**
     * .
     * 表头标题字体粗度
     */
    private static final short ONE_FONT_HEIGHT_LENGTH = 300;


    /**
     * .
     * 字段名字体粗度
     */
    private static final short TWO_FONT_HEIGHT_LENGTH = 300;


    /**
     * .
     * 数据值字体粗度
     */
    private static final short TREE_FONT_HEIGHT_LENGTH = 300;


    /**
     * .
     * 黑体宽度
     */
    private static final short BOLD_WEIGHT_LENGTH = 500;


    /**
     * .
     * 默认单元格宽度
     */
    static final int DEF_COLUMN_WIDTH = 5000;

    /**
     * .
     * 生成07格式的excel对象 使用流方式防止内存溢出
     *
     * @param list       队列
     * @param title      字段名
     * @param className  类名
     * @param exportType 指定类型
     * @param sheetName  标签页名称
     * @return Workbook
     * @throws Exception 异常
     */
    public static Workbook exportExcel(
        final List list,
        final String title,
        final Class className,
        final Integer exportType,
        final String sheetName)
        throws Exception {
        List<Field> fieldList = FieldUtil.getAllFieldByClass(className);
        int columnsize = fieldList.size(), rowindex = 0;
        // 创建一个HSSFWorbook对象

        Workbook hWorkbook = new XSSFWorkbook();
        // 创建一个HSSFSheet对象（sheet页）
        Sheet hSheet;
        if (sheetName == null) {
            hSheet = hWorkbook.createSheet();
        } else {
            hSheet = hWorkbook.createSheet(sheetName);
        }

        Row hRow;
        CellStyle cellStyle;
        if (title != null) {
            // 创建第一行(此行作为头)
            hRow = hSheet.createRow(rowindex++);
            hRow.setHeight(HEIGHT_LENGTH);
            // 创建单元格（第一（0）个）
            Cell hCell = hRow.createCell(0);
            // 设置样式
            cellStyle = getCellStyle(hWorkbook, ONE_FONT_HEIGHT_LENGTH);
            //设置背景颜色
            cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
            //solid 填充  foreground  前景色
            cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            // 将上面获得的样式对象给对应单元格
            hCell.setCellStyle(cellStyle);
            //设置标题行
            hCell.setCellValue(title);
            int lastCol = 0;
            if ((columnsize - 1) > 0) {
                lastCol = columnsize - 1;
            }
            hSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));
        }

        if (fieldList.isEmpty()) {
            return hWorkbook;
        }

        // 创建第二列，列名
        hRow = hSheet.createRow(rowindex++);
        cellStyle = getCellStyle(hWorkbook, TWO_FONT_HEIGHT_LENGTH);
        //设置背景颜色
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        createTitle(exportType, fieldList, columnsize, hSheet, hRow, cellStyle);

        if (list == null || list.isEmpty()) {
            return hWorkbook;
        }

        //生成数据
        cellStyle = getCellStyle(hWorkbook, TREE_FONT_HEIGHT_LENGTH); // 设置单元格格式
        dealCreateRow(list, fieldList, columnsize, rowindex, hSheet, cellStyle);

//        // 固定标题（前一个参数代表列，后一个参数单表行）
//        hSheet.createFreezePane(0, 1);
//        // 固定列名（前一个参数代表列，后一个参数单表行）
//        hSheet.createFreezePane(0, 2);
        return hWorkbook;
    }

    /**
     * .
     * 创建一条记录
     *
     * @param list       对象List
     * @param fieldList  字段属性List
     * @param columnSize 字段值
     * @param rowindex   行下标
     * @param hSheet     对应sheet
     * @param cellStyle  单元格属性
     * @throws NoSuchMethodException     NoSuchMethodException
     * @throws IllegalAccessException    IllegalAccessException
     * @throws InvocationTargetException InvocationTargetException
     */
    private static void dealCreateRow(final List list,
                                      final List<Field> fieldList,
                                      final int columnSize,
                                      final int rowindex,
                                      final Sheet hSheet,
                                      final CellStyle cellStyle)
        throws NoSuchMethodException,
        IllegalAccessException,
        InvocationTargetException {
        Row hRow;
        Cell hCell;
        int index = rowindex;
        for (Object model : list) {
            hRow = hSheet.createRow(index++);
            Class clazz = model.getClass();
            for (int i = 0; i < columnSize; i++) {
                Field field = fieldList.get(i);
                String methodName = "get"
                    + field.getName().substring(0, 1).toUpperCase()
                    + field.getName().substring(1);
                Method method = clazz.getMethod(methodName);
                try {
                    Object result = method.invoke(model);
                    hCell = hRow.createCell((short) i);
                    if (result != null) {
                        if (result instanceof Enum) {
                            try {
                                Enum eResult = (Enum) result;
                                Field vField = eResult
                                    .getClass()
                                    .getField(eResult.name());
                                if (vField != null) {
                                    ExcelEnumAnnotation excelEnumAnnotation
                                        = vField.getAnnotation(
                                        ExcelEnumAnnotation.class);
                                    if (excelEnumAnnotation != null) {
                                        result = excelEnumAnnotation.name();
                                    }
                                }
                            } catch (Exception ignore) {
                            }
                        } else if (result instanceof Date) {
                            JsonFormat jsonFormat
                                = field.getAnnotation(JsonFormat.class);
                            if (jsonFormat != null) {
                                String pattern = jsonFormat.pattern();
                                result = new SimpleDateFormat(pattern)
                                    .format(result);
                            } else {
                                result = result.toString();
                            }
                        }
                        DateFormat dateFormat
                            = field.getAnnotation(com.hy.util.DateFormat.class);
                        if (dateFormat != null) {
                            String source = dateFormat.source();
                            String target = dateFormat.target();
                            try {
                                result = DateUtil.conversion(result.toString(),
                                    source, target);
                                String item = result.toString();
                                item = item.replace("年0", "年");
                                result = item.replace("月0", "月");
                            } catch (ParseException ignore) {
                            }
                        }
                        hCell.setCellValue(
                            new XSSFRichTextString(result.toString())
                        );
                    } else {
                        hCell.setCellValue(new XSSFRichTextString("-"));
                    }
                    hCell.setCellStyle(cellStyle);
                } catch (IllegalArgumentException e) {
                    log.error("生成excel出错\n{}", ErrorUtil.getStackTrace(e));
                }
            }
        }
    }

    /**
     * .
     * 生成列名
     *
     * @param exportType 模板编号
     * @param fieldList  列名
     * @param columnSize 列数
     * @param hSheet     行
     * @param hRow       列
     * @param cellStyle  单元格格式
     */
    private static void createTitle(final Integer exportType,
                                    final List<Field> fieldList,
                                    final int columnSize,
                                    final Sheet hSheet,
                                    final Row hRow,
                                    final CellStyle cellStyle) {
        Cell hCell;
        for (int i = 0; i < columnSize; i++) {
            Field field = fieldList.get(i);
            if (field.isAnnotationPresent(ExcelAnnotation.class)) {
                // 获取该字段的注解对象
                ExcelAnnotation annotation
                    = field.getAnnotation(ExcelAnnotation.class);
                hCell = hRow.createCell((short) i);
                String colName;
                if (field
                    .getAnnotation(ExcelAnnotation.class)
                    .name()
                    .length > exportType) {
                    colName = field
                        .getAnnotation(ExcelAnnotation.class)
                        .name()[exportType];
                } else {
                    colName = field
                        .getAnnotation(ExcelAnnotation.class)
                        .name()[0];
                }
                hCell.setCellValue(colName);
                hCell.setCellStyle(cellStyle);
                hSheet.setColumnWidth((short) i, (short) annotation.width());
            }
        }
    }

    /**
     * .
     * 功能 :设置excel表格默认样式
     *
     * @param hWorkbook  需导出Excel数据
     * @param fontHeight 字体粗度
     * @return 单行数据
     */
    private static CellStyle getCellStyle(final Workbook hWorkbook,
                                          final short fontHeight) {
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
        font.setBoldweight(BOLD_WEIGHT_LENGTH);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        return cellStyle;
    }


    /**
     * .
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @return 对象
     */
    public static Workbook exportExcel(final String sheetName,
                                       final String[] title,
                                       final String[][] values) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        Workbook workbook = new XSSFWorkbook();
        int rowindex = 0;
        // 创建一个HSSFSheet对象（excll的表单）
        Sheet xSheet;
        Row hRow;
        CellStyle cellStyle;
        Cell hCell;
        if (sheetName != null) {
            xSheet = workbook.createSheet(sheetName);
            // 创建行（excel的行）
            hRow = xSheet.createRow(rowindex++);
            //设置行高度
            hRow.setHeight(HEIGHT_LENGTH);
            // 创建单元格（从0开始）
            hCell = hRow.createCell(0);
            //样式对象
            cellStyle = getCellStyle(workbook, ONE_FONT_HEIGHT_LENGTH);
            //设置背景颜色
            cellStyle.setFillForegroundColor(HSSFColor.LIME.index);
            //solid 填充  foreground  前景色
            cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            // 将上面获得的样式对象给对应单元格
            hCell.setCellStyle(cellStyle);
            //设置标题行
            hCell.setCellValue(sheetName);
            int lastCol = 0;
            if ((title.length - 1) > 0) {
                lastCol = title.length - 1;
            }
            xSheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol));
        } else {
            xSheet = workbook.createSheet();
        }

        if (title.length == 0) {
            return workbook;
        }

        //创建第二行，代表列名
        hRow = xSheet.createRow(rowindex);
        cellStyle = getCellStyle(workbook, TWO_FONT_HEIGHT_LENGTH);
        //设置背景颜色
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        //solid 填充  foreground  前景色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        for (int i = 0; i < title.length; i++) {
            String name = title[i];
            hCell = hRow.createCell((short) i);
            hCell.setCellValue(name);
            hCell.setCellStyle(cellStyle);
            xSheet.setColumnWidth(i, DEF_COLUMN_WIDTH);
        }

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

//        // 固定标题（前一个参数代表列，后一个参数单表行）
//        xSheet.createFreezePane(0, 1);
//        // 固定列名（前一个参数代表列，后一个参数单表行）
//        xSheet.createFreezePane(0, 2);
        return workbook;
    }
}
