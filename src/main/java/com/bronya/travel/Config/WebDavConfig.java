package com.bronya.travel.Config;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebDavConfig {

    @Value("${webdav.server.url}")
    private String serverUrl;

    @Value("${webdav.username}")
    private String username;

    @Value("${webdav.password}")
    private String password;

    @Value("${httpPic.Url}")
    private String httpPicUrl;

    @Bean
    public Sardine sardine() {
        Sardine sardine;
        if (!username.isEmpty() && !password.isEmpty()) {
            // 如果提供了用户名和密码，则使用带认证的 Sardine 连接
            sardine = SardineFactory.begin(username, password);
        } else {
            // 否则使用匿名访问的 Sardine 连接
            sardine = SardineFactory.begin();
        }
        return sardine;
    }

    @Bean
    public String webDavBaseUrl() {
        // 将 WebDAV 基础 URL 作为 Bean 提供给其他服务使用
        return serverUrl;
    }
    @Bean
    public String PicBaseUrl() {
        // 将 WebDAV 基础 URL 作为 Bean 提供给其他服务使用
        return httpPicUrl;
    }
}
