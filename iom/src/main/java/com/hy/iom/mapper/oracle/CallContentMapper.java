package com.hy.iom.mapper.oracle;

import com.hy.iom.entities.CallContent;
import com.hy.iom.mapper.IomMapper;

import java.util.List;

public interface CallContentMapper extends IomMapper<CallContent> {
    List<CallContent> selectMatchResultByUuid(String uuid);
}
