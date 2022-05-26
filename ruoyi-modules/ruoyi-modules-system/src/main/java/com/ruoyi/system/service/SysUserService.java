package com.ruoyi.system.service;

import com.ruoyi.system.api.domain.SysUser;

/**
 * @author hhg
 * @description 针对表【sys_user(用户信息表)】的数据库操作Service
 * @createDate 2022-03-15 14:43:40
 */
public interface SysUserService {
    /**
     * user dept role三表 连接，填充user
     *
     * @param username
     * @return
     */
    public SysUser getUserByUsername(String username);

    /**
     * 检查电话号码的唯一性
     *
     * @param user
     * @return
     */
    public String checkPhoneNumberUnique(SysUser user);


    /**
     * 检查邮箱的唯一性
     *
     * @param user
     * @return
     */
    public String checkEmailUnique(SysUser user);

    /**
     * 更新user信息
     *
     * @param user
     * @return
     */
    public int updateUserProfile(SysUser user);
}
