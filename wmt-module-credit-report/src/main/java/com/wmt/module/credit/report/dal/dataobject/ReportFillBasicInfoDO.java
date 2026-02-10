package com.wmt.module.credit.report.dal.dataobject;

import com.wmt.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 报表填报主记录 DO（作为填报锚点 record）
 *
 * @author AHC源码
 */
@TableName("report_fill_basic_info")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportFillBasicInfoDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 填报周期（月度）
     */
    private String periodId;

    /**
     * 角色id
     */
    private String roleId;

    /**
     * 项目id（可选，不参与唯一约束）
     */
    private String projectId;

    /**
     * 报表模板id（关联 jimu_report.id，用于映射关系）
     */
    private String reportId;

}
