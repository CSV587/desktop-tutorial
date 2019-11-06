package com.hy.base.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * .
 * 返回结果对象
 */
public class ResponseResult {
    /**
     * .
     * 返回编码
     */
    @Getter
    @Setter
    private Integer code;
    /**
     * .
     * 返回消息文本
     */
    @Getter
    @Setter
    private String message;
    /**
     * .
     * 时间
     */
    @Getter
    @Setter
    private String time;
    /**
     * .
     * 成功标记
     */
    @Getter
    @Setter
    private boolean success;

    /**
     * .
     * 数据
     */
    @Getter
    @Setter
    private Object data;

    /**
     * .
     * 成功返回
     *
     * @param msg 文本
     * @return 对象
     */
    public static ResponseResult success(final String msg) {
        ResponseResult pr = new ResponseResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            pr.message = "操作成功";
        } else {
            pr.message = msg;
        }
        pr.data = new PageBody();
        return pr;
    }

    /**
     * .
     * 成功返回对象
     *
     * @param msg  文本
     * @param body 数据
     * @return 对象
     */
    public static ResponseResult success(final String msg,
                                         final Object body) {
        ResponseResult pr = success(msg);
        pr.data = body;
        return pr;
    }

    /**
     * .
     *
     * @param msg 文本
     * @return 对象
     */
    public static ResponseResult fail(final String msg) {
        ResponseResult pr = new ResponseResult();
        pr.code = -1;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new Date());
        pr.success = false;
        if (StringUtils.isEmpty(msg)) {
            pr.message = "操作失败";
        } else {
            pr.message = msg;
        }
        pr.data = new PageBody();
        return pr;
    }

    /**
     * .
     * 转文本
     *
     * @return 文本
     */
    @Override
    public String toString() {
        return "ResponseResult{"
            + "code=" + code
            + ", message='" + message + '\''
            + ", time='" + time + '\''
            + ", data=" + data
            + ", success=" + success
            + '}';
    }
}
