package com.hy.constant;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/23
 * user: lxg
 * package_name: com.hy.constant
 */
@Component
public class CaseConstant {
    /**
     * .
     * 隐藏工具类
     */
    private CaseConstant() {

    }

    /**
     * .
     * 易混淆开头拼音对象
     */
    @Getter
    @Setter
    private static Map<String, String> caseMaps;
}
