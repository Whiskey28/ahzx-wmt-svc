package com.wmt.module.iot.dal.mysql.product;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.iot.controller.admin.product.vo.product.IotProductPageReqVO;
import com.wmt.module.iot.dal.dataobject.product.IotProductDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * IoT 产品 Mapper
 *
 * @author ahh
 */
@Mapper
public interface IotProductMapper extends BaseMapperX<IotProductDO> {

    default PageResult<IotProductDO> selectPage(IotProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<IotProductDO>()
                .likeIfPresent(IotProductDO::getName, reqVO.getName())
                .likeIfPresent(IotProductDO::getProductKey, reqVO.getProductKey())
                .orderByDesc(IotProductDO::getId));
    }

    default IotProductDO selectByProductKey(String productKey) {
        return selectOne(new LambdaQueryWrapper<IotProductDO>()
                .apply("LOWER(product_key) = {0}", productKey.toLowerCase()));
    }

    default Long selectCountByCreateTime(@Nullable LocalDateTime createTime) {
        return selectCount(new LambdaQueryWrapperX<IotProductDO>()
                .geIfPresent(IotProductDO::getCreateTime, createTime));
    }


}