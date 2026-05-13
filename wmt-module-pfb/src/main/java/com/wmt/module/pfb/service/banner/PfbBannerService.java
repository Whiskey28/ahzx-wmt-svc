package com.wmt.module.pfb.service.banner;

import com.wmt.module.pfb.controller.pfb.banner.vo.PfbBannerRespVO;

import java.util.List;

public interface PfbBannerService {

    List<PfbBannerRespVO> listActiveBanners();
}
