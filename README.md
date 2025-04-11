# AiQuestion

## 项目介绍

AiQuestion 是一个基于 **Vue 3** 和 **Spring Boot** 的 AI 答题应用平台，支持快速制作和发布多种在线答题应用。用户可以利用该平台检索和分享应用，在线答题，并通过评分算法或 AI 实现回答总结。管理员则可以审核应用，集中管理站点内容，并进行统计分析。
在线体验：[体验平台](https://yudada.code-nav.cn/)

## 项目特点

- **业务真实新颖**：提供基于 AI 的答题平台，实用价值高。
- **技术主流**：结合企业级前后端技术和最新 AI 技术。
- **学习收获**：覆盖丰富的实际业务场景、系统设计和优化技巧。

## 项目阶段

Lynx 项目设计为四个阶段，逐步引导用户学习：

1. **第一阶段**：开发本地的 MBTI 性格测试小程序，熟悉答题应用开发流程。
2. **第二阶段**：构建完整的答案应用平台，提供应用创建与管理功能。
3. **第三阶段**：实现 AI 智能评分系统，通过 AI 生成题目并分析答题结果。
4. **第四阶段**：通过使用 RxJava、SSE 等技术进行项目优化，提升性能和安全性。

## 技术选型

### 后端

- Java Spring Boot
- MySQL + Redis
- MyBatis-Plus
- Redisson分布式锁
- SSE服务端推送
- ChatGLM AI

### 前端

- Vue 3
- Axios
- Pinia状态管理
- Arco Design组件库

### 小程序开发

- Taro跨端开发框架
- React

### 开发工具

- JetBrains系列IDE
- CodeGeeX智能编程助手

## 参与与支持

如有问题或需要讨论，欢迎加入我们的项目交流群。我们提供详细的项目文档、直播笔记和持续的项目支持，帮助您在学习过程中充分掌握项目开发的每个环节。

希望您在 Lynx 项目的学习和实践中获得丰富的经验与技能提升！# AiQuestion - AI 应用答题平台

## 项目简介

AiQuestion 是一个基于 AI 的在线应用答题平台。用户可以创建、分享和参与各种类型的答题应用（如性格测试、知识问答），并利用 AI 技术（例如智谱 AI）自动生成题目或进行智能评分。

该项目包含独立的前后端代码。

## 技术栈

### 后端 (AiQuestion-backend)

*   **核心框架:** Spring Boot 2.7.2
*   **数据库:** MySQL
*   **ORM:** MyBatis-Plus 3.5.2
*   **缓存:** Redis, Spring Session Data Redis, Redisson 3.21.0, Caffeine 2.9.2
*   **对象存储:** 腾讯云 COS (cos_api 5.6.89)
*   **AI 能力:** 智谱 AI SDK (oapi-java-sdk release-V4-2.0.2)
*   **API 文档:** Knife4j OpenAPI2 4.4.0
*   **工具库:** Apache Commons Lang3, Hutool 5.8.8, EasyExcel 3.1.1
*   **构建工具:** Maven
*   **其他:** Spring Boot Starter AOP, Freemarker, DevTools, ShardingSphere JDBC

### 前端 (AiQuestion-frontend)

*   **框架:** React (基于 UmiJS)
*   **UI 库:** Ant Design Pro
*   **语言:** TypeScript
*   **API Client:** OpenAPI / umi-request
*   **构建工具:** npm / yarn

*(前端技术栈根据常见 Ant Design Pro 项目推断，请根据实际情况调整)*

## 主要功能

*   **用户模块:** 用户注册、登录、权限管理（用户、管理员）。
*   **应用模块:**
    *   创建应用（得分类 `SCORE`、测评类 `TEST`）。
    *   编辑、管理、发布应用。
    *   应用审核 (`ReviewStatusEnum`)。
*   **题目模块:**
    *   为应用添加题目 (`QuestionAddRequest`) 和选项 (`QuestionContentDTO`)。
    *   支持 AI 自动生成题目 (`AiGenerateQuestionRequest`)。
*   **答题模块:**
    *   用户在线回答应用题目 (`UserAnswerAddRequest`)。
    *   提交答案并查看结果 (`UserAnswerVO`)。
*   **评分模块:**
    *   支持自定义评分规则 (`AppScoringStrategyEnum.CUSTOM`)。
    *   支持 AI 智能评分 (`AppScoringStrategyEnum.AI`)。
    *   管理评分结果 (`ScoringResultAddRequest`, `ScoringResultVO`)。
*   **文件上传:** 支持用户头像、应用图标等文件上传 (`FileUploadBizEnum`, `CosManager`)。
*   **管理后台:** 提供管理员界面，用于管理用户、应用、审核等。
*   **其他:** 帖子、点赞、收藏功能 (`PostAddRequest`, `PostThumbAddRequest`, `PostFavourAddRequest`)。

## 项目结构

```
AiQuestion/
├── AiQuestion-backend/  # 后端 Spring Boot 项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/Lynx/AiQuestion/  # Java 源代码
│   │   │   │   ├── aop/         # AOP 切面 (如日志、权限校验)
│   │   │   │   ├── common/      # 通用类 (BaseResponse, ErrorCode, PageRequest, DeleteRequest, ReviewRequest)
│   │   │   │   ├── config/      # 配置类 (MyBatisPlus, Redis, AI, COS, CORS, Spring MVC)
│   │   │   │   ├── constant/    # 常量 (CommonConstant, UserConstant)
│   │   │   │   ├── controller/  # 控制器 (API 接口)
│   │   │   │   ├── exception/   # 全局异常处理 (BusinessException, GlobalExceptionHandler)
│   │   │   │   ├── manager/     # 封装第三方服务 (AiManager, CosManager)
│   │   │   │   ├── mapper/      # MyBatis Mapper 接口
│   │   │   │   ├── model/       # 数据模型
│   │   │   │   │   ├── dto/     # 数据传输对象 (请求参数)
│   │   │   │   │   ├── entity/  # 数据库实体
│   │   │   │   │   ├── enums/   # 枚举类
│   │   │   │   │   └── vo/      # 视图对象 (返回给前端)
│   │   │   │   ├── scoring/     # 评分策略实现
│   │   │   │   ├── service/     # 业务逻辑服务接口
│   │   │   │   ├── service/impl/ # 业务逻辑服务实现
│   │   │   │   └── utils/       # 工具类 (SqlUtils, NetUtils, SpringContextUtils)
│   │   │   └── resources/   # 资源文件 (application.yml, Mapper XML)
│   │   └── test/        # 测试代码
│   ├── pom.xml          # Maven 配置文件
│   └── README.md        # (建议添加后端专属 README)
├── AiQuestion-frontend/ # 前端 React 项目 (Ant Design Pro)
│   ├── config/          # Umi 配置
│   ├── public/          # 公共资源
│   ├── src/             # 前端源代码
│   │   ├── access.ts    # 权限配置
│   │   ├── app.tsx      # 全局入口
│   │   ├── components/  # 可复用组件
│   │   ├── constants/   # 常量
│   │   ├── enums/       # 枚举
│   │   ├── layouts/     # 布局组件
│   │   ├── models/      # DVA Models (状态管理)
│   │   ├── pages/       # 页面组件
│   │   ├── services/    # API 服务 (旧版, 可能已废弃)
│   │   ├── api/         # OpenAPI 生成的 API 服务 (typings.d.ts, 各 Controller 文件)
│   │   ├── styles/      # 样式文件
│   │   ├── typings.d.ts # 全局 TypeScript 类型定义
│   │   └── utils/       # 工具函数
│   ├── package.json     # npm 配置文件
│   └── README.md        # (建议添加前端专属 README)
└── README.md            # 本项目说明文件 (当前文件)
```

## 快速开始

### 环境准备

*   JDK 1.8+
*   Maven 3.6+
*   Node.js 16+
*   MySQL 5.7+
*   Redis

### 后端启动 (AiQuestion-backend)

1.  **数据库初始化:** 根据项目中的 SQL 文件（通常在 `sql` 目录或 `resources` 下，请检查项目）创建数据库和表结构。
2.  **配置:** 修改 `src/main/resources/application.yml` (或对应的 `dev`, `prod` 配置文件) 中的数据库连接信息、Redis 地址、腾讯云 COS 配置 (SecretId, SecretKey, Bucket)、智谱 AI Key (`KeyConstant.java` 或配置文件) 等。
3.  **安装依赖:** 在 `AiQuestion-backend` 目录下，打开命令行，执行：
    ```bash
    mvn clean install
    ```
4.  **运行项目:**
    *   方法一：直接运行 `src/main/java/com/Lynx/AiQuestion/MainApplication.java` 类。
    *   方法二：在 `AiQuestion-backend` 目录下执行 Maven 命令：
        ```bash
        mvn spring-boot:run
        ```

### 前端启动 (AiQuestion-frontend)

1.  **安装依赖:** 在 `AiQuestion-frontend` 目录下，打开命令行，执行：
    ```bash
    npm install
    ```
    或
    ```bash
    yarn install
    ```
2.  **配置代理:** (重要) 检查 `AiQuestion-frontend/config/proxy.ts` 文件，确保代理配置 (`target`) 指向您本地启动的后端服务地址（默认为 `http://localhost:8080`，请根据后端 `application.yml` 中的 `server.port` 调整）。
3.  **运行项目:** 执行：
    ```bash
    npm start
    ```
    或
    ```bash
    yarn start
    ```
4.  **访问:** 在浏览器中打开 `http://localhost:8000` (或 Umi 启动时提示的其他端口)。

## 使用说明

1.  访问前端页面 (`http://localhost:8000`) 进行用户注册和登录。
2.  管理员 (`admin` 角色) 可以登录后访问管理后台功能。
3.  普通用户可以创建自己的答题应用，设置应用类型、评分策略，并添加题目和选项。
4.  用户可以浏览平台上的其他应用，并进行答题。
5.  完成答题后，系统会根据应用的评分策略（自定义或 AI 评分）给出结果。

---

*这是一个基础的 README 模板，请根据您的项目实际情况进行详细补充和调整，例如：*
*   *添加更详细的部署说明。*
*   *提供项目演示截图或链接。*
*   *补充贡献指南或行为准则。*
*   *完善数据库初始化脚本的说明。*
