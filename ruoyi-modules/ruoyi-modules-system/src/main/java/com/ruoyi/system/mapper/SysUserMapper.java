package com.ruoyi.system.mapper;

import com.ruoyi.system.api.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author hhg
 * @description 针对表【sys_user(用户信息表)】的数据库操作Mapper
 * @createDate 2022-03-15 14:43:40
 * @Entity com.ruoyi.system.domain.SysUser
 */
@Repository
public interface SysUserMapper {
    SysUser selectByUserName(@Param("userName") String userName);

    SysUser selectByPhonenumber(@Param("phoneNumber") String phoneNumber);

    SysUser selectOneByEmail(@Param("email") String email);

    int updateUser(SysUser user);
}




