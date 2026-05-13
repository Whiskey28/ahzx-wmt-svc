package com.wmt.module.pfb.service.enterprise;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseCreateReqVO;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseListItemVO;
import com.wmt.module.pfb.controller.pfb.enterprise.vo.PfbEnterpriseUpdateReqVO;
import com.wmt.module.pfb.dal.dataobject.PfbEntInfoDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserEntInfoDO;
import com.wmt.module.pfb.dal.dataobject.PfbSystemUserExtDO;
import com.wmt.module.pfb.dal.mysql.PfbEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserEntInfoMapper;
import com.wmt.module.pfb.dal.mysql.PfbSystemUserExtMapper;
import com.wmt.module.pfb.enums.PfbErrorCodeConstants;
import com.wmt.module.pfb.util.PfbCreditCodeUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.wmt.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class PfbEnterpriseServiceImpl implements PfbEnterpriseService {

    @Resource
    private PfbEntInfoMapper entInfoMapper;
    @Resource
    private PfbSystemUserEntInfoMapper userEntMapper;
    @Resource
    private PfbSystemUserExtMapper userExtMapper;

    @Override
    public List<PfbEnterpriseListItemVO> listMine(long sysUserId) {
        List<PfbSystemUserEntInfoDO> links = userEntMapper.selectList(
                new LambdaQueryWrapper<PfbSystemUserEntInfoDO>()
                        .eq(PfbSystemUserEntInfoDO::getSysUserId, sysUserId)
                        .eq(PfbSystemUserEntInfoDO::getDeleted, false));
        List<PfbEnterpriseListItemVO> res = new ArrayList<>();
        for (PfbSystemUserEntInfoDO link : links) {
            PfbEntInfoDO ent = entInfoMapper.selectById(link.getEntId());
            if (ent == null || Boolean.TRUE.equals(ent.getDeleted())) {
                continue;
            }
            PfbEnterpriseListItemVO vo = new PfbEnterpriseListItemVO();
            vo.setEntId(ent.getId());
            vo.setEnterpriseName(ent.getEnterpriseName());
            vo.setCompanyCreditCode(ent.getCompanyCreditCode());
            vo.setIsDefault(Integer.valueOf(1).equals(link.getIsDefault()));
            res.add(vo);
        }
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAndBind(long sysUserId, PfbEnterpriseCreateReqVO reqVO) {
        String code = reqVO.getCompanyCreditCode().trim().toUpperCase();
        if (!PfbCreditCodeUtils.isValid(code)) {
            throw exception(PfbErrorCodeConstants.PFB_CREDIT_CODE_INVALID);
        }
        List<PfbEntInfoDO> sameCode = entInfoMapper.selectList(
                new LambdaQueryWrapper<PfbEntInfoDO>()
                        .eq(PfbEntInfoDO::getCompanyCreditCode, code)
                        .eq(PfbEntInfoDO::getDeleted, false));
        for (PfbEntInfoDO e : sameCode) {
            long bound = userEntMapper.selectCount(
                    new LambdaQueryWrapper<PfbSystemUserEntInfoDO>()
                            .eq(PfbSystemUserEntInfoDO::getSysUserId, sysUserId)
                            .eq(PfbSystemUserEntInfoDO::getEntId, e.getId())
                            .eq(PfbSystemUserEntInfoDO::getDeleted, false));
            if (bound > 0) {
                throw exception(PfbErrorCodeConstants.PFB_ENT_CREDIT_ALREADY_BOUND);
            }
        }

        long linkCount = userEntMapper.selectCount(
                new LambdaQueryWrapper<PfbSystemUserEntInfoDO>()
                        .eq(PfbSystemUserEntInfoDO::getSysUserId, sysUserId)
                        .eq(PfbSystemUserEntInfoDO::getDeleted, false));
        boolean first = linkCount == 0;

        String entId = UUID.randomUUID().toString(true);
        PfbEntInfoDO ent = new PfbEntInfoDO();
        ent.setId(entId);
        ent.setEnterpriseName(reqVO.getEnterpriseName());
        ent.setCompanyCreditCode(code);
        ent.setCoreContact(reqVO.getCoreContact());
        entInfoMapper.insert(ent);

        PfbSystemUserEntInfoDO link = new PfbSystemUserEntInfoDO();
        link.setId(UUID.randomUUID().toString(true));
        link.setSysUserId(sysUserId);
        link.setEntId(entId);
        link.setIsDefault(first ? 1 : 0);
        userEntMapper.insert(link);

        if (first) {
            upsertDefaultEnt(sysUserId, entId);
        }
        return entId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnt(long sysUserId, String entId, PfbEnterpriseUpdateReqVO reqVO) {
        assertUserOwnsEnt(sysUserId, entId);
        PfbEntInfoDO ent = entInfoMapper.selectById(entId);
        if (ent == null) {
            throw exception(PfbErrorCodeConstants.PFB_ENT_NOT_FOUND);
        }
        if (StrUtil.isNotBlank(reqVO.getEnterpriseName())) {
            ent.setEnterpriseName(reqVO.getEnterpriseName());
        }
        if (StrUtil.isNotBlank(reqVO.getEnglishName())) {
            ent.setEnglishName(reqVO.getEnglishName());
        }
        if (StrUtil.isNotBlank(reqVO.getCoreContact())) {
            ent.setCoreContact(reqVO.getCoreContact());
        }
        entInfoMapper.updateById(ent);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setDefault(long sysUserId, String entId) {
        assertUserOwnsEnt(sysUserId, entId);
        userEntMapper.update(null, new LambdaUpdateWrapper<PfbSystemUserEntInfoDO>()
                .eq(PfbSystemUserEntInfoDO::getSysUserId, sysUserId)
                .eq(PfbSystemUserEntInfoDO::getDeleted, false)
                .set(PfbSystemUserEntInfoDO::getIsDefault, 0));
        userEntMapper.update(null, new LambdaUpdateWrapper<PfbSystemUserEntInfoDO>()
                .eq(PfbSystemUserEntInfoDO::getSysUserId, sysUserId)
                .eq(PfbSystemUserEntInfoDO::getEntId, entId)
                .eq(PfbSystemUserEntInfoDO::getDeleted, false)
                .set(PfbSystemUserEntInfoDO::getIsDefault, 1));
        upsertDefaultEnt(sysUserId, entId);
    }

    @Override
    public void legalVerifyStub(long sysUserId, String entId) {
        assertUserOwnsEnt(sysUserId, entId);
        // 一期占位：二期对接腾讯/法人实人
    }

    private void assertUserOwnsEnt(long sysUserId, String entId) {
        long c = userEntMapper.selectCount(
                new LambdaQueryWrapper<PfbSystemUserEntInfoDO>()
                        .eq(PfbSystemUserEntInfoDO::getSysUserId, sysUserId)
                        .eq(PfbSystemUserEntInfoDO::getEntId, entId)
                        .eq(PfbSystemUserEntInfoDO::getDeleted, false));
        if (c == 0) {
            throw exception(PfbErrorCodeConstants.PFB_ENT_NO_PERMISSION);
        }
    }

    private void upsertDefaultEnt(long sysUserId, String entId) {
        PfbSystemUserExtDO ext = userExtMapper.selectById(sysUserId);
        if (ext == null) {
            ext = new PfbSystemUserExtDO();
            ext.setSysUserId(sysUserId);
            ext.setDefaultEntId(entId);
            userExtMapper.insert(ext);
        } else {
            ext.setDefaultEntId(entId);
            userExtMapper.updateById(ext);
        }
    }
}
