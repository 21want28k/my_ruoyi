package com.ruoyi.system.service;

import com.ruoyi.system.domain.SysMenu;
import com.ruoyi.system.domain.vo.RouterVo;

import java.util.List;
import java.util.Set;

/**
 * @author hhg
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service
 * @createDate 2022-04-28 14:59:04
 */
public interface SysMenuService {
    public Set<String> selectPermsByUserId(Long userId);

    /**
     * 根据userId查菜单
     */
    List<SysMenu> selectMenuTreesByUserId(Long userId);

    /**
     * 获取所有的菜单
     */
    List<SysMenu> selectAllMenuTrees();

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<SysMenu> menus);
}
