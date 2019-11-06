package com.hy.iom.homepage.page;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-27
 * user: lxg
 * package_name: com.hy.iom.homepage.page
 */
@Setter
@Getter
public class GaugeResult {
    private Integer code;
    private String message;
    private String time;
    private List<String> warningList;
    private String connectRate;
    private boolean success;

    public static GaugeResult success(String msg, List<String> data, String rate) {
        GaugeResult pr = new GaugeResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        pr.warningList = data;
        pr.connectRate = rate;
        return pr;
    }

    public static GaugeResult success(List<String> data, String rate) {
        return success("操作成功", data, rate);
    }

    public static GaugeResult failure(String msg) {
        GaugeResult pr = new GaugeResult();
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
        return "GaugeResult{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            ", warningList=" + warningList +
            ", connectRate=" + connectRate +
            ", success=" + success +
            '}';
    }
}
