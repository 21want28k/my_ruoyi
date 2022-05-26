package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.constant.UserConstants;
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

    /**
     * user dept role三表 连接，填充user
     *
     * @param username
     * @return
     */
    public SysUser getUserByUsername(String username) {
        return sysUserMapper.selectByUserName(username);
    }

    /**
     * 检查电话号码的唯一性
     *
     * @param user
     * @return
     */
    public String checkPhoneNumberUnique(SysUser user) {
        long userId = user.getUserId() == null ? -1L : user.getUserId();
        SysUser info = sysUserMapper.selectOneByEmail(user.getPhonenumber());
        // 对id的判断是因为如果修改之后的是刚好是自己用的，也就是说信息并没有变
        if (info != null && userId != info.getUserId()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 检查邮箱的唯一性
     *
     * @param user
     * @return
     */
    public String checkEmailUnique(SysUser user) {
        long userId = user.getUserId() == null ? -1L : user.getUserId();
        SysUser info = sysUserMapper.selectByPhonenumber(user.getPhonenumber());
        if (info != null && userId == info.getUserId()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 更新user信息
     *
     * @param user
     * @return
     */
    public int updateUserProfile(SysUser user) {
        return sysUserMapper.updateUser(user);
    }
}