package com.bronya.travel.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bronya.travel.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//    @Insert("INSERT INTO users(username,password,email) values(#{username},#{password},#{email})")
//    void insert(String username , String password , String email);

//    @Select("select * from users where username=#{username}")
//    User getUserByUsername(String username);

//    @Select("select * from users where email=#{email}")
//    User getUserByEmail(String email);

    @Update("UPDATE users SET password = #{encrypt},updateTime=CURRENT_TIMESTAMP WHERE id=#{id}")
    void updatePwdById(Integer id, String encrypt);

//    @Select("select * from users WHERE id=#{tempid}")
//    User getUserById(Integer tempid);

    @Update("update users SET username=#{user.username},email=#{user.email},phoneNumber=#{user.phoneNumber},gender=#{gender},updateTime=CURRENT_TIMESTAMP WHERE id=#{id} ")
    void updateInfoById(@Param("id") Integer tempid,@Param("user") User user);
}
