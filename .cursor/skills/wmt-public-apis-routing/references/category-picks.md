# 分领域 API 速查（与 public-apis README 分类对齐）

下列条目来自 [public-apis/public-apis](https://github.com/public-apis/public-apis) 常见用法归纳。**具体路径与参数以各 API 文档为准**；此处给出选型方向与典型认证方式。

---

## Weather（天气）

| 场景 | 首选方向 | Auth 常见值 | 调用提示 |
|------|----------|-------------|----------|
| 全球预报、非商业 | Open-Meteo | No | GET，坐标或城市；无需 key，注意使用条款 |
| 通用气象 | OpenWeatherMap | apiKey | `appid` query 或文档约定 header |
| 美国官方 | US Weather (weather.gov) | No | 需读 NWS 文档，注意 User-Agent |
| 历史/小时级专业数据 | 商业/科研类（如 Visual Crossing、Oikolab 等） | apiKey | 按文档计费 |

---

## Currency Exchange（法币汇率）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| 免 key、简单汇率 | Frankfurter | No | GET，基准货币 + 日期范围 |
| 巴西等拉美实时 | AwesomeAPI（Economia）等 | No | 读文档限流 |
| 需 key 的聚合 | ExchangeRate-API、Fixer 等 | apiKey | 密钥放后端 |

---

## Cryptocurrency（加密货币）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| 价格/市场概览 | CoinGecko、CoinCap | 多为 No | GET，注意 rate limit |
| 交易所级 | 各交易所 REST（Binance、Kraken 等） | apiKey / 签名校验 | **交易**必须用官方签名算法；只读行情可看公开端点 |

---

## Geocoding（地理 / IP）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| IP → 国家/城市 | ip-api、ipinfo（部分免费档） | No / apiKey | 前端直连注意 CORS；生产用后端代理 |
| 正/逆地理编码 | Nominatim（OSM） | No | 必须遵守 Usage Policy，加合理 User-Agent |
| 结构化国家/城市数据 | REST Countries、CountryStateCity | 视条目 | 静态数据可缓存 |

---

## News（新闻）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| 聚合头条 | NewsAPI、GNews、Mediastack 等 | apiKey | 密钥服务端；注意来源版权 |
| 大报官方 | Guardian、NYTimes | apiKey | 注册开发者账号 |

---

## Finance（金融）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| 美股等行情 | Alpha Vantage、Finnhub、FMP 等 | apiKey | 注意调用次数与日限制 |
| 宏观序列 | FRED | apiKey | 美国经济序列常用 |
| SEC 文件 | SEC EDGAR（开放数据） | 常无需 | 需遵守 SEC 请求头/频率规范 |

---

## Development（开发辅助）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| 请求镜像/调试 | Httpbin、Postman Echo | No | 仅用于测客户端行为 |
| 假 REST 数据 | JSONPlaceholder、ReqRes | No | 只读演示，非生产持久化 |
| 截图 | 各类 Screenshot API | apiKey | 异步任务需轮询文档 |

---

## Test Data（测试数据）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| 随机用户 | RandomUser | No | 生成测试用户画像 |
| 假电商 | FakeStoreAPI | No | 购物车流程演示 |
| Lorem | Bacon Ipsum 等 | No | 占位文本 |

---

## Dictionaries & Text Analysis（词典 / 文本）

| 场景 | 示例 API | Auth | 调用提示 |
|------|----------|------|----------|
| 英文释义 | Free Dictionary API | No | 适合快速原型 |
| 商用词典 | Oxford、Words API 等 | apiKey | 合同与用量 |
| 情感/实体 | Google Cloud NL、Watson 等 | apiKey/OAuth | 合规与区域部署 |

---

## Video / Games / Sports（影视 / 游戏 / 体育）

- **影视元数据**：OMDb、TMDb、TVMaze 等（多为 apiKey；OMDb 常见 `?apikey=&t=`）。
- **游戏数据**：RAWG、IGDB、各厂商官方 API（任天堂/索尼等需单独申请）。
- **体育**：API-FOOTBALL、TheSportsDB 等（apiKey + 配额）。

---

## Government & Open Data（政府 / 开放数据）

- **美国**：Data.gov、Census、USASpending 等（部分需 apiKey）。
- **欧盟/各国**：各国 Open Government 条目；注意语言与坐标系。
- **台湾**：`Open Government, Taiwan` 条目链接 data.gov.tw。

---

## Email / URL Shorteners

- **邮箱校验/一次性邮箱**：Kickbox、Mail.tm、Guerrilla Mail 等（Auth 各异）。
- **短链**：Shrtco、CleanURI、Kutt 等；**自定义域名**看 Rebrandly、Bitly（OAuth/apiKey）。

---

## 选型失败时的动作

1. 在 GitHub 仓库 `README.md` 对应 **Index** 小节换同分类下另一条。
2. 搜索服务商状态页（是否弃用）。
3. 若需稳定性，优先考虑**有 SLA 的商业 API** 而非个人 hobby 部署。
