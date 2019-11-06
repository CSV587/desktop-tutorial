package com.hy.iom.service;

import com.hy.iom.entities.InfoReflex;
import com.hy.iom.mapper.oracle.InfoReflexMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class InfoReflexService {

    private final InfoReflexMapper infoReflexMapper;

    @Autowired
    public InfoReflexService(InfoReflexMapper infoReflexMapper) {
        this.infoReflexMapper = infoReflexMapper;
    }


    public List<InfoReflex> selectInfoByProjectId(String projectId) {
        InfoReflex infoReflex = new InfoReflex();
        infoReflex.setProjectId(projectId);
        List<InfoReflex> list = infoReflexMapper.selectInfoByProjectId(infoReflex);
        return list;
    }

    public boolean modifyState(String projectId, String name, String state) {
        Example example = new Example(InfoReflex.class);
        example.and().andEqualTo("projectId", projectId);
        example.and().andEqualTo("name", name);
        List<InfoReflex> infoReflexs = infoReflexMapper.selectByExample(example);
        if (infoReflexs != null && infoReflexs.size() > 0) {
            InfoReflex infoReflex = infoReflexs.get(0);
            infoReflex.setState(state);
            infoReflexMapper.updateByExample(infoReflex, example);
            return true;
        } else {
            return false;
        }
    }
}
