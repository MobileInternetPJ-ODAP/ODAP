package com.example.odap.service;

import com.example.odap.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    // 注册新用户
    User register(String userName, String password);

    // 根据用户名查找用户
    User findUserByName(String userName);

    // 判断用户是否存在
    boolean isUserExist(String userName);

    // 得到当前用户id
    Long getCurrentUserId(HttpServletRequest request);
}

