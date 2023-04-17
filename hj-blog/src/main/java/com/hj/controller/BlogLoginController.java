package com.hj.controller;

import com.hj.domain.ResponseResult;
import com.hj.domain.entity.User;
import com.hj.serivce.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){

        return blogLoginService.login(user);
    }

        @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();
    }
}