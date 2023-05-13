package com.example.odap.service.impl;

import com.example.odap.entity.User;
import com.example.odap.exception.UserRegistrationException;
import com.example.odap.repository.UserRepository;
import com.example.odap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User register(String userName, String password) {
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
        return userRepository.findByUserName(userName);
    }

    @Override
    public boolean isUserExist(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public Long getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return null;
        }
        User user = (User) session.getAttribute("user");
        return user.getId();
    }
}
