package com.hy.reporting.reconciliation.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hy.mapper.oracle.CallBackEntitiesMapper;
import com.hy.reporting.reconciliation.page.ReconciliationPage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-26
 * user: lxg
 * package_name: com.hy.reporting.reconciliation.service
 */
@Service
public class ReconciliationService {
    /**
     * .
     * callBackEntitiesMapper
     */
    private final CallBackEntitiesMapper callBackEntitiesMapper;

    /**
     * .
     * 构造函数
     *
     * @param mapper CallBackEntitiesMapper
     */
    public ReconciliationService(
        final CallBackEntitiesMapper mapper) {
        this.callBackEntitiesMapper = mapper;
    }

    /**
     * .
     * 分页查询
     *
     * @param page ReconciliationPage
     * @return Page对象
     */
    public Page query(final ReconciliationPage page) {
        PageHelper.startPage(page.getCurrent(),
            page.getPageSize());
        List<ReconciliationPage> pages
            = callBackEntitiesMapper
            .queryReconciliation(page);
        checkStatus(pages);
        return (Page) pages;
    }

    /**
     * .
     * 全部查询
     *
     * @param page ReconciliationPage
     * @return 集合
     */
    public List<ReconciliationPage> queryAll(
        final ReconciliationPage page) {
        List<ReconciliationPage> pages = callBackEntitiesMapper
            .queryReconciliation(page);
        checkStatus(pages);
        return pages;
    }

    /**
     * .
     * 判断状态是否有效
     *
     * @param pages 所有页面
     */
    private void checkStatus(
        final List<ReconciliationPage> pages) {
        for (ReconciliationPage page : pages) {
            page.setStatus("0");
            if (page.getSum().equals(page.getFinished())) {
                page.setStatus("1");
            }
            page.setId(UUID.randomUUID().toString());
        }
    }
}
