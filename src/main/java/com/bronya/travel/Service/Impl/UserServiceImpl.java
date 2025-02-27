package com.bronya.travel.Service.Impl;

import com.bronya.travel.Entity.User;
import com.bronya.travel.Mapper.UserMapper;
import com.bronya.travel.Service.UserService;
import com.bronya.travel.Utils.JWTUtil;
import com.bronya.travel.Utils.MD5Util;
import com.bronya.travel.Utils.VerifyCodeGenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JWTUtil jwtUtil;


    @Override
    public List<User> getUserByUsername(String username) {
//        return userMapper.getUserByUsername(username);
        return userMapper.selectByMap(Map.of("username",username));
    }

    @Override
    public void register(String username, String password, String email) {
        String encryptpassowrd = MD5Util.encrypt(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptpassowrd);
        user.setEmail(email);
        userMapper.insert(user);
        //userMapper.insert(username, encryptpassowrd, email);
    }

    @Override
    public List<User> getUserByEmail(String email) {
        return userMapper.selectByMap(Map.of("email",email));
    }

    @Override
    public void SendVerifyCode(String email) {
        String code = VerifyCodeGenUtils.gencode();//生成验证码
        emailService.sendSimpleMail(email,"邮箱认证","你的验证码为："+code+",该验证码五分钟内有效，请及时使用");//先发邮件再存入redis 如果邮件发送失败不会占用redis
        stringRedisTemplate.opsForValue().set(email, code,5, TimeUnit.MINUTES);//设置5分钟过期验证码
    }

    @Override
    public void updateUserPwd(Integer id, String pwd) {
        userMapper.updatePwdById(id,MD5Util.encrypt(pwd));
    }

    @Override
    public User getUserById(Integer tempid) {
       return userMapper.selectById(tempid);
    }

    @Override
    public void updateUserInfo(Integer tempid, User user) {
        userMapper.updateInfoById(tempid,user);
    }

    @Override
    public String genJWTtoken(User user) {
        String claims = jwtUtil.generateToken(Map.ofEntries(
                Map.entry("id", user.getId()),
                Map.entry("username", user.getUsername()),
                Map.entry("role", user.getRole()),
                Map.entry("avatar", user.getAvatar())
        ));
        stringRedisTemplate.opsForValue().set(String.valueOf(user.getId()), claims, 7, TimeUnit.DAYS);
        return claims;
    }
}
