package com.wmt.module.credit.report.service;

import com.wmt.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.credit.report.controller.admin.report.vo.*;
import com.wmt.module.credit.report.dal.dataobject.ReportFillBasicInfoDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillInfoCollectStatDO;
import com.wmt.module.credit.report.dal.dataobject.ReportFillQuarterMicroLoanDO;
import com.wmt.module.credit.report.dal.mysql.ReportFillBasicInfoMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillInfoCollectStatMapper;
import com.wmt.module.credit.report.dal.mysql.ReportFillQuarterMicroLoanMapper;
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
            return Boolean.TRUE;
        }

        exist.setApplyHouseholds(reqVO.getApplyHouseholds());
        exist.setGrantHouseholds(reqVO.getGrantHouseholds());
        exist.setGrantAmount(reqVO.getGrantAmount());
        exist.setCreditGrantAmount(reqVO.getCreditGrantAmount());
        exist.setAvgAnnualRatePct(reqVO.getAvgAnnualRatePct());
        exist.setNplRatePct(reqVO.getNplRatePct());
        reportFillQuarterMicroLoanMapper.updateById(exist);
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
        // ytd
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
                currentSnap.getAvgAnnualRatePct(), ytdSnap.getAvgAnnualRatePct(), lastYearSnap.getAvgAnnualRatePct(),
                (c, ytd, delta, rate) -> {
                    respVO.setAvgAnnualRatePctCurrent(c);
                    respVO.setAvgAnnualRatePctYtd(ytd);
                    respVO.setAvgAnnualRatePctYoyDelta(delta);
                    respVO.setAvgAnnualRatePctYoyRate(rate);
                });

        fillRatePct(
                currentSnap.getNplRatePct(), ytdSnap.getNplRatePct(), lastYearSnap.getNplRatePct(),
                (c, ytd, delta, rate) -> {
                    respVO.setNplRatePctCurrent(c);
                    respVO.setNplRatePctYtd(ytd);
                    respVO.setNplRatePctYoyDelta(delta);
                    respVO.setNplRatePctYoyRate(rate);
                });

        return respVO;
    }

    @Override
    public ReportQuarterMicroLoanExtraStatRespVO statPlus(ReportQuarterMicroLoanStatReqVO reqVO) {
        QuarterPeriod quarter = parseQuarterPeriod(reqVO.getPeriodId());

        // 月报周期：季度末月、上年12月
        String endMonthPeriodId = toEndMonthPeriodId(quarter);
        String prevYearDecPeriodId = (quarter.getYear() - 1) + "-12";

        String lastYearEndMonthPeriodId = toEndMonthPeriodId(new QuarterPeriod(quarter.getYear() - 1, quarter.getQuarter()));
        String last2YearDecPeriodId = (quarter.getYear() - 2) + "-12";

        // 1) 数据库收录小微企业户数：当期发生额 = (本季度末月存量) - (上年12月存量)
        BigDecimal endMonthValue = readCollectMicroSmeTotalByMonth(reqVO.getReportId(), reqVO.getRoleId(), endMonthPeriodId);
        BigDecimal prevDecValue = readCollectMicroSmeTotalByMonth(reqVO.getReportId(), reqVO.getRoleId(), prevYearDecPeriodId);
        BigDecimal currentDelta = endMonthValue.subtract(prevDecValue);

        // 累计发生额：先按“季度末月存量”返回（后续如需调整口径再改）
        BigDecimal ytd = endMonthValue;

        // 同期：去年同季度口径一致： (去年季度末月存量) - (前年12月存量)
        BigDecimal lastYearEndMonthValue = readCollectMicroSmeTotalByMonth(reqVO.getReportId(), reqVO.getRoleId(), lastYearEndMonthPeriodId);
        BigDecimal last2YearDecValue = readCollectMicroSmeTotalByMonth(reqVO.getReportId(), reqVO.getRoleId(), last2YearDecPeriodId);
        BigDecimal lastYearDelta = lastYearEndMonthValue.subtract(last2YearDecValue);

        BigDecimal yoyDelta = currentDelta.subtract(lastYearDelta);
        BigDecimal yoyRate = calcYoyRatePct(currentDelta, lastYearDelta);

        // 2) 为放贷机构提供小微企业征信服务次数：当期发生额 = form-stat 总计 yearServiceCount（传季度末月 periodId）
        BigDecimal serviceCountTotal = resolveServiceCountTotal(reqVO.getReportId(), endMonthPeriodId);

        ReportQuarterMicroLoanExtraStatRespVO respVO = new ReportQuarterMicroLoanExtraStatRespVO();
        respVO.setPeriodId(reqVO.getPeriodId());
        respVO.setRoleId(reqVO.getRoleId());
        respVO.setReportId(reqVO.getReportId());

        respVO.setCollectMicroSmeTotalCurrent(currentDelta);
        respVO.setCollectMicroSmeTotalYtd(ytd);
        respVO.setCollectMicroSmeTotalYoyDelta(yoyDelta);
        respVO.setCollectMicroSmeTotalYoyRate(yoyRate);

        respVO.setServiceCountTotalCurrent(serviceCountTotal);
        respVO.setServiceCountTotalYtd(null);
        respVO.setServiceCountTotalYoyDelta(null);
        respVO.setServiceCountTotalYoyRate(null);
        return respVO;
    }

    private String toEndMonthPeriodId(QuarterPeriod quarter) {
        int endMonth = quarter.getQuarter() * 3;
        String m = endMonth < 10 ? ("0" + endMonth) : String.valueOf(endMonth);
        return quarter.getYear() + "-" + m;
    }

    private BigDecimal readCollectMicroSmeTotalByMonth(String reportId, String roleId, String monthPeriodId) {
        ReportFillBasicInfoDO record = reportFillBasicInfoMapper.selectLatestByPeriodAndReportAndRole(monthPeriodId, reportId, roleId);
        if (record == null) {
            return BigDecimal.ZERO;
        }
        ReportFillInfoCollectStatDO data = reportFillInfoCollectStatMapper.selectOne(
                new LambdaQueryWrapperX<ReportFillInfoCollectStatDO>()
                        .eq(ReportFillInfoCollectStatDO::getParentId, record.getId()));
        if (data == null || data.getCollectMicroSmeTotal() == null) {
            return BigDecimal.ZERO;
        }
        return data.getCollectMicroSmeTotal();
    }

    private BigDecimal resolveServiceCountTotal(String reportId, String periodId) {
        // 复用既有逻辑：取 form-stat 的最后“总计”行
        JmReportServiceByIndustryReqVO reqVO = new JmReportServiceByIndustryReqVO();
        reqVO.setReportId(reportId);
        reqVO.setPeriodId(periodId);
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
        BigDecimal deltaPp = c.subtract(l); // 百分点差
        BigDecimal rate = calcYoyRatePct(c, l);
        setter.accept(c, y, deltaPp, rate);
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
}

