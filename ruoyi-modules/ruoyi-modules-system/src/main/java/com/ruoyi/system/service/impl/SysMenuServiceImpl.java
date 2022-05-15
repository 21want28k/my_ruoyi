package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.constant.Constants;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.domain.SysMenu;
import com.ruoyi.system.domain.vo.MetaVo;
import com.ruoyi.system.domain.vo.RouterVo;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hhg
 * @description 针对表【sys_menu(菜单权限表)】的数据库操作Service实现
 * @createDate 2022-04-28 14:59:04
 */
@Service
public class SysMenuServiceImpl implements SysMenuService {


    @Autowired
    private SysMenuMapper menuMapper;

    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        Set<String> perms = menuMapper.selectPermsByUserId(userId);
        return perms.stream().filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
    }

    /**
     * 根据userId查菜单
     */
    @Override
    public List<SysMenu> selectMenuTreesByUserId(Long userId) {
        List<SysMenu> menus;
        if (SecurityUtils.isAdmin2()) {
            menus = menuMapper.selectAllMenuTrees();
        } else {
            menus = menuMapper.selectMenuTreesByUserId(userId);
        }
        return buildRelation(menus, 0);
    }

    /**
     * 获取所有的菜单
     */
    @Override
    public List<SysMenu> selectAllMenuTrees() {
        return menuMapper.selectAllMenuTrees();
    }

    /**
     * 填充parent这个菜单下一级菜单的所有菜单，比如0下面有1，2，3，4 分别对这4个菜单进行填充子菜单，也就是说保留parentId这下面的4个子菜单。
     * 所以返回的是list
     *
     * @param menus    所有的菜单
     * @param parentId 父菜单
     * @return parent 下一级菜单
     */
    public List<SysMenu> buildRelation(List<SysMenu> menus, int parentId) {
        List<SysMenu> result = new ArrayList<>();
        for (SysMenu m : menus) {
            Long mParentId = m.getParentId();
            if (mParentId != null && mParentId == parentId) {
                setChildrenByRecursion(menus, m);
                result.add(m);
            }
        }
        return result;
    }

    /**
     * 利用递归给父菜单填充子菜单
     *
     * @param menus  所有的菜单
     * @param parent 父菜单
     */
    public void setChildrenByRecursion(List<SysMenu> menus, SysMenu parent) {
        for (SysMenu m : menus) {
            if (m.getParentId().equals(parent.getMenuId())) {
                List<SysMenu> list = getChildrenFromSet(menus, parent);
                if (list.isEmpty()) {
                    return;
                }
                parent.setChildren(list);
                setChildrenByRecursion(menus, m);
            }
        }
    }

    /**
     * 从菜单中找到子菜单
     *
     * @param menus  所有的菜单
     * @param parent 父菜单
     * @return 子菜单
     */
    public List<SysMenu> getChildrenFromSet(List<SysMenu> menus, SysMenu parent) {
        List<SysMenu> result = new ArrayList<>();
        for (SysMenu menu1 : menus) {
            if (menu1.getParentId().equals(parent.getMenuId())) {
                result.add(menu1);
            }
        }
        return result;
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    @Override
    public List<RouterVo> buildMenus(List<SysMenu> menus) {
        List<RouterVo> routers = new LinkedList<RouterVo>();
        for (SysMenu menu : menus) {
            RouterVo router = new RouterVo();
            router.setHidden("1".equals(menu.getVisible()));
            router.setName(getRouteName(menu));
            router.setPath(getRouterPath(menu));
            router.setComponent(getComponent(menu));
            router.setQuery(menu.getQuery());
            router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
            List<SysMenu> cMenus = menu.getChildren();
            if (!cMenus.isEmpty() && cMenus.size() > 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                router.setAlwaysShow(true);
                router.setRedirect("noRedirect");
                router.setChildren(buildMenus(cMenus));
            } else if (isMenuFrame(menu)) {
                router.setMeta(null);
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                children.setPath(menu.getPath());
                children.setComponent(menu.getComponent());
                children.setName(StringUtils.capitalize(menu.getPath()));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), StringUtils.equals("1", menu.getIsCache()), menu.getPath()));
                children.setQuery(menu.getQuery());
                childrenList.add(children);
                router.setChildren(childrenList);
            } else if (menu.getParentId().intValue() == 0 && isInnerLink(menu)) {
                router.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon()));
                router.setPath("/inner");
                List<RouterVo> childrenList = new ArrayList<RouterVo>();
                RouterVo children = new RouterVo();
                String routerPath = innerLinkReplaceEach(menu.getPath());
                children.setPath(routerPath);
                children.setComponent(UserConstants.INNER_LINK);
                children.setName(StringUtils.capitalize(routerPath));
                children.setMeta(new MetaVo(menu.getMenuName(), menu.getIcon(), menu.getPath()));
                childrenList.add(children);
                router.setChildren(childrenList);
            }
            routers.add(router);
        }
        return routers;
    }

    /**
     * 获取路由名称
     *
     * @param menu 菜单信息
     * @return 路由名称
     */
    public String getRouteName(SysMenu menu) {
        String routerName = StringUtils.capitalize(menu.getPath());
        // 非外链并且是一级目录（类型为目录）
        if (isMenuFrame(menu)) {
            routerName = StringUtils.EMPTY;
        }
        return routerName;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    public String getRouterPath(SysMenu menu) {
        String routerPath = menu.getPath();
        // 内链打开外网方式
        if (menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            routerPath = innerLinkReplaceEach(routerPath);
        }
        // 非外链并且是一级目录（类型为目录）
        if (0 == menu.getParentId().intValue() && UserConstants.TYPE_DIR.equals(menu.getMenuType())
                && UserConstants.NO_FRAME.equals(menu.getIsFrame())) {
            routerPath = "/" + menu.getPath();
        }
        // 非外链并且是一级目录（类型为菜单）
        else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }


    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isMenuFrame(SysMenu menu) {
        return menu.getParentId().intValue() == 0 && UserConstants.TYPE_MENU.equals(menu.getMenuType())
                && menu.getIsFrame().equals(UserConstants.NO_FRAME);
    }

    /**
     * 是否为内链组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isInnerLink(SysMenu menu) {
        return menu.getIsFrame().equals(UserConstants.NO_FRAME) && StringUtils.ishttp(menu.getPath());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(SysMenu menu) {
        return menu.getParentId().intValue() != 0 && UserConstants.TYPE_DIR.equals(menu.getMenuType());
    }

    /**
     * 内链域名特殊字符替换
     *
     * @return
     */
    public String innerLinkReplaceEach(String path) {
        return StringUtils.replaceEach(path, new String[]{Constants.HTTP, Constants.HTTPS},
                new String[]{"", ""});
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(SysMenu menu) {
        String component = UserConstants.LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && menu.getParentId().intValue() != 0 && isInnerLink(menu)) {
            component = UserConstants.INNER_LINK;
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = UserConstants.PARENT_VIEW;
        }
        return component;
    }
}




