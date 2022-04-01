package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/info/{username}")
    private RequestResult<LoginUser> getUserInfo(@PathVariable("username") String username) {
        SysUser user = sysUserService.getUserByUsername(username);
        LoginUser loginUser = new LoginUser();
        loginUser.setSysUser(user);
        return RequestResult.success(loginUser);
    }
}