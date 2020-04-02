package com.management.core.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.core.entity.SysResource;
import com.management.core.entity.SysRoleResource;

import java.util.List;

/**
 * <p>
 * 角色-权限表 服务类
 * </p>
 *
 * @author zyf
 * @since 2019-09-18
 */
public interface SysRoleResourceService extends IService<SysRoleResource> {
    /**
     * 返回角色所拥有的所有资源
     *
     * @param rid 角色id
     * @return list
     */
    List<SysResource> findAllResourceByRoleId(Long rid);
}
