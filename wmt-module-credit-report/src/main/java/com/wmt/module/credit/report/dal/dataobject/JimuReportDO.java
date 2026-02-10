package com.wmt.module.credit.report.dal.dataobject;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 积木报表模板 DO
 *
 * @author AHC源码
 */
@TableName("jimu_report")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JimuReportDO implements Serializable {

    /**
     * 主键
     */
    @TableId
    private String id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 说明
     */
    private String note;

    /**
     * 状态
     */
    private String status;

    /**
     * 类型（关联 jimu_report_category.id）
     */
    private String type;

    /**
     * json字符串
     */
    private String jsonStr;

    /**
     * 请求地址
     */
    private String apiUrl;

    /**
     * 缩略图
     */
    private String thumb;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String updateBy;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 删除标识0-正常,1-已删除
     */
    private Integer delFlag;

    /**
     * 请求方法0-get,1-post
     */
    private String apiMethod;

    /**
     * 请求编码
     */
    private String apiCode;

    /**
     * 是否是模板 0-是,1-不是
     */
    private Integer template;

    /**
     * 浏览次数
     */
    private Long viewCount;

    /**
     * css增强
     */
    private String cssStr;

    /**
     * js增强
     */
    private String jsStr;

    /**
     * py增强
     */
    private String pyStr;

    /**
     * 多租户标识
     */
    private String tenantId;

    /**
     * 乐观锁版本
     */
    private Integer updateCount;

    /**
     * 是否填报报表 0不是,1是
     */
    private Integer submitForm;

}
