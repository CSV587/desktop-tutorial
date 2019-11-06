package com.hy.iom.load.service;

import com.hy.iom.dao.CallContentDao;
import com.hy.iom.dao.CallContentDetailDao;
import com.hy.iom.dao.CustomerInfoDao;
import com.hy.iom.dao.InfoReflexDao;
import com.hy.iom.dao.RecordInfoDao;
import com.hy.iom.dao.ResInfoDao;
import com.hy.iom.dao.SceneThroughDetailDao;
import com.hy.iom.entities.CallContent;
import com.hy.iom.entities.CallContentDetail;
import com.hy.iom.entities.CustomerInfo;
import com.hy.iom.entities.InfoReflex;
import com.hy.iom.entities.RecordInfo;
import com.hy.iom.entities.ResInfo;
import com.hy.iom.entities.SceneThroughDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class CallDetailsService {

    private final ResInfoDao resInfoDao;
    private final CustomerInfoDao customerInfoDao;
    private final CallContentDao callContentDao;
    private final RecordInfoDao recordInfoDao;
    private final InfoReflexDao infoReflex;
    private final CallContentDetailDao callContentDetailDao;
    private final SceneThroughDetailDao throughDetailDao;

    @Autowired
    public CallDetailsService(ResInfoDao resInfoDao, CustomerInfoDao customerInfoDao, CallContentDao callContentDao, RecordInfoDao recordInfoDao, InfoReflexDao infoReflex, CallContentDetailDao callContentDetailDao, SceneThroughDetailDao throughDetailDao) {
        this.resInfoDao = resInfoDao;
        this.customerInfoDao = customerInfoDao;
        this.callContentDao = callContentDao;
        this.recordInfoDao = recordInfoDao;
        this.infoReflex = infoReflex;
        this.callContentDetailDao = callContentDetailDao;
        this.throughDetailDao = throughDetailDao;
    }


    @Transactional
    public boolean insertCallDetails(List<RecordInfo> ls_recordInfo, List<CallContent> ls_callContent, List<CustomerInfo> ls_customerInfo, List<ResInfo> ls_resInfo, List<InfoReflex> ls_infoReflexes, List<CallContentDetail> callContentDetailList, List<SceneThroughDetail> ls_throughDetail) {
        int[] r_recordInfo = recordInfoDao.insertRecordInfoList(ls_recordInfo);
        log.info("success import recordInfo【" + r_recordInfo.length + "】" + Arrays.toString(r_recordInfo));

        int[] r_callContent = callContentDao.insertCallContentList(ls_callContent);
        log.info("success import callContent【" + r_callContent.length + "】");

        int[] r_customerInfo = customerInfoDao.insertCustomerInfoList(ls_customerInfo);
        log.info("success import customerInfo 【" + r_customerInfo.length + "】");

        int[] r_resInfo = resInfoDao.insertResInfoList(ls_resInfo);
        log.info("success import resInfo 【" + r_resInfo.length + "】");

        log.info("start import infoReflex【" + ls_infoReflexes.size() + "】");
        int[] r_infoReflexes = infoReflex.insertInfoReflexList(ls_infoReflexes);
        log.info("success import infoReflex【" + r_infoReflexes.length + "】");

        int[] r_callContentDetail = callContentDetailDao.insertCallContentDetailList(callContentDetailList);
        log.info("success import callContentDetail【" + r_callContentDetail.length + "】");

        int[] r_throughDetail = throughDetailDao.insertThroughDetailList(ls_throughDetail);
        log.info("success import sceneThroughDetail【" + r_throughDetail.length + "】");

        return true;
    }
}
