package com.hj.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.MenuTreeVo;
import com.hj.domain.VO.MenuVo;
import com.hj.domain.VO.RoleMenuTreeSelectVo;
import com.hj.domain.entity.Menu;
import com.hj.serivce.MenuService;
import com.hj.utils.BeanCopyUtils;
import com.hj.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/system/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单列表,
     */
    @GetMapping("/list")
    public ResponseResult list(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);

    }

    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    @PostMapping
    public ResponseResult addMenu(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单
     * 需判断是否有子菜单,如果有子菜单则提示
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseResult delete(@PathVariable Long id) {
        if (menuService.hasChild(id)) {
            return ResponseResult.errorResult(500, "存在子菜单不允许删除");
        }
        menuService.removeById(id);
        return ResponseResult.okResult();

    }

    /**
     * 更新表回显
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseResult selectMenu(@PathVariable Long id) {
        return menuService.selectMenu(id);

    }

    /**
     * 更新菜单
     * 不能把父菜单设置当当前菜单
     *
     * @param menu
     * @return
     */
    @PutMapping
    public ResponseResult updateMenu(@RequestBody Menu menu) {

        return menuService.updateMenu(menu);
    }

    /**
     * 添加菜单信息回显,树状结构,难点
     *
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeSelect() {
        //查询菜单信息,复用selectMenuList方法,无需参数查询所有菜单
        List<Menu> menus = menuService.selectMenuList(new Menu());
        //构建树结构
        List<MenuTreeVo> menuVos = SystemConverter.buildMenuSelectTree(menus);

        return ResponseResult.okResult(menuVos);

    }

    /**
     * 更新角色信息回显数据
     *
     * @param id
     * @return
     */
    //TODO 难点
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable Long id) {
        //查询所有菜单列表
        List<Menu> menus = menuService.selectMenuList(new Menu());
        //查询角色关联的菜单id表
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(id);
        //构建树
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo roleMenuTreeSelectVo = new RoleMenuTreeSelectVo(checkedKeys, menuTreeVos);
        return ResponseResult.okResult(roleMenuTreeSelectVo);

    }



}
