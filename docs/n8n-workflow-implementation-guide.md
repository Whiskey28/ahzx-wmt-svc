# n8n 内容自动化工作流实施指南

## 📚 目录

1. [概述](#概述)
2. [架构设计](#架构设计)
3. [环境准备](#环境准备)
4. [API 接口说明](#api-接口说明)
5. [n8n 工作流配置](#n8n-工作流配置)
6. [实施步骤](#实施步骤)
7. [注意事项](#注意事项)
8. [故障排查](#故障排查)

## 概述

本工作流实现了从 mediaCrawler 数据采集到内容生成和发布的完整自动化流程：

1. **数据采集**: 从 mediaCrawler 数据库查询未处理的数据
2. **数据分析**: 过滤和评估数据质量
3. **内容生成**: 使用 AI 生成文案和配图
4. **内容审核**: 敏感词检测
5. **内容发布**: 自动发布到小红书和抖音

## 架构设计

### 工作流流程图

```
定时触发 → 查询数据 → 分批处理 → 数据过滤 → 生成内容 → 内容审核 → 发布平台 → 更新状态
```

### 技术栈

- **后端**: Spring Boot (本项目)
- **工作流引擎**: n8n
- **AI 服务**: 项目内置 AI 模块（写作 + 绘画）
- **数据源**: mediaCrawler 数据库

## 环境准备

### 1. 后端服务配置

确保以下服务正常运行：

- Spring Boot 应用（端口：48080）
- MySQL 数据库（包含 mediaCrawler 数据）
- Redis（如需要）

### 2. n8n 安装

```bash
# 使用 Docker 安装 n8n
docker run -it --rm \
  --name n8n \
  -p 5678:5678 \
  -v ~/.n8n:/home/node/.n8n \
  n8nio/n8n
```

访问：http://localhost:5678

### 3. 配置环境变量

在 n8n 中配置以下环境变量：

- `API_TOKEN`: 后端 API 认证令牌
- `XIAOHONGSHU_TOKEN`: 小红书 API Token（如需要）
- `DOUYIN_TOKEN`: 抖音 API Token（如需要）

## API 接口说明

### 1. 查询 mediaCrawler 数据

**接口**: `POST /api/content/crawler/query`

**请求体**:
```json
{
  "keywords": ["科技", "AI"],
  "platform": "douyin",
  "limit": 10,
  "processed": false,
  "minLikeCount": 100,
  "minCommentCount": 10
}
```

**响应**:
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "platform": "douyin",
      "title": "标题",
      "content": "内容",
      "likeCount": 1000,
      "commentCount": 100,
      "processed": false
    }
  ]
}
```

### 2. 生成完整内容

**接口**: `POST /api/content/generate`

**请求体**:
```json
{
  "dataId": 1,
  "sourceData": "{\"title\": \"...\", \"content\": \"...\"}",
  "platform": "xiaohongshu",
  "generateImage": true
}
```

**响应**:
```json
{
  "code": 0,
  "data": {
    "title": "生成的标题",
    "content": "生成的正文",
    "tags": ["标签1", "标签2"],
    "imageUrls": ["http://.../image1.jpg"],
    "generateTime": 1704067200000
  }
}
```

### 3. 发布到小红书

**接口**: `POST /api/content/publish/xiaohongshu`

**请求体**:
```json
{
  "platform": "xiaohongshu",
  "title": "标题",
  "content": "正文",
  "images": ["http://.../image1.jpg"],
  "tags": ["标签1", "标签2"],
  "dataId": 1
}
```

### 4. 发布到抖音

**接口**: `POST /api/content/publish/douyin`

**请求体**: 同小红书

### 5. 更新数据状态

**接口**: `POST /api/content/update-status`

**请求体**:
```json
{
  "dataId": 1,
  "processed": true,
  "published": true,
  "publishTime": 1704067200000
}
```

## n8n 工作流配置

### 导入工作流

1. 打开 n8n 界面
2. 点击 "Workflows" → "Import from File"
3. 选择 `n8n-workflow-json-example.json` 文件
4. 配置各个节点的参数

### 节点配置说明

#### 1. 定时触发器
- **频率**: 建议每 6 小时执行一次
- **时区**: 根据实际需求设置

#### 2. 查询数据节点
- **URL**: 修改为实际的后端服务地址
- **认证**: 配置 API Token

#### 3. 数据过滤节点
- **过滤条件**: 可根据实际需求调整
- **质量分数**: 可自定义计算逻辑

#### 4. 内容生成节点
- **平台**: 可配置为 `xiaohongshu` 或 `douyin`
- **生成图片**: 根据需要开启/关闭

#### 5. 发布节点
- **平台**: 分别配置小红书和抖音
- **重试机制**: 建议配置自动重试

## 实施步骤

### 第一步：完善后端服务

1. **实现 mediaCrawler 数据查询**

   在 `ContentAutomationServiceImpl` 中实现 `queryCrawlerData` 方法：

   ```java
   // 需要根据实际的 mediaCrawler 数据库表结构实现
   // 示例：查询未处理的数据
   List<MediaCrawlerDataDO> dataList = mediaCrawlerDataMapper.selectList(
       new QueryWrapper<MediaCrawlerDataDO>()
           .in("keyword", queryReqVO.getKeywords())
           .eq("platform", queryReqVO.getPlatform())
           .eq("processed", false)
           .ge("like_count", queryReqVO.getMinLikeCount())
           .last("LIMIT " + queryReqVO.getLimit())
   );
   ```

2. **实现发布接口**

   需要集成小红书和抖音的开放平台 API：

   - 小红书：https://open.xiaohongshu.com/
   - 抖音：https://open.douyin.com/

3. **实现敏感词检测**

   使用项目中的敏感词服务：

   ```java
   @Resource
   private SensitiveWordService sensitiveWordService;
   ```

### 第二步：配置 n8n 工作流

1. 导入工作流 JSON 文件
2. 配置各个节点的 URL 和认证信息
3. 测试各个节点的连接
4. 激活工作流

### 第三步：测试验证

1. **手动触发测试**
   - 在 n8n 中手动执行工作流
   - 检查每个节点的输出
   - 验证数据流转是否正确

2. **端到端测试**
   - 确保数据能正确查询
   - 验证内容生成质量
   - 检查发布是否成功

3. **监控和优化**
   - 监控工作流执行情况
   - 记录错误日志
   - 优化生成质量

## 注意事项

### 1. 数据安全

- API Token 需要妥善保管
- 敏感信息不要硬编码
- 使用环境变量管理配置

### 2. 频率控制

- 避免频繁调用 API
- 建议设置合理的执行间隔
- 注意平台 API 限流

### 3. 内容质量

- 定期检查生成的内容质量
- 调整 AI 提示词优化效果
- 建立内容审核机制

### 4. 错误处理

- 配置错误重试机制
- 记录详细的错误日志
- 设置告警通知

### 5. 成本控制

- AI 生成有成本，注意使用量
- 合理设置生成频率
- 监控 API 调用次数

## 故障排查

### 问题 1: 无法查询数据

**可能原因**:
- 数据库连接失败
- 查询条件不正确
- 数据表不存在

**解决方法**:
- 检查数据库连接配置
- 验证查询 SQL
- 确认 mediaCrawler 数据表结构

### 问题 2: 内容生成失败

**可能原因**:
- AI 服务未配置
- API 密钥无效
- 提示词格式错误

**解决方法**:
- 检查 AI 模型配置
- 验证 API 密钥
- 调整提示词格式

### 问题 3: 发布失败

**可能原因**:
- 平台 API 认证失败
- 内容不符合平台规范
- 网络连接问题

**解决方法**:
- 检查平台 API Token
- 验证内容格式
- 检查网络连接

### 问题 4: 工作流执行超时

**可能原因**:
- 处理数据量过大
- 网络延迟
- AI 生成耗时过长

**解决方法**:
- 减少批次大小
- 优化网络配置
- 增加超时时间设置

## 扩展功能

### 1. 多平台支持

可以扩展支持更多平台：
- 微博
- 知乎
- B站

### 2. 内容模板

可以预设多种内容模板：
- 产品推广
- 知识分享
- 生活记录

### 3. 数据分析

可以添加数据分析功能：
- 发布效果统计
- 内容质量评估
- 用户互动分析

## 技术支持

如有问题，请参考：
- [n8n 官方文档](https://docs.n8n.io/)
- [项目 README](../README.md)
- [工作流设计文档](n8n-workflow-design.md)
