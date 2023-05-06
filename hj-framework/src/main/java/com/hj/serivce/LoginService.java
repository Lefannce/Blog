package com.hj.serivce;

import com.hj.domain.ResponseResult;
import com.hj.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
