package com.management.core.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.core.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author zyf
 * @since 2019-09-18
 */
public interface SysRoleService extends IService<SysRole> {
    /**
     * 获取指定ID用户的所有角色（并附带查询所有的角色的权限）
     *
     * @param uid 用户ID
     * @return List
     */
    List<SysRole> findAllRoleByUserId(String uid);

    /**
     * 更新缓存
     *
     * @param role   角色
     * @param author 是否清空授权信息
     * @param out    是否清空session
     */
    void updateCache(SysRole role, Boolean author, Boolean out);

    void updateRole(Long uid, Long type);
}
