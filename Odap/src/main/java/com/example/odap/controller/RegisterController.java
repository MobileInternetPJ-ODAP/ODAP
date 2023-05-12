package com.example.odap.controller;

import com.example.odap.entity.User;
import com.example.odap.exception.UserRegistrationException;
import com.example.odap.request.SignupForm;
import com.example.odap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class RegisterController {

    @Autowired
    private UserService userService;
    private static final int CODE_SUCCESS = 200;
    private static final int CODE_FAILURE = -1;
    private static final String MSG_SUCCESS = "Register user successfully.";


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody SignupForm signUpRequest) {

        // 调用 UserService 的 register 方法进行用户注册
        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.register(signUpRequest.getUsername(), signUpRequest.getPassword());
            response.put("code", CODE_SUCCESS);
            response.put("error_msg", MSG_SUCCESS);
            response.put("data", user);
        } catch (UserRegistrationException e) {
            String s = e.getMessage();
            response.put("code", CODE_FAILURE);
            response.put("error_msg", s);
            response.put("data", null);
        }
        return ResponseEntity.ok(response);
    }
}

