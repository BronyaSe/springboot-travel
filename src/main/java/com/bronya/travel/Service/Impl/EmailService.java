package com.bronya.travel.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * 发送简单文本邮件
     * @param to 收件人
     * @param subject 邮件主题
     * @param content 邮件内容
     */
    @Value("${spring.mail.username}")
    private String from;

    public void sendSimpleMail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from); // 发件人（需与配置文件的 spring.mail.username 一致）
            message.setTo(to); // 收件人
            message.setSubject(subject); // 邮件主题
            message.setText(content); // 邮件内容
            mailSender.send(message);
        } catch (Exception ignored) {

        }
    }
}
