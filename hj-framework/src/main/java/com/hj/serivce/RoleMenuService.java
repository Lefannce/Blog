package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.entity.RoleMenu;

/**
 */
public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenu(Long id);
}