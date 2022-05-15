package com.ruoyi.system.mapper;

import com.ruoyi.system.api.domain.SysRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author hhg
 * @description 针对表【sys_role(角色信息表)】的数据库操作Mapper
 * @createDate 2022-04-28 09:42:43
 * @Entity com.ruoyi.system.SysRole
 */
@Repository
public interface SysRoleMapper {
    List<SysRole> selectAllByUserId(@Param("userId") Long userId);

    Set<String> selectAllByUserId2(@Param("userId") Long userId);
}




