package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.service.SysMenuService;
import com.ruoyi.system.service.SysPermissionService;
import com.ruoyi.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public Set<String> selectRolesByUserId(Long userId) {

        return roleService.selectRolesByUserId(userId);
    }

    /**
     * 根据userId来查询用户菜单的权限
     *
     * @param userId
     * @return
     */
    @Override
    public Set<String> selectMenuPermissionsByUserId(Long userId) {

        return new HashSet<>(sysMenuService.selectPermsByUserId(userId));
    }

}
