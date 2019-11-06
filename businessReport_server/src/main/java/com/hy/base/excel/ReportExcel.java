package com.hy.base.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


/**
 * .
 *
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 11:26 2018/8/23
 * @ Description ：导出报表
 * @ Modified By ：
 * @ Version     ：1.0
 */
public final class ReportExcel {


    /**
     * .
     * 工具类隐藏构造函数
     */
    private ReportExcel() {

    }

    /**
     * .
     * 功能: Excel导出公共方法
     *
     * @param list       需要导出的列表数据
     * @param title      导出文件的标题
     * @param className  导出对象的类名
     * @param exportType 针对同一个pojo可能有多个不同的导出模板时,可以通过此属性来决定导出哪一套模板，默认第一套
     * @param response   用来获取输出流
     * @param request    针对火狐浏览器导出时文件名乱码的问题,也可以不传入此值
     * @throws Exception Exception
     */
    public static void excelExport(final List list,
                                   final String title,
                                   final Class className,
                                   final Integer exportType,
                                   final HttpServletResponse response,
                                   final HttpServletRequest request)
        throws Exception {
        Workbook wb;
        if (exportType == null) {
            wb = ExcelUtil.exportExcel(
                list,
                title,
                className,
                0,
                title);
        } else {
            wb = ExcelUtil.exportExcel(
                list,
                title,
                className,
                exportType,
                title);
        }
        export(wb, title, response, request);
    }

    /**
     * .
     * 文件导出
     *
     * @param wb       Workbook
     * @param title    title
     * @param response response
     * @param request  request
     */
    public static void export(final Workbook wb,
                              final String title,
                              final HttpServletResponse response,
                              final HttpServletRequest request) {
        try {
            OutputStream out = response.getOutputStream();
            String disposition = "attachment;filename=";
            if (request != null
                && request.getHeader("USER-AGENT") != null
                && StringUtils.contains(request.getHeader("USER-AGENT"),
                "Firefox")
            ) {
                disposition += new String(
                    (title + ".xlsx")
                        .getBytes(),
                    "ISO8859-1");
            } else {
                disposition += URLEncoder.encode(title + ".xlsx", "UTF-8");
            }
            response.setContentType("application/"
                + "vnd.openxmlformats-officedocument.spreadsheetml.sheet;"
                + "charset=UTF-8");
            response.setHeader("Content-disposition", disposition);
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * .
     * 功能: Excel导出公共方法
     * 记录条数大于50000时 导出.xlsx文件(excel07+)  小于等于50000时导出 .xls文件(excel97-03)
     *
     * @param list       需要导出的列表数据
     * @param title      导出文件的标题
     * @param className  导出对象的类名
     * @param exportType 针对同一个pojo可能有多个不同的导出模板时,可以通过此属性来决定导出哪一套模板，默认第一套
     * @param sheetName  标签页名称
     * @return Workbook Workbook
     */
    public static Workbook createNewSheet(final List list,
                                          final String title,
                                          final Class className,
                                          final Integer exportType,
                                          final String sheetName) {
        Workbook wb = null;
        try {
            if (exportType == null) {
                wb = ExcelUtil.exportExcel(list,
                    title,
                    className,
                    0,
                    sheetName);
            } else {
                wb = ExcelUtil.exportExcel(list,
                    title,
                    className,
                    exportType,
                    sheetName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wb;
    }

    /**
     * .
     * 功能: Excel非注解方式导出表
     *
     * @param sheetName sheet页名称
     * @param title     表格名称
     * @param values    数据值
     * @param response  response
     * @param request   request
     */
    public static void excelExport(final String sheetName,
                                   final String[] title,
                                   final String[][] values,
                                   final HttpServletResponse response,
                                   final HttpServletRequest request) {
        Workbook wb = ExcelUtil.exportExcel(sheetName, title, values);
        export(wb, sheetName, response, request);
    }
}
