package com.management.core.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.common.exception.RequestException;
import com.management.core.entity.*;
import com.management.core.mapper.AdministratorMapper;
import com.management.core.mapper.ProprietorMapper;
import com.management.core.mapper.SysRoleMapper;
import com.management.core.mapper.SysUserMapper;
import com.management.core.service.system.SysUserRoleService;
import com.management.core.service.system.SysUserService;
import org.apache.shiro.crypto.hash.Sha384Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户个人信息-权限关联表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2019-09-18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserRoleService userRoleService;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private AdministratorMapper administratorMapper;

    @Resource
    private ProprietorMapper proprietorMapper;

    @Override
    public void login(String username, String password) {

    }

    @Override
    @Transactional
    public void register(String username, String password, Integer type) {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(new Sha384Hash(password + username).toBase64());
        user.setType(type);
        this.save(user);
        Long uid = this.getOne(new QueryWrapper<SysUser>().eq("username", username)).getId();
        log.debug(" >>> register : userId : " + uid);
        SysRole role = roleMapper.selectOne(new QueryWrapper<SysRole>().eq("role_name", "tourist"));
        if (role != null) {
            Long rid = role.getId();
            log.debug(">>>> rid   " + rid);
            userRoleService.save(SysUserRole.builder().roleId(rid).userId(uid).build());
        } else {
            throw new RequestException();
        }

        if (type == 1) {
            Administrator administrator = new Administrator();
            administrator.setAdministratorId(uid);
            administratorMapper.insert(administrator);
        } else {
            Proprietor proprietor = new Proprietor();
            proprietor.setProprietorId(uid);
            proprietorMapper.insert(proprietor);
        }

    }

    @Override
    @Transactional
    public void deleteUser(Long uid) {
        SysUser user = this.getById(uid);
        Map<String, Object> map = new HashMap<>();
        if (user.getType() == 1) {
            map.put("administrator_id", uid);
            administratorMapper.deleteByMap(map);
        } else {
            map.put("proprietor_id", uid);
            proprietorMapper.deleteByMap(map);
        }
        this.removeById(uid);
    }
}
