---
name: public-apis-routing
description: 根据任务领域为「公共 REST/开放 API」选型并给出调用要点。当用户需要天气、汇率、地理编码、新闻、金融行情、词典、测试数据、图片/视频元数据、政府开放数据、体育、加密货币、邮件校验、短链、或任何「用现成 HTTP API 拉数据/能力」时必读；即使用户只说「找个免费 API」「集成第三方数据」「不用自建后端取数」也应触发。主索引来源为社区维护清单 public-apis/public-apis（GitHub）。本技能不替代各服务商官方文档，负责缩小范围与统一调用范式。
---

# 公共 API 选型与调用（基于 Public APIs 清单）

## 目标

在实现功能前，先**按领域选对 API**，再按**认证方式与调用形态**写请求。权威条目表在仓库 [public-apis/public-apis](https://github.com/public-apis/public-apis) 的 `README.md`（分类索引 + 表格列：API 名称、说明、Auth、HTTPS、CORS）。

## 执行顺序（模型必须遵守）

1. **明确用户要的数据或能力**（例如：实时天气、股票 K 线、IP 归属地、随机用户数据）。
2. **在下方「领域 → 参考文件」中打开对应小节**；若跨领域，先拆成子任务再分别选型。
3. **记录表格中的 Auth 列**：`No` / `apiKey` / `OAuth` 等，决定环境变量名与请求头。
4. **打开该 API 在 README 中链接的文档**（用浏览器或 `WebFetch`），核对**基础 URL、路径、必填参数、配额与错误码**；不要凭记忆拼 URL。
5. **实现调用**：优先 HTTPS；服务端保存密钥，勿写进前端仓库；浏览器直连时注意 CORS 列是否为 `Yes`。
6. **失败时**：先读文档里的 rate limit 与认证错误，再换同领域备选 API（见 `references/category-picks.md`）。

## 与清单列的对应关系

| README 列 | 模型应做的事 |
|-----------|----------------|
| **Auth** | `No`：可直接匿名或仅 User-Agent；`apiKey`：Query `?access_key=` 或 Header `Authorization`/服务商约定；`OAuth`：走授权码流程，存 refresh token。 |
| **HTTPS** | 优先 `Yes`；若为 `No`，评估中间人风险，默认不建议在生产使用。 |
| **CORS** | 浏览器直连需 `Yes`；`No`/`Unknown` 时由**后端**代发请求。 |

## 通用调用模板

**GET + Query（常见于天气、部分开放数据）**

```http
GET {baseUrl}/{path}?{param}={value}
```

**GET + Header Key**

```http
GET {baseUrl}/{path}
X-Api-Key: {API_KEY}
```

**服务端代理（推荐密钥类、或 CORS 为 No）**

由 Spring Boot / Node 等后端持有 `apiKey`，前端只调自有 `/api/...`，避免泄露与跨域问题。

## 领域索引（详细条目见 references）

| 用户意图关键词 | 先读 |
|----------------|------|
| 天气、降水、UV、气象 | `references/category-picks.md` → Weather / Environment |
| 汇率、法币、加密货币 | Currency Exchange / Cryptocurrency |
| IP 定位、经纬度、地址解析、国家城市 | Geocoding |
| 新闻头条、媒体 | News |
| 股票、财报、宏观、加密货币行情 | Finance |
| 随机用户、假数据、占位图 | Test Data / Development（Httpbin、JSONPlaceholder 等） |
| 词典、词义、多语言 | Dictionaries / Text Analysis |
| 图片搜索、去背、截图 | Photography / Development |
| 影视/剧集元数据 | Video |
| 政府统计、开放数据 | Government / Open Data |
| 赛事、球队、比分 | Sports & Fitness |
| 邮件是否有效、一次性邮箱 | Email |
| 缩短链接 | URL Shorteners |

完整分领域「首选 + 备选 + 备注」见：**[references/category-picks.md](references/category-picks.md)**。

## 清单与 API 元数据

- **人工维护的 API 列表**：仓库根目录 `README.md`。
- **可编程检索（若需批量/自动生成客户端）**：仓库说明中的 **API for this project**（见 GitHub 仓库描述区），需要时从 README 进入链接查询最新端点说明。

## 合规与安全

- 遵守各 API 的服务条款与配额；商业用途额外确认许可。
- 密钥仅放服务端或密钥管理，勿提交仓库。
- 用户个人数据、健康、金融场景注意最小化采集与区域法规。

## 何时不要强依赖本技能

- 需要**官方强制 SDK**且文档极长（部分云厂商）：以厂商文档为准，本技能只作选型参考。
- **用户已指定**某一服务商：直接按其文档实现，无需再扫清单。
