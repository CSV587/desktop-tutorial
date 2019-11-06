#iom
***
#####application.properties
```
servlet容器配置
server.servlet.context-path=/iom  #上下文的根
spring.profiles.active=dev #根据这个配置决定读取 application-dev还是application-prod
```

#####application-dev || application-prod.properties
```
server.port=8080 端口配置 起作用的是这个
```
***

####scheduler.properties
#####json导入时间时间表达式 和 路径
```
import.oracle.cron = 0 * * * * SUN-SAT
import.oracle.path = /home/fs/callinfo/finsh
```

####application.yml
```
    username: nchuaan #数据库路径名
    password: nchuaan #数据库密码
    url: jdbc:oracle:thin:@172.16.18.29:1521:orcl #数据库url
```