package com.wmt.module.pfb.service.loanproduct;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.pfb.controller.pfb.loanproduct.vo.PfbLoanProductRespVO;
import com.wmt.module.pfb.dal.dataobject.PfbLoanProductDO;
import com.wmt.module.pfb.dal.mysql.PfbLoanProductMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class PfbLoanProductServiceImpl implements PfbLoanProductService {

    private static final int DEFAULT_HOT_LIMIT = 20;

    @Resource
    private PfbLoanProductMapper pfbLoanProductMapper;

    @Override
    public List<PfbLoanProductRespVO> listOnShelf() {
        List<PfbLoanProductDO> list = pfbLoanProductMapper.selectList(baseOnShelfWrapper().orderByAsc(PfbLoanProductDO::getSortOrder).orderByAsc(PfbLoanProductDO::getId));
        return BeanUtils.toBean(list, PfbLoanProductRespVO.class);
    }

    @Override
    public List<PfbLoanProductRespVO> listHot(int limit) {
        int n = limit > 0 ? limit : DEFAULT_HOT_LIMIT;
        List<PfbLoanProductDO> list = pfbLoanProductMapper.selectList(
                baseOnShelfWrapper()
                        .eq(PfbLoanProductDO::getIsHot, 1)
                        .orderByAsc(PfbLoanProductDO::getSortOrder)
                        .orderByAsc(PfbLoanProductDO::getId)
                        .last("LIMIT " + n));
        return BeanUtils.toBean(list, PfbLoanProductRespVO.class);
    }

    @Override
    public PfbLoanProductRespVO getOnShelf(String id) {
        PfbLoanProductDO row = pfbLoanProductMapper.selectOne(
                baseOnShelfWrapper().eq(PfbLoanProductDO::getId, id));
        if (row == null) {
            throw exception(PfbErrorCodeConstants.PFB_PRODUCT_NOT_ONLINE);
        }
        return BeanUtils.toBean(row, PfbLoanProductRespVO.class);
    }

    private LambdaQueryWrapper<PfbLoanProductDO> baseOnShelfWrapper() {
        return new LambdaQueryWrapper<PfbLoanProductDO>()
                .eq(PfbLoanProductDO::getDeleted, false)
                .eq(PfbLoanProductDO::getProductStatus, 1);
    }
}
