package com.wmt.module.credit.report.service;

import com.wmt.module.credit.report.controller.admin.report.vo.*;

/**
 * 季报（小微贷款）Service
 */
public interface ReportQuarterMicroLoanService {

    /**
     * 获取/初始化当季填报数据（如不存在则创建 basic_info 及 1:1 业务表空记录）
     */
    ReportQuarterMicroLoanRespVO getOrInit(ReportQuarterMicroLoanGetReqVO reqVO);

    /**
     * 保存当季发生额字段
     */
    Boolean save(ReportQuarterMicroLoanSaveReqVO reqVO);

    /**
     * 展示统计（当季/年累计/同比增长量/同比增长率）
     */
    ReportQuarterMicroLoanStatRespVO stat(ReportQuarterMicroLoanStatReqVO reqVO);

    /**
     * 数据库收录小微企业户数统计（当季/累计/同比增长量/同比增长率）
     */
    ReportQuarterMicroLoanCollectMicroSmeStatRespVO statCollectMicroSme(ReportQuarterMicroLoanCollectMicroSmeStatReqVO reqVO);

    /**
     * 为放贷机构提供小微企业征信服务次数统计（当季/累计/同比增长量/同比增长率）
     */
    ReportQuarterMicroLoanServiceCountStatRespVO statServiceCountTotal(ReportQuarterMicroLoanServiceCountStatReqVO reqVO);
}

