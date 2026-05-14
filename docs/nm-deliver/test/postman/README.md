# Postman — PFB 一期

## 导入

1. Postman：**Import** → 选择本目录下的 `WMT-PFB-api.postman_collection.json`。
2. 打开集合 **Variables**，按需修改：
   - `baseUrl`：默认 `http://127.0.0.1:48080/pfb-api`
   - `mobile` / `smsCode`：与本地短信或固定验证码一致
   - 调用登录或注册成功后，将响应中的 `accessToken`、`refreshToken` 粘贴到集合变量
   - 调用「新增企业并绑定」后，将返回的 `data`（企业 id）粘贴到 `entId`

## 与文档/SQL 对齐

- 自测清单与 curl：`../2026-05-14-pfb-dev-self-test-checklist.md`
- 种子数据：`../sql/pfb-self-test-data-seed.sql`

## 说明

- 开启 `wmt.captcha.enable=true` 时，登录/注册等请求需按 Swagger 补充 `captchaVerification` 等字段；本集合默认按 **未开启图形验证码** 编写。
- 文件夹 **03/04/05** 使用集合级 Bearer `{{accessToken}}`；若导入后子请求未继承鉴权，可在对应文件夹上检查 **Authorization** 是否为 **Inherit auth from parent**。
