package com.ruoyi.system.service;

import com.ruoyi.system.api.domain.SysRole;

import java.util.List;
import java.util.Set;

/**
 * @author hhg
 * @description 针对表【sys_role(角色信息表)】的数据库操作Service
 * @createDate 2022-04-28 09:42:43
 */
public interface SysRoleService {
    Set<String> selectRolesByUserId(Long userId);

    Set<String> selectRolesByUserId2(Long userId);
}
