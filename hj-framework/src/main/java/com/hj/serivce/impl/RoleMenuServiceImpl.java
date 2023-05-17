package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.entity.RoleMenu;
import com.hj.mapper.RoleMenuMapper;
import com.hj.serivce.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {


    @Override
    public void deleteRoleMenu(Long id) {
        LambdaQueryWrapper<RoleMenu> roleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        roleMenuLambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        remove(roleMenuLambdaQueryWrapper);
    }
}
