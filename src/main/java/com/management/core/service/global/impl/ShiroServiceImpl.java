package com.management.core.service.global.impl;

import com.management.common.bean.ResponseCode;
import com.management.common.exception.RequestException;
import com.management.common.util.SpringUtils;
import com.management.core.entity.SysResource;
import com.management.core.service.global.ShiroService;
import com.management.core.service.system.SysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @description: Shiro业务类
 * @author: zyf
 * @create: 2019-09-15
 **/
@Slf4j
public class ShiroServiceImpl implements ShiroService {

    @Resource
    private SysResourceService resourceService;

    @Override
    public Map<String, String> getFilterChainDefinitionMap() {
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        List<String[]> permsList = new LinkedList<>();
        List<String[]> anonList = new LinkedList<>();

        List<SysResource> resources = resourceService.list();
        if (resources != null) {
            for (SysResource resource : resources) {
                addToSet(resource, permsList, anonList, resource);
                iterationAllResourceInToFilter(resource, permsList, anonList);
            }
        }

        for (String[] strings : anonList) {
            filterChainDefinitionMap.put(strings[0], strings[1]);
        }

        for (String[] strings : permsList) {
            filterChainDefinitionMap.put(strings[0], strings[1]);
        }

        filterChainDefinitionMap.put("/**", "anon");
        return filterChainDefinitionMap;
    }

    @Override
    public void iterationAllResourceInToFilter(SysResource resource, List<String[]> permsSet, List<String[]> anonSet) {
        if (resource.getChildren() != null && resource.getChildren().size() > 0) {
            for (SysResource v : resource.getChildren()) {
                addToSet(v, permsSet, anonSet, v);
                iterationAllResourceInToFilter(v, permsSet, anonSet);
            }
        }
    }

    @Override
    public void reloadPerms() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringUtils.getBean(ShiroFilterFactoryBean.class);

        AbstractShiroFilter abstractShiroFilter;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
        } catch (Exception e) {
            throw new RequestException(ResponseCode.FAIL.code, "重新加载权限失败", e);
        }
        PathMatchingFilterChainResolver filterChainResolver =
                (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                .getFilterChainManager();

        /*清除旧版权限*/
        manager.getFilterChains().clear();
        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();

        /*更新新数据*/
        Map<String, String> filterChainDefinitionMap = getFilterChainDefinitionMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        filterChainDefinitionMap.forEach(manager::createChain);
    }

    @Override
    public void clearAuthByUserId(String uid, Boolean author, boolean out) {
        // TODO
    }

    @Override
    public void clearAuthByUserIdCollection(List<Long> userList, Boolean author, Boolean out) {
        // TODO
    }

    private void addToSet(SysResource resource, List<String[]> permsSet, List<String[]> anonSet, SysResource v) {
        if (!StringUtils.isEmpty(resource.getUrl())
                && !StringUtils.isEmpty(resource.getPermission())
                && !"".equals(resource.getPermission().trim())
        ) {
            log.debug(resource.getId() + "   getUrl >> " + resource.getUrl());
            if (v.getVerification()) {
                permsSet.add(new String[]{resource.getUrl() + "/**",
                        "perms[" + resource.getPermission() + ":*]"});
            } else {
                anonSet.add(new String[]{resource.getUrl() + "/**",
                        "anon"});
            }
        }
    }

}
