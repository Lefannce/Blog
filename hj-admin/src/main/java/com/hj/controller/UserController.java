package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.domain.VO.UserInfoAndRoleIdsVo;
import com.hj.domain.entity.Role;
import com.hj.domain.entity.User;
import com.hj.enums.AppHttpCodeEnum;
import com.hj.exception.SystemException;
import com.hj.serivce.RoleService;
import com.hj.serivce.UserService;
import com.hj.utils.SecurityUtils;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hj.enums.AppHttpCodeEnum.REQUIRE_USERNAME;

@RestController
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     * 查询user用户列表
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult UserList(User user, Integer pageNum, Integer pageSize) {
        return userService.selectUserList(user, pageNum, pageSize);

    }

    @PostMapping()
    public ResponseResult addUser(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }

    /**
     * 删除用户,不能删除当前用户
     *
     * @param ids
     * @return
     */
    @DeleteMapping("{ids}")
    public ResponseResult delUser(@PathVariable List<Long> ids) {
        if (ids.contains(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户");
        }
        userService.removeByIds(ids);
        return ResponseResult.okResult();
    }

    @GetMapping("{id}")
    public ResponseResult selectUser(@PathVariable Long id) {
        //查询所有角色,状态为正常
        List<Role> roles = roleService.selectRoleAll();
        //查询用户
        User user = userService.getById(id);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(id);
        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
        return ResponseResult.okResult(vo);
    }

    @PutMapping
    public ResponseResult edit(@RequestBody User user){
        userService.updateUser(user);
        return ResponseResult.okResult();

    }
}
