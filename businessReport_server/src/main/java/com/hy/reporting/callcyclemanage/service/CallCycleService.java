package com.hy.reporting.callcyclemanage.service;

import com.alibaba.fastjson.JSONObject;
import com.hy.base.api.BasicConfig;
import com.hy.error.ErrorUtil;
import com.hy.mapper.oracle.CallBackEntitiesMapper;
import com.hy.mapper.oracle.CallCycleMapper;
import com.hy.reporting.callback.entities.CallBackEntities;
import com.hy.reporting.callcyclemanage.dao.CallCycleDao;
import com.hy.reporting.callcyclemanage.entities.CallCycleEntity;
import com.hy.reporting.callcyclemanage.page.CallCyclePage;
import com.hy.util.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.parseObject;

/**
 * .
 * CallCycleService
 */
@Slf4j
@Service
public class CallCycleService {

    private final CallCycleMapper callCycleMapper;

    private final CallCycleDao callCycleDao;

    private final CallBackEntitiesMapper callBackEntitiesMapper;

    public CallCycleService(CallCycleMapper callCycleMapper,
                            CallCycleDao callCycleDao,
                            CallBackEntitiesMapper callBackEntitiesMapper) {
        this.callCycleMapper = callCycleMapper;
        this.callCycleDao = callCycleDao;
        this.callBackEntitiesMapper = callBackEntitiesMapper;
    }


    public List<CallCyclePage> query(CallCyclePage callCyclePage) {
        return callCycleMapper.queryList(callCyclePage);
    }

    /**
     * .
     * 上传信息并入库
     */
    public void store(MultipartFile file) throws IOException {
        if (file != null) {
            List<String> lines = FileUtils.readLines(
                (File) file,
                StandardCharsets.UTF_8
            );
            List<CallBackEntities> callBackEntitiesList = new ArrayList<>();
            for (String line : lines) {
                CallBackEntities entities = parseObject(
                    String.valueOf(line),
                    CallBackEntities.class
                );
                callBackEntitiesList.add(entities);
            }
            insertCallCycle(callBackEntitiesList);
        }
    }


    /**
     * .
     * 解析当前插入实体类
     *
     * @param callBackEntitiesList callBackEntity集合
     */
    public void insertCallCycle(final List<CallBackEntities> callBackEntitiesList) {
        List<CallCycleEntity> callCycleEntityList = new ArrayList<>();
        Map<String, Integer> m = new LinkedHashMap<>();
        for (CallBackEntities entities : callBackEntitiesList) {
            String tmpKey = entities.getCallTask_id() + "," + entities.getCallTask_fisc_id();
            if (m.containsKey(tmpKey)) {
                int num = m.get(tmpKey);
                num = num + 1;
                m.replace(tmpKey, num);
            } else {
                m.put(tmpKey, 1);
            }
        }
        Timestamp endData = getEndDate();
        for (Map.Entry<String, Integer> entry : m.entrySet()) {
            CallCycleEntity cce = new CallCycleEntity();
            String[] ss = entry.getKey().split(",");
            cce.setPushTaskId(ss[0]);
            cce.setReturnTaskId(ss[1]);
            cce.setTaskTotal(entry.getValue());
            cce.setStartDate(new Timestamp(System.currentTimeMillis()));
            cce.setNewStartDate(new Timestamp(System.currentTimeMillis()));
            cce.setEndDate(endData);
            cce.setNewEndDate(endData);
            cce.setCallState(0);
            cce.setTaskState(0);
            callCycleEntityList.add(cce);
        }
        this.save(callCycleEntityList);
    }

    /**
     * .
     * 获取结束时间
     *
     * @return 结束
     */
    private Timestamp getEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        Date d = new Date();
        calendar.setTime(d);
        int count = 0;
        do {
            calendar.add(Calendar.DATE, 1);
            Date cur = calendar.getTime();
            String getDateTypeUrl = BasicConfig.getIcmUrl()
                + "connector/getDateType?date=" + sdf.format(cur);
            try {
                String res = HttpRequestUtils.httpGet(getDateTypeUrl, null);
                JSONObject jsonObject = parseObject(res);
                if (jsonObject.getString("value").equals("workday")) {
                    count++;
                }
            } catch (Exception e) {
                log.error("{}", ErrorUtil.getStackTrace(e));
                calendar.setTime(d);
                calendar.add(Calendar.DATE, 2);
                return new Timestamp(calendar.getTimeInMillis());
            }
        } while (count != 2);
        return new Timestamp(calendar.getTimeInMillis());
    }

    /**
     * .
     * 保存数据
     *
     * @param entitiesList CallCycleEntity集合
     */
    public void save(final List<CallCycleEntity> entitiesList) {
        if (!entitiesList.isEmpty()) {
            callCycleDao.insertCallCycleList(entitiesList);
        }
        log.debug("插入成功{}条", entitiesList.size());
    }


    /**
     * .
     * 任务管理设为无效
     *
     * @param id id值
     */
    public void invalidCallCycle(final String id) {
        callCycleMapper.invalidCallCycle(id);
    }

    public void terminate(String pushTaskId) {
        callBackEntitiesMapper.terminate(pushTaskId);
        callCycleMapper.terminate(pushTaskId);
    }
}
