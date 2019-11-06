package com.hy.iom.quality.connectionrate.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.iom.base.api.IPMApi;
import com.hy.iom.mapper.oracle.RecordInfoMapper;
import com.hy.iom.quality.connectionrate.page.ConnectionRatePage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * .
 * Created by of liaoxg
 * date: 2019-03-20
 * user: lxg
 * package_name: com.hy.iom.quality.connectionrate.service
 */
@Slf4j
@Service
public class ConnectionRateService {
    private final RecordInfoMapper recordInfoMapper;
    private final IPMApi ipmApi;

    public ConnectionRateService(RecordInfoMapper recordInfoMapper, IPMApi ipmApi) {
        this.recordInfoMapper = recordInfoMapper;
        this.ipmApi = ipmApi;
    }

    public Page query(ConnectionRatePage page) {
        PageHelper.startPage(page.getCurrent(), page.getPageSize());
        List<ConnectionRatePage> pages = recordInfoMapper.queryConnectionRate(page);
        tranferProjectId(pages);
        return (Page) pages;
    }

    private void tranferProjectId(List<ConnectionRatePage> pages) {
        for (ConnectionRatePage page : pages) {
            page.setProjectName(ipmApi.getProjectName(page.getProjectName()));
        }
    }

    public JSONObject add(ConnectionRatePage page) {
        int effectRecord = recordInfoMapper.addConnectionRate(page);
        JSONObject res = new JSONObject();
        if (effectRecord == 1) {
            res.put("message", "添加成功");
            res.put("code", "success");
        } else {
            res.put("message", "警告:维度名称重复");
            res.put("code", "fail");
        }
        return res;
    }

    public JSONObject edit(ConnectionRatePage page) {
        int effectRecord = recordInfoMapper.editQuality(page);
        JSONObject res = new JSONObject();
        if (effectRecord == 1) {
            res.put("message", "编辑成功");
            res.put("code", "success");
        } else {
            res.put("message", "编辑失败");
            res.put("code", "fail");
        }
        return res;
    }

    public JSONObject del(ConnectionRatePage page) {
        int effectRecord = recordInfoMapper.delQuality(page);
        JSONObject res = new JSONObject();
        if (effectRecord == 1) {
            log.warn("删除预警记录{}成功", page);
            res.put("message", "删除成功");
            res.put("code", "success");
        } else {
            log.warn("删除预警记录{}失败", page);
            res.put("message", "删除失败");
            res.put("code", "fail");
        }
        return res;
    }
}
