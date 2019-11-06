package com.hy.iom.queue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author      ：wellhor Zhao
 * @ Date        ：Created in 15:26 2018/8/31
 * @ Description ：录音基本信息以及衍生信息
 * @ Modified By ：
 * @ Version     ：1.0
 */
public class CallData {

   private Map<String,List> map = new HashMap<>();

   public CallData(){
   }

   public CallData push(String key , List value){
       map.put(key,value);
       return this;
   }

   public JSONObject toJSONObject(){
       return JSONObject.parseObject(JSON.toJSONString(map));
   }

}
