package com.hy.iom.dao;

import com.hy.iom.entities.InfoReflex;
import com.hy.iom.mapper.oracle.InfoReflexMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.UUID;

@Repository
public class InfoReflexDao {

    private final InfoReflexMapper infoReflexMapper;

    private final JdbcTemplate template;

    public InfoReflexDao(InfoReflexMapper infoReflexMapper, JdbcTemplate template) {
        this.infoReflexMapper = infoReflexMapper;
        this.template = template;
    }

    public int[] insertInfoReflexList(List<InfoReflex> ls_infoReflexes) {
        int[] res = new int[ls_infoReflexes.size()];
        if (ls_infoReflexes.size() > 0) {
            for (int i = 0; i < ls_infoReflexes.size(); ++i) {
                InfoReflex infoReflex = ls_infoReflexes.get(i);
                Example example = new Example(InfoReflex.class);
                example.and().andEqualTo("projectId", infoReflex.getProjectId());
                example.and().andEqualTo("name", infoReflex.getName());
                List<InfoReflex> infos = infoReflexMapper.selectByExample(example);
                if (infos != null && infos.size() > 0) {
                    res[i] = 0;
                } else {
                    String insertSql = "INSERT INTO T_IOM_INFOREFLEX_TMP (ID,PROJECTID,NAME,STATE) VALUES(?,?,?,? )";
                    res[i] = template.update(insertSql, ps -> {
                        ps.setString(1, UUID.randomUUID().toString());
                        ps.setString(2, infoReflex.getProjectId());
                        ps.setString(3, infoReflex.getName());
                        ps.setString(4, infoReflex.getState());
                    });
                }
            }
        }
        return res;
    }
}
