package com.wmt.module.pfb.service.loanproduct;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.module.pfb.controller.pfb.loanproduct.vo.PfbLoanProductRespVO;
import com.wmt.module.pfb.dal.dataobject.PfbLoanProductDO;
import com.wmt.module.pfb.dal.mysql.PfbLoanProductMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.wmt.framework.test.core.util.AssertUtils.assertServiceException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PfbLoanProductServiceImplTest {

    @Mock
    private PfbLoanProductMapper pfbLoanProductMapper;

    @InjectMocks
    private PfbLoanProductServiceImpl pfbLoanProductService;

    @Test
    void listOnShelf_delegatesToMapper() {
        PfbLoanProductDO row = new PfbLoanProductDO();
        row.setId("p1");
        row.setProductStatus(1);
        when(pfbLoanProductMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(row));

        List<PfbLoanProductRespVO> list = pfbLoanProductService.listOnShelf();
        assertEquals(1, list.size());
        assertEquals("p1", list.get(0).getId());
    }

    @Test
    void listHot_callsMapper() {
        when(pfbLoanProductMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.emptyList());
        pfbLoanProductService.listHot(50);
        verify(pfbLoanProductMapper).selectList(any(LambdaQueryWrapper.class));
    }

    @Test
    void getOnShelf_notFound_throws() {
        when(pfbLoanProductMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(null);
        assertServiceException(() -> pfbLoanProductService.getOnShelf("missing-id"),
                PfbErrorCodeConstants.PFB_PRODUCT_NOT_ONLINE);
    }
}
