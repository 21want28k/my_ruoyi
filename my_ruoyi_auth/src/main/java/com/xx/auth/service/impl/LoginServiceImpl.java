package com.xx.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.enums.UserStatus;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.ServletUtils;
import com.ruoyi.common.core.utils.ip.IpUtils;
import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.domain.SysLogininfor;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.model.LoginUser;
import com.ruoyi.system.api.service.RemoteLogService;
import com.ruoyi.system.api.service.RemoteUserService;
import com.xx.auth.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RemoteLogService remoteLogService;

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     *
     * 在日志表里面记录相关信息
     * @param username
     * @param status
     * @param message
     */
    @Override
    public void recordLogininfor(String username, String status, String message) {
        SysLogininfor sysLogininfor = new SysLogininfor();
        sysLogininfor.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        sysLogininfor.setMsg(message);
        sysLogininfor.setStatus(status);
        sysLogininfor.setUserName(username);

        if (Constants.LOGIN_SUCCESS.equals(status) || Constants.REGISTER.equals(status) || Constants.LOGOUT.equals(status)) {
            sysLogininfor.setStatus(Constants.LOGIN_SUCESS_STATUS_CODE);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            sysLogininfor.setStatus(Constants.LOGIN_FAIL_STATUS_CODE);
        }
        remoteLogService.saveLogininfor(sysLogininfor, SecurityConstants.INNER);
    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public LoginUser login(String username, String password) {
        if (StrUtil.hasBlank(username, password)) {
            this.recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }


        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            this.recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            this.recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }

        RequestResult<LoginUser> userRequestResult = remoteUserService.getUserInfo(username, SecurityConstants.INNER);
        if (HttpStatus.ERROR == userRequestResult.getCode()) {
            throw new ServiceException(userRequestResult.getMsg());
        }

        if (userRequestResult.getData() == null) {
            this.recordLogininfor(username, Constants.LOGIN_FAIL, "登录的用户名不存在");
            throw new ServiceException("登录的用户名" + username + "不存在");
        }

        LoginUser userInfo = userRequestResult.getData();
        SysUser user = userRequestResult.getData().getSysUser();
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            this.recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            this.recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            this.recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码错误");
            throw new ServiceException("用户不存在/密码错误");
        }
        this.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    public void logout(String loginName)
    {
        recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }
}
