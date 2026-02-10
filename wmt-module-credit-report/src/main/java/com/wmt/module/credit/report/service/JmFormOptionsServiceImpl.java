package com.wmt.module.credit.report.service;

import cn.hutool.core.collection.CollUtil;
import com.wmt.framework.common.enums.CommonStatusEnum;
import com.wmt.module.credit.report.framework.jmreport.vo.DeptOptionsVO;
import com.wmt.module.credit.report.framework.jmreport.vo.OptionsResult;
import com.wmt.module.credit.report.framework.jmreport.vo.RoleOptionsVO;
import com.wmt.module.credit.report.framework.jmreport.vo.UserOptionsVO;
import com.wmt.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import com.wmt.module.system.dal.dataobject.dept.DeptDO;
import com.wmt.module.system.dal.dataobject.permission.RoleDO;
import com.wmt.module.system.dal.dataobject.user.AdminUserDO;
import com.wmt.module.system.service.dept.DeptService;
import com.wmt.module.system.service.permission.RoleService;
import com.wmt.module.system.service.user.AdminUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.jmreport.api.JmFormOptionsServiceI;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义选项数据获取实现类
 * 用于积木报表中的组织角色、部门、用户组件数据获取
 *
 * @author Auto
 * @date 2026/01/29
 */
@Slf4j
@Service("jmFormOptionsService")
public class JmFormOptionsServiceImpl implements JmFormOptionsServiceI {

    @Resource
    private RoleService roleService;

    @Resource
    private DeptService deptService;

    @Resource
    private AdminUserService userService;

    /**
     * 获取角色列表
     *
     * @return 角色选项结果
     */
    public OptionsResult<RoleOptionsVO> getRoleList() {
        try {
            // 获取所有启用的角色
            List<RoleDO> roleList = roleService.getRoleListByStatus(
                    Collections.singleton(CommonStatusEnum.ENABLE.getStatus())
            );

            if (CollUtil.isEmpty(roleList)) {
                return OptionsResult.success(new ArrayList<>());
            }

            // 转换为积木报表需要的格式
            List<RoleOptionsVO> roleOptionsList = roleList.stream()
                    .map(role -> {
                        RoleOptionsVO vo = new RoleOptionsVO();
                        vo.setId(String.valueOf(role.getId()));
                        vo.setName(role.getName());
                        vo.setCode(role.getCode());
                        return vo;
                    })
                    .collect(Collectors.toList());

            return OptionsResult.success(roleOptionsList);
        } catch (Exception e) {
            log.error("获取角色列表失败", e);
            return OptionsResult.error("获取角色列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取部门列表
     *
     * @return 部门选项结果
     */
    public OptionsResult<DeptOptionsVO> getDeptList() {
        try {
            // 构建查询条件，只查询启用的部门
            DeptListReqVO reqVO = new DeptListReqVO();
            reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
            List<DeptDO> deptList = deptService.getDeptList(reqVO);

            if (CollUtil.isEmpty(deptList)) {
                return OptionsResult.success(new ArrayList<>());
            }

            // 转换为积木报表需要的格式
            List<DeptOptionsVO> deptOptionsList = deptList.stream()
                    .map(dept -> {
                        DeptOptionsVO vo = new DeptOptionsVO();
                        vo.setId(String.valueOf(dept.getId()));
                        vo.setName(dept.getName());
                        // 处理父部门ID，如果为0或null，设置为"0"表示根节点
                        Long parentId = dept.getParentId();
                        if (parentId == null || parentId.equals(DeptDO.PARENT_ID_ROOT)) {
                            vo.setParentId("0");
                        } else {
                            vo.setParentId(String.valueOf(parentId));
                        }
                        return vo;
                    })
                    .collect(Collectors.toList());

            return OptionsResult.success(deptOptionsList);
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            return OptionsResult.error("获取部门列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户列表
     *
     * @return 用户选项结果
     */
    public OptionsResult<UserOptionsVO> getUserList() {
        try {
            // 获取所有启用的用户
            List<AdminUserDO> userList = userService.getUserListByStatus(
                    CommonStatusEnum.ENABLE.getStatus()
            );

            if (CollUtil.isEmpty(userList)) {
                return OptionsResult.success(new ArrayList<>());
            }

            // 转换为积木报表需要的格式
            List<UserOptionsVO> userOptionsList = userList.stream()
                    .map(user -> {
                        UserOptionsVO vo = new UserOptionsVO();
                        vo.setId(String.valueOf(user.getId()));
                        vo.setUsername(user.getUsername());
                        vo.setNickname(user.getNickname());
                        vo.setDeptId(user.getDeptId() != null ? String.valueOf(user.getDeptId()) : null);
                        return vo;
                    })
                    .collect(Collectors.toList());

            return OptionsResult.success(userOptionsList);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return OptionsResult.error("获取用户列表失败：" + e.getMessage());
        }
    }
}
