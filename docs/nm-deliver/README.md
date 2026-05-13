# 内蒙古银行普惠金融需求 — 交付文档索引

本目录集中存放与该需求相关的持久化交付物（含 `superpowers/` 子目录下的实现计划），避免分散在仓库其他 `docs/` 路径。

| 内容 | 路径 |
|------|------|
| WMT 框架 JDK17 源码根（CommonResult、PageParam、BaseDO、BaseMapperX 等；与业务仓分离克隆） | 本机示例：`/Users/whiskey/Projects/Github/Whiskey1028/wmt-framework/wmt-framework-jdk17`；关键类 `file://` 链接见一期计划 |
| 原始需求规格说明书（Word） | `0508-1.doc`、`0508-1.docx` |
| 加固 PRD（用户故事 / 验收 / 图；**已与 v0 DDL 同步 Banner、企业码等**） | `PRD-普惠金融数字化系统-小程序-需求加固-V0.1.md` |
| 核心表结构 DDL（`pfb_` + UUID；自然人 `pfb_individual_customer`；Banner `pfb_banner`；申请含 `ent_id` 与企业快照） | `docs/nm-deliver/sql/pfb-core-schema-v0.sql`（副本：`sql/mysql/pfb-core-schema-v0.sql`） |
| 表结构优化与演进建议（MySQL） | `docs/nm-deliver/sql/PFB-SCHEMA-OPTIMIZATION.md` |
| 后端计划索引（指向一期 / 二期） | `superpowers/plans/2026-05-11-pfb-miniprogram-backend-mvp.md` |
| 后端 **一期** 任务（与 v0 DDL 对齐；含数据库初始化 Task 2） | `superpowers/plans/2026-05-11-pfb-phase1-backend-tasks.md` |
| 后端 **二期** 任务（检索、进度、幂等、运营等） | `superpowers/plans/2026-05-11-pfb-phase2-backend-tasks.md` |
