package com.hj.serivce.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.UserInfoVo;
import com.hj.domain.entity.User;
import com.hj.mapper.UserMapper;
import com.hj.serivce.UserService;
import com.hj.utils.BeanCopyUtils;
import com.hj.utils.SecurityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-11 15:18:21
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        //获取当前用户id,工具类获取id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    /**
     * 直接更新用户数据(不安全)
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        //TODO 此方法不安全,后期改为保存制定出参数,防止注入
        updateById(user);
        return ResponseResult.okResult();
    }
}

