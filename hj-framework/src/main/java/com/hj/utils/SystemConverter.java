package com.hj.utils;

import com.hj.domain.VO.MenuTreeVo;
import com.hj.domain.VO.MenuVo;
import com.hj.domain.entity.Menu;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 构建菜单树结构工具类
 */
public class SystemConverter {

    private SystemConverter() {
    }


    public static List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus) {
        //把所有菜单转换为menuVo
        List<MenuTreeVo> collect = menus.stream()
                .map(menu -> new MenuTreeVo(menu.getId(), menu.getMenuName(), menu.getParentId(), null))
                .collect(Collectors.toList());

        //过滤掉非主目录的菜单
        List<MenuTreeVo> options = collect.stream()
                .filter(o -> o.getParentId().equals(0L))
                //设置子菜单
                .map(m -> m.setChildren(getChildrenList(collect, m)))
                .collect(Collectors.toList());
        return options;
    }

    /**
     * 查询子菜单
     *
     * @param collect 所有的菜单
     * @param m       当前的主页菜单
     * @return
     */
    private static List<MenuTreeVo> getChildrenList(List<MenuTreeVo> collect, MenuTreeVo m) {
        List<MenuTreeVo> options = collect.stream()
                //过滤掉所有父节点,如果传入的菜单节点id等于某个菜单的父id就说明这个节点不是子节点就过滤掉
                .filter(o -> Objects.equals(o.getParentId(), m.getId()))
                //设置子节点
                //递归调用方法,找到子节点的子节点
                .map(o -> o.setChildren(getChildrenList(collect, o)))
                .collect(Collectors.toList());
        return options;

    }
}
