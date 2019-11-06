package com.hy.iom.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 15:10 2018/8/31
 * @ Description ：消息队列
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class DataQueue{

    private static Queue<CallData> queue = new ConcurrentLinkedQueue<>();

    private DataQueue(){
    }

    public static Queue<CallData> getInstance(){
        return queue;
    }
    
    public static List<CallData> getData(int count){
       List<CallData> callDatas = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            if (!queue.isEmpty()){
                CallData callData = queue.poll();
                if(callData!=null){
                    callDatas.add(callData);
                }
            }
        }
       return callDatas;
    }

}
