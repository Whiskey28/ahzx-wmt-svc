package com.wmt.module.pfb.service.banner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.module.pfb.controller.pfb.banner.vo.PfbBannerRespVO;
import com.wmt.module.pfb.dal.dataobject.PfbBannerDO;
import com.wmt.module.pfb.dal.mysql.PfbBannerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PfbBannerServiceImplTest {

    @Mock
    private PfbBannerMapper pfbBannerMapper;

    @InjectMocks
    private PfbBannerServiceImpl pfbBannerService;

    @Test
    void listActiveBanners_returnsMapperRows() {
        PfbBannerDO b = new PfbBannerDO();
        b.setId("b1");
        b.setTitle("首页");
        when(pfbBannerMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(b));

        List<PfbBannerRespVO> list = pfbBannerService.listActiveBanners();
        assertEquals(1, list.size());
        assertEquals("b1", list.get(0).getId());
        assertEquals("首页", list.get(0).getTitle());
    }
}
