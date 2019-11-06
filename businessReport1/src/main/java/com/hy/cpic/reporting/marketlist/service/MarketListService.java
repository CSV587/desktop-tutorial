package com.hy.cpic.reporting.marketlist.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.cpic.base.excel.ReportExcel;
import com.hy.cpic.base.excel.ReportUtils;
import com.hy.cpic.base.utils.ErrorUtil;
import com.hy.cpic.base.utils.PageUtils;
import com.hy.cpic.mapper.oracle.CallInfoStatisticsMapper;
import com.hy.cpic.reporting.marketlist.page.MarketListPage;
import com.hy.cpic.reporting.marketrate.page.MarketRatePage;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-18
 * user: lxg
 * package_name: com.hy.cpic.reporting.marketlist.service
 */
@Slf4j
@Service
public class MarketListService {
    private final CallInfoStatisticsMapper callInfoStatisticsMapper;

    @Autowired
    public MarketListService(CallInfoStatisticsMapper callInfoStatisticsMapper) {
        this.callInfoStatisticsMapper = callInfoStatisticsMapper;
    }

    public Page query(MarketListPage marketListPage) {
        PageHelper.startPage(marketListPage.getCurrent(), marketListPage.getPageSize()); //分页利器
        List<MarketListPage> marketListPages = callInfoStatisticsMapper.queryMarketList(marketListPage);
        PageUtils.randomId(marketListPages);
        return (Page) marketListPages;
    }

    public List<MarketListPage> queryAll(MarketListPage marketListPage) {
        return callInfoStatisticsMapper.queryMarketList(marketListPage);
    }

    public void exportExecl(MarketListPage marketListPage,
                            HttpServletResponse response,
                            HttpServletRequest request)
        throws UnsupportedEncodingException {

        long sum = callInfoStatisticsMapper.queryMarketListCount(marketListPage);
        //响应头的设置
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");

        //设置压缩包的名字
        //解决不同浏览器压缩包名字含有中文时乱码的问题
        String downloadName = System.currentTimeMillis() + ".zip";
        String agent = request.getHeader("USER-AGENT");


        if (agent.contains("MSIE") || agent.contains("Trident")) {
            downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
        } else {
            downloadName = new String(downloadName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");
        //设置压缩流：直接写入response，实现边压缩边下载
        try (ZipOutputStream zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
             DataOutputStream os = new DataOutputStream(zipos)) {
            //设置压缩方法
            zipos.setMethod(ZipOutputStream.DEFLATED);
            int pageSize = 10000;
            for (int i = 1; (i - 1) * 10000 < sum; i++) {
                writeZipFile(zipos, marketListPage, os, i, pageSize);
            }
        } catch (IOException e) {
            log.error("{}", ErrorUtil.getStackTrace(e));
        }
    }

    /**
     * .
     * 分流写文件
     *
     * @param zipos          压缩流
     * @param marketListPage 分页查询条件
     * @param current        当前页
     * @param pageSize       每页大小
     */
    private void writeZipFile(ZipOutputStream zipos,
                              MarketListPage marketListPage,
                              DataOutputStream os,
                              int current, int pageSize)
        throws IOException {
        PageHelper.startPage(current, pageSize);
        List<MarketListPage> marketListPages = callInfoStatisticsMapper.queryMarketList(marketListPage);
        Workbook workbook = new XSSFWorkbook();
        ReportExcel reportExcel = new ReportExcel();
        reportExcel.createNewSheet(workbook, marketListPages,
            "寿险营销挂断明细报表_" + ReportUtils.getTimeString() + "-" + current,
            MarketListPage.class, 1, "数据列表");
        File xlsxFile = File.createTempFile("temp", ".xlsx");
        try {
            try (FileOutputStream fos = new FileOutputStream(xlsxFile)) {
                workbook.write(fos);
            }
            if (zipos != null) {
                zipos.putNextEntry(new ZipEntry(xlsxFile.getName()));
                try (InputStream is = new FileInputStream(xlsxFile)) {
                    byte[] b = new byte[64 * 1024];
                    int length;
                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                } catch (IOException e) {
                    log.error("单个文件压缩出错,{}", xlsxFile.getAbsolutePath());
                }
            }
        } finally {
            if (xlsxFile != null && xlsxFile.exists()) {
                if (xlsxFile.delete()) {
                    log.info("文件删除成功{}", xlsxFile.getName());
                }
            }
        }
    }
}
