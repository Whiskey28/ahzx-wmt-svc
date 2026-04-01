package com.wmt.module.credit.report.service;

import com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.framework.mq.redis.core.RedisMQTemplate;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBasicInfoDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoCollectStatDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillQuarterMicroLoanDO;
import com.wmt.module.credit.report.dal.mysql.ReportFillBasicInfoMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoCollectStatMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillQuarterMicroLoanMapper;
import com.wmt.module.credit.report.mq.message.ReportQuarterMicroLoanSavedStreamMessage;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 季报（小微贷款）Service 实现
 */
@Service
@Validated
@Slf4j
public class ReportQuarterMicroLoanServiceImpl implements ReportQuarterMicroLoanService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    @Resource
    private ReportFillBasicInfoMapper reportFillBasicInfoMapper;

    @Resource
    private ReportFillQuarterMicroLoanMapper reportFillQuarterMicroLoanMapper;

    @Resource
    private ReportFillInfoCollectStatMapper reportFillInfoCollectStatMapper;

    @Resource
    private JimuReportDataService jimuReportDataService;

    @Resource
    private RedisMQTemplate redisMQTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReportQuarterMicroLoanRespVO getOrInit(ReportQuarterMicroLoanGetReqVO reqVO) {
        validateQuarterPeriod(reqVO.getPeriodId());

        ReportFillBasicInfoDO record = reportFillBasicInfoMapper
                .selectLatestByPeriodAndReportAndRole(reqVO.getPeriodId(), reqVO.getReportId(), reqVO.getRoleId());
        if (record == null) {
            record = new ReportFillBasicInfoDO();
            record.setPeriodId(reqVO.getPeriodId());
            record.setRoleId(reqVO.getRoleId());
            record.setReportId(reqVO.getReportId());
            reportFillBasicInfoMapper.insert(record);
        }

        ReportFillQuarterMicroLoanDO data = reportFillQuarterMicroLoanMapper.selectOne(
                new LambdaQueryWrapperX<ReportFillQuarterMicroLoanDO>()
                        .eq(ReportFillQuarterMicroLoanDO::getParentId, record.getId())
        );
        if (data == null) {
            data = new ReportFillQuarterMicroLoanDO();
            data.setParentId(record.getId());
            reportFillQuarterMicroLoanMapper.insert(data);
        }

        ReportQuarterMicroLoanRespVO respVO = BeanUtils.toBean(data, ReportQuarterMicroLoanRespVO.class);
        respVO.setRecordId(record.getId());
        respVO.setPeriodId(record.getPeriodId());
        respVO.setRoleId(record.getRoleId());
        respVO.setReportId(record.getReportId());
        return respVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(ReportQuarterMicroLoanSaveReqVO reqVO) {
        validateQuarterPeriod(reqVO.getPeriodId());

        if (reqVO.getCreditGrantAmount() != null && reqVO.getGrantAmount() != null
                && reqVO.getCreditGrantAmount().compareTo(reqVO.getGrantAmount()) > 0) {
            throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "信用贷款金额不能大于获得贷款金额");
        }

        ReportFillBasicInfoDO record = reportFillBasicInfoMapper
                .selectLatestByPeriodAndReportAndRole(reqVO.getPeriodId(), reqVO.getReportId(), reqVO.getRoleId());
        if (record == null) {
            record = new ReportFillBasicInfoDO();
            record.setPeriodId(reqVO.getPeriodId());
            record.setRoleId(reqVO.getRoleId());
            record.setReportId(reqVO.getReportId());
            reportFillBasicInfoMapper.insert(record);
        }

        ReportFillQuarterMicroLoanDO exist = reportFillQuarterMicroLoanMapper.selectOne(
                new LambdaQueryWrapperX<ReportFillQuarterMicroLoanDO>()
                        .eq(ReportFillQuarterMicroLoanDO::getParentId, record.getId()));
        if (exist == null) {
            exist = new ReportFillQuarterMicroLoanDO();
            exist.setParentId(record.getId());
            exist.setApplyHouseholds(reqVO.getApplyHouseholds());
            exist.setGrantHouseholds(reqVO.getGrantHouseholds());
            exist.setGrantAmount(reqVO.getGrantAmount());
            exist.setCreditGrantAmount(reqVO.getCreditGrantAmount());
            exist.setAvgAnnualRatePct(reqVO.getAvgAnnualRatePct());
            exist.setNplRatePct(reqVO.getNplRatePct());
            reportFillQuarterMicroLoanMapper.insert(exist);
            publishSavedMessage(reqVO, record.getId(), "INSERT");
            return Boolean.TRUE;
        }

        exist.setApplyHouseholds(reqVO.getApplyHouseholds());
        exist.setGrantHouseholds(reqVO.getGrantHouseholds());
        exist.setGrantAmount(reqVO.getGrantAmount());
        exist.setCreditGrantAmount(reqVO.getCreditGrantAmount());
        exist.setAvgAnnualRatePct(reqVO.getAvgAnnualRatePct());
        exist.setNplRatePct(reqVO.getNplRatePct());
        reportFillQuarterMicroLoanMapper.updateById(exist);
        publishSavedMessage(reqVO, record.getId(), "UPDATE");
        return Boolean.TRUE;
    }

    @Override
    public ReportQuarterMicroLoanStatRespVO stat(ReportQuarterMicroLoanStatReqVO reqVO) {
        QuarterPeriod current = parseQuarterPeriod(reqVO.getPeriodId());

        // current
        MetricSnapshot currentSnap = snapshot(reqVO.getReportId(), reqVO.getRoleId(), current.toPeriodId());
        // last year same quarter
        MetricSnapshot lastYearSnap = snapshot(reqVO.getReportId(), reqVO.getRoleId(),
                new QuarterPeriod(current.getYear() - 1, current.getQuarter()).toPeriodId());
        // ytd（历史累计：过去所有年份、所有季度的当期发生额加和；仅统计 periodId <= 当前季度）
        MetricSnapshot ytdSnap = snapshotYtd(reqVO.getReportId(), reqVO.getRoleId(), current);

        ReportQuarterMicroLoanStatRespVO respVO = new ReportQuarterMicroLoanStatRespVO();
        respVO.setPeriodId(reqVO.getPeriodId());
        respVO.setRoleId(reqVO.getRoleId());
        respVO.setReportId(reqVO.getReportId());

        // 户数/金额类：当季、累计、同比增量、同比增速
        fillNumber(
                currentSnap.getApplyHouseholds(), ytdSnap.getApplyHouseholds(), lastYearSnap.getApplyHouseholds(),
                (c, ytd, delta, rate) -> {
                    respVO.setApplyHouseholdsCurrent(c);
                    respVO.setApplyHouseholdsYtd(ytd);
                    respVO.setApplyHouseholdsYoyDelta(delta);
                    respVO.setApplyHouseholdsYoyRate(rate);
                });

        fillNumber(
                currentSnap.getGrantHouseholds(), ytdSnap.getGrantHouseholds(), lastYearSnap.getGrantHouseholds(),
                (c, ytd, delta, rate) -> {
                    respVO.setGrantHouseholdsCurrent(c);
                    respVO.setGrantHouseholdsYtd(ytd);
                    respVO.setGrantHouseholdsYoyDelta(delta);
                    respVO.setGrantHouseholdsYoyRate(rate);
                });

        fillNumber(
                currentSnap.getGrantAmount(), ytdSnap.getGrantAmount(), lastYearSnap.getGrantAmount(),
                (c, ytd, delta, rate) -> {
                    respVO.setGrantAmountCurrent(c);
                    respVO.setGrantAmountYtd(ytd);
                    respVO.setGrantAmountYoyDelta(delta);
                    respVO.setGrantAmountYoyRate(rate);
                });

        fillNumber(
                currentSnap.getCreditGrantAmount(), ytdSnap.getCreditGrantAmount(), lastYearSnap.getCreditGrantAmount(),
                (c, ytd, delta, rate) -> {
                    respVO.setCreditGrantAmountCurrent(c);
                    respVO.setCreditGrantAmountYtd(ytd);
                    respVO.setCreditGrantAmountYoyDelta(delta);
                    respVO.setCreditGrantAmountYoyRate(rate);
                });

        // 比率：当季、累计（按金额加权）、同比增量（百分点差）、同比增速
        fillRatePct(
                currentSnap.getAvgAnnualRatePct(),
                calcYtdWeightedRatePct(current, reqVO.getReportId(), reqVO.getRoleId(),
                        currentSnap.getGrantAmount(), currentSnap.getAvgAnnualRatePct(),
                        RateType.AVG_ANNUAL_RATE_PCT),
                lastYearSnap.getAvgAnnualRatePct(),
                (c, ytd, delta, rate) -> {
                    respVO.setAvgAnnualRatePctCurrent(c);
                    respVO.setAvgAnnualRatePctYtd(ytd);
                    respVO.setAvgAnnualRatePctYoyDelta(delta);
                    respVO.setAvgAnnualRatePctYoyRate(rate);
                });

        fillRatePct(
                currentSnap.getNplRatePct(),
                calcYtdWeightedRatePct(current, reqVO.getReportId(), reqVO.getRoleId(),
                        currentSnap.getGrantAmount(), currentSnap.getNplRatePct(),
                        RateType.NPL_RATE_PCT),
                lastYearSnap.getNplRatePct(),
                (c, ytd, delta, rate) -> {
                    respVO.setNplRatePctCurrent(c);
                    respVO.setNplRatePctYtd(ytd);
                    respVO.setNplRatePctYoyDelta(delta);
                    respVO.setNplRatePctYoyRate(rate);
                });

        return respVO;
    }

    @Override
    public ReportQuarterMicroLoanCollectMicroSmeStatRespVO statCollectMicroSme(ReportQuarterMicroLoanCollectMicroSmeStatReqVO reqVO) {
        QuarterPeriod quarter = parseQuarterPeriod(reqVO.getPeriodId());

        // 口径（按你提供的说明）：
        // - 当期发生额 = 当季最新月 collect_micro_sme_total - 上一季度 collect_micro_sme_total
        // - 累计发生额 = 当季最新月 collect_micro_sme_total
        // - 同期增长量 = 当期发生额 - 去年同季度最新月的当期发生额
        // - 同期增长率 = 同期增长量 / 去年同季度最新月的当期发生额
        BigDecimal currentQuarterLatestMonthTotal = resolveLatestMonthCollectMicroSmeTotalInQuarter(
                reqVO.getReportId(), reqVO.getRoleId(), quarter);
        BigDecimal prevQuarterLatestMonthTotal = resolveLatestMonthCollectMicroSmeTotalInQuarter(
                reqVO.getReportId(), reqVO.getRoleId(), prevQuarter(quarter));
        BigDecimal current = currentQuarterLatestMonthTotal.subtract(prevQuarterLatestMonthTotal);

        BigDecimal ytd = currentQuarterLatestMonthTotal;

        QuarterPeriod lastYearQuarter = new QuarterPeriod(quarter.getYear() - 1, quarter.getQuarter());
        BigDecimal lastYearQuarterLatestMonthTotal = resolveLatestMonthCollectMicroSmeTotalInQuarter(
                reqVO.getReportId(), reqVO.getRoleId(), lastYearQuarter);
        BigDecimal lastYearPrevQuarterLatestMonthTotal = resolveLatestMonthCollectMicroSmeTotalInQuarter(
                reqVO.getReportId(), reqVO.getRoleId(), prevQuarter(lastYearQuarter));
        BigDecimal lastYearCurrent = lastYearQuarterLatestMonthTotal.subtract(lastYearPrevQuarterLatestMonthTotal);

        BigDecimal yoyDelta = current.subtract(lastYearCurrent);
        BigDecimal yoyRate = calcCollectMicroSmeYoyRate(yoyDelta, lastYearCurrent);

        ReportQuarterMicroLoanCollectMicroSmeStatRespVO respVO = new ReportQuarterMicroLoanCollectMicroSmeStatRespVO();
        respVO.setPeriodId(reqVO.getPeriodId());
        respVO.setRoleId(reqVO.getRoleId());
        respVO.setReportId(reqVO.getReportId());
        respVO.setCollectMicroSmeTotalCurrent(current);
        respVO.setCollectMicroSmeTotalYtd(ytd);
        respVO.setCollectMicroSmeTotalYoyDelta(yoyDelta);
        respVO.setCollectMicroSmeTotalYoyRate(yoyRate);
        return respVO;
    }

    /**
     * 同期增长率（stat-collect-micro-sme 专用）：
     * - 分母使用去年同期当期发生额的绝对值，避免出现“增长量为正但增长率为负”的符号反转
     * - 分母为 0 时返回 null
     */
    private BigDecimal calcCollectMicroSmeYoyRate(BigDecimal yoyDelta, BigDecimal lastYearCurrent) {
        BigDecimal denominator = nz(lastYearCurrent).abs();
        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        // 输出为“同比增速（%）”：(current - last) / last * 100
        return nz(yoyDelta)
                .divide(denominator, 10, RoundingMode.HALF_UP)
                .multiply(ONE_HUNDRED)
                .setScale(6, RoundingMode.HALF_UP);
    }

    @Override
    public ReportQuarterMicroLoanServiceCountStatRespVO statServiceCountTotal(ReportQuarterMicroLoanServiceCountStatReqVO reqVO) {
        QuarterPeriod quarter = parseQuarterPeriod(reqVO.getPeriodId());

        // 数据源：/credit/jmreport/data/service-by-industry/form-stat 中每月“总计”行的 totalYearServiceCount（当年累计）
        // 口径（按你提供的说明）：
        // - 当期发生额：Q1 = 当季最新月；其它季度 = 当季最新月 - 上季度最新月
        // - 累计发生额：当季最新月 + 过去每年12月（历年累计）的 totalYearServiceCount 之和
        // - 同期增长量：当期发生额 - 去年同季度当期发生额
        // - 同期增长率：同期增长量 / 去年同季度当期发生额（分母为 0 返回 null）
        BigDecimal currentQuarterLatestMonthTotal = resolveLatestMonthServiceCountTotalInQuarter(reqVO.getReportId(), quarter);
        BigDecimal prevQuarterLatestMonthTotal = resolveLatestMonthServiceCountTotalInQuarter(reqVO.getReportId(), prevQuarter(quarter));

        BigDecimal current = quarter.getQuarter() == 1
                ? currentQuarterLatestMonthTotal
                : currentQuarterLatestMonthTotal.subtract(prevQuarterLatestMonthTotal);

        BigDecimal cumulative = currentQuarterLatestMonthTotal.add(sumHistoricalDecTotals(reqVO.getReportId(), quarter.getYear()));

        QuarterPeriod lastYearQuarter = new QuarterPeriod(quarter.getYear() - 1, quarter.getQuarter());
        BigDecimal lastYearQuarterLatestMonthTotal = resolveLatestMonthServiceCountTotalInQuarter(reqVO.getReportId(), lastYearQuarter);
        BigDecimal lastYearPrevQuarterLatestMonthTotal = resolveLatestMonthServiceCountTotalInQuarter(reqVO.getReportId(), prevQuarter(lastYearQuarter));
        BigDecimal lastYearCurrent = lastYearQuarter.getQuarter() == 1
                ? lastYearQuarterLatestMonthTotal
                : lastYearQuarterLatestMonthTotal.subtract(lastYearPrevQuarterLatestMonthTotal);

        BigDecimal yoyDelta = current.subtract(lastYearCurrent);
        BigDecimal yoyRate = calcRate(yoyDelta, lastYearCurrent);

        ReportQuarterMicroLoanServiceCountStatRespVO respVO = new ReportQuarterMicroLoanServiceCountStatRespVO();
        respVO.setPeriodId(reqVO.getPeriodId());
        respVO.setReportId(reqVO.getReportId());
        respVO.setServiceCountTotalCurrent(current);
        respVO.setServiceCountTotalYtd(cumulative);
        respVO.setServiceCountTotalYoyDelta(yoyDelta);
        respVO.setServiceCountTotalYoyRate(yoyRate);
        return respVO;
    }

    private QuarterPeriod prevQuarter(QuarterPeriod quarter) {
        int year = quarter.getYear();
        int q = quarter.getQuarter() - 1;
        if (q <= 0) {
            year = year - 1;
            q = 4;
        }
        return new QuarterPeriod(year, q);
    }

    /**
     * 当季最新月的 collect_micro_sme_total（季度 3 个月中，从后往前取第一个有填报主记录的月份）。
     * 若季度三个月都无记录，则返回 0。
     */
    private BigDecimal resolveLatestMonthCollectMicroSmeTotalInQuarter(String reportId, String roleId, QuarterPeriod quarter) {
        int endMonth = quarter.getQuarter() * 3;
        int midMonth = endMonth - 1;
        int startMonth = endMonth - 2;
        for (int month : new int[]{endMonth, midMonth, startMonth}) {
            String monthPeriodId = toMonthPeriodId(quarter.getYear(), month);
            ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectLatestByPeriodAndReportAndRole(monthPeriodId, reportId, roleId);
            if (record == null) {
                continue;
            }
            ReportFillInfoCollectStatDO data = reportFillInfoCollectStatMapper.selectOne(
                    new LambdaQueryWrapperX<ReportFillInfoCollectStatDO>()
                            .eq(ReportFillInfoCollectStatDO::getParentId, record.getId()));
            if (data == null || data.getCollectMicroSmeTotal() == null) {
                return BigDecimal.ZERO;
            }
            return data.getCollectMicroSmeTotal();
        }
        return BigDecimal.ZERO;
    }

    private String toMonthPeriodId(int year, int month) {
        String m = month < 10 ? ("0" + month) : String.valueOf(month);
        return year + "-" + m;
    }

    /**
     * 当季最新月的 totalYearServiceCount（季度 3 个月中，从后往前取第一个非空值）。
     * 若季度三个月都无有效数据，则返回 0。
     */
    private BigDecimal resolveLatestMonthServiceCountTotalInQuarter(String reportId, QuarterPeriod quarter) {
        int endMonth = quarter.getQuarter() * 3;
        int midMonth = endMonth - 1;
        int startMonth = endMonth - 2;
        for (int month : new int[]{endMonth, midMonth, startMonth}) {
            String monthPeriodId = toMonthPeriodId(quarter.getYear(), month);
            BigDecimal value = resolveServiceCountTotalFromDb(reportId, monthPeriodId);
            // 这里将 0 视作“无有效数据”并回退到前一个月（服务次数通常不会为 0）
            if (value != null && value.compareTo(BigDecimal.ZERO) != 0) {
                return value;
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 历史累计：当季最新月（当年累计） + 过去每年12月的当年累计 之和。
     * 这里按 year-1 ... year-1 的 12 月累加；若某年缺数据则按 0 处理。
     */
    private BigDecimal sumHistoricalDecTotals(String reportId, int currentYear) {
        BigDecimal sum = BigDecimal.ZERO;
        int minYear = 2000; // 避免从 1 年开始导致无意义的超长循环
        for (int y = currentYear - 1; y >= minYear; y--) {
            BigDecimal dec = resolveServiceCountTotalFromDb(reportId, y + "-12");
            sum = sum.add(nz(dec));
        }
        return sum;
    }

    private BigDecimal resolveServiceCountTotalFromDb(String reportId, String monthPeriodId) {
        // 复用既有逻辑：取 form-stat 的最后“总计”行
        JmReportServiceByIndustryReqVO reqVO = new JmReportServiceByIndustryReqVO();
        reqVO.setReportId(reportId);
        reqVO.setPeriodId(monthPeriodId);
        JmReportServiceByIndustryFormStatRespVO resp = jimuReportDataService.getServiceByIndustryFormStat(reqVO);
        if (resp == null || resp.getItems() == null || resp.getItems().isEmpty()) {
            return BigDecimal.ZERO;
        }
        // 优先按 industryCode=total 找；否则取最后一行
        for (JmReportServiceByIndustryItemRespVO item : resp.getItems()) {
            if ("total".equals(item.getIndustryCode())) {
                return item.getYearServiceCount() == null ? BigDecimal.ZERO : item.getYearServiceCount();
            }
        }
        JmReportServiceByIndustryItemRespVO last = resp.getItems().get(resp.getItems().size() - 1);
        return last.getYearServiceCount() == null ? BigDecimal.ZERO : last.getYearServiceCount();
    }

    /**
     * 同比增速（%）：numerator / denominator * 100；denominator 为 0 返回 null
     */
    private BigDecimal calcRate(BigDecimal numerator, BigDecimal denominator) {
        if (denominator == null || denominator.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return nz(numerator)
                .divide(denominator, 10, RoundingMode.HALF_UP)
                .multiply(ONE_HUNDRED)
                .setScale(6, RoundingMode.HALF_UP);
    }

    /**
     * 截至当前季度的累计（跨年累计）：
     * 统计同一 reportId + roleId 下，所有季度 periodId <= 当前季度 的当期发生额之和。
     */
    private MetricSnapshot snapshotYtd(String reportId, String roleId, QuarterPeriod current) {
        // 查询该报表 + 角色的所有填报主记录（可能跨年）
        List<ReportFillBasicInfoDO> records = reportFillBasicInfoMapper.selectList(
                new LambdaQueryWrapperX<ReportFillBasicInfoDO>()
                        .eq(ReportFillBasicInfoDO::getReportId, reportId)
                        .eq(ReportFillBasicInfoDO::getRoleId, roleId));

        MetricSnapshot sum = MetricSnapshot.zero();
        BigDecimal rateWeightedSum = BigDecimal.ZERO;
        BigDecimal nplWeightedSum = BigDecimal.ZERO;
        BigDecimal amountSum = BigDecimal.ZERO;

        for (ReportFillBasicInfoDO record : records) {
            // 仅统计季度周期，且 periodId 不晚于当前季度
            QuarterPeriod period = parseQuarterPeriod(record.getPeriodId());
            if (compareQuarter(period, current) > 0) {
                continue;
            }
            MetricSnapshot snap = snapshot(reportId, roleId, record.getPeriodId());
            sum.applyHouseholds = sum.applyHouseholds.add(snap.applyHouseholds);
            sum.grantHouseholds = sum.grantHouseholds.add(snap.grantHouseholds);
            sum.grantAmount = sum.grantAmount.add(snap.grantAmount);
            sum.creditGrantAmount = sum.creditGrantAmount.add(snap.creditGrantAmount);

            // 按金额加权累计比率（百分数）
            if (snap.grantAmount.compareTo(BigDecimal.ZERO) > 0) {
                amountSum = amountSum.add(snap.grantAmount);
                rateWeightedSum = rateWeightedSum.add(snap.grantAmount.multiply(snap.avgAnnualRatePct));
                nplWeightedSum = nplWeightedSum.add(snap.grantAmount.multiply(snap.nplRatePct));
            }
        }

        sum.avgAnnualRatePct = amountSum.compareTo(BigDecimal.ZERO) > 0
                ? rateWeightedSum.divide(amountSum, 6, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        sum.nplRatePct = amountSum.compareTo(BigDecimal.ZERO) > 0
                ? nplWeightedSum.divide(amountSum, 6, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        return sum;
    }

    private enum RateType {
        AVG_ANNUAL_RATE_PCT,
        NPL_RATE_PCT
    }

    /**
     * 按你描述的递推加权口径计算累计比率（百分数）：
     * (上季度累计金额 * 上季度累计比率 + 当季当期金额 * 当季当期比率) / (上季度累计金额 + 当季当期金额)
     *
     * Q1 无上季度累计，则累计比率 = 当季当期比率（若金额为 0 则返回 0）。
     */
    private BigDecimal calcYtdWeightedRatePct(QuarterPeriod currentQuarter, String reportId, String roleId,
                                             BigDecimal currentGrantAmount, BigDecimal currentRatePct, RateType rateType) {
        BigDecimal curAmt = nz(currentGrantAmount);
        BigDecimal curRate = nz(currentRatePct);
        // 跨年连续累计：Q1 的上季度应为上一年 Q4，而不是直接退化为“仅当季”
        QuarterPeriod prev = prevQuarter(currentQuarter);
        // 历史累计口径：上季度累计使用跨年累计（过去所有年份的季度当期发生额加和）
        MetricSnapshot prevYtd = snapshotYtd(reportId, roleId, prev);
        BigDecimal prevAmt = nz(prevYtd.getGrantAmount());
        BigDecimal prevRate = rateType == RateType.AVG_ANNUAL_RATE_PCT
                ? nz(prevYtd.getAvgAnnualRatePct())
                : nz(prevYtd.getNplRatePct());

        BigDecimal denom = prevAmt.add(curAmt);
        if (denom.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return prevAmt.multiply(prevRate)
                .add(curAmt.multiply(curRate))
                .divide(denom, 6, RoundingMode.HALF_UP);
    }

    private MetricSnapshot snapshot(String reportId, String roleId, String periodId) {
        ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectLatestByPeriodAndReportAndRole(periodId, reportId, roleId);
        if (record == null) {
            return MetricSnapshot.zero();
        }
        ReportFillQuarterMicroLoanDO data = reportFillQuarterMicroLoanMapper.selectOne(
                new LambdaQueryWrapperX<ReportFillQuarterMicroLoanDO>()
                        .eq(ReportFillQuarterMicroLoanDO::getParentId, record.getId()));
        if (data == null) {
            return MetricSnapshot.zero();
        }
        return MetricSnapshot.from(data);
    }

    private void fillNumber(BigDecimal current, BigDecimal ytd, BigDecimal last,
                            QuadConsumer<BigDecimal, BigDecimal, BigDecimal, BigDecimal> setter) {
        BigDecimal c = nz(current);
        BigDecimal y = nz(ytd);
        BigDecimal l = nz(last);
        BigDecimal delta = c.subtract(l);
        BigDecimal rate = calcYoyRatePct(c, l);
        setter.accept(c, y, delta, rate);
    }

    private void fillRatePct(BigDecimal currentPct, BigDecimal ytdPct, BigDecimal lastPct,
                             QuadConsumer<BigDecimal, BigDecimal, BigDecimal, BigDecimal> setter) {
        BigDecimal c = nz(currentPct);
        BigDecimal y = nz(ytdPct);
        BigDecimal l = nz(lastPct);
        // 同期增长量：不乘 100
        // DB/中间计算的 avgAnnualRatePct / nplRatePct 为百分数（如 0.54 表示 0.54%），前端期望增长量为小数形式（0.0054）
        BigDecimal delta = c.subtract(l)
                .divide(ONE_HUNDRED, 10, RoundingMode.HALF_UP);
        BigDecimal rate = calcYoyRatePct(c, l);
        setter.accept(c, y, delta, rate);
    }

    /**
     * 同比增长率（%）：(current - last) / last * 100；last 为 0 返回 null
     */
    private BigDecimal calcYoyRatePct(BigDecimal current, BigDecimal last) {
        if (last == null || last.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return current.subtract(last)
                .divide(last, 10, RoundingMode.HALF_UP)
                .multiply(ONE_HUNDRED)
                .setScale(6, RoundingMode.HALF_UP);
    }

    private BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

    private void validateQuarterPeriod(String periodId) {
        parseQuarterPeriod(periodId);
    }

    private QuarterPeriod parseQuarterPeriod(String periodId) {
        if (StringUtils.isBlank(periodId)) {
            throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期不能为空");
        }
        String p = periodId.trim();
        // 格式：yyyyQn
        if (p.length() != 6 || p.charAt(4) != 'Q') {
            throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期格式不正确，应为 yyyyQn，例如 2026Q1");
        }
        try {
            int year = Integer.parseInt(p.substring(0, 4));
            int quarter = Integer.parseInt(p.substring(5, 6));
            if (quarter < 1 || quarter > 4) {
                throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "季度必须在 1-4 之间");
            }
            return new QuarterPeriod(year, quarter);
        } catch (NumberFormatException e) {
            throw exception(GlobalErrorCodeConstants.BAD_REQUEST, "填报周期格式不正确，应为 yyyyQn，例如 2026Q1");
        }
    }

    @Data
    @AllArgsConstructor
    private static class QuarterPeriod {
        private int year;
        private int quarter;

        String toPeriodId() {
            return year + "Q" + quarter;
        }
    }

    private int compareQuarter(QuarterPeriod a, QuarterPeriod b) {
        if (a.getYear() != b.getYear()) {
            return Integer.compare(a.getYear(), b.getYear());
        }
        return Integer.compare(a.getQuarter(), b.getQuarter());
    }

    @Data
    private static class MetricSnapshot {
        private BigDecimal applyHouseholds;
        private BigDecimal grantHouseholds;
        private BigDecimal grantAmount;
        private BigDecimal creditGrantAmount;
        private BigDecimal avgAnnualRatePct;
        private BigDecimal nplRatePct;

        static MetricSnapshot zero() {
            MetricSnapshot s = new MetricSnapshot();
            s.applyHouseholds = BigDecimal.ZERO;
            s.grantHouseholds = BigDecimal.ZERO;
            s.grantAmount = BigDecimal.ZERO;
            s.creditGrantAmount = BigDecimal.ZERO;
            s.avgAnnualRatePct = BigDecimal.ZERO;
            s.nplRatePct = BigDecimal.ZERO;
            return s;
        }

        static MetricSnapshot from(ReportFillQuarterMicroLoanDO data) {
            MetricSnapshot s = new MetricSnapshot();
            s.applyHouseholds = Optional.ofNullable(data.getApplyHouseholds()).orElse(BigDecimal.ZERO);
            s.grantHouseholds = Optional.ofNullable(data.getGrantHouseholds()).orElse(BigDecimal.ZERO);
            s.grantAmount = Optional.ofNullable(data.getGrantAmount()).orElse(BigDecimal.ZERO);
            s.creditGrantAmount = Optional.ofNullable(data.getCreditGrantAmount()).orElse(BigDecimal.ZERO);
            s.avgAnnualRatePct = Optional.ofNullable(data.getAvgAnnualRatePct()).orElse(BigDecimal.ZERO);
            s.nplRatePct = Optional.ofNullable(data.getNplRatePct()).orElse(BigDecimal.ZERO);
            return s;
        }
    }

    @FunctionalInterface
    private interface QuadConsumer<A, B, C, D> {
        void accept(A a, B b, C c, D d);
    }

    /**
     * 最小 MQ 试验：保存成功后发布 Redis Channel 消息，不影响主流程事务结果。
     */
    private void publishSavedMessage(ReportQuarterMicroLoanSaveReqVO reqVO, String recordId, String action) {
        try {
            ReportQuarterMicroLoanSavedStreamMessage message = new ReportQuarterMicroLoanSavedStreamMessage();
            message.setRecordId(recordId);
            message.setPeriodId(reqVO.getPeriodId());
            message.setReportId(reqVO.getReportId());
            message.setRoleId(reqVO.getRoleId());
            message.setAction(action);
            redisMQTemplate.send(message);
        } catch (Exception ex) {
            log.warn("[publishSavedMessage][发布 Redis MQ 消息失败 recordId={} periodId={} action={}]",
                    recordId, reqVO.getPeriodId(), action, ex);
        }
    }
}

