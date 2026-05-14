-- PFB 模块单测占位表（H2 初始化）；业务表访问在测试中多以 Mockito 替身 Mapper 完成。
CREATE TABLE IF NOT EXISTS pfb_unit_test_placeholder (
    id BIGINT NOT NULL PRIMARY KEY
);
