package com.management.core.service.global;

import com.management.core.entity.SysResource;

import java.util.List;
import java.util.Map;

/**
 * @author: zyf
 * @create: 2019-09-22
 **/
public interface ShiroService {
    /**
     * 获取拦截器数据
     *
     * @return
     */
    Map<String, String> getFilterChainDefinitionMap();

    /**
     * 迭代所有的资源子集
     *
     * @param resource
     * @param permsSet
     * @param anonSet
     */
    void iterationAllResourceInToFilter(SysResource resource,
                                        List<String[]> permsSet, List<String[]> anonSet);

    /**
     * 重新加载权限
     */
    void reloadPerms();

    /**
     * 清楚指定用户ID的授权信息
     *
     * @param uid    用户ID
     * @param author 是否清空授权信息
     * @param out    是否清空session
     */
    void clearAuthByUserId(String uid, Boolean author, boolean out);

    /**
     * 清除指定用户ID集合的授权信息
     *
     * @param userList 用户ID集合
     * @param author   是否清空授权信息
     * @param out      是否清空session
     */
    void clearAuthByUserIdCollection(List<Long> userList, Boolean author, Boolean out);

}
