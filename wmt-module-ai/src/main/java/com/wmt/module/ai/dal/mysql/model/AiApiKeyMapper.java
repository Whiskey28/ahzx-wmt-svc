package com.wmt.module.ai.dal.mysql.model;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.framework.mybatis.core.query.QueryWrapperX;
import com.wmt.module.ai.controller.admin.model.vo.apikey.AiApiKeyPageReqVO;
import com.wmt.module.ai.dal.dataobject.model.AiApiKeyDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI API 密钥 Mapper
 *
 * @author AHC源码
 */
@Mapper
public interface AiApiKeyMapper extends BaseMapperX<AiApiKeyDO> {

    default PageResult<AiApiKeyDO> selectPage(AiApiKeyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AiApiKeyDO>()
                .likeIfPresent(AiApiKeyDO::getName, reqVO.getName())
                .eqIfPresent(AiApiKeyDO::getPlatform, reqVO.getPlatform())
                .eqIfPresent(AiApiKeyDO::getStatus, reqVO.getStatus())
                .orderByDesc(AiApiKeyDO::getId));
    }

    default AiApiKeyDO selectFirstByPlatformAndStatus(String platform, Integer status) {
        return selectOne(new QueryWrapperX<AiApiKeyDO>()
                .eq("platform", platform)
                .eq("status", status)
                .limitN(1)
                .orderByAsc("id"));
    }

}
