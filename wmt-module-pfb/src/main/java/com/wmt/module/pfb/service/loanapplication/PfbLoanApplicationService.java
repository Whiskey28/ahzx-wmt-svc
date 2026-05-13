package com.wmt.module.pfb.service.loanapplication;

import com.wmt.module.pfb.controller.pfb.loanapplication.vo.PfbLoanApplicationListItemVO;
import com.wmt.module.pfb.controller.pfb.loanapplication.vo.PfbLoanApplicationSubmitReqVO;

import java.util.List;

public interface PfbLoanApplicationService {

    void submit(long sysUserId, PfbLoanApplicationSubmitReqVO reqVO);

    List<PfbLoanApplicationListItemVO> listMine(long sysUserId);
}
