# Spring AI 智能餐厅推荐系统

[![CI/CD Pipeline](https://github.com/chensoul/spring-ai-restaurant-showcase/actions/workflows/ci.yml/badge.svg)](https://github.com/chensoul/spring-ai-restaurant-showcase/actions/workflows/ci.yml)
[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=spring-ai-restaurant-showcase&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=spring-ai-restaurant-showcase)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=spring-ai-restaurant-showcase&metric=coverage)](https://sonarcloud.io/summary/new_code?id=spring-ai-restaurant-showcase)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=spring-ai-restaurant-showcase&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=spring-ai-restaurant-showcase)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=spring-ai-restaurant-showcase&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=spring-ai-restaurant-showcase)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=spring-ai-restaurant-showcase&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=spring-ai-restaurant-showcase)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=spring-ai-restaurant-showcase&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=spring-ai-restaurant-showcase)

这是一个基于 Spring AI 框架构建的智能餐厅推荐系统，展示了如何使用不同的 AI 模型提供商来构建智能化的餐饮推荐应用。

## 项目特色

- 🍽️ **智能餐厅推荐** - 根据用户偏好推荐合适的餐厅
- 🍜 **菜品生成** - 自动生成菜品描述和营养信息
- 💬 **多语言支持** - 支持中文和英文交互
- 🤖 **多模型支持** - 支持 OpenAI、Mistral AI 和 Ollama
- 💾 **对话记忆** - 支持上下文对话和记忆功能
- 📊 **结构化输出** - 返回标准化的 JSON 数据
- ⚡ **代码简化** - 使用 Lombok 减少样板代码

## 技术栈

- **Spring Boot 3.5.6** - 主框架
- **Spring AI 1.1.0-M2** - AI 集成框架
- **Java 21** - 编程语言
- **Maven** - 构建工具
- **Lombok** - 代码简化工具
- **JaCoCo** - 代码覆盖率分析
- **SonarQube** - 代码质量分析
- **GitHub Actions** - CI/CD 自动化

## 支持的 AI 模型

### 1. OpenAI
- 模型：GPT-5
- 特点：最新模型，性能强大，支持更复杂的推理
- 配置：需要 OpenAI API Key

### 2. Mistral AI
- 模型：Mistral Large
- 特点：性能优秀，支持多语言
- 配置：需要 Mistral AI API Key

### 3. Ollama
- 模型：Llama 3.2
- 特点：本地运行，无需 API Key
- 配置：需要本地安装 Ollama

## 支持的 OpenAI Compatible LLMs

### 1. Groq AI
- **模型**: Llama 3.1 70B Versatile
- **特点**: 极快的推理速度，高性能
- **API**: https://api.groq.com/openai/v1
- **获取 API Key**: https://console.groq.com/keys

### 2. Docker Model Runner (DMR)
- **模型**: Llama 3.2
- **特点**: 本地运行，Docker 容器化部署
- **API**: http://localhost:11434/v1
- **安装**: https://docs.docker.com/ai/model-runner/

### 3. OpenRouter AI
- **模型**: Llama 3.1 8B Instruct (Free)
- **特点**: 多种开源模型选择，成本较低
- **API**: https://openrouter.ai/api/v1
- **获取 API Key**: https://openrouter.ai/keys

### 4. DeepSeek AI
- **模型**: DeepSeek Chat
- **特点**: 强大的中文理解能力
- **API**: https://api.deepseek.com/v1
- **获取 API Key**: https://platform.deepseek.com/api_keys

### 5. Qwen AI
- **模型**: Qwen Plus
- **特点**: 阿里巴巴开发的中文大模型
- **API**: https://dashscope.aliyuncs.com/compatible-mode/v1
- **获取 API Key**: https://dashscope.console.aliyun.com/apiKey

## 项目结构

```
spring-ai-restaurant-showcase/
├── src/main/java/cc/chensoul/springai/restaurant/
│   ├── model/                          # 数据模型
│   │   ├── Restaurant.java             # 餐厅模型
│   │   ├── Dish.java                   # 菜品模型
│   │   └── RecommendationRequest.java  # 推荐请求模型
│   ├── controller/                     # 控制器
│   │   └── RestaurantRecommendationController.java
│   ├── config/                         # 配置类
│   │   ├── RestaurantProperties.java   # 餐厅配置属性
│   │   └── ChatConfig.java             # Chat 配置
│   └── RestaurantApplication.java      # 主应用类
├── src/main/resources/
│   ├── application.yml                 # 通用配置
│   ├── application-groq.yml           # Groq AI 配置
│   ├── application-dmr.yml            # DMR AI 配置
│   ├── application-openrouter.yml     # OpenRouter AI 配置
│   ├── application-deepseek.yml       # DeepSeek AI 配置
│   └── application-qwen.yml           # Qwen AI 配置
├── examples/                          # 示例和测试
│   └── api-test-examples.md
├── README.md                          # 项目说明
└── pom.xml                           # Maven 配置
```

## 核心功能

### 1. 餐厅推荐 API
- **POST** `/api/restaurants/recommend` - 根据用户偏好推荐餐厅
- 支持地理位置、菜系、价格范围、饮食限制等多维度筛选
- 返回结构化的餐厅信息

### 2. 菜品生成 API
- **POST** `/api/restaurants/dishes/generate` - 生成菜品信息（支持中文参数）
- 自动生成菜品描述、营养信息、制作建议
- 支持不同菜系的菜品生成，无需 URL 编码

### 3. 智能交互 API
- **POST** `/api/restaurants/advice` - 获取个性化建议（支持中文查询）
- **POST** `/api/restaurants/chat` - 多语言聊天接口（支持中文消息）
- **GET** `/api/restaurants/{id}/details` - 获取餐厅详细信息

## 技术实现

### 1. 数据模型设计
- **Restaurant**：餐厅信息模型，包含名称、菜系、位置、评分等
- **Dish**：菜品信息模型，包含名称、描述、食材、营养信息等
- **RecommendationRequest**：推荐请求模型，包含用户偏好参数
- **Lombok 集成**：使用 `@Data`、`@NoArgsConstructor`、`@AllArgsConstructor` 注解自动生成 getter、setter、构造函数等方法

### 2. Spring AI 集成
- 使用 `ChatClient` 进行 AI 模型交互
- 通过 `PromptTemplate` 管理复杂的 prompt
- 支持结构化输出，直接转换为 Java 对象
- 使用 `@RequiredArgsConstructor` 简化依赖注入
- 集成 `MessageChatMemoryAdvisor` 实现对话记忆功能
- 集成 `SimpleLoggerAdvisor` 记录 AI 请求和响应日志

### 3. 中文参数支持
- 所有 API 使用 POST 方法，支持 JSON 请求体
- 直接支持中文字符，无需 URL 编码
- 简化了 API 调用，提升用户体验

### 3. 多模型支持
- Maven Profiles 管理不同模型的依赖
- 独立的配置文件管理不同模型的参数
- 启动脚本简化不同模型的运行

## 快速开始

### 1. 环境准备

```bash
# 克隆项目
git clone https://github.com/chensoul/spring-ai-restaurant-showcase
cd spring-ai-restaurant-showcase

# 设置环境变量（选择其中一个）
export OPENAI_API_KEY=your-openai-api-key
export MISTRAL_AI_TOKEN=your-mistral-api-key
export GROQ_API_KEY=your-groq-api-key
export OPENROUTER_API_KEY=your-openrouter-api-key
export DEEPSEEK_API_KEY=your-deepseek-api-key
export QWEN_API_KEY=your-qwen-api-key

# 或安装 Ollama（本地运行）
# 访问 https://ollama.ai 下载安装
ollama run llama3.2
```

### 2. 运行应用

```bash
# 使用 OpenAI GPT-4o（推荐）
mvn spring-boot:run

# 使用 OpenAI（默认配置）
mvn spring-boot:run

# 使用 Mistral AI
mvn spring-boot:run -Pmistral-ai

# 使用 Ollama
mvn spring-boot:run -Pollama-ai

# 使用 Groq AI
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=groq"

# 使用 DeepSeek AI
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=deepseek"

# 使用 Qwen AI
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=qwen"

# 使用 OpenRouter AI
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=openrouter"

# 使用 DMR AI
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dmr"
```

### 3. 测试 API

```bash
# 推荐餐厅
curl -X POST http://localhost:8080/api/restaurants/recommend \
  -H "Content-Type: application/json" \
  -d '{
    "location": "北京市",
    "cuisine": "中餐",
    "priceRange": "中等价位",
    "dietaryRestrictions": ["素食"],
    "occasion": "商务聚餐",
    "groupSize": 4,
    "timeOfDay": "晚餐"
  }'

# 生成菜品
curl -X POST "http://localhost:8080/api/restaurants/dishes/generate" \
  -H "Content-Type: application/json" \
  -d '{"cuisine": "中餐", "count": 3}'

# 获取建议
curl -X POST "http://localhost:8080/api/restaurants/advice" \
  -H "Content-Type: application/json" \
  -d '{"query": "适合情侣约会的餐厅"}'

# 多语言聊天
curl -X POST "http://localhost:8080/api/restaurants/chat" \
  -H "Content-Type: application/json" \
  -d '{"message": "推荐一家好吃的川菜馆", "language": "zh"}'
```

## 配置说明

### Maven Profiles

项目使用 Maven Profiles 来管理不同的 AI 模型依赖：

- `open-ai` (默认) - OpenAI 集成
- `mistral-ai` - Mistral AI 集成  
- `ollama-ai` - Ollama 集成

## 核心特性

### 1. 智能推荐算法
系统使用 AI 模型分析用户偏好，包括：
- 地理位置
- 菜系偏好
- 价格范围
- 饮食限制
- 用餐场合
- 人数规模

### 2. 结构化输出
所有 AI 响应都转换为标准化的 JSON 格式，便于前端集成。

### 3. 中文参数支持
- 所有 API 使用 POST 方法，支持 JSON 请求体
- 直接支持中文字符，无需 URL 编码
- 简化了 API 调用，提升用户体验

### 4. 对话记忆
- 使用 `MessageWindowChatMemory` 和 `InMemoryChatMemoryRepository` 实现对话上下文记忆
- 集成 `MessageChatMemoryAdvisor` 自动管理对话历史
- 支持多轮对话，AI 能够记住之前的交互内容
- 提供更自然、连贯的对话体验

### 4. 多语言支持
支持中文和英文交互，根据用户语言偏好返回相应内容。

## 创新点

### 1. 场景创新
- 选择了更贴近实际应用的餐厅推荐场景
- 提供了完整的业务逻辑和用户体验

### 2. 功能创新
- 多语言支持，适应国际化需求
- 多维度推荐，提供更精准的推荐结果
- 菜品生成功能，丰富了应用内容

### 3. 技术创新
- 优雅的多模型切换机制
- 结构化的 AI 输出处理
- 完善的配置管理

## 开发指南

### 添加新的 AI 模型

1. 在 `pom.xml` 中添加新的 profile
2. 创建对应的配置文件
3. 在控制器中根据需要调整 prompt 模板

### 自定义 Prompt 模板

```java
PromptTemplate template = new PromptTemplate("""
    你的自定义 prompt 模板
    支持变量替换: {variableName}
    """);
```

### 扩展数据模型

在 `model` 包中添加新的实体类，确保包含必要的 getter/setter 方法。

## 扩展方向

### 1. 功能扩展
- 用户偏好学习
- 实时推荐更新
- 图像识别推荐
- 语音交互支持

### 2. 技术扩展
- 缓存机制优化
- 异步处理支持
- 监控和日志完善
- 性能优化

### 3. 业务扩展
- 餐厅预订集成
- 支付系统集成
- 用户评价系统
- 社交分享功能

## 许可证

MIT License

## 贡献

欢迎提交 Issue 和 Pull Request 来改进这个项目！

### 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request
