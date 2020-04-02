package com.management.core.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.core.entity.SysResource;
import com.management.core.mapper.SysResourceMapper;
import com.management.core.service.system.SysResourceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zyf
 * @since 2019-09-19
 */
@Service
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource> implements SysResourceService {

    @Resource
    private SysResourceMapper resourceMapper;

    @Override
    public List<SysResource> list() {
        QueryWrapper<SysResource> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0)
                .or()
                .isNull("parent_id")
                .orderByAsc("sort");

        List<SysResource> resources = resourceMapper.selectList(wrapper);
        if (resources != null && resources.size() > 0) {
            resources.forEach(this::findAllChild);
        }
        return resources;
    }

    @Override
    public void add(SysResource resource) {
        resourceMapper.insert(resource);
        // TODO reloadPerms
    }

    @Override
    public boolean update(String id, SysResource resource) {
        if (resourceMapper.selectById(id) == null) {
            return false;
        }
        if (resourceMapper.updateById(resource) != 0) {
            // TODO shiroService.reloadPerms
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(String id) {
        if (resourceMapper.selectOne(new QueryWrapper<SysResource>().eq("id", id)) == null) {
            return false;
        }
        if (resourceMapper.deleteById(id) != 0) {
            // TODO shiroService.reloadPerms
            return true;
        }
        return false;
    }

    @Override
    public void findAllChild(SysResource resource) {
        QueryWrapper<SysResource> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", resource.getId())
                .orderByAsc("level");
        List<SysResource> resources = resourceMapper.selectList(wrapper);
        resource.setChildren(resources);
        if (resources != null && resources.size() > 0) {
            resources.forEach(this::findAllChild);
        }
    }

    @Override
    public SysResource getResourceAllParent(SysResource resource, Map<Long, SysResource> cacheMap, Map<Long, SysResource> cacheMap2) {
        if (resource.getParentId() != null && resource.getParentId() != 0) {
            SysResource cacheParent = cacheMap.get(resource.getParentId());
            SysResource parent;
            if (cacheParent != null) {
                parent = cacheParent;
            } else {
                cacheParent = cacheMap2.get(resource.getParentId());
                if (cacheParent != null) {
                    parent = cacheParent;
                } else {
                    parent = resourceMapper.selectById(resource.getParentId());
                }
            }
            if (parent != null) {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                //判断是否已经包含此对象
                if (!parent.getChildren().contains(resource)) {
                    parent.getChildren().add(resource);
                }
                cacheMap.put(resource.getParentId(), parent);
                //如果此父级还有父级，继续递归查询
                if (parent.getParentId() != null && parent.getParentId() != 0) {
                    return getResourceAllParent(parent, cacheMap, cacheMap2);
                } else {
                    return parent;
                }
            }
        }
        return resource;
    }
}
