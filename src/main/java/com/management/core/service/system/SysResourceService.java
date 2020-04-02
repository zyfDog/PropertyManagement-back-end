package com.management.core.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.core.entity.SysResource;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zyf
 * @since 2019-09-18
 */
public interface SysResourceService extends IService<SysResource> {
    /**
     * 获取所有资源列表
     *
     * @return
     */
    @Override
    List<SysResource> list();

    /**
     * 添加资源
     *
     * @param resource
     */
    void add(SysResource resource);

    /**
     * 更新资源
     *
     * @param id
     * @param resource
     */
    boolean update(String id, SysResource resource);

    /**
     * 删除资源
     *
     * @param id
     */
    boolean remove(String id);

    /**
     * 递归查找所有的子集
     *
     * @param resource
     */
    void findAllChild(SysResource resource);

    /**
     * 获取资源所有的父级
     *
     * @param resource
     * @param cacheMap
     * @param cacheMap2
     * @return
     */
    SysResource getResourceAllParent(SysResource resource, Map<Long, SysResource> cacheMap,
                                     Map<Long, SysResource> cacheMap2);

}
