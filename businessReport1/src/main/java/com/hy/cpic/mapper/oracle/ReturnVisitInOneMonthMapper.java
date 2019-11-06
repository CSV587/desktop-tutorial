package com.hy.cpic.mapper.oracle;

import com.hy.cpic.entities.ReturnVisitInOneMonth;
import com.hy.cpic.mapper.IomMapper;
import org.apache.ibatis.annotations.Param;

public interface ReturnVisitInOneMonthMapper extends IomMapper<ReturnVisitInOneMonth> {
    int withinOneMonth(@Param("callNumber") String callNumber);
}
