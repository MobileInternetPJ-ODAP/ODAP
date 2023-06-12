package com.example.odap.controller;

import com.example.odap.entity.User;
import com.example.odap.request.LoginForm;
import com.example.odap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class LoginController {
    @Autowired
    private UserService userService;
    private static final int CODE_SUCCESS = 200;
    private static final int CODE_FAILURE = 405;

    private static final String MSG_SUCCESS = "Login successfully.";
    private static final String MSG_FAILURE = "Login failed.";

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request, @RequestBody LoginForm loginForm) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.findUserByName(loginForm.getUsername());
        if (user == null || !user.getPassWord().equals(loginForm.getPassword())) {
            response.put("code", CODE_FAILURE);
            response.put("error_msg", MSG_FAILURE);
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 将用户信息保存到会话，并设置会话的超时时间为10分钟
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(10 * 60); // 10分钟

        response.put("code", CODE_SUCCESS);
        response.put("error_msg", MSG_SUCCESS);
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

}
