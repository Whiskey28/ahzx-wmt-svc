package com.wmt.module.pfb.service.individual;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualSubmitReqVO;
import com.wmt.module.pfb.dal.dataobject.PfbIndividualCustomerDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserIndividualCustomerDO;
import com.wmt.module.pfb.dal.mysql.PfbIndividualCustomerMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserExtMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserIndividualCustomerMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.wmt.framework.test.core.util.AssertUtils.assertServiceException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PfbIndividualServiceImplTest {

    @Mock
    private PfbIndividualCustomerMapper individualCustomerMapper;
    @Mock
    private PfbSystemUserIndividualCustomerMapper userIndividualMapper;
    @Mock
    private PfbSystemUserExtMapper userExtMapper;

    @InjectMocks
    private PfbIndividualServiceImpl individualService;

    @Test
    void submitOrUpdate_idCardBoundOtherUser_throws() {
        PfbIndividualCustomerDO existing = new PfbIndividualCustomerDO();
        existing.setId("cid-1");
        existing.setIdCardNo("110101199001011234");
        when(individualCustomerMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(existing);

        PfbSystemUserIndividualCustomerDO link = new PfbSystemUserIndividualCustomerDO();
        link.setSysUserId(2L);
        link.setIndividualCustomerId("cid-1");
        List<PfbSystemUserIndividualCustomerDO> links = Collections.singletonList(link);
        when(userIndividualMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(links);

        PfbIndividualSubmitReqVO req = new PfbIndividualSubmitReqVO();
        req.setRealName("王五");
        req.setIdCardNo("110101199001011234");
        req.setAddress("北京市");
        req.setIdCardFrontImage("http://f");
        req.setIdCardBackImage("http://b");

        assertServiceException(() -> individualService.submitOrUpdate(1L, req),
                PfbErrorCodeConstants.PFB_ID_CARD_BOUND_OTHER_USER);
    }
}
