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


@Component
public class LoginInterceptors implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            response.setHeader("Access-Control-Allow-Origin", "http://pc.bronyahan.top");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return false; // 直接返回，不再进行后续处理
            }
            //拦截器会优先于配置文件的corsmapping之前执行
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("非authorization类型"); // 或抛出异常，如 throw new IllegalArgumentException("Invalid Authorization header");
        }
        token = token.substring(7);
        Claims tempclaim = jwtUtil.validateToken(token);
        Integer tempid = (Integer) tempclaim.get("id");
        String redistoken = stringRedisTemplate.opsForValue().get(tempid.toString());
        if (redistoken == null) {
            throw new RuntimeException("不存在jwt");
        }else if(redistoken.equals(token)){
            ThreadLocalUtils.set(tempclaim);
            return true;
        }else {
            throw new RuntimeException("验证失败");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtils.remove();
    }
}
