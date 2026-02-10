package com.wmt.module.credit.framework.calculator;

import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import com.wmt.module.credit.dal.dataobject.report.CreditSummaryReportDO;
import com.wmt.module.credit.service.form.CreditFormDataService;
import com.wmt.module.credit.service.report.CreditSummaryReportService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 征信计算上下文构建器
 * 用于构建SpEL表达式的执行上下文
 *
 * @author AHC源码
 */
@Component
@Slf4j
public class CreditCalculationContextBuilder {

    @Resource
    private CreditFormDataService formDataService;

    @Resource
    private CreditSummaryReportService summaryReportService;

    /**
     * 构建计算上下文
     *
     * @param reportPeriod 报送周期
     * @param reportType   报表类型
     * @return SpEL执行上下文
     */
    public EvaluationContext buildContext(String reportPeriod, String reportType) {
        StandardEvaluationContext context = new StandardEvaluationContext();

        // 1. 注册自定义函数
        registerCustomFunctions(context);

        // 2. 加载各部门表单数据
        Map<Long, Map<String, Object>> deptFormDataMap = formDataService.getDeptFormDataMap(reportPeriod, reportType);
        context.setVariable("deptFormData", deptFormDataMap);
        // 为了方便访问，也设置每个部门的数据
        deptFormDataMap.forEach((deptId, formData) -> {
            context.setVariable("dept" + deptId, formData);
        });

        // 3. 如果是季报，加载相关月报数据
        if ("QUARTERLY".equals(reportType)) {
            loadMonthlyReports(context, reportPeriod);
        }

        // 4. 如果是季报，加载之前季报数据
        if ("QUARTERLY".equals(reportType)) {
            loadPreviousQuarterReport(context, reportPeriod);
        }

        return context;
    }

    /**
     * 注册自定义函数到SpEL上下文
     *
     * @param context SpEL上下文
     */
    private void registerCustomFunctions(StandardEvaluationContext context) {
        try {
            // 注册sum函数
            context.registerFunction("sum",
                    CreditCalculationFunctions.class.getDeclaredMethod("sum", java.util.Collection.class));
            // 注册avg函数
            context.registerFunction("avg",
                    CreditCalculationFunctions.class.getDeclaredMethod("avg", java.util.Collection.class));
            // 注册max函数
            context.registerFunction("max",
                    CreditCalculationFunctions.class.getDeclaredMethod("max", java.util.Collection.class));
            // 注册min函数
            context.registerFunction("min",
                    CreditCalculationFunctions.class.getDeclaredMethod("min", java.util.Collection.class));
            // 注册safeGet函数
            context.registerFunction("safeGet",
                    CreditCalculationFunctions.class.getDeclaredMethod("safeGet", Map.class, String.class));
            // 注册safeDivide函数（避免除零）
            context.registerFunction("safeDivide",
                    CreditCalculationFunctions.class.getDeclaredMethod("safeDivide", Number.class, Number.class));
        } catch (NoSuchMethodException e) {
            log.error("注册自定义函数失败", e);
        }
    }

    /**
     * 加载月报数据（用于季报计算）
     *
     * @param context      SpEL上下文
     * @param reportPeriod 季度周期（格式：2024-Q1）
     */
    private void loadMonthlyReports(StandardEvaluationContext context, String reportPeriod) {
        // 解析季度周期，获取对应的月份
        List<String> monthlyPeriods = parseQuarterToMonths(reportPeriod);
        Map<String, Map<String, Object>> monthlyReports = new HashMap<>();
        Map<String, Object> latestMonthlyReport = null;
        
        for (String monthlyPeriod : monthlyPeriods) {
            CreditSummaryReportDO monthlyReport = summaryReportService.getByPeriodAndType(monthlyPeriod, "MONTHLY");
            if (monthlyReport != null && monthlyReport.getReportData() != null) {
                monthlyReports.put(monthlyPeriod, monthlyReport.getReportData());
                // 最后一个月份就是当季最新月报
                latestMonthlyReport = monthlyReport.getReportData();
            }
        }
        
        // 设置月报Map（按月份索引）
        context.setVariable("monthlyReports", monthlyReports);
        
        // 设置当季最新月报（季度最后一个月的月报）
        if (latestMonthlyReport != null) {
            context.setVariable("latestMonthlyReport", latestMonthlyReport);
        } else {
            // 如果找不到月报，至少设置一个空的Map，避免SpEL表达式报错
            context.setVariable("latestMonthlyReport", new HashMap<>());
        }
    }

    /**
     * 加载之前季报数据
     *
     * @param context      SpEL上下文
     * @param reportPeriod 当前季度周期（格式：2024-Q1）
     */
    private void loadPreviousQuarterReport(StandardEvaluationContext context, String reportPeriod) {
        // 计算上一个季度周期
        String previousQuarter = calculatePreviousQuarter(reportPeriod);
        if (previousQuarter != null) {
            CreditSummaryReportDO previousReport = summaryReportService.getByPeriodAndType(previousQuarter, "QUARTERLY");
            if (previousReport != null && previousReport.getReportData() != null) {
                context.setVariable("previousQuarterReport", previousReport.getReportData());
            }
        }
    }

    /**
     * 解析季度周期为月份列表
     * 例如：2024-Q1 -> [2024-01, 2024-02, 2024-03]
     *
     * @param quarterPeriod 季度周期
     * @return 月份列表
     */
    private List<String> parseQuarterToMonths(String quarterPeriod) {
        // 解析格式：2024-Q1
        String[] parts = quarterPeriod.split("-Q");
        if (parts.length != 2) {
            return List.of();
        }
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        int startMonth = (quarter - 1) * 3 + 1;
        return List.of(
                String.format("%d-%02d", year, startMonth),
                String.format("%d-%02d", year, startMonth + 1),
                String.format("%d-%02d", year, startMonth + 2)
        );
    }

    /**
     * 计算上一个季度周期
     * 例如：2024-Q1 -> 2023-Q4
     *
     * @param currentQuarter 当前季度周期
     * @return 上一个季度周期
     */
    private String calculatePreviousQuarter(String currentQuarter) {
        String[] parts = currentQuarter.split("-Q");
        if (parts.length != 2) {
            return null;
        }
        int year = Integer.parseInt(parts[0]);
        int quarter = Integer.parseInt(parts[1]);
        if (quarter == 1) {
            return String.format("%d-Q4", year - 1);
        } else {
            return String.format("%d-Q%d", year, quarter - 1);
        }
    }

}
