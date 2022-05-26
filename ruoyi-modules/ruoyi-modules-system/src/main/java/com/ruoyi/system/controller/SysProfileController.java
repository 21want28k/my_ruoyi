package com.ruoyi.system.controller;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.context.SecurityContextHolder;
import com.ruoyi.common.core.utils.CheckUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.service.TokenService;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysRole;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.service.SysPostService;
import com.ruoyi.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/profile")
public class SysProfileController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysPostService sysPostService;

    @Autowired
    private TokenService tokenService;

    /**
     * 从已经登陆的用户里面获取相应的角色名称
     *
     * @return
     */
    @GetMapping
    public AjaxResult profile() {
        LoginUser loginUser = SecurityContextHolder.get(SecurityConstants.LOGIN_USER, LoginUser.class);
        SysUser sysUser = loginUser.getSysUser();
        String roleNames = getRoleGroupFromUser(sysUser);
        String postGroup = sysPostService.selectUserPostGroup(sysUser.getUserId());

        AjaxResult ajax = AjaxResult.success(loginUser.getSysUser());
        ajax.put("roleGroup", roleNames);
        ajax.put("postGroup", postGroup);
        return ajax;
    }

    @PutMapping
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    public AjaxResult updateProfile(@RequestBody SysUser user) {
        // 连接数据库检查唯一性
        if (StringUtils.isEmpty(user.getPhonenumber())
                && UserConstants.NOT_UNIQUE.equals(sysUserService.checkPhoneNumberUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (StringUtils.isEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(sysUserService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }

        // 更新数据库中的数据
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser sysUser = loginUser.getSysUser();
        user.setUserName(sysUser.getUserName());
        user.setUserId(sysUser.getUserId());
        user.setPassword(null);
        if (sysUserService.updateUserProfile(user) <= 0) {
            return AjaxResult.error("修改失败出现异常，请联系管理员");
        }

        // 更新缓存用户信息
        loginUser.getSysUser().setNickName(user.getNickName());
        loginUser.getSysUser().setPhonenumber(user.getPhonenumber());
        loginUser.getSysUser().setEmail(user.getEmail());
        loginUser.getSysUser().setSex(user.getSex());
        tokenService.setLoginUser(loginUser);

        return AjaxResult.success();
    }

    /**
     * 拼接sysUser中的角色名字list，用，隔开形成字符串比如：list = [role1,role2,role3] => string = role1,role2,role3
     *
     * @param sysUser
     * @return
     */
    private String getRoleGroupFromUser(SysUser sysUser) {
        String roleNames;
        List<SysRole> roles = sysUser.getRoles();
        if (CollectionUtils.isEmpty(roles)) {
            roleNames = StringUtils.EMPTY;
        } else {
            roleNames = roles.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
        }
        return roleNames;
    }
}
