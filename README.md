# Spring AI 智能餐厅推荐系统

[![GitHub release](https://img.shields.io/github/v/release/chensoul/spring-ai-restaurant-showcase)](https://github.com/chensoul/spring-ai-restaurant-showcase/releases)
[![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/chensoul/spring-ai-restaurant-showcase/ci.yml?branch=main)](https://github.com/chensoul/spring-ai-restaurant-showcase/actions/workflows/test.yml)
[![License](https://img.shields.io/github/license/chensoul/spring-ai-restaurant-showcase)](LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/chensoul/spring-ai-restaurant-showcase)](https://github.com/chensoul/spring-ai-restaurant-showcase/stargazers)

这是一个基于 Spring AI 框架构建的智能餐厅推荐系统，展示了如何使用不同的 AI 模型提供商来构建智能化的餐饮推荐应用。

## 🛠️ 技术栈

- **Spring Boot 3.5.9**: Web 框架
- **Spring AI 1.1.2**: AI 框架
- **PostgreSQL + pgvector**: 向量数据库
- **Lombok**: 简化 Java 代码
- **Docker Compose**: 容器编排

## 📝 支持的 AI 模型

- OpenAI (GPT-4, GPT-3.5)
- Mistral AI
- Ollama (本地模型)
- DeepSeek
- Gemini
- Groq
- OpenRouter
- Qwen

## ✨ 核心功能

### 基础功能
- 🍽️ **餐厅推荐**: 基于用户偏好的智能餐厅推荐
- 🥘 **菜品生成**: AI 生成菜品描述和营养信息
- 💬 **智能对话**: 多语言餐厅推荐对话
- 📊 **结构化输出**: 多种结构化输出方式示例

### RAG 功能
- 🔍 **向量搜索**: 基于语义相似度的文档检索
- 💡 **RAG 聊天**: 检索增强生成的智能对话
- 🎯 **个性化 RAG**: 基于用户偏好的个性化推荐

### 🆕 AI 扩展功能（新增）

- 🔧 **Function Calling**: AI 自动调用外部工具和函数
- 🌊 **流式响应**: Server-Sent Events 实时流式输出
- 🚀 **高级 RAG**: Re-ranking、混合搜索、多查询 RAG
- 😊 **情感分析**: 分析用户评论和反馈的情感倾向

详细功能说明请查看 [AI_EXTENSIONS.md](AI_EXTENSIONS.md)

## 🚀 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- Docker & Docker Compose（用于 PostgreSQL + pgvector）

### 运行步骤

1. **启动数据库**
```bash
docker-compose up -d
```

2. **配置 API Key**
```bash
export OPENAI_API_KEY=your-api-key
# 或配置其他模型的 API Key
```

3. **运行应用**
```bash
./mvnw spring-boot:run
```

4. **测试 API**
```bash
# 测试基础功能
curl -X POST http://localhost:8080/api/restaurants/recommend \
  -H "Content-Type: application/json" \
  -d '{"location": "北京市", "cuisine": "川菜"}'

# 测试 AI 扩展功能
./examples/test-ai-extensions.sh
```

## 📖 API 文档

### 基础 API

- `POST /api/restaurants/recommend` - 餐厅推荐
- `POST /api/restaurants/dishes/generate` - 生成菜品
- `POST /api/restaurants/advice` - 用餐建议
- `POST /api/restaurants/chat` - 智能对话
- `GET /api/restaurants/{id}/details` - 获取餐厅详细信息

### RAG API

- `POST /api/rag/load` - 加载文档到向量存储
- `POST /api/rag/chat` - RAG 聊天
- `POST /api/rag/search` - 向量相似性搜索
- `POST /api/rag/chat-personalized` - 个性化 RAG 聊天

### 🆕 AI 扩展 API

- `POST /api/function-calling/chat` - Function Calling 聊天
- `POST /api/streaming/chat` - 流式聊天（SSE）
- `POST /api/streaming/recommend` - 流式推荐（SSE）
- `POST /api/advanced-rag/rerank` - Re-ranking RAG
- `POST /api/advanced-rag/hybrid-search` - 混合搜索
- `POST /api/advanced-rag/multi-query` - 多查询 RAG
- `POST /api/advanced-rag/chat` - 高级 RAG 聊天
- `POST /api/sentiment/analyze` - 情感分析（单条）
- `POST /api/sentiment/batch-analyze` - 情感分析（批量）

## 📚 文章目录

- [基于 Spring AI 构建智能餐厅推荐系统：多模型集成的实践指南](https://blog.chensoul.cc/posts/2025/09/25/spring-ai-restaurant-showcase/)
- [基于 Spring AI 构建智能餐厅推荐系统：RAG 技术实战](https://blog.chensoul.cc/posts/2025/09/26/spring-ai-restaurant-showcase-rag/)

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

本项目采用 [MIT License](LICENSE) 许可证。