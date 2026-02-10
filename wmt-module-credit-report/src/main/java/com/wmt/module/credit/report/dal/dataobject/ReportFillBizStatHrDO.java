package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 报表填报 - 经营情况（人员口径）DO（1:1 关联主记录）
 *
 * @author AHC源码
 */
@TableName("report_fill_biz_stat_hr")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillBizStatHrDO extends BaseDO {

    /**
     * 主键（UUID）
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 父记录id（= report_fill_basic_info.id）
     */
    private String parentId;

    /**
     * 员工总人数
     */
    private BigDecimal employeeTotal;

    /**
     * 从事征信业务员工数
     */
    private BigDecimal employeeCreditBiz;

}

