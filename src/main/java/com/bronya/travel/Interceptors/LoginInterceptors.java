package com.bronya.travel.Interceptors;

import com.bronya.travel.Utils.JWTUtil;
import com.bronya.travel.Utils.ThreadLocalUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;


@Component
public class LoginInterceptors implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {//判断是否是预检请求 如果是则放行并不处理后续代码
                response.setStatus(HttpServletResponse.SC_OK);
                return false; // 直接返回，不再进行后续处理
            }
            //拦截器会优先于配置文件的corsmapping之前执行
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "请登录");
            return false;// 或抛出异常，如 throw new IllegalArgumentException("Invalid Authorization header");
        }
        token = token.substring(7);
        Claims tempclaim = jwtUtil.validateToken(token);
        Integer tempid = (Integer) tempclaim.get("id");
        String redistoken = stringRedisTemplate.opsForValue().get(tempid.toString());
        if (redistoken == null) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "请重新登录");
            return false;
        }else if(redistoken.equals(token)){
            ThreadLocalUtils.set(tempclaim);
            return true;
        }else {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "验证失败，请重新登陆");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.remove();
    }

    private void sendError(HttpServletResponse response, int statusCode, String errorMessage) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"" + errorMessage + "\"}");
        response.getWriter().flush();
    }
}
