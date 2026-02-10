package com.wmt.module.pay.api.refund;

import com.wmt.framework.common.util.object.BeanUtils;
import com.wmt.module.pay.api.refund.dto.PayRefundCreateReqDTO;
import com.wmt.module.pay.api.refund.dto.PayRefundRespDTO;
import com.wmt.module.pay.dal.dataobject.refund.PayRefundDO;
import com.wmt.module.pay.service.refund.PayRefundService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 退款单 API 实现类
 *
 * @author AHC源码
 */
@Service
@Validated
public class PayRefundApiImpl implements PayRefundApi {

    @Resource
    private PayRefundService payRefundService;

    @Override
    public Long createRefund(PayRefundCreateReqDTO reqDTO) {
        return payRefundService.createRefund(reqDTO);
    }

    @Override
    public PayRefundRespDTO getRefund(Long id) {
        PayRefundDO refund = payRefundService.getRefund(id);
        return BeanUtils.toBean(refund, PayRefundRespDTO.class);
    }

}
