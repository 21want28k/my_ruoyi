package com.ruoyi.system.controller;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.service.SysPermissionService;
import com.ruoyi.system.service.SysRoleService;
import com.ruoyi.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysPermissionService permissionService;

    @GetMapping("/info/{username}")
    private RequestResult<LoginUser> getUserInfo(@PathVariable("username") String username) {
        SysUser user = userService.getUserByUsername(username);
        if (user == null) {
            return RequestResult.fail("用户名或密码错误");
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(user);

        //获取相关的权限
        Long userId = user.getUserId();
        Set<String> roles = permissionService.selectRolesByUserId(userId);
        Set<String> permissions = permissionService.selectMenuPermissionsByUserId(userId);
        loginUser.setRoles(roles);
        loginUser.setPermissions(permissions);

        return RequestResult.success(loginUser);
    }

    /**
     * get roles and permissions of logged in user 获取已经登录过的用户的角色和权限
     *
     * @return
     */
    @GetMapping("/getInfo")
    public AjaxResult getRolesAndPermissions() {
        AjaxResult ajaxResult = AjaxResult.success();
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            return AjaxResult.error("该用户没有登录");
        }

//        Long userId = loginUser.getUserid();
//        Set<String> roles = permissionService.selectRolesByUserId(userId);
//
//        Set<String> perms = permissionService.selectMenuPermissionsByUserId(userId);
//        ajaxResult.put("roles", roles);
//        ajaxResult.put("user", loginUser.getSysUser());
//        ajaxResult.put("permissions", perms);
        ajaxResult.put("roles", loginUser.getRoles());
        ajaxResult.put("user", loginUser.getSysUser());
        ajaxResult.put("permissions", loginUser.getPermissions());
        return ajaxResult;
    }
}