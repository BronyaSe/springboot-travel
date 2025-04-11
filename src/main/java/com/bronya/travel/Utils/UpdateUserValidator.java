package com.bronya.travel.Utils;


import com.bronya.travel.Entity.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateUserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        //用户名验证
        if(user.getUsername()!=null){
            if (user.getUsername().length() < 5||user.getUsername().length() > 16) {
                errors.rejectValue("username", "too.short", "用户名至少5-16位");
            }
        }

        // 邮箱验证
        if(user.getEmail()!=null){
            if (!user.getEmail().matches(".+@.+\\..+")) {
                errors.rejectValue("email", "invalid", "邮箱格式不正确");
            }
        }

        //性别验证
        if(user.getGender()!=null){
            if(!(user.getGender().equals("Male") || user.getGender().equals("Female") || user.getGender().equals("Other"))){
                errors.rejectValue("gender", "invalid", "性别格式不正确");
            }
        }
        //手机验证
        if(user.getPhoneNumber()!=null){
            if (!user.getPhoneNumber().matches("^1[3-9]\\d{9}$")) {
                errors.rejectValue("phoneNumber", "invalid", "手机格式不正确");
            }
        }
    }
}
