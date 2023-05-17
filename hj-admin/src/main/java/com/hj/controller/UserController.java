package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.domain.entity.User;
import com.hj.serivce.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("system/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询user用户列表
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult UserList(User user,Integer pageNum,Integer pageSize){
        return userService.selectUserList(user,pageNum,pageSize);

    }
}
