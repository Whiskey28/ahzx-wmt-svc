package com.wmt.module.pfb.service.loanapplication;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.module.pfb.controller.pfb.loanapplication.vo.PfbLoanApplicationSubmitReqVO;
import com.wmt.module.pfb.dal.dataobject.PfbEntInfoDO;
import com.wmt.module.pfb.dal.dataobject.PfbIndividualCustomerDO;
import com.wmt.module.pfb.dal.dataobject.PfbLoanApplicationDO;
import com.wmt.module.pfb.dal.dataobject.PfbLoanProductDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserExtDO;
import com.wmt.module.pfb.dal.mysql.PfbEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbIndividualCustomerMapper;
import com.wmt.module.pfb.dal.mysql.PfbLoanApplicationMapper;
import com.wmt.module.pfb.dal.mysql.PfbLoanProductMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserExtMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.wmt.framework.test.core.util.AssertUtils.assertServiceException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PfbLoanApplicationServiceImplTest {

    @Mock
    private PfbLoanApplicationMapper loanApplicationMapper;
    @Mock
    private PfbSystemUserExtMapper userExtMapper;
    @Mock
    private PfbIndividualCustomerMapper individualCustomerMapper;
    @Mock
    private PfbSystemUserEntInfoMapper userEntMapper;
    @Mock
    private PfbEntInfoMapper entInfoMapper;
    @Mock
    private PfbLoanProductMapper loanProductMapper;

    @InjectMocks
    private PfbLoanApplicationServiceImpl loanApplicationService;

    @Test
    void submit_creditAuthNotAccepted_throws() {
        PfbLoanApplicationSubmitReqVO req = baseReq();
        req.setCreditAuthAccepted(0);
        assertServiceException(() -> loanApplicationService.submit(1L, req),
                PfbErrorCodeConstants.PFB_CREDIT_AUTH_NOT_ACCEPTED);
    }

    @Test
    void submit_amountNotWanMultiple_throws() {
        PfbLoanApplicationSubmitReqVO req = baseReq();
        req.setApplyAmountCent(100L);
        assertServiceException(() -> loanApplicationService.submit(1L, req),
                PfbErrorCodeConstants.PFB_APPLY_AMOUNT_NOT_WAN);
    }

    @Test
    void submit_noIndividual_throws() {
        when(userExtMapper.selectById(1L)).thenReturn(null);
        assertServiceException(() -> loanApplicationService.submit(1L, baseReq()),
                PfbErrorCodeConstants.PFB_GATE_INDIVIDUAL);
    }

    @Test
    void submit_success_inserts() {
        PfbSystemUserExtDO ext = new PfbSystemUserExtDO();
        ext.setSysUserId(1L);
        ext.setCurrentIndividualCustomerId("ind-1");
        when(userExtMapper.selectById(1L)).thenReturn(ext);

        PfbIndividualCustomerDO ind = new PfbIndividualCustomerDO();
        ind.setId("ind-1");
        ind.setIsVerified(1);
        when(individualCustomerMapper.selectById("ind-1")).thenReturn(ind);

        when(userEntMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        PfbEntInfoDO ent = new PfbEntInfoDO();
        ent.setId("ent-1");
        ent.setEnterpriseName("有名称公司");
        ent.setCompanyCreditCode("91110000100000001X");
        when(entInfoMapper.selectById("ent-1")).thenReturn(ent);

        PfbLoanProductDO product = new PfbLoanProductDO();
        product.setId("prod-1");
        when(loanProductMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(product);

        loanApplicationService.submit(1L, baseReq());
        verify(loanApplicationMapper).insert(any(PfbLoanApplicationDO.class));
    }

    private static PfbLoanApplicationSubmitReqVO baseReq() {
        PfbLoanApplicationSubmitReqVO req = new PfbLoanApplicationSubmitReqVO();
        req.setProductId("prod-1");
        req.setEntId("ent-1");
        req.setApplyAmountCent(1_000_000L);
        req.setTermMonths(12);
        req.setRepayMethod("EQUAL_PRINCIPAL_INTEREST");
        req.setCreditAuthAccepted(1);
        return req;
    }
}
