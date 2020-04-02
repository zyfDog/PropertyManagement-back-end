package com.management.common.config.shiro;

import com.management.common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha384Hash;

/**
 * @description:
 * @Auther: zyf
 * @Date: 2019-09-22
 **/
@Slf4j
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        Object accountCredentials = getCredentials(info);

        if (jwtToken.isBlacklist()) {
            log.debug(">>> 已在黑名单中");
            throw new DisabledAccountException("该Token已失效！");
        }

        if (jwtToken.isCache()) {
            log.debug(">>> JWTToken 已缓存，免认证");
            return true;
        }

        log.debug(jwtToken.toString());

        if (jwtToken.getPassword() != null && !accountCredentials.equals(encrypt(jwtToken.getPassword() + jwtToken.getUsername()))) {
            throw new DisabledAccountException("密码不正确！");
        } else if (!JwtUtil.verify(jwtToken.getToken(), jwtToken.getUid().toString(), jwtToken.getUsername(), accountCredentials.toString())) {
            throw new DisabledAccountException("verifyFail");
        }
        return true;
    }

    private String encrypt(String data) {
        return new Sha384Hash(data).toBase64();
    }

}
