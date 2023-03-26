package com.example.odap.service.impl;

import com.example.odap.entity.User;
import com.example.odap.exception.UserRegistrationException;
import com.example.odap.repository.UserRepository;
import com.example.odap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User registerUser(String userName, String password) {
        // 检查用户名是否已经存在
        if (userRepository.existsByUserName(userName)){
            throw new UserRegistrationException("用户名已存在");
        }
        // 创建一个新用户对象
        User user = new User(userName, password);
        // 将新用户对象保存到数据库中
        return userRepository.save(user);
    }

    @Override
    public User findUserByName(String userName) {
        return null;
    }

    @Override
    public boolean isUserExist(String userName) {
        return false;
    }
}
