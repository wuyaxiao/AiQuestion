# AiQuestion

## 简介

AiQuestion 是一个基于 AI 的在线应用答题平台。用户可以创建、分享和参与各种类型的答题应用，平台支持 AI
自动生成题目和评分，提供个性化的答题体验。

网站：待添加

## 主要功能

 • 应用创建与管理: 用户可以创建不同类型（如测评类、得分类）的答题应用。
 • 题目管理: 支持手动添加题目或利用 AI 快速生成题目。
 • 答题与评分: 用户可以参与答题，系统支持自定义评分规则和 AI 智能评分。
 • 用户系统: 包含用户注册、登录、权限管理等功能。
 • 结果统计与分析: 提供答题结果的统计和可视化展示。
 • 文件存储: 使用腾讯云 COS 存储用户头像、应用图标等文件。
 • 分库分表: 使用 ShardingSphere 对用户答案表进行分表。

## 技术栈

### 后端 (AiQuestion-backend)

 • 核心框架: Spring Boot 2.x
 • 数据库: MySQL 5.7+
 • ORM: MyBatis Plus
 • 数据库中间件: ShardingSphere-JDBC (用于 user_answer 表分表)
 • 缓存: Redis
 • 对象存储: 腾讯云 COS
 • AI 能力: 智谱 AI SDK (com.zhipu.oapi.ClientV4)
 • API 文档: Knife4j
 • 构建工具: Maven

### 前端 (AiQuestion-frontend)

 • 框架: Vue 3
 • UI 库: Arco Design Vue
 • 状态管理: Pinia
 • 路由: Vue Router
 • HTTP 请求: Axios
 • 图表: Echarts (vue-echarts)
 • Markdown 编辑器: Bytemd
 • 构建工具: Vue CLI

## 快速开始

### 环境准备

 • JDK 1.8 或更高版本
 • Maven 3.6 或更高版本
 • Node.js (建议 LTS 版本，包含 npm)
 • MySQL 5.7 或更高版本
 • Redis
 • 腾讯云 COS (需要配置 application.yml 中的 cos.client.*)
 • 智谱 AI (需要配置 application.yml 中的 ai.apiKey)

### 后端启动 (AiQuestion-backend)

 1 克隆仓库
 2 进入后端目录: cd AiQuestion-backend
 3 重要: 修改 src/main/resources/application.yml 文件，配置以下信息：
    • 数据库连接 (spring.datasource 和 spring.shardingsphere.datasource.lynx)
    • Redis 连接 (spring.redis)
    • 腾讯云 COS 配置 (cos.client.)
    • 智谱 AI API Key (ai.apiKey) - 注意：请勿将敏感密钥直接提交到 Git 仓库！ 建议使用环境变量或外部配置文件。
 4 构建项目: mvn clean package -DskipTests (跳过测试以加快构建速度)
 5 运行项目: java -jar target/AiQuestion-backend-.jar (替换为实际生成的 jar 文件名)
    • 后端服务将运行在 http://localhost:8101/api (根据 application.yml 配置)

### 前端启动 (AiQuestion-frontend)

 1 进入前端目录: cd AiQuestion-frontend
 2 安装依赖: npm install
 3 (可选) 配置 API 代理: 检查 vue.config.js (如果存在) 或其他配置文件，确保前端请求能正确代理到后端服务
   (http://localhost:8101/api)。
 4 启动开发服务器: npm run serve
 5 浏览器访问: 通常是 http://localhost:8080 (具体端口见启动日志)

## 使用说明

 1 访问前端页面。
 2 注册或登录账号。
 3 浏览、创建或参与答题应用。
 4 管理员可以进行用户管理、应用审核等操作。

## 贡献指南

欢迎参与项目贡献！请遵循以下步骤：

 1 Fork 本仓库
 2 创建您的 Feature 分支 (git checkout -b feature/AmazingFeature)
 3 提交您的更改 (git commit -m 'Add some AmazingFeature')
 4 将更改推送到分支 (git push origin feature/AmazingFeature)
 5 创建 Pull Request