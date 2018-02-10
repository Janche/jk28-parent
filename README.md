# jk28-parent
物流管理系统
版本：1.0
  本系统由Struts2+Spring+Hibernate+Shiro+Oracle来进行搭建
  其中Shiro实现了按钮粒度的权限控制
运行环境：JDK1.8 + myeclipse/eclipse + maven
本项目采用maven多模块构建的方式，其模块名如下：
  jk28-parent
  jk28-dao
  jk28-service
  jk28-web
  jk28-domain
  jk28-exception
  jk28-utils
目前采用setter方法注入依赖，后期将改成注解的方式
配置文件目录如下：
  src/main/resources
    struts2
      struts-sysadmin.xml
     applicationContext-action.xml
     applicationContext-shiro.xml
     applicationContext.xml
     ehcache-shiro.xml
     hibernate.cfg.xml
     struts.xml
  
