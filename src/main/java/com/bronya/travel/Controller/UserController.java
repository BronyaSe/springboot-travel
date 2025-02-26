package com.bronya.travel.Controller;

import com.bronya.travel.Entity.Result;
import com.bronya.travel.Entity.User;
import com.bronya.travel.Service.UserService;
import com.bronya.travel.Utils.MD5Util;
import com.bronya.travel.Utils.ThreadLocalUtils;
import io.jsonwebtoken.Claims;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



//requestparam注解不能接受请求体参数 而且必须接受该值    body注解为只接受请求体
    //前端axios请求默认请求体（json）请求  如给自动绑定（非注解参数）或者requestparam传参需转为表单数据（kv数据）
    @PostMapping("/login")
    public Result<String> login(@NotBlank @Email String email,@NotBlank String password){
//        String redistemp = stringRedisTemplate.opsForValue().get(username);  //弃置redis验证为防止用户删除本地数据导致保存登录失效 无法再次登录获取新token存本地 未来使用限流防止接口被攻击
//        if(redistemp == null){
        User user = userService.getUserByEmail(email).getFirst();
        if(user == null){
            return Result.error("该用户不存在");
        }else {
            String temppassword = user.getPassword();
            if (MD5Util.encrypt(password).equals(temppassword)) {
                return Result.success(userService.genJWTtoken(user));
            } else {
                return Result.error("密码错误");
                }
            }
//        else{
//            return Result.error("该用户已生成jwt");
//        }
    }



    @PostMapping("/register")
    public Result<String> register(@NotBlank String username, @NotBlank String password, @Email String email,
                                   String verifycode) {
//        User tempuser = userService.getUserByUsername(username);
//        if (tempuser != null) {
//            return Result.error("该用户已注册");
//        }else{
        String redisverifycode = stringRedisTemplate.opsForValue().get(email);
        if (redisverifycode != null && redisverifycode.equals(verifycode)) {
            userService.register(username, password, email);
            stringRedisTemplate.opsForValue().getOperations().delete(email);
            return Result.success();
        } else {
            return Result.error("验证码错误");
        }
//     }
    }

    @PostMapping("/SendVerifyCode")
    public Result<String> SendVerifyCode(@RequestParam @NotBlank @Email String email) {
        User tempuser = userService.getUserByEmail(email).getFirst();
        if (tempuser != null) {
            return Result.error("该邮箱已注册");
        }else{
//            if(stringRedisTemplate.opsForValue().get(email)==null){
                userService.SendVerifyCode(email);
                return Result.success();
            }
//        else{
//                return Result.error("邮件已发送");                   //和上述同理 后期限流防止压测接口
//          }
    }

    @PostMapping("/updatepwd")
    public Result<String> updatePwd(@NotBlank @RequestParam(name = "opwd") String oldpsd ,@NotBlank @RequestParam(name = "npwd") String newpwd){
        Claims claims = ThreadLocalUtils.get();
        Integer tempid = (Integer) claims.get("id");
        User tempuser = userService.getUserById(tempid);
        String encryptopwd = MD5Util.encrypt(oldpsd);
        if(encryptopwd.equals(tempuser.getPassword())){
            userService.updateUserPwd(tempid, newpwd);
            return Result.success();
        }else {
            return Result.error("旧的密码输入错误");
        }
    }

    @PostMapping("/updateinfo")
    public Result<String> updateUserInfo(@RequestBody @Validated User user){
        Claims claims = ThreadLocalUtils.get();
        Integer tempid = (Integer) claims.get("id");
        userService.updateUserInfo(tempid,user);
        return Result.success();
    }

    @GetMapping("/getUser")
    public Result<User> getInfoById(@RequestParam Integer id){
      return Result.success(userService.getUserById(id));
    }

    @PostMapping("/logoff")
    public Result<String> logoff(){
        Claims claims = ThreadLocalUtils.get();
        String id = claims.get("id").toString();
        try {
            stringRedisTemplate.delete(id);
            return Result.success();
        }catch (Exception e){
            return Result.error(e.getMessage());
        }
    }
}
