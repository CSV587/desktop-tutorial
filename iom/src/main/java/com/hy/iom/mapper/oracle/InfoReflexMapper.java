package com.hy.iom.mapper.oracle;

import com.hy.iom.entities.InfoReflex;
import com.hy.iom.mapper.IomMapper;

import java.util.List;

public interface InfoReflexMapper extends IomMapper<InfoReflex> {

    List<InfoReflex> selectInfoByProjectId(InfoReflex infoReflex);

}
