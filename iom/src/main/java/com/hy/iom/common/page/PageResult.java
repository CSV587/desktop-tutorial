package com.hy.iom.common.page;

import com.github.pagehelper.Page;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PageResult<T> {
    private Integer code;
    private String message;
    private String time;
    private PageBody body;
    private Long count;
    private boolean success;

    public static PageResult success(String msg, Page page, List<TableHead> tableHead) {
        PageResult pr = new PageResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        pr.count = page.getTotal();
        pr.body = new PageBody();
        pr.body.setValue(page.getResult());
        pr.body.setTheader(tableHead);
        return pr;
    }

    public static PageResult success(String msg, List tableBody, List<TableHead> tableHead) {
        PageResult pr = new PageResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        pr.body = new PageBody();
        pr.body.setValue(tableBody);
        pr.body.setTheader(tableHead);
        return pr;
    }

    public static PageResult success(String msg, List tableBody, List<TableHead> tableHead, long total) {
        PageResult pr = new PageResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        pr.count = total;
        pr.body = new PageBody();
        pr.body.setValue(tableBody);
        pr.body.setTheader(tableHead);
        return pr;
    }

    public static PageResult success(Page page, List<TableHead> tableHead) {
        return success("操作成功", page, tableHead);
    }

    public static PageResult fail(String msg, int code) {
        PageResult pr = new PageResult();
        pr.code = code;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = false;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作失败";
        }
        pr.message = msg;
        return pr;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public PageBody getBody() {
        return body;
    }

    public void setBody(PageBody body) {
        this.body = body;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "PageResult{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            ", body=" + body +
            ", count=" + count +
            ", success=" + success +
            '}';
    }
}
