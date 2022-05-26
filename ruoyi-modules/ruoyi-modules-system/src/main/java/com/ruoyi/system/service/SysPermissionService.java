package com.ruoyi.system.service;

import com.ruoyi.common.core.web.domain.RequestResult;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface SysPermissionService {
    public Set<String> selectRolesByUserId(Long userId);

    /**
     * 根据userId来查询用户菜单的权限
     *
     * @param userId
     * @return
     */
    public Set<String> selectMenuPermissionsByUserId(Long userId);

    /**
     * 通过username查找user，并且填充相关的权限信息
     * @param username
     * @return
     */
    public RequestResult<LoginUser> setUserInformation(String username);
}