package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.ReturnVisitData;
import com.hy.cpic.mapper.IomMapper;
import com.hy.cpic.reporting.extractData.returnVisit.page.ReturnVisitDataPage;
import com.hy.cpic.reporting.recordinfo.page.RecordInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnVisitDataMapper extends IomMapper<RecordInfo> {

    int isCalledInOneMonth(@Param("callNumber") String callNumber);

    void save(ReturnVisitData returnVisitData);

    List<ReturnVisitDataPage> query(ReturnVisitDataPage returnVisitDataPage);

    List<ReturnVisitData> queryUnrealNumber(ReturnVisitDataPage returnVisitDataPage);

}
