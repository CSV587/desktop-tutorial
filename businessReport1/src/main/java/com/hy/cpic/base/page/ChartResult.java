package com.hy.cpic.base.page;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChartResult<T> {
    private Integer code;
    private String message;
    private String time;
    private Object data;
    private boolean success;

    public static ChartResult success(String msg, Object data) {
        ChartResult pr = new ChartResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        pr.data = data;
        return pr;
    }

    public static ChartResult success(Object data) {
        return success("操作成功", data);
    }

    public static ChartResult failure(String msg) {
        ChartResult pr = new ChartResult();
        pr.code = 0;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
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
                ", data=" + data +
                ", success=" + success +
                '}';
    }
}
