package com.hy.iom.base.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;


/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 11:26 2018/8/23
 * @ Description ：导出报表
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class ReportExcel {

    /**
     * .
     * 文件导出
     *
     * @param wb        Workbook
     * @param sheetName title
     * @param response  response
     * @param request   request
     */
    public void export(Workbook wb, String sheetName, HttpServletResponse response, HttpServletRequest request) {
        try {
            OutputStream out = response.getOutputStream();
            String disposition = "attachment;filename=";
            if (request != null && request.getHeader("USER-AGENT") != null && StringUtils.contains(request.getHeader("USER-AGENT"), "Firefox")) {
                disposition += new String((sheetName + ".xlsx").getBytes(), "ISO8859-1");
            } else {
                disposition += URLEncoder.encode(sheetName + ".xlsx", "UTF-8");
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8");
            response.setHeader("Content-disposition", disposition);
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能: Excel导出公共方法
     * 记录条数大于50000时 导出.xlsx文件(excel07+)  小于等于50000时导出 .xls文件(excel97-03)
     *
     * @param list       需要导出的列表数据
     * @param sheetName  导出文件的标题
     * @param className  导出对象的类名
     * @param exportType 针对同一个pojo可能有多个不同的导出模板时,可以通过此属性来决定导出哪一套模板，默认第一套
     */
    public void createNewSheet(Workbook wb, List list, String sheetName, Class className, Integer exportType) {
        try {
            ExcelUtil excel = new ExcelUtil();
            if (exportType == null) {
                excel.exportExcel(wb, list, sheetName + "_" + ReportUtils.getTimeString(), className, 0, sheetName);
            } else {
                excel.exportExcel(wb, list, sheetName + "_" + ReportUtils.getTimeString(), className, exportType, sheetName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能: Excel非注解方式导出表
     */
    public void excelExport(String sheetName, String[] head, String[][] values, HttpServletResponse response, HttpServletRequest request) {
        ExcelUtil excelUtil = new ExcelUtil();
        Workbook wb = excelUtil.exportExcel(sheetName + "_" + ReportUtils.getTimeString(), sheetName, head, values);
        export(wb, sheetName, response, request);
    }
}
