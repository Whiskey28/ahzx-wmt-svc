package com.wmt.module.credit.report.service;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.report.controller.admin.report.vo.JimuReportTemplateRespVO;
import com.wmt.module.credit.report.controller.admin.report.vo.JimuReportCategoryRespVO;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportFillRecordEditUrlRespVO;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportFillRecordPageReqVO;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportFillRecordRespVO;

import java.util.List;

/**
 * 报表填报 Service 接口
 *
 * @author AHC源码
 */
public interface ReportFillService {

    /**
     * 查询积木报表分类列表（只返回 id、name、parentId）
     */
    List<JimuReportCategoryRespVO> getCategoryList();

    /**
     * 根据分类id查询报表模板列表
     *
     * @param categoryId 分类id
     * @param submitForm 是否填报报表（0-数据报表，1-在线填报表单）。如果为null，默认查询在线填报表单（submitForm=1）
     * @return 报表模板列表
     */
    List<JimuReportTemplateRespVO> getTemplateListByCategoryId(String categoryId, Integer submitForm);

    /**
     * 分页查询填报记录
     *
     * @param pageReqVO 查询条件
     * @return 分页结果
     */
    PageResult<ReportFillRecordRespVO> getFillRecordPage(ReportFillRecordPageReqVO pageReqVO);

    /**
     * 根据记录id获取编辑URL信息
     *
     * @param recordId 记录id
     * @return 编辑URL信息
     */
    ReportFillRecordEditUrlRespVO getEditUrlByRecordId(String recordId);

    /**
     * 物理删除填报记录
     *
     * @param id 记录id
     * @return 是否删除成功
     */
    Boolean deleteFillRecord(String id);

}
