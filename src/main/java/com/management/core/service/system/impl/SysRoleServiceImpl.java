package com.management.core.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.core.entity.SysResource;
import com.management.core.entity.SysRole;
import com.management.core.entity.SysUserRole;
import com.management.core.mapper.SysRoleMapper;
import com.management.core.mapper.SysUserRoleMapper;
import com.management.core.service.system.SysRoleResourceService;
import com.management.core.service.system.SysRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2019-09-25
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleResourceService roleResourceService;

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Override
    public List<SysRole> findAllRoleByUserId(String uid) {
        List<SysUserRole> userRoles = userRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", uid));
        List<SysRole> roles = new ArrayList<>();
        userRoles.forEach(v -> {
            SysRole role = roleMapper.selectById(v.getRoleId());
            if (role != null) {
                List<SysResource> permissions = roleResourceService.findAllResourceByRoleId(role.getId());
                role.setResources(permissions);
            }
            roles.add(role);
        });
        return roles;
    }

    @Override
    public void updateCache(SysRole role, Boolean author, Boolean out) {

    }

    @Override
    @Transactional
    public void updateRole(Long uid, Long type) {
        Long adminId = this.getOne(new QueryWrapper<SysRole>().eq("role_name", "admin")).getId();
        Long touristId = this.getOne(new QueryWrapper<SysRole>().eq("role_name", "tourist")).getId();
        if (type == 1L) {
            SysUserRole userRole = SysUserRole.builder().userId(uid).roleId(adminId).build();
            userRoleMapper.update(userRole, new QueryWrapper<SysUserRole>()
                    .eq("role_id", touristId).eq("user_id", uid));
        } else {
            SysUserRole userRole = SysUserRole.builder().roleId(touristId).build();
            userRoleMapper.update(userRole, new QueryWrapper<SysUserRole>()
                    .eq("role_id", adminId).eq("user_id", uid));
        }
    }
}
