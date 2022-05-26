package com.xx.auth.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.JwtUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.common.security.auth.AuthUtil;
import com.ruoyi.common.security.service.TokenService;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.api.service.RemoteLogService;
import com.ruoyi.system.api.service.RemoteUserService;
import com.xx.auth.form.LoginBody;
import com.xx.auth.service.LoginService;
import com.xx.auth.service.impl.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public RequestResult<?> login(@RequestBody LoginBody loginBody) {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        LoginUser user = loginService.login(username, password);
        return RequestResult.success(tokenService.createToken(user));
    }

    @DeleteMapping("logout")
    public RequestResult<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志
            loginService.logout(username);
        }
        return RequestResult.success();
    }
}