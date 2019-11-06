package com.hy.iom.common.page;

import com.github.pagehelper.Page;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-18
 * user: lxg
 * package_name: com.hy.cpic.base.page
 */
public class PageResultTM<T> {
    private Integer code;
    private String message;
    private String time;
    @Getter
    @Setter
    private List<TableHead> columns;
    @Getter
    @Setter
    private List<T> value;
    private Long count;
    private boolean success;

    public static PageResultTM success(String msg) {
        return getBaseMessage(msg);
    }

    private static PageResultTM getBaseMessage(String msg) {
        PageResultTM pr = new PageResultTM();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        return pr;
    }

    public static PageResultTM success(String msg, Page page, List<TableHead> tableHead) {
        PageResultTM pr = getBaseMessage(msg);
        pr.count = page.getTotal();
        pr.value = page.getResult();
        pr.columns = tableHead;
        return pr;
    }

    public static PageResultTM success(String msg, List body) {
        PageResultTM pr = getBaseMessage(msg);
        pr.value = body;
        return pr;
    }

    public static PageResultTM success(String msg, Page page) {
        PageResultTM pr = getBaseMessage(msg);
        pr.count = page.getTotal();
        pr.value = page.getResult();
        return pr;
    }

    public static PageResultTM success(String msg, List tableBody, List<TableHead> tableHead) {
        PageResultTM pr = getBaseMessage(msg);
        pr.value = tableBody;
        pr.columns = tableHead;
        return pr;
    }

    public static PageResultTM fail(String msg, int code) {
        PageResultTM pr = new PageResultTM();
        pr.code = code;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = false;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作失败";
        }
        pr.message = msg;
        return pr;
    }

    public static PageResultTM success(Page page, List<TableHead> tableHead) {
        return success("操作成功", page, tableHead);
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
        return "PageResultTM{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            ", columns=" + columns +
            ", value=" + value +
            ", count=" + count +
            ", success=" + success +
            '}';
    }
}