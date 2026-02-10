# n8n 自动化内容生成与发布工作流设计方案

## 📋 工作流概述

本工作流整合 mediaCrawler 数据采集、AI 内容生成和社交媒体发布，实现自动化内容创作与发布。

## 🔄 工作流架构

```
┌─────────────────┐
│  定时触发器      │ (Cron: 每天/每小时)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  查询数据库      │ (从 mediaCrawler 数据库获取未处理数据)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  数据过滤分析    │ (筛选、去重、质量评估)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  AI 生成文案     │ (调用 AI 写作服务)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  AI 生成图片     │ (调用 AI 图片生成服务)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  内容审核        │ (可选：敏感词检测、内容质量检查)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  发布到小红书    │ (HTTP 请求调用发布接口)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  发布到抖音      │ (HTTP 请求调用发布接口)
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│  记录发布结果    │ (更新数据库，记录发布状态)
└─────────────────┘
```

## 📦 需要创建的 API 接口

### 1. 数据查询接口
- **路径**: `/api/content/crawler/query`
- **方法**: POST
- **功能**: 从 mediaCrawler 数据库查询未处理的数据
- **参数**: 
  ```json
  {
    "keywords": ["关键词1", "关键词2"],
    "platform": "douyin|xiaohongshu|weibo",
    "limit": 10,
    "processed": false
  }
  ```

### 2. 内容生成接口
- **路径**: `/api/content/generate`
- **方法**: POST
- **功能**: 基于数据生成文案和图片
- **参数**:
  ```json
  {
    "dataId": 123,
    "sourceData": {...},
    "generateType": "text|image|both"
  }
  ```

### 3. 发布接口
- **路径**: `/api/content/publish`
- **方法**: POST
- **功能**: 发布内容到小红书/抖音
- **参数**:
  ```json
  {
    "platform": "xiaohongshu|douyin",
    "title": "标题",
    "content": "正文内容",
    "images": ["图片URL1", "图片URL2"],
    "tags": ["标签1", "标签2"]
  }
  ```

## 🔧 n8n 工作流节点配置

### 节点 1: Schedule Trigger (定时触发器)
```json
{
  "name": "定时触发",
  "type": "n8n-nodes-base.scheduleTrigger",
  "parameters": {
    "rule": {
      "interval": [
        {
          "field": "hours",
          "hoursInterval": 6
        }
      ]
    }
  }
}
```

### 节点 2: HTTP Request (查询数据)
```json
{
  "name": "查询 mediaCrawler 数据",
  "type": "n8n-nodes-base.httpRequest",
  "parameters": {
    "method": "POST",
    "url": "http://your-api:48080/api/content/crawler/query",
    "authentication": "genericCredentialType",
    "genericAuthType": "httpHeaderAuth",
    "sendHeaders": true,
    "headerParameters": {
      "parameters": [
        {
          "name": "Authorization",
          "value": "Bearer {{$env.API_TOKEN}}"
        },
        {
          "name": "Content-Type",
          "value": "application/json"
        }
      ]
    },
    "sendBody": true,
    "bodyParameters": {
      "parameters": [
        {
          "name": "keywords",
          "value": "={{$json.keywords}}"
        },
        {
          "name": "limit",
          "value": "10"
        },
        {
          "name": "processed",
          "value": "false"
        }
      ]
    },
    "options": {}
  }
}
```

### 节点 3: Split In Batches (分批处理)
```json
{
  "name": "分批处理数据",
  "type": "n8n-nodes-base.splitInBatches",
  "parameters": {
    "batchSize": 1,
    "options": {}
  }
}
```

### 节点 4: Code (数据过滤和分析)
```javascript
// 数据过滤逻辑
const items = $input.all();

const filteredItems = items
  .filter(item => {
    const data = item.json;
    // 过滤条件：去重、质量检查等
    return data.likeCount > 100 && 
           data.commentCount > 10 &&
           !data.processed;
  })
  .map(item => ({
    json: {
      ...item.json,
      qualityScore: calculateQualityScore(item.json)
    }
  }));

return filteredItems;
```

### 节点 5: HTTP Request (生成文案)
```json
{
  "name": "AI 生成文案",
  "type": "n8n-nodes-base.httpRequest",
  "parameters": {
    "method": "POST",
    "url": "http://your-api:48080/api/content/generate/text",
    "authentication": "genericCredentialType",
    "sendBody": true,
    "bodyParameters": {
      "parameters": [
        {
          "name": "dataId",
          "value": "={{$json.id}}"
        },
        {
          "name": "sourceData",
          "value": "={{$json}}"
        },
        {
          "name": "platform",
          "value": "xiaohongshu"
        }
      ]
    }
  }
}
```

### 节点 6: HTTP Request (生成图片)
```json
{
  "name": "AI 生成图片",
  "type": "n8n-nodes-base.httpRequest",
  "parameters": {
    "method": "POST",
    "url": "http://your-api:48080/api/content/generate/image",
    "sendBody": true,
    "bodyParameters": {
      "parameters": [
        {
          "name": "prompt",
          "value": "={{$json.generatedText}}"
        },
        {
          "name": "width",
          "value": "1024"
        },
        {
          "name": "height",
          "value": "1024"
        }
      ]
    }
  }
}
```

### 节点 7: IF (内容审核)
```json
{
  "name": "内容审核",
  "type": "n8n-nodes-base.if",
  "parameters": {
    "conditions": {
      "string": [
        {
          "value1": "={{$json.sensitiveWords}}",
          "operation": "isEmpty"
        }
      ]
    }
  }
}
```

### 节点 8: HTTP Request (发布到小红书)
```json
{
  "name": "发布到小红书",
  "type": "n8n-nodes-base.httpRequest",
  "parameters": {
    "method": "POST",
    "url": "http://your-api:48080/api/content/publish/xiaohongshu",
    "sendBody": true,
    "bodyParameters": {
      "parameters": [
        {
          "name": "title",
          "value": "={{$json.title}}"
        },
        {
          "name": "content",
          "value": "={{$json.content}}"
        },
        {
          "name": "images",
          "value": "={{$json.images}}"
        },
        {
          "name": "tags",
          "value": "={{$json.tags}}"
        }
      ]
    }
  }
}
```

### 节点 9: HTTP Request (发布到抖音)
```json
{
  "name": "发布到抖音",
  "type": "n8n-nodes-base.httpRequest",
  "parameters": {
    "method": "POST",
    "url": "http://your-api:48080/api/content/publish/douyin",
    "sendBody": true,
    "bodyParameters": {
      "parameters": [
        {
          "name": "title",
          "value": "={{$json.title}}"
        },
        {
          "name": "content",
          "value": "={{$json.content}}"
        },
        {
          "name": "images",
          "value": "={{$json.images}}"
        }
      ]
    }
  }
}
```

### 节点 10: HTTP Request (更新发布状态)
```json
{
  "name": "更新发布状态",
  "type": "n8n-nodes-base.httpRequest",
  "parameters": {
    "method": "POST",
    "url": "http://your-api:48080/api/content/update-status",
    "sendBody": true,
    "bodyParameters": {
      "parameters": [
        {
          "name": "dataId",
          "value": "={{$json.dataId}}"
        },
        {
          "name": "published",
          "value": "true"
        },
        {
          "name": "publishTime",
          "value": "={{$now}}"
        }
      ]
    }
  }
}
```

### 节点 11: Error Trigger (错误处理)
```json
{
  "name": "错误处理",
  "type": "n8n-nodes-base.errorTrigger",
  "parameters": {}
}
```

## 📝 工作流 JSON 导出示例

完整的工作流 JSON 文件将包含所有节点的连接关系和配置。

## 🔐 环境变量配置

在 n8n 中配置以下环境变量：
- `API_TOKEN`: API 认证令牌
- `XIAOHONGSHU_TOKEN`: 小红书 API Token
- `DOUYIN_TOKEN`: 抖音 API Token
- `AI_MODEL_ID`: AI 模型 ID

## ⚙️ 配置建议

1. **定时频率**: 建议每 6 小时执行一次，避免频繁请求
2. **批次大小**: 每次处理 1-3 条数据，避免超时
3. **错误重试**: 配置自动重试机制，最多重试 3 次
4. **日志记录**: 记录每个节点的执行结果，便于排查问题

## 🚀 下一步

1. 创建后端 API 接口
2. 配置 n8n 工作流
3. 测试各个节点
4. 监控和优化
