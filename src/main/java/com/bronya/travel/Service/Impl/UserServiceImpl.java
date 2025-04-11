package com.bronya.travel.Service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bronya.travel.Entity.DTO.UserFavoritesPageDTO;
import com.bronya.travel.Entity.Route;
import com.bronya.travel.Entity.User;
import com.bronya.travel.Mapper.RouteMapper;
import com.bronya.travel.Mapper.UserMapper;
import com.bronya.travel.Service.UserService;
import com.bronya.travel.Utils.JWTUtil;
import com.bronya.travel.Utils.MD5Util;
import com.bronya.travel.Utils.ThreadLocalUtils;
import com.bronya.travel.Utils.VerifyCodeGenUtils;
import com.github.sardine.Sardine;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    @Autowired
    private Sardine sardine;
    @Autowired
    private String webDavBaseUrl;
    @Autowired
    private String PicBaseUrl;
    @Autowired
    private RouteMapper routeMapper;


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
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.select(User::getId, User::getUsername, User::getEmail, User::getAvatar,User::getGender,User::getPhoneNumber,User::getRole)
                .eq(User::getId,tempid);
       return userMapper.selectOne(userLambdaQueryWrapper);
    }


    @Override
    @Transactional(rollbackFor = Exception.class) // 所有异常都触发回滚
    public void updateUserInfo(User user, MultipartFile file) {
        Claims tempclaims =(Claims)ThreadLocalUtils.get();
        User tempUser = userMapper.selectById(tempclaims.get("id").toString());
        user.setId(tempUser.getId());
        user.setAvatar(tempUser.getAvatar());
        // 当文件为空时，仅更新用户信息
        if (file == null || file.isEmpty()) {
            userMapper.updateInfoById(user);
            return;
        }


        // 1. 获取并验证原始文件名
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 2. 去除路径信息（兼容Windows/Linux路径格式）
        int lastPathSeparator = Math.max(
                originalFilename.lastIndexOf('/'),
                originalFilename.lastIndexOf('\\')
        );
        String pureFilename = (lastPathSeparator != -1)
                ? originalFilename.substring(lastPathSeparator + 1)
                : originalFilename;

        // 3. 提取文件后缀
        int extensionIndex = pureFilename.lastIndexOf('.');
        if (extensionIndex == -1) {
            throw new IllegalArgumentException("文件缺少后缀名");
        }
        String fileExtension = pureFilename.substring(extensionIndex);

        String uuid = UUID.randomUUID().toString();
        String filePath = webDavBaseUrl + "avatar/"+uuid+fileExtension;
        boolean fileUploaded = false;
        try {
            String url = user.getAvatar();
            int lastSlashIndex = url.lastIndexOf("/");
            String oldPicWebDavUrl=webDavBaseUrl+"avatar/"+ url.substring(lastSlashIndex + 1);;
            sardine.put(filePath,file.getInputStream());
            fileUploaded = true;
            user.setAvatar(PicBaseUrl+uuid+fileExtension);
            userMapper.updateInfoById(user);
            if(!url.substring(lastSlashIndex + 1).equals("default.png")){
                sardine.delete(oldPicWebDavUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch (Exception e) {
            if(fileUploaded){
                try {
                    sardine.delete(filePath);
                } catch (IOException ex) {
                    throw new RuntimeException("头像删除回滚失败");
                }
            }
            throw new RuntimeException("用户信息更新失败", e);
        }
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

    @Override
    public UserFavoritesPageDTO getFavoritesByPage(int current, int size) {
        Claims claims = ThreadLocalUtils.get();
        String userId = claims.get("id").toString();
        Page<Route> page = new Page<>(current, size);
        LambdaQueryWrapper<Route> routeLambdaQueryWrapper = Wrappers.<Route>lambdaQuery()
                .apply("id IN (select location_id from user_favorites where user_id = {0})",userId)
                .select(
                        Route::getId,
                        Route::getName,
                        Route::getCategory
                );
        Page<Route> resultPage =routeMapper.selectPage(page,routeLambdaQueryWrapper);
        return UserFavoritesPageDTO.builder().
                favorites(resultPage.getRecords()).
                total(resultPage.getTotal()).
                build();
    }

    @Override
    public void deleteFavoriteById(int location_id) {
        Claims claims = ThreadLocalUtils.get();
        String userId = claims.get("id").toString();
        userMapper.deleteFavoritesById(location_id,userId);
    }
}
