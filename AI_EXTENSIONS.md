# Spring AI 餐厅推荐系统 - AI 扩展功能

本文档介绍了项目新增的 AI 扩展功能，展示了 Spring AI 框架的更多高级特性。

## 📋 目录

1. [Function Calling / Tool Calling](#function-calling--tool-calling)
2. [流式响应（Streaming）](#流式响应streaming)
3. [高级 RAG 功能](#高级-rag-功能)
4. [情感分析](#情感分析)
5. [未来扩展建议](#未来扩展建议)

---

## Function Calling / Tool Calling

### 功能说明

Function Calling 允许 AI 模型在对话过程中自动调用外部函数和工具，获取实时数据。这对于需要访问外部 API、数据库或执行特定计算的场景非常有用。

### 实现的功能

- **天气查询函数** (`getWeather`): 获取指定地点的天气信息
- **餐厅营业时间查询** (`getRestaurantHours`): 查询餐厅的营业时间
- **距离计算函数** (`calculateDistance`): 计算两个地点之间的距离和预计到达时间

### API 端点

```http
POST /api/function-calling/chat
Content-Type: application/json

{
  "message": "北京今天天气怎么样？我想去附近的川菜馆，帮我查一下营业时间"
}
```

### 使用示例

```bash
curl -X POST http://localhost:8080/api/function-calling/chat \
  -H "Content-Type: application/json" \
  -d '{
    "message": "我想知道北京今天的天气，以及附近川菜馆的营业时间"
  }'
```

### 技术实现

**当前实现（简化版本）**：
- 使用提示词增强方式模拟函数调用效果
- 适用于所有模型，不依赖 Tool Calling 支持
- 提供降级方案，确保功能可用性

**完整实现（需要模型支持）**：
- 使用 `FunctionToolCallback` 或 `MethodToolCallback` 包装 Java 函数
- 通过 `ChatClient.Builder.defaultTools()` 注册工具
- 需要模型支持 Tool Calling（如 GPT-4, Claude）
- AI 模型根据用户查询自动决定是否需要调用函数

**注意**：当前代码使用简化版本，如需完整功能请参考 Spring AI 官方文档配置 Tool Callbacks。

---

## 流式响应（Streaming）

### 功能说明

流式响应使用 Server-Sent Events (SSE) 技术，实时返回 AI 生成的文本，提供更好的用户体验。

### API 端点

#### 1. 流式聊天

```http
POST /api/streaming/chat
Content-Type: application/json

{
  "message": "推荐几家北京的好餐厅"
}
```

#### 2. 流式餐厅推荐

```http
POST /api/streaming/recommend
Content-Type: application/json

{
  "location": "北京市",
  "cuisine": "川菜"
}
```

### 使用示例

```javascript
const eventSource = new EventSource('/api/streaming/chat', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ message: '推荐餐厅' })
});

eventSource.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('收到数据:', data);
};
```

### 技术实现

- 使用 Spring 的 `SseEmitter` 实现 SSE
- `ChatClient.stream().content()` 返回 `Flux<String>`（响应式流）
- 使用 `Flux.subscribe()` 处理流式响应
- 使用 `CompletableFuture` 异步处理避免阻塞

---

## 高级 RAG 功能

### 功能说明

在基础 RAG 之上，实现了多种高级检索技术，提高检索质量和准确性。

### 1. Re-ranking RAG

先进行向量搜索获取候选结果，然后使用 LLM 对结果进行重新排序，提高相关性。

#### API 端点

```http
POST /api/advanced-rag/rerank
Content-Type: application/json

{
  "query": "北京最好的川菜馆",
  "topK": 10,
  "topN": 5
}
```

### 2. 混合搜索（Hybrid Search）

结合向量搜索（语义相似度）和关键词搜索，提供更全面的检索结果。

#### API 端点

```http
POST /api/advanced-rag/hybrid-search
Content-Type: application/json

{
  "query": "北京川菜馆",
  "topK": 5,
  "keywordWeight": 0.3
}
```

**参数说明**:
- `keywordWeight`: 关键词权重（0.0-1.0），0.3 表示 30% 关键词，70% 向量相似度

### 3. 多查询 RAG

自动生成多个相关查询，然后合并搜索结果，提高召回率。

#### API 端点

```http
POST /api/advanced-rag/multi-query
Content-Type: application/json

{
  "query": "北京好吃的餐厅",
  "topK": 5
}
```

### 4. 高级 RAG 聊天

结合 Re-ranking 的智能对话。

#### API 端点

```http
POST /api/advanced-rag/chat
Content-Type: application/json

{
  "query": "北京有哪些值得推荐的餐厅？"
}
```

---

## 情感分析

### 功能说明

使用 AI 分析用户评论和反馈的情感倾向，帮助餐厅了解客户满意度。

### API 端点

#### 1. 单条评论分析

```http
POST /api/sentiment/analyze
Content-Type: application/json

{
  "review": "这家餐厅的菜品非常好吃，服务也很周到，强烈推荐！"
}
```

**响应示例**:
```json
{
  "sentiment": "positive",
  "score": 0.95,
  "emotions": ["满意", "高兴"],
  "summary": "用户对餐厅的菜品和服务都非常满意"
}
```

#### 2. 批量评论分析

```http
POST /api/sentiment/batch-analyze
Content-Type: application/json

{
  "reviews": [
    "菜品很好吃，服务也不错",
    "价格有点贵，但味道还可以",
    "环境很好，推荐大家来"
  ]
}
```

**响应示例**:
```json
{
  "overallSentiment": "positive",
  "averageScore": 0.75,
  "positiveCount": 2,
  "negativeCount": 0,
  "neutralCount": 1,
  "keyInsights": [
    "用户对菜品质量普遍满意",
    "价格是主要关注点"
  ]
}
```

---

## 未来扩展建议

### 1. 图像分析（多模态）

- **菜品图片识别**: 上传菜品图片，AI 识别菜品名称、食材、营养信息
- **餐厅环境分析**: 分析餐厅照片，评估环境、氛围
- **实现方式**: 使用 Spring AI 的多模态模型支持（如 GPT-4 Vision, Gemini Vision）

### 2. 语音处理

- **语音转文字**: 用户语音输入转换为文本
- **文字转语音**: AI 回答转换为语音输出
- **实现方式**: 集成 Spring AI 的语音模型或第三方语音服务

### 3. 推荐系统增强

- **协同过滤**: 基于用户行为数据的推荐
- **内容过滤**: 基于菜品特征的推荐
- **混合推荐**: 结合多种推荐算法

### 4. 代码生成

- **餐厅管理代码生成**: 根据需求生成 CRUD 代码
- **API 文档生成**: 自动生成 API 文档
- **实现方式**: 使用代码生成模型（如 Claude, GPT-4）

### 5. 评估和测试框架

- **RAG 评估**: 评估检索质量和生成质量
- **A/B 测试**: 对比不同模型和策略的效果
- **性能监控**: 监控 AI 调用的延迟、成本等指标

### 6. 更多 Function Calling 示例

- **实时价格查询**: 查询餐厅实时价格
- **预订管理**: 餐厅预订相关操作
- **地图集成**: 集成地图 API 获取位置信息
- **支付集成**: 集成支付 API

### 7. 多模型路由

- **智能模型选择**: 根据任务类型自动选择最合适的模型
- **模型降级**: 主模型不可用时自动切换到备用模型
- **成本优化**: 根据成本选择模型

### 8. 对话管理

- **会话持久化**: 将对话保存到数据库
- **上下文管理**: 更好的上下文窗口管理
- **多轮对话优化**: 优化多轮对话的连贯性

---

## 技术栈

- **Spring AI 1.1.2**: 核心 AI 框架
- **PostgreSQL + pgvector**: 向量数据库
- **Server-Sent Events**: 流式响应
- **Spring Boot 3.5.9**: Web 框架

## 使用建议

1. **Function Calling**: 适合需要实时数据的场景
2. **流式响应**: 适合长文本生成，提升用户体验
3. **高级 RAG**: 适合知识库查询，提高检索准确性
4. **情感分析**: 适合用户反馈分析，了解客户满意度

## 注意事项

1. **Function Calling**: 当前实现为简化版本（提示词增强），适用于所有模型。完整功能需要模型支持 Tool Calling（如 GPT-4, Claude）
2. **流式响应**: 需要前端支持 SSE（Server-Sent Events）
3. **高级 RAG**: 功能会增加计算成本，特别是 Re-ranking 会调用 LLM
4. **情感分析**: 准确性取决于模型和提示词质量
5. **Spring AI 版本**: 本项目基于 Spring AI 1.1.2，API 可能与早期版本不同

---

## 贡献

欢迎提交 Issue 和 Pull Request 来扩展更多 AI 功能！
