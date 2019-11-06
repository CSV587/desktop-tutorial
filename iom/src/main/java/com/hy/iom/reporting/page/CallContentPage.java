package com.hy.iom.reporting.page;

import com.hy.iom.reporting.excel.CallContents;

import java.util.List;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 16:25 2018/9/5
 * @ Description ：对白下载请求页
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class CallContentPage {

    private List<CallContents> contentList;

    public List<CallContents> getContentList() {
        return contentList;
    }

    public void setContentList(List<CallContents> contentList) {
        this.contentList = contentList;
    }
}
