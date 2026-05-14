package com.wmt.module.pfb.service.enterprise;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseCreateReqVO;
import com.wmt.module.pfb.dal.dataobject.PfbEntInfoDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserEntInfoDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserExtDO;
import com.wmt.module.pfb.dal.mysql.PfbEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserExtMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.wmt.framework.test.core.util.AssertUtils.assertServiceException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PfbEnterpriseServiceImplTest {

    @Mock
    private PfbEntInfoMapper entInfoMapper;
    @Mock
    private PfbSystemUserEntInfoMapper userEntMapper;
    @Mock
    private PfbSystemUserExtMapper userExtMapper;

    @InjectMocks
    private PfbEnterpriseServiceImpl enterpriseService;

    @Test
    void createAndBind_invalidCreditCode_throws() {
        PfbEnterpriseCreateReqVO req = new PfbEnterpriseCreateReqVO();
        req.setEnterpriseName("测试企业");
        req.setCompanyCreditCode("123");
        req.setCoreContact("张三");
        assertServiceException(() -> enterpriseService.createAndBind(1L, req),
                PfbErrorCodeConstants.PFB_CREDIT_CODE_INVALID);
    }

    @Test
    void createAndBind_alreadyBoundSameCode_throws() {
        PfbEnterpriseCreateReqVO req = new PfbEnterpriseCreateReqVO();
        req.setEnterpriseName("测试企业");
        req.setCompanyCreditCode("91110000100000001X");
        req.setCoreContact("张三");

        PfbEntInfoDO existing = new PfbEntInfoDO();
        existing.setId("ent-old");
        existing.setCompanyCreditCode("91110000100000001X");
        when(entInfoMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(existing));
        when(userEntMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        assertServiceException(() -> enterpriseService.createAndBind(99L, req),
                PfbErrorCodeConstants.PFB_ENT_CREDIT_ALREADY_BOUND);
    }

    @Test
    void createAndBind_firstEnt_setsDefaultExt() {
        PfbEnterpriseCreateReqVO req = new PfbEnterpriseCreateReqVO();
        req.setEnterpriseName("新公司");
        req.setCompanyCreditCode("91110000100000001X");
        req.setCoreContact("李四");

        when(entInfoMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        when(userEntMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(userExtMapper.selectById(eq(1L))).thenReturn(null);

        String entId = enterpriseService.createAndBind(1L, req);
        verify(entInfoMapper).insert(any(PfbEntInfoDO.class));
        verify(userEntMapper).insert(any(PfbSystemUserEntInfoDO.class));
        verify(userExtMapper).insert(any(PfbSystemUserExtDO.class));
        assertNotNull(entId);
    }
}
