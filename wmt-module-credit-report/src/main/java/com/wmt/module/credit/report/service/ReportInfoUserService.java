package com.wmt.module.credit.report.service;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.report.controller.admin.report.vo.*;

import java.util.List;

/**
 * 信息使用者机构相关 Service
 *
 * @author Auto
 */
public interface ReportInfoUserService {

    // ========== 机构明细 ==========

    PageResult<ReportInfoUserOrgRespVO> getOrgPage(ReportInfoUserOrgPageReqVO pageReqVO);

    String createOrg(ReportInfoUserOrgSaveReqVO reqVO);

    Boolean updateOrg(ReportInfoUserOrgUpdateReqVO reqVO);

    Boolean deleteOrg(String id);

    Boolean deleteOrgBatch(List<String> ids);

    // ========== 政府机构明细 ==========

    PageResult<ReportInfoUserGovRespVO> getGovPage(ReportInfoUserGovPageReqVO pageReqVO);

    String createGov(ReportInfoUserGovSaveReqVO reqVO);

    Boolean updateGov(ReportInfoUserGovUpdateReqVO reqVO);

    Boolean deleteGov(String id);

    Boolean deleteGovBatch(List<String> ids);

    // ========== 按行业统计（全局，不再区分 recordId） ==========

    ReportInfoUserStatByIndustryRespVO getStatByIndustry();
}

