package com.hy.util;

import lombok.Getter;

/**
 * .
 * Created by of liaoxg
 * date: 2019-07-17
 * user: lxg
 * package_name: com.hy.util
 */
@Getter
public enum BusinessType {
    /**
     * .
     * 文件通知
     */
    FileNotices("IntelligentVoiceReturn"),
    /**
     * .
     * 回访历史
     */
    VisitHistory("saveAIVisitHistory"),
    /**
     * .
     * 名单下发
     */
    Notice("notice"),
    /**
     * .
     * 回访问卷
     */
    Papers("SaveAIQuestionnaireResult");


    /**
     * .
     * 类型名称
     */
    private String name;

    /**
     * .
     * 构造函数
     *
     * @param value 枚举类型值
     */
    BusinessType(final String value) {
        this.name = value;
    }
}
