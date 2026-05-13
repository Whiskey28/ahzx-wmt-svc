# 普惠金融小程序进件端 — 二期后端任务（扩展能力 / 无独立 DDL 或待决策）

> **范围原则**：下列项在 **PRD 或运营** 中已出现需求苗头，但 **不属于** v0 `pfb_*` 表必须在一期完成的「最小闭环」；或依赖 **产品/安全定案** 后再改表/改接口。  
> **与一期关系**：一期完成后，数据库已具备 `pfb_loan_product.product_name`、`pfb_loan_application.status` 等字段，二期多在 **查询条件、索引、管理端、外围对接** 上扩展。  
> **PFB-API（C2）**：一期进件接口已统一在 **`/pfb-api`** + **`**.controller.pfb.**`**；二期新增能力若仍面向小程序，**默认仍在 `wmt-module-pfb` 的 `controller.pfb`**；管理端/运营端若走 **`/admin-api`**，可使用 `controller.admin` 或既有 system 模块，与 **`userType=PFB`** 的 C 端链路分离。集成说明见 [pfb-api-c2-framework-and-app-integration.md](../../../learning-roadmap/pfb-api-c2-framework-and-app-integration.md)。

---

## 1. 产品检索（PRD US-08）

- 在现有 **`GET /pfb-api/pfb/loan-product/list`**（路径以一期实现为准）上增加 **`productName`（可选）** 模糊查询。
- 评估索引：`LIKE %x%` 无法高效走 BTree；可选 **全文索引**、**前缀索引** 或 **Elastic**（见 `PFB-SCHEMA-OPTIMIZATION.md`）。

---

## 2. 申请进度（PRD US-12）

- 只读列表：`sys_user_id` + `ent_id`（及可选 `product_name` 过滤）；依赖一期索引 **`idx_pfb_loan_app_user_ent_time`**（`sys_user_id`, `ent_id`, `create_time`）。
- 详情：按申请 `id`（UUID）返回状态、快照字段、产品关联展示。
- **状态机**：与信贷/管理端对齐后再扩展 `status` 枚举及同步任务（可能需新列如 `external_apply_no`）。

---

## 3. 提交幂等与防重（`PFB-SCHEMA-OPTIMIZATION.md` §0.3）

- 定案后可选：`Idempotency-Key` 头、业务唯一约束、或短时防重表。

---

## 4. 运营与管理端（Banner / 产品 CRUD）

- **一期已交付**：小程序侧 **`pfb_banner` / `pfb_loan_product` 只读列表**（`/pfb-api` + `controller.pfb`）；匿名策略以一期 `permit-all_urls` 为准。
- **二期本节负责**：**Banner** 与 **贷款产品** 的 **CRUD**、上下架、生效时间维护、运营配置；若管理 UI 在本仓外，仍须约定接口契约与权限（通常 **`admin-api`** + `ADMIN` **或** 独立运营服务）。
- 数据字典与行方口径字段维护。

---

## 5. 登录 / 注册 / 短信 / 人脸（与 SRS 深度集成）

- **框架一期已落地**：内蒙小程序走 **`/pfb-api`**，Token **`userType = PFB`**，**`OAuth2TokenServiceImpl.buildUserInfo`** 对 **`UserTypeEnum.PFB`** 按 `userId` 拉取 **`system_users`** 组装 `LoginUser`（见业务仓改造与 [集成文档 §5.1](../../../learning-roadmap/pfb-api-c2-framework-and-app-integration.md)）。
- **二期本项**：在 PFB 登录链路上叠加 **短信验证码、注册补全、人脸核身** 等与行方 SRS 的深度集成；**注意** `permit-all_urls` 与 **`buildPfbApi`** 白名单随新匿名接口同步维护，避免 profile 覆盖丢失。

---

## 6. 信贷、ESB、对账字段

- 按行方接口目录扩展 `pfb_loan_application` 或旁路表；属 **二期+**。

---

*二期文档随 PRD/对接规格迭代更新；不以 v0 DDL 缺表为由阻塞一期闭环。*
