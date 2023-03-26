package com.example.odap.service;

import com.example.odap.entity.User;

public interface UserService {

    // 注册新用户
    User registerUser(String userName, String password);

    // 根据用户名查找用户
    User findUserByName(String userName);

    // 判断用户是否存在
    boolean isUserExist(String userName);
}

