package com.hy.mapper.oracle;

import com.hy.reporting.questionnaire.page.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/6.
 * @Description :
 */
public interface PaperMapper {

    void addPaper(PaperPage p);

    int delPaperById(String id);

    List<Map<String, Object>> selectPaperInfoByCondition(PaperPage p);

    List<Map<String, Object>> selectQuestionBySort(@Param("sort") String sort);

    void saveChildQuestion(ChildQuestion2 childQuestion);

    void addQuestion(QuestionPage2 questionPage2);

    void delQuestion(String id);

    List<ChildQuestion2> selectChildQuestionByParentId(String id);

    void addChildQuestion(ChildQuestion2 childQuestion);

    void delChildQuestion(String id);

    List<Map<String,String>> selectAllQuestionByPaperId(String id);

    Map<String, Object> selectMaxIdentifier(@Param("sort") String sort);

    ChildQuestion2 editChildQuestion(String id);

    int hasChildQuestion(String id);

    void updateQuestionIdentifier(@Param("idf")int idf, @Param("id")String id);

    void updateChildQuestionIdentifier(@Param("idf")int idf, @Param("id")String id);

    int sameNameJugde(PaperPage p);

    List<ChildQuestion4> selectChildQuestionByFlowId(String flowId);

    String selectQuestionContentById(@Param("id")String id);

}
