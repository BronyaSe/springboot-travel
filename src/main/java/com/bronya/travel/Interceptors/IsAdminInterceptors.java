package com.bronya.travel.Interceptors;

import com.bronya.travel.Utils.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class IsAdminInterceptors implements HandlerInterceptor {

    @Autowired
    private JWTUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        Claims tempclaim = jwtUtil.validateToken(token);
        String role = tempclaim.get("role").toString();
        if (role.equals("ADMIN")) {
            return true;
        }else {
            sendError(response,HttpServletResponse.SC_FORBIDDEN,"角色身份不符");
            return false;
        }
    }
    private void sendError(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
        response.getWriter().flush();
    }


}
