package com.wmt.module.credit.report.service;

import com.wmt.module.credit.report.controller.admin.report.vo.*;

/**
 * 积木报表数据接口 Service
 *
 * 专用于为积木报表提供 API 数据集，避免在报表里编写复杂 SQL。
 *
 * @author Auto
 */
public interface JimuReportDataService {

    /**
     * 信息来源情况（银行、证券、保险等行业）数据集
     *
     * @param reqVO 查询条件
     * @return 行业明细及汇总
     */
    JmReportInfoSourceStatusRespVO getInfoSourceStatus(JmReportInfoSourceStatusReqVO reqVO);

    /**
     * 产品与服务提供情况（按行业）数据集
     *
     * @param reqVO 查询条件
     * @return 行业明细及汇总
     */
    JmReportServiceByIndustryRespVO getServiceByIndustry(JmReportServiceByIndustryReqVO reqVO);

    /**
     * 产品与服务提供情况（按行业）当年提供次数（字段展开）
     *
     * 按 periodId + reportId +（数据管理中心/普惠/企信）角色聚合，返回 bank/security/.../other 等字段。
     */
    JmReportServiceByIndustryYearCountFieldsRespVO getServiceByIndustryYearCountFields(JmReportServiceByIndustryReqVO reqVO);

    /**
     * 产品与服务提供情况（按行业）统计（来自表单填写）
     *
     * - 机构总累计/当前使用：市场部表单（政府取数据部表单）
     * - 当年提供次数：数据部+企信部+普惠部聚合
     */
    JmReportServiceByIndustryFormStatRespVO getServiceByIndustryFormStat(JmReportServiceByIndustryReqVO reqVO);

    /**
     * 产品与服务提供情况（按行业）总计
     *
     * @param reqVO 查询条件
     * @return 总计数据
     */
    JmReportServiceByIndustryTotalRespVO getServiceByIndustryTotal(JmReportServiceByIndustryReqVO reqVO);

    /**
     * 提供的征信产品(服务)数据集
     *
     * @param reqVO 查询条件
     * @return 产品明细及汇总
     */
    JmReportProductServiceRespVO getProductService(JmReportProductServiceReqVO reqVO);

    /**
     * 查询产品与服务提供情况（industry_code 字典）的所有字典项
     *
     * @return 字典项简单列表
     */
    java.util.List<JmDictItemSimpleRespVO> getIndustryCodeDictItems();

    /**
     * 信息使用者机构下拉树数据集（用于积木报表下拉树组件）：
     * 1) 一级节点：固定为“产品与服务提供情况”；
     * 2) 二级节点：行业分类（industry_code 字典项）；
     * 3) 三级节点：各行业下的机构名称（org_name 或 gov_org_name），按 sortNo 排序。
     *
     * @return 下拉树数据
     */
    JmReportInfoUserTreeRespVO getInfoUserTree();

    /**
     * 信息使用者机构下拉树数据集（测试版）：
     * 使用纯数字 ID 与 parentId，验证积木报表下拉树组件在数字主键场景下的兼容性。
     *
     * @return 下拉树测试数据
     */
    JmReportInfoUserTreeRespVO getInfoUserTreeTest();

    /**
     * 产品与服务提供情况 - 下半部分（提供的征信产品/服务次数）三项合计
     *
     * 统计规则：
     * 1) 从 report_fill_product_stat 聚合 report_year_count / credit_year_count / anti_year_count；
     * 2) 过滤条件：period_id + 报表ID集合 + 角色集合（创新研发中心 + 企业信用部 + 普惠信用部）；
     * 3) 每个字段的最终值 = 汇总求和 + 3000 × 当前月数。
     * 4) report_year_count+长三角征信链补充次数
     */
    JmReportProductServiceTotalRespVO getProductServiceTotal(JmReportProductServiceReqVO reqVO);
}
