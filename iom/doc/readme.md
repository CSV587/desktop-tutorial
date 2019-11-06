# 一、项目介绍
智能外呼机器人SCR（Smart Callout Robot）运营支撑管理平台IOM（AI Operate Manage）报表系统

# 二、系统架构
## 1、技术框架：
 * Spring Boot
 * druid：数据源
 * MyBatis + Mapper + PageHelper
 * common-io：文件读取
 * jackson（Spring Boot）：json解析
 
 
## 2、开发环境
 * JDK版本：1.8.0_45
 * Spring Boot版本：2.0.3

# 三、目录结构
## 1、src代码目录结构
### 1）iom子目录结构：
 * config：自定义配置
 * entities：实体类
 * mapper:mybatis的mapper接口
   * oracle：oracle数据库的mapper接口
 * dao：
 * load：Json数据解析，并导入数据库
   * service：
     * schedule：定时任务
   * controller：
   * utils：数据解析、导入工具类
 * reporting：读取报表数据
   * controller：控制层，http Restful 接口
 * service：
 
## 2、resources配置文件目录结构
### 1）配置文件：
 * 基本配置：application.properties
 * 主配置：application.yml 
 * application-dev.properties
 * application-prod.properties
### 2）sql：保存初始化的SQL文件
### 3）mybatis：mybatis的xml映射文件

# 四、功能说明


# 五、JSON结构说明：
 * flows不需要解析
 * recordInfo：数据结构固定
 * customerInfo：业务属性，数据结构不固定
 * resInfo：呼叫结果，数据结构不固定
 * content：通话内容，数据结构固定
 
 
# 六、数据量预估：
 * 每天50-60W，报表数据取最近一年；历史数据分表保存
 
# 七、数据库设计
## 1、命名规范
 * 表名：大写；以T_开头；后跟系统模块名称。
    如：T_IOM_RECORDINFO
 * 视图名：
    如：V_IOM_CALLINFO
 * Sequence：
    如：SEQ_IOM_CALLINFO_ID
       
## 2、表结构设计：
 * T_IOM_RECORDINFO：
 * T_IOM_CALLCONTENT：列表
 * T_IOM_CUSTOMERINFO： 列表
 * T_IOM_RESINFO：
