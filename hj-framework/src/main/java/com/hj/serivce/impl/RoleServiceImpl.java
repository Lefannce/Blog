package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.constants.SystemConstants;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.PageVo;
import com.hj.domain.VO.RoleVo;
import com.hj.domain.entity.Role;
import com.hj.domain.entity.RoleMenu;
import com.hj.mapper.RoleMapper;
import com.hj.serivce.RoleMenuService;
import com.hj.serivce.RoleService;
import com.hj.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hj.constants.SystemConstants.NORMAL;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 09:18:24
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 根据id查询用户信息
     *
     * @param id
     * @return
     */
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是返回集合中只需要有admin
        if (id == 1L) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult roleList(Role role, Integer pageNum, Integer pageSize) {
        //根据角色列表名称(模糊查询)和状态进行查询
        LambdaQueryWrapper<Role> roleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleLambdaQueryWrapper.like(StringUtils.hasText(role.getRoleName()), Role::getRoleName, role.getRoleName());
        roleLambdaQueryWrapper.eq(StringUtils.hasText(role.getStatus()), Role::getStatus, role.getStatus());

        //根据role_sort排序
        roleLambdaQueryWrapper.orderByAsc(Role::getRoleSort);

        //分页查询
        Page<Role> rolePage = new Page<>();
        rolePage.setSize(pageSize);
        rolePage.setCurrent(pageNum);
        page(rolePage, roleLambdaQueryWrapper);
        //转换为RoleVo
        List<RoleVo> vs = BeanCopyUtils.copyBeanList(rolePage.getRecords(), RoleVo.class);

        //创建PageVo
        PageVo pageVo = new PageVo();
        pageVo.setRows(vs);
        pageVo.setTotal(rolePage.getTotal());
        return ResponseResult.okResult(pageVo);


    }

    /**
     * 新增角色方法
     *
     * @param role
     */
    @Override
    public void addRole(Role role) {
        save(role);
        //ids不为空 和大于0
        if (role.getMenuIds() != null && role.getMenuIds().length > 0) {
            addRoleMenu(role);
        }
    }

    @Override
    public void updateRole(Role role) {
        //更新角色
        updateById(role);
        //删除角色权限关联表
        roleMenuService.deleteRoleMenu(role.getId());
        //插入角色关联表 复用添加角色关联表方法
        addRoleMenu(role);
    }

    @Override
    public List<Role> selectRoleList() {
       return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, NORMAL));


    }

    @Override
    public List<Role> selectRoleAll() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus,NORMAL));
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long id) {
        return  getBaseMapper().selectRoleUser(id);

    }

    /**
     * 添加角色菜单关联表
     * @param role
     */
    private void addRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(m -> new RoleMenu(role.getId(), m))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }


}

