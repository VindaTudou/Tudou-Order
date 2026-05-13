# 土豆点餐 (Tudou-Order)

基于 Spring Boot + Vue 2 的外卖管理系统，参考苍穹外卖项目进行开发。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.7.3 |
| ORM | MyBatis 2.2.0 + PageHelper |
| 数据库 | MySQL 8.0 + Druid 连接池 |
| 认证 | JWT (jjwt 0.9.1, HS256) |
| 接口文档 | Knife4j 3.0.2 (Swagger) |
| 前端 | Vue 2 + Element UI (编译后静态文件) |
| 反向代理 | nginx 1.20 |
| 构建工具 | Maven (多模块) |

## 项目结构

```
Tudou-Order/
├── dou-common/     公共模块：常量、枚举、异常、JSON 序列化、统一返回结果、ThreadLocal
├── dou-pojo/       数据模型：Entity、DTO、VO
├── dou-server/     Spring Boot 服务端：Controller、Service、Mapper、AOP、配置
├── dou-admin/      管理端前端（编译产物，nginx 直接托管）
├── images/         本地上传图片存储目录
└── pom.xml         父 POM
```

## 功能模块

### 管理端（已完成）

- **员工管理** — 登录/登出、新增、编辑、修改密码、启用/禁用、分页查询
- **分类管理** — 新增、修改、删除、分页查询、启用/禁用
- **菜品管理** — 新增（含口味）、修改、批量删除、起售/停售、分页查询、根据分类查询
- **套餐管理** — 新增（含菜品关系）、修改、批量删除、起售/停售、分页查询、根据id查询
- **文件上传** — 图片上传/删除，存储至本地 images 目录
- **接口文档** — Knife4j 自动生成

### 管理端（待完成）

- 订单管理、报表统计、工作台概览
- 用户端所有接口（用户、地址、购物车、下单等）

## 快速启动

### 环境要求

- JDK 8+
- MySQL 8.0
- Maven 3.6+
- nginx（用于托管前端）

### 1. 数据库

创建数据库并导入初始数据：

```sql
CREATE DATABASE IF NOT EXISTS tudou_take_out DEFAULT CHARACTER SET utf8mb4;
```

### 2. 后端配置

编辑 `dou-server/src/main/resources/application-dev.yml`，修改数据库连接信息：

```yaml
tudou:
  datasource:
    host: localhost
    port: 3306
    database: tudou_take_out
    username: root
    password: 你的密码
  upload:
    path: /你的项目路径/Tudou-Order/images/   # 图片上传目录，需改为你的实际路径
```

同时修改 `dou-server/src/main/java/io/fangtudou/config/WebMvcConfiguration.java` 中的图片资源路径为你的实际路径。

### 3. 启动后端

在项目根目录执行：

```bash
mvn clean install -DskipTests
cd dou-server
mvn spring-boot:run
```

或使用 IntelliJ IDEA 直接运行 `DouServerApplication`。

后端启动后访问：
- API 服务：`http://localhost:8080`
- 接口文档：`http://localhost:8080/doc.html`

### 4. 启动前端

前端由 nginx 托管，配置文件位于 `土豆点餐前端运行环境/nginx-1.20.2/conf/nginx-mac.conf`。

```bash
cd 土豆点餐前端运行环境/nginx-1.20.2
./start-mac.sh
```

访问管理端：`http://localhost:8081`

## nginx 配置要点

| 路径 | 用途 |
|------|------|
| `/` | 管理端 SPA 前端 |
| `/api/` | 代理至后端 `/admin/`（管理端接口） |
| `/images/` | 代理至后端静态资源（图片文件） |
| `/user/` | 代理至后端（用户端接口） |
| `/ws/` | WebSocket 代理 |

nginx 启动脚本使用 `-p` 指定 prefix 目录，配置文件中的绝对路径需按本机实际路径修改。
