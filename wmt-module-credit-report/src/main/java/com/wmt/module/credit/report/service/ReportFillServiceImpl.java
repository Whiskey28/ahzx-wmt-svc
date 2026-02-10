package com.wmt.module.credit.report.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.common.util.collection.CollectionUtils;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.credit.report.controller.admin.report.vo.JimuReportCategoryRespVO;
import com.wmt.module.credit.report.controller.admin.report.vo.JimuReportTemplateRespVO;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportFillRecordEditUrlRespVO;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportFillRecordPageReqVO;
import com.wmt.module.credit.report.controller.admin.report.vo.ReportFillRecordRespVO;
import com.wmt.module.credit.report.dal.dataobject.JimuReportCategoryDO;
import com.wmt.module.credit.report.dal.dataobject.JimuReportDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBasicInfoDO;
import com.wmt.module.credit.report.dal.mysql.JimuReportCategoryMapper;
import com.wmt.module.credit.report.dal.mysql.JimuReportMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillBasicInfoMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 报表填报 Service 实现类
 *
 * @author AHC源码
 */
@Service
@Slf4j
public class ReportFillServiceImpl implements ReportFillService {

    @Resource
    private JimuReportMapper jimuReportMapper;

    @Resource
    private JimuReportCategoryMapper jimuReportCategoryMapper;

    @Resource
    private ReportFillBasicInfoMapper reportFillBasicInfoMapper;

    @Override
    public List<JimuReportCategoryRespVO> getCategoryList() {
        List<JimuReportCategoryDO> list = jimuReportCategoryMapper.selectList(
                new LambdaQueryWrapper<JimuReportCategoryDO>()
                        .eq(JimuReportCategoryDO::getDelFlag, 0)
                        .orderByAsc(JimuReportCategoryDO::getSortNo)
        );
        return BeanUtils.toBean(list, JimuReportCategoryRespVO.class);
    }

    @Override
    public List<JimuReportTemplateRespVO> getTemplateListByCategoryId(String categoryId, Integer submitForm) {
        // 如果未指定submitForm，默认查询在线填报表单（submitForm=1），保持向后兼容
        Integer actualSubmitForm = submitForm != null ? submitForm : 1;
        // 查询该分类下的报表模板列表
        List<JimuReportDO> templateList = jimuReportMapper.selectListByCategoryId(categoryId, actualSubmitForm);
        return BeanUtils.toBean(templateList, JimuReportTemplateRespVO.class);
    }

    @Override
    public PageResult<ReportFillRecordRespVO> getFillRecordPage(ReportFillRecordPageReqVO pageReqVO) {
        // 1. 处理报表ID查询条件（分类ID、报表ID、报表名称的组合逻辑）
        List<String> reportIds = null;
        
        // 1.1 如果传入了报表分类id，查询该分类下的所有报表id
        if (pageReqVO.getCategoryId() != null && !pageReqVO.getCategoryId().trim().isEmpty()) {
            List<JimuReportDO> reports = jimuReportMapper.selectListByCategoryId(pageReqVO.getCategoryId());
            if (reports.isEmpty()) {
                // 如果该分类下没有报表，返回空结果
                return PageResult.empty();
            }
            reportIds = CollectionUtils.convertList(reports, JimuReportDO::getId);
        }

        // 1.2 如果传入了报表模板名称，查询匹配的报表id
        if (pageReqVO.getReportName() != null && !pageReqVO.getReportName().trim().isEmpty()) {
            LambdaQueryWrapper<JimuReportDO> wrapper = new LambdaQueryWrapper<JimuReportDO>()
                    .like(JimuReportDO::getName, pageReqVO.getReportName())
                    .eq(JimuReportDO::getDelFlag, 0);
            // 如果已经有分类id，则同时按分类过滤
            if (pageReqVO.getCategoryId() != null && !pageReqVO.getCategoryId().trim().isEmpty()) {
                wrapper.eq(JimuReportDO::getType, pageReqVO.getCategoryId());
            }
            List<JimuReportDO> reports = jimuReportMapper.selectList(wrapper);
            if (reports.isEmpty()) {
                // 如果没有找到匹配的报表，返回空结果
                return PageResult.empty();
            }
            List<String> nameMatchedReportIds = CollectionUtils.convertList(reports, JimuReportDO::getId);
            
            // 如果之前已经有分类过滤的报表ID列表，取交集；否则使用名称匹配的报表ID列表
            if (reportIds != null) {
                reportIds = CollectionUtils.filterList(reportIds, nameMatchedReportIds::contains);
                if (reportIds.isEmpty()) {
                    // 分类和名称的交集为空，返回空结果
                    return PageResult.empty();
                }
            } else {
                reportIds = nameMatchedReportIds;
            }
        }

        // 1.3 如果传入了报表ID，需要验证是否与分类/名称匹配，并验证是否未删除
        if (pageReqVO.getReportId() != null && !pageReqVO.getReportId().trim().isEmpty()) {
            // 验证报表是否存在且未删除
            JimuReportDO report = jimuReportMapper.selectOne(new LambdaQueryWrapper<JimuReportDO>()
                    .eq(JimuReportDO::getId, pageReqVO.getReportId())
                    .eq(JimuReportDO::getDelFlag, 0));
            if (report == null) {
                // 报表不存在或已删除，返回空结果
                return PageResult.empty();
            }
            
            if (reportIds != null) {
                // 如果之前有过滤条件，验证报表ID是否在过滤结果中
                if (!reportIds.contains(pageReqVO.getReportId())) {
                    // 报表ID不在过滤结果中，返回空结果
                    return PageResult.empty();
                }
            }
            // 如果验证通过，只查询指定的报表ID
            reportIds = List.of(pageReqVO.getReportId());
        }

        // 2. 分页查询填报主记录（只查询未删除的记录）
        PageResult<ReportFillBasicInfoDO> pageResult = reportFillBasicInfoMapper.selectPage(
                pageReqVO, pageReqVO, reportIds);

        // 4. 如果查询结果为空，直接返回
        List<ReportFillBasicInfoDO> records = pageResult.getList();
        if (records.isEmpty()) {
            return PageResult.empty();
        }

        // 5. 收集所有报表ID，批量查询报表模板信息（只查询未删除的）
        List<String> queryReportIds = CollectionUtils.convertList(records, ReportFillBasicInfoDO::getReportId,
                record -> record.getReportId() != null && !record.getReportId().trim().isEmpty());
        Map<String, JimuReportDO> reportMap = queryReportIds.isEmpty() ? Map.of() :
                CollectionUtils.convertMap(
                        jimuReportMapper.selectList(new LambdaQueryWrapper<JimuReportDO>()
                                .in(JimuReportDO::getId, queryReportIds)
                                .eq(JimuReportDO::getDelFlag, 0)),
                        JimuReportDO::getId);

        // 6. 收集所有分类ID，批量查询分类信息（只查询未删除的）
        List<String> categoryIds = CollectionUtils.convertList(reportMap.values(), JimuReportDO::getType,
                report -> report.getType() != null && !report.getType().trim().isEmpty());
        Map<String, JimuReportCategoryDO> categoryMap = categoryIds.isEmpty() ? Map.of() :
                CollectionUtils.convertMap(
                        jimuReportCategoryMapper.selectList(new LambdaQueryWrapper<JimuReportCategoryDO>()
                                .in(JimuReportCategoryDO::getId, categoryIds)
                                .eq(JimuReportCategoryDO::getDelFlag, 0)),
                        JimuReportCategoryDO::getId);

        // 7. 转换为VO并填充关联数据
        List<ReportFillRecordRespVO> voList = CollectionUtils.convertList(records, record -> {
            ReportFillRecordRespVO vo = BeanUtils.toBean(record, ReportFillRecordRespVO.class);
            
            // 填充报表模板信息
            JimuReportDO report = reportMap.get(record.getReportId());
            if (report != null) {
                vo.setReportName(report.getName());
                vo.setReportCode(report.getCode());
                vo.setReportNote(report.getNote());
                
                // 填充分类信息
                JimuReportCategoryDO category = categoryMap.get(report.getType());
                if (category != null) {
                    vo.setCategoryId(category.getId());
                    vo.setCategoryName(category.getName());
                }
            }
            
            return vo;
        });

        return new PageResult<>(voList, pageResult.getTotal());
    }

    @Override
    public ReportFillRecordEditUrlRespVO getEditUrlByRecordId(String recordId) {
        // 1. 查询记录信息
        ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectById(recordId);
        if (record == null) {
            throw exception(com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_FOUND,
                    "填报记录不存在");
        }

        // 2. 校验报表id是否存在
        if (record.getReportId() == null || record.getReportId().trim().isEmpty()) {
            throw exception(com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants.BAD_REQUEST,
                    "填报记录未关联报表模板，无法生成编辑URL");
        }

        // 3. 构建URL信息
        ReportFillRecordEditUrlRespVO respVO = new ReportFillRecordEditUrlRespVO();
        respVO.setReportId(record.getReportId());
        respVO.setRecordId(record.getId());
        respVO.setEditUrl(String.format("/jmreport/view/%s/edit/%s", record.getReportId(), record.getId()));
        respVO.setViewUrl(String.format("/jmreport/view/%s", record.getReportId()));

        return respVO;
    }

    @Override
    public Boolean deleteFillRecord(String id) {
        // 1. 校验记录是否存在
        ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectById(id);
        if (record == null) {
            throw exception(com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_FOUND,
                    "填报记录不存在");
        }

        // 2. 逻辑删除主记录（MyBatis-Plus 会自动处理 deleted 字段）
        return reportFillBasicInfoMapper.deleteById(id) > 0;
    }

}
