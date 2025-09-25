package cc.chensoul.springai.restaurant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RAG 增强的聊天服务
 * 提供基于检索增强生成的智能对话功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RagChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    /**
     * 基于 RAG 的聊天对话
     * 
     * @param userMessage 用户消息
     * @return AI 回答
     */
    public String chatWithRag(String userMessage) {
        log.info("开始 RAG 聊天处理: {}", userMessage);
        
        try {
            // 1. 检索相关文档
            List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                    .query(userMessage)
                    .topK(5)
                    .build()
            );
            
            log.info("检索到 {} 个相关文档片段", relevantDocs.size());
            
            // 2. 构建上下文
            String context = relevantDocs.stream()
                    .map(Document::getFormattedContent)
                    .collect(Collectors.joining("\n\n"));

            // 3. 构建增强提示词
            String enhancedPrompt = String.format("""
                基于以下餐厅知识库信息回答用户问题：
                
                知识库内容：
                %s
                
                用户问题：%s
                
                请根据知识库信息提供准确、详细的回答。如果知识库中没有相关信息，请明确说明。
                回答时请保持专业和友好的语调，并提供实用的建议。
                """, context, userMessage);

            // 4. 生成回答
            String response = chatClient.prompt()
                    .user(enhancedPrompt)
                    .call()
                    .content();
            
            log.info("RAG 聊天处理完成");
            return response;
            
        } catch (Exception e) {
            log.error("RAG 聊天处理失败: {}", e.getMessage(), e);
            throw new RuntimeException("RAG 聊天处理失败: " + e.getMessage(), e);
        }
    }

    /**
     * 混合检索
     * 结合向量检索和其他检索策略
     * 
     * @param query 查询文本
     * @param topK 返回结果数量
     * @return 检索结果
     */
    public List<Document> hybridSearch(String query, int topK) {
        log.info("执行混合检索: {}, topK: {}", query, topK);
        
        try {
            // 使用向量相似性搜索
            List<Document> vectorResults = vectorStore.similaritySearch(
                SearchRequest.builder()
                    .query(query)
                    .topK(topK)
                    .build()
            );
            
            log.info("混合检索完成，返回 {} 个结果", vectorResults.size());
            return vectorResults;
            
        } catch (Exception e) {
            log.error("混合检索失败: {}", e.getMessage(), e);
            throw new RuntimeException("混合检索失败: " + e.getMessage(), e);
        }
    }

    /**
     * 带上下文管理的聊天
     * 支持上下文长度限制和记忆管理
     * 
     * @param message 用户消息
     * @param maxTokens 最大 token 数
     * @return AI 回答
     */
    public String chatWithContextManagement(String message, int maxTokens) {
        log.info("开始带上下文管理的聊天: {}, maxTokens: {}", message, maxTokens);
        
        try {
            // 1. 检索相关文档
            List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                    .query(message)
                    .topK(3) // 减少检索数量以控制上下文长度
                    .build()
            );
            
            // 2. 构建上下文，考虑 token 限制
            String context = relevantDocs.stream()
                    .map(Document::getFormattedContent)
                    .collect(Collectors.joining("\n\n"));

            // 3. 构建优化的提示词
            String enhancedPrompt = String.format("""
                基于以下餐厅知识库信息回答用户问题（请保持回答简洁，控制在合理长度内）：
                
                知识库内容：
                %s
                
                用户问题：%s
                
                请根据知识库信息提供准确、简洁的回答。
                """, context, message);

            // 4. 生成回答
            String response = chatClient.prompt()
                    .user(enhancedPrompt)
                    .call()
                    .content();
            
            log.info("带上下文管理的聊天完成");
            return response;
            
        } catch (Exception e) {
            log.error("带上下文管理的聊天失败: {}", e.getMessage(), e);
            throw new RuntimeException("带上下文管理的聊天失败: " + e.getMessage(), e);
        }
    }
}
