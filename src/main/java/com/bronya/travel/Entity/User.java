package com.bronya.travel.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "users")
public class User {
    @NotNull
    private Integer id;
    @NotBlank(message = "用户名不能为空")
    @Size(min = 5 , max = 16,message = "用户名长度必须在5-16字符")
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(min = 5,max = 15,message ="密码长度为5-15字符")
    private String password;
    @NotBlank(message = "邮箱不能为空")
    @Email
    private String email;

    @URL
    private String avatar;

    private String phoneNumber;
    private String gender;

    @TableField(exist = false)
    private List<String> travelHistory; // 用户旅行历史记录
    @TableField(exist = false)
    private List<String> favoriteDestinations; // 用户收藏的旅游地点

    private UserRole role;//使用枚举类存储角色身份方便后续开发对应功能

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
