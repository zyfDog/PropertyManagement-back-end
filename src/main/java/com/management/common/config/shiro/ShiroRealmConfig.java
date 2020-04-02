package com.management.common.config.shiro;

import com.management.common.exception.RequestException;
import com.management.common.util.JwtUtil;
import com.management.core.entity.SysRole;
import com.management.core.entity.SysUser;
import com.management.core.mapper.SysUserMapper;
import com.management.core.service.system.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @description: shiro验证规则
 * @Auther: zyf
 * @Date: 2019-09-22
 * subject.login -> Security Manager -> Authenticator -> Realm
 **/

@Slf4j
public class ShiroRealmConfig extends AuthorizingRealm {

    @Resource
    private SysUserMapper userBaseMapper;

    @Resource
    private SysRoleService sysRoleService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.debug("Shiro权限验证执行");
        log.debug("principals: " + principals.getPrimaryPrincipal().toString());
        JwtToken jwtToken = (JwtToken) principals.getPrimaryPrincipal();
        log.debug("jwtToken : " + jwtToken.toString());
        if (jwtToken.getToken() != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            String uid = JwtUtil.get(jwtToken.getToken(), "uid");
            log.debug(">> uid  " + uid);
            SysUser findUser = userBaseMapper.selectById(Long.parseLong(uid));
            if (findUser != null) {
                List<SysRole> sysUsersRoles = sysRoleService.findAllRoleByUserId(uid);
                if (sysUsersRoles != null) {
                    sysUsersRoles.forEach(role -> {
                        info.addRole(role.getRoleName());
                        if (role.getResources() != null) {
                            role.getResources().forEach(v -> {
                                if (!"".equals(v.getPermission().trim())) {
                                    info.addStringPermission(v.getPermission());
                                }
                            });
                        }
                    });
                }
                return info;
            }
        }
        throw new DisabledAccountException("用户信息异常，请重新登录！");
    }

    /**
     * 认证
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        log.debug("ShiroRealm 认证开始 >>>" + authenticationToken.getPrincipal());
        JwtToken token = (JwtToken) authenticationToken;


        if (token.getToken() == null && (token.getPassword() == null || token.getUsername() == null)) {
            throw new DisabledAccountException("请输入用户名/密码！");
        }
        if (token.getUid() == null) {
            token.setUid(Long.parseLong(Objects.requireNonNull(JwtUtil.get(token.getToken(), "uid"))));
        }

        log.debug(token.getUsername());
        log.debug(token.toString());
        log.debug(">>> 读取redis中token黑名单");

        String prekey = "JWTToken-Cache-Blacklist::" + token.getUid();
        if (token.getToken() != null
                && token.getToken().length() > 0
                && token.getUid() != null
                && redisTemplate.opsForSet().isMember(prekey, token.getToken())
        ) {
            token.setBlacklist(true);
            log.debug(" >>> Cache-BlackList 读取成功");
            return new SimpleAuthenticationInfo(token, "", token.getUid().toString());
        }


        log.debug(" >>> 开始读取redis中有效token");
        String redisKey = "JWTToken-Cache::" + token.getUid();
        if (token.getToken() != null
                && token.getToken().length() > 0
                && token.getUid() != null
                && Objects.equals(redisTemplate.opsForValue().get(redisKey), token.getToken())
        ) {
            token.setCache(true);
            log.debug(" >>> Cache 读取成功");
            return new SimpleAuthenticationInfo(token, "", token.getUid().toString());
        }
        SysUser user;
        Long uid = token.getUid();
        try {
            user = userBaseMapper.selectById(uid);
        } catch (RequestException e) {
            throw new DisabledAccountException(e.getMsg());
        }
        if (user == null) {
            throw new DisabledAccountException("用户不存在！");
        }
        if (user.getLocked()) {
            throw new DisabledAccountException("用户账户已锁定，暂无法登陆！");
        }

        if (token.getUsername() == null) {
            token.setUsername(user.getUsername());
        }
        String sign = JwtUtil.sign(user.getId().toString(), user.getUsername(), user.getPassword());
        if (token.getToken() == null) {
            token.setToken(sign);
        }
        return new SimpleAuthenticationInfo(token, user.getPassword(), user.getId().toString());
    }

    public void clearAuthByUserId(String uid, Boolean author, Boolean out) {
        //获取所有session
        Cache<Object, Object> cache = cacheManager
                .getCache(ShiroRealmConfig.class.getName() + ".authorizationCache");
        cache.remove(uid);
    }

    public void clearAuthByUserIdCollection(List<String> userList, Boolean author, Boolean out) {
        Cache<Object, Object> cache = cacheManager
                .getCache(ShiroRealmConfig.class.getName() + ".authorizationCache");
        userList.forEach(cache::remove);
    }
}

