# Spring AI RAG 集成指南：构建智能餐厅推荐系统

## 前言

在人工智能快速发展的今天，检索增强生成（RAG - Retrieval Augmented Generation）技术已经成为构建智能应用的重要工具。本文将详细介绍如何在 Spring AI 项目中集成 RAG 功能，以智能餐厅推荐系统为例，展示如何通过向量存储和文档检索来增强 AI 模型的回答质量。

## 什么是 RAG？

RAG（Retrieval Augmented Generation）是一种结合了信息检索和文本生成的技术架构。它通过以下步骤工作：

1. **文档存储**：将相关文档分割并转换为向量存储在向量数据库中
2. **查询检索**：根据用户问题检索最相关的文档片段
3. **增强生成**：将检索到的文档作为上下文信息，让 AI 模型生成更准确、更相关的回答

RAG 的优势在于能够利用外部知识库，提供更准确、更及时的信息，同时减少 AI 模型的幻觉问题。

## 技术选型

### 向量存储方案

Spring AI 1.1.0-M2 支持多种向量存储方案：

- **Pinecone**：生产环境推荐，高性能云服务
- **Chroma**：开发环境推荐，轻量级本地部署
- **PgVector**：基于 PostgreSQL 的向量存储
- **Redis**：基于 Redis 的向量存储

### 文档处理

- **Spring AI Document API**：文档加载、分割、向量化
- **Embedding Models**：文本向量化模型
- **Vector Store**：向量存储和检索

## 环境准备

### 1. 添加依赖

在 `pom.xml` 中添加 RAG 相关依赖：

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-vector-store-pgvector</artifactId>
</dependency>

<!-- 文档处理依赖 -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-tika-document-reader</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-markdown-document-reader</artifactId>
</dependency>
```

### 2. Docker Compose 环境

创建 `docker-compose.yml` 文件：

```yaml
services:
  # PostgreSQL with PgVector 扩展
  postgres:
    image: pgvector/pgvector:pg16
    container_name: restaurant-postgres
    environment:
      POSTGRES_DB: postgres
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
```

启动服务：

```bash
docker-compose up -d
```

## 核心实现

### 1. 文档加载服务

创建 `DocumentService.java`：

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final VectorStore vectorStore;

    public void loadDocuments(String filePath) {
        try {
            // 加载文档
            TikaDocumentReader reader = new TikaDocumentReader(filePath);
            List<Document> documents = reader.get();

            // 分割文档
            TokenTextSplitter splitter = new TokenTextSplitter();
            List<Document> splitDocuments = splitter.apply(documents);

            // 存储到向量数据库
            vectorStore.add(splitDocuments);
            
            log.info("成功加载 {} 个文档片段到向量存储", splitDocuments.size());
        } catch (Exception e) {
            log.error("加载文档失败: {}", e.getMessage(), e);
        }
    }

    public List<Document> searchSimilar(String query, int topK) {
        return vectorStore.similaritySearch(query, topK);
    }
}
```

### 3. RAG 增强的聊天服务

创建 `RagChatService.java`：

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class RagChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public String chatWithRag(String userMessage) {
        // 1. 检索相关文档
        List<Document> relevantDocs = vectorStore.similaritySearch(userMessage, 5);
        
        // 2. 构建上下文
        String context = relevantDocs.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n\n"));

        // 3. 构建增强提示词
        String enhancedPrompt = String.format("""
            基于以下餐厅知识库信息回答用户问题：
            
            知识库内容：
            %s
            
            用户问题：%s
            
            请根据知识库信息提供准确、详细的回答。如果知识库中没有相关信息，请明确说明。
            """, context, userMessage);

        // 4. 生成回答
        return chatClient.prompt()
                .user(enhancedPrompt)
                .call()
                .content();
    }
}
```

### 4. RAG 控制器

创建 `RagController.java`：

```java
@Slf4j
@RestController
@RequestMapping("/api/rag")
@RequiredArgsConstructor
public class RagController {

    private final RagChatService ragChatService;

    @PostMapping("/chat")
    public String chatWithRag(@RequestBody String message) {
        log.info("RAG 聊天请求: {}", message);
        return ragChatService.chatWithRag(message);
    }
}
```

## 知识库准备

### 1. 餐厅数据文档

创建 `restaurant-knowledge.txt`：

```text
# 北京川菜餐厅推荐

## 峨嵋酒家
- 地址：北京市西城区车公庄大街1号
- 特色：北京老字号川菜，招牌菜宫保鸡丁
- 人均：100-120元
- 推荐菜：宫保鸡丁、水煮鱼、夫妻肺片
- 环境：传统中式装修，适合家庭聚餐

## 锦府盐帮
- 地址：北京市朝阳区三里屯北路45号
- 特色：融合川南盐商菜与经典川菜
- 人均：120-150元
- 推荐菜：退秋鱼、盐帮仔蛙、招牌毛血旺
- 环境：古风庭院，适合商务宴请

## 张妈妈特色川菜馆
- 地址：北京市东城区簋街123号
- 特色：性价比超高的小馆子
- 人均：60-80元
- 推荐菜：张妈妈小炒、钵钵鸡、连山回锅肉
- 环境：烟火气十足，适合年轻人聚餐

## 龙人居·水煮三峡鱼
- 地址：北京市海淀区中关村南大街12号
- 特色：专注水煮鱼，活鱼现杀
- 人均：130-160元
- 推荐菜：水煮三峡鱼、辣子鸡、糯米鸭
- 环境：油质清澈可打包带走

# 武汉川菜餐厅推荐

## 蜀九香火锅
- 地址：武汉市江汉区万象城
- 特色：虽是火锅品牌，但川味炒菜同样出色
- 人均：120元
- 推荐菜：九香牛肉、毛血旺、夫妻肺片
- 环境：舒适，适合聚餐

## 川霸味道
- 地址：武汉市武昌区楚河汉街
- 特色：武汉本土知名川菜品牌
- 人均：80-100元
- 推荐菜：川霸腰花、豆花牛肉、麻婆豆腐
- 环境：分店多，方便选择

## 俏立方餐厅
- 地址：武汉市武昌区楚河汉街
- 特色：环境优雅，主打新派川菜
- 人均：100-130元
- 推荐菜：水煮鱼、辣子鸡、蒜泥白肉
- 环境：临湖view佳，适合约会

## 小民大排档
- 地址：武汉市洪山区光谷步行街
- 特色：武汉宵夜名店，川味江湖菜
- 人均：60-90元
- 推荐菜：凤爪、毛豆、香辣蟹
- 环境：烟火气十足，晚市需排队
```

### 2. 文档加载配置

在 `application.yml` 中添加配置：

```yaml

```

## 测试和验证

### 1. 启动应用

```bash
# 启动向量存储服务
docker-compose up -d

# 启动应用
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=deepseek"
```

### 2. 加载知识库

```bash
# 加载餐厅知识库
curl -X POST "http://localhost:8080/api/rag/load" \
  -H "Content-Type: application/json" \
  -d '{"filePath": "classpath:restaurant-knowledge.txt"}'
```

### 3. 测试 RAG 功能

```bash
# 测试 RAG 聊天
curl -X POST "http://localhost:8080/api/rag/chat" \
  -H "Content-Type: application/json" \
  -d '"我想在北京找一家川菜馆"'
```

## 高级特性

### 1. 混合检索

结合关键词搜索和语义搜索：

```java
public List<Document> hybridSearch(String query, int topK) {
    // 语义搜索
    List<Document> semanticResults = vectorStore.similaritySearch(query, topK);
    
    // 关键词搜索
    List<Document> keywordResults = vectorStore.similaritySearch(
        query, topK, SearchRequest.defaults()
            .withFilterExpression("content LIKE '%" + query + "%'")
    );
    
    // 合并和去重
    return mergeAndDeduplicate(semanticResults, keywordResults);
}
```

### 2. 上下文窗口管理

```java
public String chatWithContextManagement(String userMessage, int maxTokens) {
    List<Document> relevantDocs = vectorStore.similaritySearch(userMessage, 10);
    
    // 根据 token 限制选择文档
    List<Document> selectedDocs = selectDocumentsWithinTokenLimit(
        relevantDocs, maxTokens
    );
    
    String context = selectedDocs.stream()
            .map(Document::getContent)
            .collect(Collectors.joining("\n\n"));
    
    return chatClient.prompt()
            .user(buildEnhancedPrompt(context, userMessage))
            .call()
            .content();
}
```

### 3. 多模态 RAG

支持图片和文本的混合检索：

```java
@Service
public class MultiModalRagService {
    
    public String chatWithImages(String userMessage, List<String> imageUrls) {
        // 处理文本查询
        List<Document> textDocs = vectorStore.similaritySearch(userMessage, 5);
        
        // 处理图片查询
        List<Document> imageDocs = processImages(imageUrls);
        
        // 合并结果
        List<Document> allDocs = mergeDocuments(textDocs, imageDocs);
        
        return generateResponse(userMessage, allDocs);
    }
}
```

## 监控和日志

### 1. 性能监控

```java
@Component
public class RagMetrics {
    
    private final MeterRegistry meterRegistry;
    private final Timer searchTimer;
    private final Counter searchCounter;
    
    public RagMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.searchTimer = Timer.builder("rag.search.duration")
                .register(meterRegistry);
        this.searchCounter = Counter.builder("rag.search.count")
                .register(meterRegistry);
    }
    
    public void recordSearch(Duration duration) {
        searchTimer.record(duration);
        searchCounter.increment();
    }
}
```

## 总结

通过本文的介绍，我们了解了如何在 Spring AI 项目中集成 RAG 功能。主要步骤包括：

1. **环境准备**：选择合适的向量存储方案
2. **依赖配置**：添加必要的 Spring AI 依赖
3. **核心实现**：实现文档加载、向量存储、检索增强
4. **知识库构建**：准备结构化的餐厅数据
5. **性能优化**：实现缓存、异步处理等优化策略
6. **监控运维**：添加性能监控和日志记录

RAG 技术的集成能够显著提升 AI 应用的准确性和实用性，特别是在需要利用外部知识库的场景中。通过合理的架构设计和性能优化，可以构建出高效、可靠的智能应用系统。

## 参考资源

- [Spring AI 官方文档](https://docs.spring.io/spring-ai/reference/)
- [RAG 技术原理](https://arxiv.org/abs/2005.11401)
- [向量数据库对比](https://www.pinecone.io/learn/vector-database/)
- [Chroma 官方文档](https://docs.trychroma.com/)
- [PgVector 扩展](https://github.com/pgvector/pgvector)

---

*本文基于 Spring AI 1.1.0-M2 版本编写，如有疑问欢迎交流讨论。*
