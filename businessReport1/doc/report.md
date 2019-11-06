# 一、呼叫列表查询
## API接口：
 * 列表地址：reporting/recordInfo?current=2&pageSize=5
 * 对话录音详情：
   * 客户基本信息：reporting/customerInfo/{uuid}
   * 对话详情：reporting/callContent/{uuid}
## 问题：
###数据来源：
 * 项目名称：阿刚待定
 
 * 通话时长：时间段

###页面展示：
 * 外呼列表-客户详情：去掉
 * 外呼列表-外呼详情：展示内容域外呼列表多有重复，合并到外呼列表中
 * 外呼列表-录音调听：展示结构调整，与客户详情合并

### 对应关系：
 * 外呼开始时间：T_IOM_RECORDINFO.CHANNELSTARTTIME
 * 外呼结束时间：T_IOM_RECORDINFO.CHANNELENDTIME
 * 被叫号码：T_IOM_RECORDINFO.CALLNUMBER
 * 呼叫结果：阿刚待定
 * 接通状态：阿刚待定
 * 外呼次数：阿刚待定

 
 
 * 规则名称：阿刚待定
 * 流程名称：阿刚待定
 * 核身分数：去掉
 * 呼叫结果：阿刚待定
 
 
 * 机器人话术：content.proType='tts'的content字段
 * 客户话术：content.proType='speechSuccess'的content字段
 * 匹配节点：下一个content.proType='tts'的nodeName

 
#### 衍生数据：
 * 通话时长：T_IOM_RECORDINFO.RECORDSTARTTIME - T_IOM_RECORDINFO.RECORDENDTIME
 * 流转次数：content.proType='tts'的个数   

# 二、接通率统计
## 1、接通与未接通占比图
### 问题：
 * 节点通过如何判断？？？ONSTATE=‘connect’ >>>>>>
 * 所有状态有哪些？>>>>>>
 
### 衍生表：
 * 每日外呼数量与状态统计表： id  yyyy  yyyy-MM yyyy-MM-dd hh 项目名 流程名称 cnt onstate 外呼次数
 
## 2、外呼次数接通率

### API接口：

### 问题：
 * 接通与未接通如何判断？在呼叫结果中判断。

## 3、时间段接通率：

# 四、外呼差错统计：
 * 差错判读：
   * 呼叫差错：？？？？ 状态？？？ onstate>>>>>>>>>>
   //* 客户要求转人工：content.proType='transfer'的session
   //* 未接通：呼叫结果


# 五、呼叫量统计：
 * 外呼总量、已呼叫量、未呼叫量、未接通量，
 * 其中未呼叫量：去掉
 
# 六、首呼接通率统计：
 * 与外呼次数接通率功能重复
 
# 七、外呼成功率统计：

# 八、呼叫结果统计：

## 衍生数据：
 * 最后一个业务节点：
 

# 实时监控
外呼量：正在外呼，暂未接通
通话中：正在通话


