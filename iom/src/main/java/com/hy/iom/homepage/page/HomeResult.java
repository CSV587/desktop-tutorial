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
public class HomeResult {
    private Integer code;
    private String message;
    private String time;
    private HomeBody value;
    private boolean success;

    public static HomeResult success(String msg, HomeBody data) {
        HomeResult pr = new HomeResult();
        pr.code = 0;
        pr.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        pr.success = true;
        if (StringUtils.isEmpty(msg)) {
            msg = "操作成功";
        }
        pr.message = msg;
        pr.value = data;
        return pr;
    }

    public static HomeResult success(HomeBody data) {
        return success("操作成功", data);
    }

    public static HomeResult failure(String msg) {
        HomeResult pr = new HomeResult();
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
        return "HomeResult{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", time='" + time + '\'' +
            ", value=" + value +
            ", success=" + success +
            '}';
    }
}
