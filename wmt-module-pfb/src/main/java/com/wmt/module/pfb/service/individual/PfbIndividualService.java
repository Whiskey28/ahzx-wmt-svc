package com.wmt.module.pfb.service.individual;

import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualStatusRespVO;
import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualSubmitReqVO;

public interface PfbIndividualService {

    PfbIndividualStatusRespVO getStatus(long sysUserId);

    void submitOrUpdate(long sysUserId, PfbIndividualSubmitReqVO reqVO);

    void l2CallbackStub();
}
