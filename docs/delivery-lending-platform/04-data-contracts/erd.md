# ERD（核心域）— Lending Digital Platform

> 本 ERD 只覆盖新建的 `fin_*` 业务域表；`system_*`（用户/权限/OAuth2）与 `infra_*`（文件等）视作既有依赖。

```mermaid
erDiagram
  FIN_CUSTOMER ||--o| FIN_CUSTOMER_PROFILE : has
  FIN_CUSTOMER ||--o{ FIN_CUSTOMER_WECHAT_BIND : binds
  FIN_CUSTOMER ||--o{ FIN_AUTH_CONSENT : grants

  FIN_CUSTOMER ||--o{ FIN_LOAN_APPLICATION : applies
  FIN_LOAN_APPLICATION ||--o{ FIN_APPLICATION_MATERIAL : uploads
  FIN_LOAN_APPLICATION ||--o{ FIN_LOAN_APPLICATION_TIMELINE : progresses
  FIN_LOAN_APPLICATION ||--o{ FIN_CREDIT_ASSESSMENT : assessed

  FIN_LOAN_APPLICATION ||--o| FIN_LOAN_ACCOUNT : becomes
  FIN_CUSTOMER ||--o{ FIN_LOAN_ACCOUNT : owns

  FIN_LOAN_ACCOUNT ||--o{ FIN_POSTLOAN_ALERT : triggers
  FIN_CUSTOMER ||--o{ FIN_POSTLOAN_ALERT : relates

  FIN_POSTLOAN_ALERT ||--o| FIN_COLLECTION_CASE : opens
  FIN_LOAN_ACCOUNT ||--o{ FIN_COLLECTION_CASE : collected
  FIN_COLLECTION_CASE ||--o{ FIN_COLLECTION_ACTION : actions

  FIN_REG_REPORT_TASK }o--o| INFRA_FILE : exports
  FIN_APPLICATION_MATERIAL }o--|| INFRA_FILE : references

  FIN_CUSTOMER {
    int8 id PK
    int8 user_id "system_oauth2_access_token.user_id (MEMBER)"
    varchar mobile_enc
    varchar mobile_masked
    varchar id_no_enc
    varchar id_no_masked
    int2 status
    int2 deleted
    int8 tenant_id
    timestamp create_time
    timestamp update_time
  }

  FIN_LOAN_APPLICATION {
    int8 id PK
    varchar application_no UK
    int8 customer_id FK
    int2 status
    numeric amount_requested
    int8 owner_user_id "system_users.id"
    int2 deleted
    int8 tenant_id
    timestamp create_time
    timestamp update_time
  }

  FIN_APPLICATION_MATERIAL {
    int8 id PK
    int8 application_id FK
    int8 file_id "infra_file.id"
    varchar material_type
    int2 status
    int2 deleted
    int8 tenant_id
  }

  FIN_LOAN_ACCOUNT {
    int8 id PK
    varchar loan_no UK
    int8 application_id FK
    int8 customer_id FK
    numeric principal
    int2 status
    int2 deleted
    int8 tenant_id
  }

  FIN_POSTLOAN_ALERT {
    int8 id PK
    varchar alert_no UK
    int8 customer_id FK
    int8 loan_account_id FK
    int2 alert_level
    varchar alert_type
    int2 status
    int2 deleted
    int8 tenant_id
  }

  FIN_COLLECTION_CASE {
    int8 id PK
    varchar case_no UK
    int8 customer_id FK
    int8 loan_account_id FK
    int8 source_alert_id FK
    int2 status
    int2 deleted
    int8 tenant_id
  }
```

## 关联既有表（不在本 ERD DDL 中创建）

- `system_users`: 后台用户（OWNER/经办人/催收人员等）
- `system_oauth2_access_token`: Token 校验与 `user_id/user_type`（MEMBER/ADMIN）
- `infra_file`: 文件元数据（材料/报送导出文件引用）

