package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.mapper.SysRoleMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.SysMenuService;
import com.ruoyi.system.service.SysPermissionService;
import com.ruoyi.system.service.SysRoleService;
import com.ruoyi.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

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


    /**
     * 通过username查找user，并且填充相关的权限信息
     * @param username
     * @return
     */
    public RequestResult<LoginUser> setUserInformation(String username) {
        SysUser user = sysUserService.getUserByUsername(username);
        if (user == null) {
            return RequestResult.fail("用户名或密码错误");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(user);
        //获取相关的权限
        Long userId = user.getUserId();
//        List<SysRole> sysRoles = sysRoleMapper.selectAllByUserId(userId);
//        user.setRoles(sysRoles);

        // 把roleKey用，拼接起来
        Set<String> roles = new HashSet<>();
        for (SysRole role : user.getRoles()) {
            roles.addAll(Arrays.asList(role.getRoleKey().split(",")));
        }

        Set<String> permissions = this.selectMenuPermissionsByUserId(userId);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);
        return RequestResult.success(loginUser);
    }
}
