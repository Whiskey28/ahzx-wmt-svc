package com.wmt.module.pfb.service.individual;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualStatusRespVO;
import com.wmt.module.pfb.controller.pfb.individual.vo.PfbIndividualSubmitReqVO;
import com.wmt.module.pfb.dal.dataobject.PfbIndividualCustomerDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserExtDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserIndividualCustomerDO;
import com.wmt.module.pfb.dal.mysql.PfbIndividualCustomerMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserExtMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserIndividualCustomerMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class PfbIndividualServiceImpl implements PfbIndividualService {

    @Resource
    private PfbIndividualCustomerMapper individualCustomerMapper;
    @Resource
    private PfbSystemUserIndividualCustomerMapper userIndividualMapper;
    @Resource
    private PfbSystemUserExtMapper userExtMapper;

    @Override
    public PfbIndividualStatusRespVO getStatus(long sysUserId) {
        PfbSystemUserExtDO ext = userExtMapper.selectById(sysUserId);
        String cid = ext != null ? ext.getCurrentIndividualCustomerId() : null;
        boolean l1 = false;
        if (cid != null) {
            PfbIndividualCustomerDO c = individualCustomerMapper.selectById(cid);
            l1 = c != null && Integer.valueOf(1).equals(c.getIsVerified());
        }
        PfbIndividualStatusRespVO vo = new PfbIndividualStatusRespVO();
        vo.setL1Verified(l1);
        vo.setL2Verified(false);
        vo.setCurrentIndividualCustomerId(cid);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitOrUpdate(long sysUserId, PfbIndividualSubmitReqVO reqVO) {
        String idCard = reqVO.getIdCardNo().trim();
        PfbIndividualCustomerDO existingByCard = individualCustomerMapper.selectOne(
                new LambdaQueryWrapper<PfbIndividualCustomerDO>()
                        .eq(PfbIndividualCustomerDO::getIdCardNo, idCard)
                        .eq(PfbIndividualCustomerDO::getDeleted, false)
                        .last("LIMIT 1"));
        if (existingByCard != null) {
            List<PfbSystemUserIndividualCustomerDO> links = userIndividualMapper.selectList(
                    new LambdaQueryWrapper<PfbSystemUserIndividualCustomerDO>()
                            .eq(PfbSystemUserIndividualCustomerDO::getIndividualCustomerId, existingByCard.getId())
                            .eq(PfbSystemUserIndividualCustomerDO::getDeleted, false));
            for (PfbSystemUserIndividualCustomerDO link : links) {
                if (!link.getSysUserId().equals(sysUserId)) {
                    throw exception(PfbErrorCodeConstants.PFB_ID_CARD_BOUND_OTHER_USER);
                }
            }
        }

        String customerId;
        if (existingByCard != null) {
            customerId = existingByCard.getId();
            existingByCard.setRealName(reqVO.getRealName());
            existingByCard.setIdCardNo(idCard);
            existingByCard.setAddress(reqVO.getAddress());
            existingByCard.setIdCardFrontImage(reqVO.getIdCardFrontImage());
            existingByCard.setIdCardBackImage(reqVO.getIdCardBackImage());
            existingByCard.setIsVerified(1);
            individualCustomerMapper.updateById(existingByCard);
        } else {
            customerId = UUID.randomUUID().toString(true);
            PfbIndividualCustomerDO row = new PfbIndividualCustomerDO();
            row.setId(customerId);
            row.setRealName(reqVO.getRealName());
            row.setIdCardNo(idCard);
            row.setAddress(reqVO.getAddress());
            row.setIdCardFrontImage(reqVO.getIdCardFrontImage());
            row.setIdCardBackImage(reqVO.getIdCardBackImage());
            row.setIsVerified(1);
            individualCustomerMapper.insert(row);
        }

        PfbSystemUserIndividualCustomerDO ownLink = userIndividualMapper.selectOne(
                new LambdaQueryWrapper<PfbSystemUserIndividualCustomerDO>()
                        .eq(PfbSystemUserIndividualCustomerDO::getSysUserId, sysUserId)
                        .eq(PfbSystemUserIndividualCustomerDO::getIndividualCustomerId, customerId)
                        .eq(PfbSystemUserIndividualCustomerDO::getDeleted, false)
                        .last("LIMIT 1"));
        if (ownLink == null) {
            PfbSystemUserIndividualCustomerDO link = new PfbSystemUserIndividualCustomerDO();
            link.setId(UUID.randomUUID().toString(true));
            link.setSysUserId(sysUserId);
            link.setIndividualCustomerId(customerId);
            userIndividualMapper.insert(link);
        }

        upsertExtCurrent(sysUserId, customerId);
    }

    private void upsertExtCurrent(long sysUserId, String customerId) {
        PfbSystemUserExtDO ext = userExtMapper.selectById(sysUserId);
        if (ext == null) {
            ext = new PfbSystemUserExtDO();
            ext.setSysUserId(sysUserId);
            ext.setCurrentIndividualCustomerId(customerId);
            userExtMapper.insert(ext);
        } else {
            ext.setCurrentIndividualCustomerId(customerId);
            userExtMapper.updateById(ext);
        }
    }

    @Override
    public void l2CallbackStub() {
        // 二期：公安/腾讯实人回调落库
    }
}
