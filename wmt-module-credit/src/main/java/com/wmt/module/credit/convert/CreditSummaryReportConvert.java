package com.wmt.module.credit.convert;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportExcelVO;
import com.wmt.module.credit.controller.admin.report.vo.CreditSummaryReportRespVO;
import com.wmt.module.credit.dal.dataobject.report.CreditSummaryReportDO;
import com.wmt.module.credit.enums.CreditReportStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 征信汇总报表 Convert
 *
 * @author AHC源码
 */
@Mapper
public interface CreditSummaryReportConvert {

    CreditSummaryReportConvert INSTANCE = Mappers.getMapper(CreditSummaryReportConvert.class);

    CreditSummaryReportRespVO convert(CreditSummaryReportDO bean);

    List<CreditSummaryReportRespVO> convertList(List<CreditSummaryReportDO> list);

    PageResult<CreditSummaryReportRespVO> convertPage(PageResult<CreditSummaryReportDO> page);

    /**
     * 转换为Excel VO
     * 注意：需要手动设置状态名称
     */
    default CreditSummaryReportExcelVO convertExcel(CreditSummaryReportDO bean) {
        if (bean == null) {
            return null;
        }
        CreditSummaryReportExcelVO excelVO = new CreditSummaryReportExcelVO();
        excelVO.setId(bean.getId());
        excelVO.setReportPeriod(bean.getReportPeriod());
        excelVO.setReportType(bean.getReportType());
        CreditReportStatusEnum statusEnum = CreditReportStatusEnum.fromStatus(bean.getStatus());
        excelVO.setStatusName(statusEnum != null ? statusEnum.getName() : null);
        excelVO.setGenerateTime(bean.getGenerateTime());
        excelVO.setCreateTime(bean.getCreateTime());
        return excelVO;
    }

    /**
     * 批量转换为Excel VO列表
     */
    default List<CreditSummaryReportExcelVO> convertExcelList(List<CreditSummaryReportDO> list) {
        if (list == null || list.isEmpty()) {
            return List.of();
        }
        return list.stream()
                .map(this::convertExcel)
                .toList();
    }

}
