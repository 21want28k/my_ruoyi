package com.xx.auth.service;

import com.ruoyi.system.api.model.LoginUser;

public interface LoginService {
    void recordLogininfor(String username, String status, String message);

    LoginUser login(String username, String password);

    public void logout(String loginName);
}
