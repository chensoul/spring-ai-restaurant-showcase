#!/bin/bash

# Spring AI RAG 功能测试脚本

echo "🚀 启动 Spring AI RAG 测试..."

# 检查应用是否运行
if ! curl -s http://localhost:8080/actuator/health > /dev/null; then
    echo "❌ 应用未运行，请先启动应用：mvn spring-boot:run"
    exit 1
fi

echo "✅ 应用运行正常"

# 测试 RAG 聊天功能
echo ""
echo "🧪 测试 RAG 聊天功能..."

# 测试1: 北京川菜推荐
echo "测试1: 北京川菜推荐"
curl -X POST "http://localhost:8080/api/rag/chat" \
  -H "Content-Type: application/json" \
  -d '"我想在北京找一家川菜馆"' \
  -w "\n状态码: %{http_code}\n\n"

# 测试2: 武汉川菜推荐
echo "测试2: 武汉川菜推荐"
curl -X POST "http://localhost:8080/api/rag/chat" \
  -H "Content-Type: application/json" \
  -d '"武汉有什么好的川菜馆吗？"' \
  -w "\n状态码: %{http_code}\n\n"

# 测试3: 价格查询
echo "测试3: 价格查询"
curl -X POST "http://localhost:8080/api/rag/chat" \
  -H "Content-Type: application/json" \
  -d '"北京最便宜的川菜馆是哪家？"' \
  -w "\n状态码: %{http_code}\n\n"

# 测试4: 具体菜品推荐
echo "测试4: 具体菜品推荐"
curl -X POST "http://localhost:8080/api/rag/chat" \
  -H "Content-Type: application/json" \
  -d '"推荐几家有水煮鱼的川菜馆"' \
  -w "\n状态码: %{http_code}\n\n"

echo "🎉 RAG 功能测试完成！"
