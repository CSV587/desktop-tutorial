package com.hy.iom.index.bussiness;

import com.hy.iom.entities.*;
import com.hy.iom.load.service.CallDetailsService;
import com.hy.iom.load.utils.JsonParser;
import com.hy.iom.service.CallStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 14:20 2018/8/20
 * @ Description ：文件建索处理类
 * @ Modified By ：
 * @ Version     ：1.0
 */
@Component
public class FileIndexWorker {

    private static Logger log = LoggerFactory.getLogger(FileIndexWorker.class);

    private static CallDetailsService callDetailsService;

    private static CallStatisticsService callStatisticsService;

    public FileIndexWorker() {
    }

    public void process(List<String> records) throws IOException {
        //解析Json
        Map<String, List<?>> map = JsonParser.parseCallDetails(records);
        modifyData(map);

    }

    @Transactional(rollbackFor = Exception.class)
    public void modifyData(Map<String, List<?>> map) {
        //数据入库
        List<RecordInfo> recordInfoList = (List<RecordInfo>) map.get("recordInfoList");
        List<CallContent> callContents = (List<CallContent>) map.get("callContentList");
        List<CustomerInfo> customerInfos = (List<CustomerInfo>) map.get("customerInfoList");
        List<ResInfo> resInfos = (List<ResInfo>) map.get("resInfoList");
        List<InfoReflex> infoReflexes = (List<InfoReflex>) map.get("infoReflex");
        List<CallContentDetail> callContentDetailList = (List<CallContentDetail>) map.get("callContentDetailList");
        List<SceneThroughDetail> throughDetail = (List<SceneThroughDetail>) map.get("throughDetail");
        boolean success = callDetailsService.insertCallDetails(recordInfoList,
            callContents,
            customerInfos,
            resInfos,
            infoReflexes,
            callContentDetailList,
            throughDetail);
        if (success) {
            //衍生统计数据：
            callStatisticsService.updateByRecordInfos(recordInfoList);
            log.debug("解析JSON入库成功");
        }
    }

    @Autowired
    public void setCallDetailsService(CallDetailsService callDetailsService) {
        this.callDetailsService = callDetailsService;
    }

    @Autowired
    public void setCallStatisticsService(CallStatisticsService callStatisticsService) {
        this.callStatisticsService = callStatisticsService;
    }
}
