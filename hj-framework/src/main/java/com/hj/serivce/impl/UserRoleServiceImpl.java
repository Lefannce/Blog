package com.hj.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.entity.UserRole;
import com.hj.mapper.UserRoleMapper;
import com.hj.serivce.UserRoleService;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
