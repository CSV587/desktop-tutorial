package com.hy.runner;

import com.hy.constant.CaseConstant;
import com.hy.context.SpringContextAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .
 * Created by of liaoxg
 * date: 2019/10/23
 * user: lxg
 * package_name: com.hy.runner
 */
@Slf4j
@Component
@Order(value = 1)
public class InitCaseStatusRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        URL initUrl = InitCaseStatusRunner
            .class
            .getResource("/caseStatusFile");
        File file = new File(initUrl.getFile());
        List<String> keyWords = FileUtils.readLines(file, StandardCharsets.UTF_8);
        Map<String, String> caseMaps = new HashMap<>();
        for (String line : keyWords) {
            String[] items = line.split(";");
            if (items.length == 3) {
                caseMaps.put(items[1], String.format("%s;%s", items[0], items[2]));
            }
        }
        CaseConstant.setCaseMaps(caseMaps);
        log.debug("初始化呼叫结果与结案状态对应关系成功");
    }
}
