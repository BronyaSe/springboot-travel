package com.bronya.travel.Service;


import com.bronya.travel.Entity.User;

import java.util.List;

public interface UserService {

    List<User> getUserByUsername(String username);
    void register(String username, String password, String email);
    List<User> getUserByEmail(String email);
    void SendVerifyCode(String email);
    void updateUserPwd(Integer id, String pwd);
    User getUserById(Integer tempid);

    void updateUserInfo(Integer tempid, User user);

    String genJWTtoken(User user);
}
