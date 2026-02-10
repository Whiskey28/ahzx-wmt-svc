package com.wmt.module.member.convert.tag;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.module.member.controller.admin.tag.vo.MemberTagCreateReqVO;
import com.wmt.module.member.controller.admin.tag.vo.MemberTagRespVO;
import com.wmt.module.member.controller.admin.tag.vo.MemberTagUpdateReqVO;
import com.wmt.module.member.dal.dataobject.tag.MemberTagDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 会员标签 Convert
 *
 * @author AHC源码
 */
@Mapper
public interface MemberTagConvert {

    MemberTagConvert INSTANCE = Mappers.getMapper(MemberTagConvert.class);

    MemberTagDO convert(MemberTagCreateReqVO bean);

    MemberTagDO convert(MemberTagUpdateReqVO bean);

    MemberTagRespVO convert(MemberTagDO bean);

    List<MemberTagRespVO> convertList(List<MemberTagDO> list);

    PageResult<MemberTagRespVO> convertPage(PageResult<MemberTagDO> page);

}
