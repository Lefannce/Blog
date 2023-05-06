package com.hj.serivce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hj.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 09:18:24
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

