package com.hj.serivce;

import com.hj.domain.ResponseResult;
import com.hj.domain.entity.User;

public interface BlogLoginService {
     ResponseResult login(User user);

     ResponseResult logout();
}
