package com.ruoyi.system.controller;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.context.SecurityContextHolder;
import com.ruoyi.common.core.utils.ServletUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/profile")
public class SysProfileController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 从已经登陆的用户里面获取相应的角色名称
     *
     * @return
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
        SysUser sysUser = loginUser.getSysUser();
        AjaxResult ajax = AjaxResult.success(loginUser.getSysUser());
        String roleNames;
        List<SysRole> roles = sysUser.getRoles();
        if (!roles.isEmpty()) {
            roleNames = StringUtils.EMPTY;
        } else {
            roleNames = roles.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
        }
        return ajax;
    }
}
