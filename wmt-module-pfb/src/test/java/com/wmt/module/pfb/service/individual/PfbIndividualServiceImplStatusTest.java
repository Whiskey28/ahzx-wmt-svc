package com.wmt.module.pfb.service.individual;

import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualStatusRespVO;
import com.wmt.module.pfb.dal.dataobject.PfbIndividualCustomerDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserExtDO;
import com.wmt.module.pfb.dal.mysql.PfbIndividualCustomerMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserExtMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserIndividualCustomerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PfbIndividualServiceImplStatusTest {

    @Mock
    private PfbIndividualCustomerMapper individualCustomerMapper;
    @Mock
    private PfbSystemUserIndividualCustomerMapper userIndividualMapper;
    @Mock
    private PfbSystemUserExtMapper userExtMapper;

    @InjectMocks
    private PfbIndividualServiceImpl individualService;

    @Test
    void getStatus_noExt_returnsUnverified() {
        when(userExtMapper.selectById(1L)).thenReturn(null);
        PfbIndividualStatusRespVO vo = individualService.getStatus(1L);
        assertFalse(vo.getL1Verified());
        assertNull(vo.getCurrentIndividualCustomerId());
    }

    @Test
    void getStatus_verifiedCustomer_returnsL1() {
        PfbSystemUserExtDO ext = new PfbSystemUserExtDO();
        ext.setSysUserId(1L);
        ext.setCurrentIndividualCustomerId("cid-1");
        when(userExtMapper.selectById(1L)).thenReturn(ext);
        PfbIndividualCustomerDO c = new PfbIndividualCustomerDO();
        c.setId("cid-1");
        c.setIsVerified(1);
        when(individualCustomerMapper.selectById("cid-1")).thenReturn(c);

        PfbIndividualStatusRespVO vo = individualService.getStatus(1L);
        assertTrue(vo.getL1Verified());
        assertFalse(vo.getL2Verified());
    }
}
