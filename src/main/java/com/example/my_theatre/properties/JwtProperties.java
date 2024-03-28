package com.example.my_theatre.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端生成jwt令牌相关配置
     */
    //管理员密钥
    private String adminSecretKey;
    //管理员校验令牌有效期
    private long adminTtl;
    //管理员令牌名称
    private String adminTokenName;

    /**
     * 用户端jwt令牌相关配置
     */
    //用户端密钥
    private String userSecretKey;
    //用户端校验令牌有效期
    private long userTtl;
    //用户令牌名称
    private String userTokenName;

}
