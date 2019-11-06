package com.hy.reporting.callcyclemanage.entities;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/8.
 * @Description :
 */
@Getter
@Setter
public class CallCycleEntity {

    private String id = UUID.randomUUID().toString();

    private String pushTaskId;

    private String returnTaskId;

    private long taskTotal;

    private int taskState;

    private Timestamp startDate;

    private Timestamp endDate;

    private Timestamp newStartDate;

    private Timestamp newEndDate;

    private String editor;

    private Timestamp editDate;

    private int callState;

}
