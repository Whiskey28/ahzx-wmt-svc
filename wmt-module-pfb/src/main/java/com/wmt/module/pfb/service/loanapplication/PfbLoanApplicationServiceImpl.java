package com.wmt.module.pfb.service.loanapplication;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.pfb.controller.pfb.loanapplication.vo.PfbLoanApplicationListItemVO;
import com.wmt.module.pfb.controller.pfb.loanapplication.vo.PfbLoanApplicationSubmitReqVO;
import com.wmt.module.pfb.dal.dataobject.PfbEntInfoDO;
import com.wmt.module.pfb.dal.dataobject.PfbIndividualCustomerDO;
import com.wmt.module.pfb.dal.dataobject.PfbLoanApplicationDO;
import com.wmt.module.pfb.dal.dataobject.PfbLoanProductDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserEntInfoDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserExtDO;
import com.wmt.module.pfb.dal.mysql.PfbEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbIndividualCustomerMapper;
import com.wmt.module.pfb.dal.mysql.PfbLoanApplicationMapper;
import com.wmt.module.pfb.dal.mysql.PfbLoanProductMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserExtMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class PfbLoanApplicationServiceImpl implements PfbLoanApplicationService {

    private static final long WAN_YUAN_IN_CENT = 1_000_000L;

    @Resource
    private PfbLoanApplicationMapper loanApplicationMapper;
    @Resource
    private PfbSystemUserExtMapper userExtMapper;
    @Resource
    private PfbIndividualCustomerMapper individualCustomerMapper;
    @Resource
    private PfbSystemUserEntInfoMapper userEntMapper;
    @Resource
    private PfbEntInfoMapper entInfoMapper;
    @Resource
    private PfbLoanProductMapper loanProductMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submit(long sysUserId, PfbLoanApplicationSubmitReqVO reqVO) {
        if (!Integer.valueOf(1).equals(reqVO.getCreditAuthAccepted())) {
            throw exception(PfbErrorCodeConstants.PFB_CREDIT_AUTH_NOT_ACCEPTED);
        }
        if (reqVO.getApplyAmountCent() % WAN_YUAN_IN_CENT != 0) {
            throw exception(PfbErrorCodeConstants.PFB_APPLY_AMOUNT_NOT_WAN);
        }

        PfbSystemUserExtDO ext = userExtMapper.selectById(sysUserId);
        if (ext == null || StrUtil.isBlank(ext.getCurrentIndividualCustomerId())) {
            throw exception(PfbErrorCodeConstants.PFB_GATE_INDIVIDUAL);
        }
        PfbIndividualCustomerDO ind = individualCustomerMapper.selectById(ext.getCurrentIndividualCustomerId());
        if (ind == null || !Integer.valueOf(1).equals(ind.getIsVerified())) {
            throw exception(PfbErrorCodeConstants.PFB_GATE_INDIVIDUAL);
        }

        long entBind = userEntMapper.selectCount(
                new LambdaQueryWrapper<PfbSystemUserEntInfoDO>()
                        .eq(PfbSystemUserEntInfoDO::getSysUserId, sysUserId)
                        .eq(PfbSystemUserEntInfoDO::getEntId, reqVO.getEntId())
                        .eq(PfbSystemUserEntInfoDO::getDeleted, false));
        if (entBind == 0) {
            throw exception(PfbErrorCodeConstants.PFB_GATE_ENTERPRISE);
        }

        PfbEntInfoDO ent = entInfoMapper.selectById(reqVO.getEntId());
        if (ent == null) {
            throw exception(PfbErrorCodeConstants.PFB_ENT_NOT_FOUND);
        }
        if (StrUtil.isBlank(ent.getEnterpriseName())) {
            throw exception(PfbErrorCodeConstants.PFB_ENT_NAME_EMPTY);
        }

        PfbLoanProductDO product = loanProductMapper.selectOne(
                new LambdaQueryWrapper<PfbLoanProductDO>()
                        .eq(PfbLoanProductDO::getId, reqVO.getProductId())
                        .eq(PfbLoanProductDO::getDeleted, false)
                        .eq(PfbLoanProductDO::getProductStatus, 1));
        if (product == null) {
            throw exception(PfbErrorCodeConstants.PFB_PRODUCT_NOT_ONLINE);
        }

        PfbLoanApplicationDO row = new PfbLoanApplicationDO();
        row.setId(UUID.randomUUID().toString(true));
        row.setSysUserId(sysUserId);
        row.setIndividualCustomerId(ext.getCurrentIndividualCustomerId());
        row.setEntId(reqVO.getEntId());
        row.setEnterpriseNameSnapshot(ent.getEnterpriseName());
        row.setCompanyCreditCodeSnapshot(ent.getCompanyCreditCode());
        row.setProductId(reqVO.getProductId());
        row.setApplyAmountCent(reqVO.getApplyAmountCent());
        row.setTermMonths(reqVO.getTermMonths());
        row.setRepayMethod(reqVO.getRepayMethod());
        row.setCreditAuthAccepted(reqVO.getCreditAuthAccepted());
        row.setStatus("SUBMITTED");
        loanApplicationMapper.insert(row);
    }

    @Override
    public List<PfbLoanApplicationListItemVO> listMine(long sysUserId) {
        List<PfbLoanApplicationDO> list = loanApplicationMapper.selectList(
                new LambdaQueryWrapper<PfbLoanApplicationDO>()
                        .eq(PfbLoanApplicationDO::getSysUserId, sysUserId)
                        .eq(PfbLoanApplicationDO::getDeleted, false)
                        .orderByDesc(PfbLoanApplicationDO::getCreateTime));
        return BeanUtils.toBean(list, PfbLoanApplicationListItemVO.class);
    }
}
