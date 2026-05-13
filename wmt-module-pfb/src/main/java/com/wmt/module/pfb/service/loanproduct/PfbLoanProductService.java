package com.wmt.module.pfb.service.loanproduct;

import com.wmt.module.pfb.controller.pfb.loanproduct.vo.PfbLoanProductRespVO;

import java.util.List;

public interface PfbLoanProductService {

    List<PfbLoanProductRespVO> listOnShelf();

    List<PfbLoanProductRespVO> listHot(int limit);

    PfbLoanProductRespVO getOnShelf(String id);
}
