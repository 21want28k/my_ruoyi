package com.ruoyi.system.service.impl;

import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hhg
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service实现
 * @createDate 2022-03-15 14:43:40
 */
@Service
public class SysUserServiceImpl implements SysUserService {


    @Autowired
    private SysUserMapper sysUserMapper;

    public SysUser getUserByUsername(String username) {
        return sysUserMapper.selectByUserName(username);
    }
}