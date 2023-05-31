package com.example.odap.Interceptor;

import com.example.odap.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

/* 一个拦截器，用于验证用户是否已登录
* 在WebMvcConfig.java 配置类中配置了需要进行身份验证的API路径
* */
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 如果用户未登录，返回错误响应
        if (!isUserLoggedIn(request)) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("code", 401);
            //用户未登录，返回错误信息
            errorResponse.put("error_msg", "unauthorized user");
            errorResponse.put("data", null);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
            return false;
        }

        //每次请求都更新最后访问时间
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.setAttribute("lastAccessTime",System.currentTimeMillis());
        }
        // 用户已登录，继续处理请求
        return true;
    }

    private boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 获取会话，如果不存在则返回null
        if (session != null) {
            User user = (User) session.getAttribute("user");
            return user != null;
        }
        return false;
    }
}
