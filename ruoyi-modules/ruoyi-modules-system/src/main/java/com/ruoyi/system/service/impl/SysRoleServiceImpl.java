package com.ruoyi.system.service.impl;

import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author hhg
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service实现
 * @createDate 2022-04-28 09:42:43
 */
@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public Set<String> selectRolesByUserId(Long userId) {
        List<SysRole> sysRoles = sysRoleMapper.selectAllByUserId(userId);

        Set<String> permissions = new HashSet<>();
        for (SysRole role : sysRoles) {
            permissions.addAll(Arrays.asList(role.getRoleKey().split(",")));
        }
        return permissions;
    }

    @Override
    public Set<String> selectRolesByUserId2(Long userId) {
        Set<String> roles = sysRoleMapper.selectAllByUserId2(userId);

        Set<String> permissions = new HashSet<>();
        for (String role : roles) {
            permissions.addAll(Arrays.asList(role.split(",")));
        }
        return permissions;
    }
}




