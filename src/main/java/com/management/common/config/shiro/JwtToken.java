package com.management.common.config.shiro;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description: JWTToken
 * @Auther: zyf
 * @Date: 2019-09-22
 **/
@NoArgsConstructor
@Data
public class JwtToken implements AuthenticationToken {

    private String token;

    private String username;

    private String password;

    private Long uid;

    private boolean isCache;

    private boolean isBlacklist;

    public JwtToken(String token, Long uid, String username, String password) {
        this.token = token;
        this.uid = uid;
        this.username = username;
        this.password = password;
    }

    public JwtToken(String token, String username, String password) {
        this.token = token;
        this.username = username;
        this.password = password;
    }

    public JwtToken(String token, Long uid) {
        this.token = token;
        this.uid = uid;
    }

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return password;
    }
}