package com.wmt.module.pfb.service.enterprise;

import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseCreateReqVO;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseListItemVO;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseUpdateReqVO;

import java.util.List;

public interface PfbEnterpriseService {

    List<PfbEnterpriseListItemVO> listMine(long sysUserId);

    String createAndBind(long sysUserId, PfbEnterpriseCreateReqVO reqVO);

    void updateEnt(long sysUserId, String entId, PfbEnterpriseUpdateReqVO reqVO);

    void setDefault(long sysUserId, String entId);

    void legalVerifyStub(long sysUserId, String entId);
}
