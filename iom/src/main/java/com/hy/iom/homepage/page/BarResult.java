package com.hy.iom.homepage.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-27
 * user: lxg
 * package_name: com.hy.iom.homepage.page
 */
@Setter
@Getter
public class BarResult {
    private Integer code;
    private String message;
    private String time;
    private BarData barData;
    private boolean success;

    public static BarResult success(String msg, BarData data) {
        BarResult pr = new BarResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        pr.barData = data;
        return pr;
    }

    public static BarResult success(BarData data) {
        return success("操作成功", data);
    }

    public static BarResult failure(String msg) {
        BarResult pr = new BarResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = false;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作失败";
        }
        pr.message = msg;
        return pr;
    }

    @Override
    public String toString() {
        return "BarResult{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            ", barData=" + barData +
            ", success=" + success +
            '}';
    }
}
