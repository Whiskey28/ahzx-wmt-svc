package com.wmt.module.credit.framework.calculator;

import com.wmt.framework.test.core.ut.BaseDbUnitTest;
import com.wmt.module.credit.dal.dataobject.calculation.CreditCalculationRuleDO;
import com.wmt.module.credit.service.calculation.CreditCalculationRuleService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@link CreditCalculationEngine} 的最小可运行单元测试示例
 *
 * 覆盖典型场景：
 * - 单条规则：计算 deptFormData 中字段的求和
 *
 * 依赖：
 * - CreditCalculationRuleService 通过 @MockBean 提供规则
 * - CreditCalculationContextBuilder 真实构建上下文（可在内部从 Service 获取数据，这里只验证自定义函数和表达式执行流程）
 *
 * @author AHC源码
 */
@Import({CreditCalculationEngine.class, CreditCalculationContextBuilder.class})
public class CreditCalculationEngineTest extends BaseDbUnitTest {

    @Resource
    private CreditCalculationEngine calculationEngine;

    @MockBean
    private CreditCalculationRuleService calculationRuleService;

    @MockBean
    private com.wmt.module.credit.service.form.CreditFormDataService formDataService;

    @MockBean
    private com.wmt.module.credit.service.report.CreditSummaryReportService summaryReportService;

    @Test
    public void testCalculateSummaryReport_simpleRule() {
        String reportPeriod = "2025-01";
        String reportType = "MONTHLY";

        // 1. mock 规则：targetFieldCode = totalAssets, 表达式 = "#sum(#dept1['assets'], #dept2['assets'])"
        CreditCalculationRuleDO rule = new CreditCalculationRuleDO();
        rule.setTargetFieldCode("totalAssets");
        rule.setReportType(reportType);
        rule.setRuleExpression("#sum(#dept1['assets'], #dept2['assets'])");

        Mockito.when(calculationRuleService.getRulesByReportType(reportType))
                .thenReturn(List.of(rule));

        // 2. mock 上下文数据：dept1.assets = 100, dept2.assets = 200
        Map<String, Object> dept1Data = Map.of("assets", new BigDecimal("100"));
        Map<String, Object> dept2Data = Map.of("assets", new BigDecimal("200"));
        Map<Long, Map<String, Object>> deptFormDataMap = Map.of(
                1L, dept1Data,
                2L, dept2Data
        );

        Mockito.when(formDataService.getDeptFormDataMap(reportPeriod, reportType))
                .thenReturn(deptFormDataMap);

        // 3. 调用计算引擎
        Map<String, Object> result = calculationEngine.calculateSummaryReport(reportPeriod, reportType);

        // 4. 断言结果：totalAssets = 300
        BigDecimal totalAssets = (BigDecimal) result.get("totalAssets");
        assertEquals(new BigDecimal("300"), totalAssets);
    }

}

