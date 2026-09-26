# 企业管理系统
基于Spring+MySQL设计的企业管理系统后端，主要面对纸类加工企业，在linux上部署。若需要在Windows上运行，请注释打印调用代码，仅作测试使用。前端仓库链接

## 技术栈
- JDK: 17
- 框架: SpringBoot 4.0.x
- ORM框架: MyBatis-Plus 3.5.x
- 数据库: MySQL 8.0.x
- 构建工具: Maven 4.0.x
- 核心依赖: 
    - Lombok: 简化实体类代码
    - Odfdom-java: 操作联票模板
    - Apache POI: Excel导出

## API接口列表
- [x] Excel数据导出接口 (GET /orders/export)
- [x] 订单信息条件分页查询接口 (POST /orders/list)
- [x] 订单打印接口 (GET /prints)
- [x] 客户信息列表查询接口 (GET /customers)
详细请看`接口文档.md`

## 环境要求
运行本项目，需要本地安装
1. JDK 17
2. Maven 4.0+
3. MySQL 8.0

## 快速启动
### 1.拉取代码
```bash
git clone https://github.com/skyi-1018/EMS.git
cd EMS
```

### 2.数据库准备
1. 创建数据库: `ems_db`
2. 执行sql脚本: 脚本路径`src/main/resources/sql/ems.sql`

### 3.修改配置文件
打开配置文件`src/main/resources/application.yml`
修改数据库连接信息：地址、用户名、密码，打印模板位置信息与打印文件输出路径，jwt密钥，管理员密码

### 4.启动项目
```bash
mvn clean package
mvn spring-boot:run
```
启动成功后，默认服务端口: `8080`
