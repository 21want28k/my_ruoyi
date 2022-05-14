package com.ruoyi.system.service;

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

}