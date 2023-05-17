package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.MenuVo;
import com.hj.domain.entity.Menu;
import com.hj.mapper.MenuMapper;
import com.hj.serivce.MenuService;
import com.hj.utils.BeanCopyUtils;
import com.hj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.hj.constants.SystemConstants.*;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 09:09:46
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    /**
     * 根据id查询角色信息
     * 如果用户id为1代表管理员，roles 中只需要有admin，permissions中需要有所有菜单类型为C或者F的，状态为正常的，未被删除的权限
     *
     * @param id
     * @return
     */
    @Override
    public List<String> selectPermsByUserId(Long id) {

        //如果是管理员返回所以权限
        //否则返回其具有的权限
        if (id == 1L) {
            //管理员返回所有权限
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, MENU, BUTTON);
            queryWrapper.eq(Menu::getStatus, STATUS_NORMAL);
            List<Menu> list = list(queryWrapper);
            List<String> perms = list.stream().map(Menu::getPerms) //直接用map转换为getPerms
                    .collect(Collectors.toList());
            //getBaseMapper获取的是menuMapper的方法
            return perms;
        }
        //否则返回用户具有的信息,多表查询,手写mapper

        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        //判断是否是管理员用户
         List<Menu> menus =null;
        if (SecurityUtils.isAdmin()) {
            //true 返回符合条件的menu
            menus = menuMapper.selectAllRouterMenu();
        }else {
                //否则返回当前用户的menu
            menus = menuMapper.selectRouterMenuTreeByUserId();
        }

        //构建tree 体现层级关系.
        List<Menu> menuTree = builderMenuTree(menus,0L);


       return menuTree;
    }

    @Override
    public List<Menu> selectMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper
                .like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName())
                .eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus())
                .orderByAsc(Menu::getParentId,Menu::getOrderNum);//菜单要按照父菜单id和orderNum进行排序
        return list(menuLambdaQueryWrapper);
    }

    /**
     * 判断是否存子菜单
     * 通过查询此id是否存在于parent_id一栏
     * @param id
     * @return
     */
    @Override
    public boolean hasChild(Long id) {
        LambdaQueryWrapper<Menu> menuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        menuLambdaQueryWrapper.eq(Menu::getParentId,id);
        return count(menuLambdaQueryWrapper) !=0; //0个就代表没有子菜单

    }

    @Override
    public ResponseResult selectMenu(Long id) {
        Menu menu = menuMapper.selectById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        //判断parent_id是否是当前id
        if (menu.getId() == menu.getParentId()){
            return ResponseResult.errorResult(500,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long id) {
       return getBaseMapper().selectMenuListByRoleId(id);
    }

    private List<Menu> builderMenuTree(List<Menu> menus,Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId)) //找到条件为0的父菜单
                .map(menu -> menu.setChildren(getChildren(menu, menus))) //找到子菜单然后设置为children
                .collect(Collectors.toList());

        return menuTree;
    }

    /**
     * 获取传入参数的子菜单menu
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId())) //在menus中找到的传入menu的子菜单然后返回
                .map(m->m.setChildren(getChildren(m,menus)))//递归调用找到子菜单的子菜单
                .collect(Collectors.toList());
        return childrenList;

    }

}

