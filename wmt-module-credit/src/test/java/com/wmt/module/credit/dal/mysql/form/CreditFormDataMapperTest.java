package com.wmt.module.credit.dal.mysql.form;

import com.wmt.framework.common.pojo.PageResult;
import com.wmt.framework.test.core.ut.BaseDbUnitTest;
import com.wmt.module.credit.controller.admin.form.vo.CreditFormDataPageReqVO;
import com.wmt.module.credit.dal.dataobject.form.CreditFormDataDO;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;

import static com.wmt.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.wmt.framework.test.core.util.RandomUtils.randomLongId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link CreditFormDataMapper} 的单元测试
 *
 * 只做最小可运行示例：
 * - insert + selectById
 * - selectPage
 *
 * @author AHC源码
 */
@Import(CreditFormDataMapper.class)
public class CreditFormDataMapperTest extends BaseDbUnitTest {

    @Resource
    private CreditFormDataMapper creditFormDataMapper;

    @Test
    public void testInsertAndSelectById() {
        // 准备数据
        CreditFormDataDO insertObj = new CreditFormDataDO();
        insertObj.setDeptId(1L);
        insertObj.setReportPeriod("2025-01");
        insertObj.setReportType("MONTHLY");
        insertObj.setStatus(1);
        insertObj.setSubmitUserId(1L);
        insertObj.setSubmitTime(LocalDateTime.now());
        // 插入
        creditFormDataMapper.insert(insertObj);

        // 查询
        CreditFormDataDO dbObj = creditFormDataMapper.selectById(insertObj.getId());
        assertNotNull(dbObj);
        assertPojoEquals(insertObj, dbObj, "createTime", "updateTime");
    }

    @Test
    public void testSelectPage() {
        // 插入一条符合条件的数据
        CreditFormDataDO match = new CreditFormDataDO();
        match.setDeptId(2L);
        match.setReportPeriod("2025-02");
        match.setReportType("MONTHLY");
        match.setStatus(1);
        creditFormDataMapper.insert(match);

        // 插入一条不匹配的数据（不同部门）
        CreditFormDataDO notMatch = new CreditFormDataDO();
        notMatch.setDeptId(3L);
        notMatch.setReportPeriod("2025-02");
        notMatch.setReportType("MONTHLY");
        notMatch.setStatus(1);
        creditFormDataMapper.insert(notMatch);

        // 构造查询条件：按 deptId=2L 查询
        CreditFormDataPageReqVO reqVO = new CreditFormDataPageReqVO();
        reqVO.setDeptId(2L);

        // 调用
        PageResult<CreditFormDataDO> pageResult = creditFormDataMapper.selectPage(reqVO);

        // 断言
        assertEquals(1, pageResult.getTotal());
        List<CreditFormDataDO> list = pageResult.getList();
        assertEquals(1, list.size());
        assertEquals(match.getDeptId(), list.get(0).getDeptId());
        assertEquals(match.getReportPeriod(), list.get(0).getReportPeriod());
    }

}

