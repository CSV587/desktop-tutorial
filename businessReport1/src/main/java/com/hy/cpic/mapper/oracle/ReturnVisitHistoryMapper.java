package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.ReturnVisitMetaInfoHistory;
import com.hy.cpic.mapper.IomMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnVisitHistoryMapper extends IomMapper<ReturnVisitMetaInfoHistory> {
    void save(ReturnVisitMetaInfoHistory returnVisitMetaInfoHistory);
}
