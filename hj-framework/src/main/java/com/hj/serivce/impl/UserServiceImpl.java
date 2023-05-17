package com.hj.serivce.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.domain.ResponseResult;
import com.hj.domain.VO.PageVo;
import com.hj.domain.VO.UserInfoVo;
import com.hj.domain.VO.UserVo;
import com.hj.domain.entity.Role;
import com.hj.domain.entity.RoleMenu;
import com.hj.domain.entity.User;
import com.hj.domain.entity.UserRole;
import com.hj.enums.AppHttpCodeEnum;
import com.hj.exception.SystemException;
import com.hj.mapper.UserMapper;
import com.hj.serivce.RoleMenuService;
import com.hj.serivce.RoleService;
import com.hj.serivce.UserRoleService;
import com.hj.serivce.UserService;
import com.hj.utils.BeanCopyUtils;
import com.hj.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-11 15:18:21
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleService userRoleService;

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
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult updateUserInfo(User user) {
        //TODO 此方法不安全,后期改为保存制定出参数,防止注入
        updateById(user);
        return ResponseResult.okResult();
    }

    /*
    注册方法
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (passwordExist(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 需要用户分页列表接口。
     * 可以根据用户名模糊搜索。
     * 以进行手机号的搜索。
     * 可以进行状态的查询。
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public ResponseResult selectUserList(User user, Integer pageNum, Integer pageSize) {
        //根据用户名模糊查询,根据手机号查询,根据状态查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(user.getUserName()), User::getUserName, user.getUserName());
        queryWrapper.like(StringUtils.hasText(user.getPhonenumber()), User::getPhonenumber, user.getPhonenumber());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()), User::getStatus, user.getStatus());
        //分页查询
        Page<User> userPage = new Page<>();
        userPage.setCurrent(pageNum);
        userPage.setSize(pageSize);
        page(userPage, queryWrapper);

        List<UserVo> userVos = BeanCopyUtils.copyBeanList(userPage.getRecords(), UserVo.class);
        PageVo pageVo = new PageVo(userVos, userPage.getTotal());
        return ResponseResult.okResult(pageVo);

    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName, userName)) == 0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber, user.getPhonenumber())) == 0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail, user.getEmail())) == 0;
    }

    @Override
    public ResponseResult addUser(User user) {
        //加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        //如果存在关联则添加
        if (user.getRoleIds() != null && user.getRoleIds().length > 0) {
            addUserRole(user);
        }
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        //删除原有关联
        userRoleService.remove(Wrappers.<UserRole> lambdaQuery().eq(UserRole::getUserId,user.getId()));
        addUserRole(user);
        updateById(user);


    }


    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;

    }

    private boolean nickNameExist(String nickname) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickname);
        return count(queryWrapper) > 0;

    }

    private boolean passwordExist(String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPassword, password);
        return count(queryWrapper) > 0;

    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;
    }

    /**
     * 用户角色关联表
     *
     * @param user
     */
   private void addUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }

}

