package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author hhg
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Mapper
 * @createDate 2022-04-28 14:59:04
 * @Entity com.ruoyi.system.SysMenu
 */
@Repository
public interface SysMenuMapper {
    /**
     * 根据userId 查权限
     */
    Set<String> selectPermsByUserId(@Param("userId") Long userId);

    /**
     * 根据userId查菜单
     */
    List<SysMenu> selectMenuTreesByUserId(@Param("userId") Long userId);

    /**
     * 获取所有的菜单
     */
    List<SysMenu> selectAllMenuTrees();
}




