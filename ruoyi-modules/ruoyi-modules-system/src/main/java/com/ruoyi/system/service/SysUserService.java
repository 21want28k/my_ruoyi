package com.ruoyi.system.service;

import com.ruoyi.system.api.domain.SysUser;

/**
 * @author hhg
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service
 * @createDate 2022-03-15 14:43:40
 */
public interface SysUserService {
    public SysUser getUserByUsername(String username);
}
