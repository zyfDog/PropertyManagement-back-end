package com.management.core.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.core.entity.SysResource;
import com.management.core.entity.SysRoleResource;
import com.management.core.mapper.SysResourceMapper;
import com.management.core.mapper.SysRoleResourceMapper;
import com.management.core.service.system.SysRoleResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色-权限表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2019-09-22
 */
@Service
public class SysRoleResourceServiceImpl extends ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements SysRoleResourceService {

    @Resource
    private SysResourceMapper resourceService;

    @Resource
    private SysRoleResourceMapper roleResourceMapper;

    @Override
    public List<SysResource> findAllResourceByRoleId(Long rid) {
        List<SysRoleResource> rps = roleResourceMapper.selectList(new QueryWrapper<SysRoleResource>().eq("role_id", rid));

        if (rps != null) {
            List<Long> pids = new ArrayList<>();
            rps.forEach(v -> pids.add(v.getPermissionId()));

            if (pids.size() == 0) {
                return null;
            }
            return resourceService.selectList(new QueryWrapper<SysResource>().in("id", pids).orderByAsc("sort"));
        }
        return null;
    }

}
