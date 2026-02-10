package com.wmt.module.product.dal.mysql.brand;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.mybatis.core.mapper.BaseMapperX;
import com.wmt.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.wmt.module.product.controller.admin.brand.vo.ProductBrandListReqVO;
import com.wmt.module.product.controller.admin.brand.vo.ProductBrandPageReqVO;
import com.wmt.module.product.dal.dataobject.brand.ProductBrandDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductBrandMapper extends BaseMapperX<ProductBrandDO> {

    default PageResult<ProductBrandDO> selectPage(ProductBrandPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductBrandDO>()
                .likeIfPresent(ProductBrandDO::getName, reqVO.getName())
                .eqIfPresent(ProductBrandDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ProductBrandDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductBrandDO::getId));
    }


    default List<ProductBrandDO> selectList(ProductBrandListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProductBrandDO>()
                .likeIfPresent(ProductBrandDO::getName, reqVO.getName()));
    }

    default ProductBrandDO selectByName(String name) {
        return selectOne(ProductBrandDO::getName, name);
    }

    default List<ProductBrandDO> selectListByStatus(Integer status) {
        return selectList(ProductBrandDO::getStatus, status);
    }
}
