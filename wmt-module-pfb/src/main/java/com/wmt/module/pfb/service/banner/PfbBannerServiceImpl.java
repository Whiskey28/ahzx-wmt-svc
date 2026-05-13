package com.wmt.module.pfb.service.banner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.pfb.controller.pfb.banner.vo.PfbBannerRespVO;
import com.wmt.module.pfb.dal.dataobject.PfbBannerDO;
import com.wmt.module.pfb.dal.mysql.PfbBannerMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PfbBannerServiceImpl implements PfbBannerService {

    @Resource
    private PfbBannerMapper pfbBannerMapper;

    @Override
    public List<PfbBannerRespVO> listActiveBanners() {
        LocalDateTime now = LocalDateTime.now();
        LambdaQueryWrapper<PfbBannerDO> q = new LambdaQueryWrapper<PfbBannerDO>()
                .eq(PfbBannerDO::getDeleted, false)
                .eq(PfbBannerDO::getBannerStatus, 1)
                .and(w -> w.isNull(PfbBannerDO::getEffectiveBegin).or().le(PfbBannerDO::getEffectiveBegin, now))
                .and(w -> w.isNull(PfbBannerDO::getEffectiveEnd).or().ge(PfbBannerDO::getEffectiveEnd, now))
                .orderByAsc(PfbBannerDO::getSortOrder)
                .orderByAsc(PfbBannerDO::getId);
        List<PfbBannerDO> list = pfbBannerMapper.selectList(q);
        return BeanUtils.toBean(list, PfbBannerRespVO.class);
    }
}
