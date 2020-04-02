package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.management.common.bean.ResponseCode;
import com.management.common.bean.ResponseResult;
import com.management.core.entity.SysRole;
import com.management.core.service.system.SysResourceService;
import com.management.core.service.system.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description:
 * @author: zyf
 * @create: 2019-09-25
 **/
@RestController
@RequestMapping("/system")
public class SystemController {

    @Resource
    private SysRoleService roleService;

    @Resource
    private SysResourceService resourceService;

    @GetMapping("/role")
    @RequiresPermissions("system:")
    public ResponseResult<Object> listRoleByUserId(@RequestBody JSONObject jsonObject) {
        String uid = jsonObject.getString("id");
        return ResponseResult.e(ResponseCode.OK, JSONObject.toJSON(roleService.findAllRoleByUserId(uid)));
    }

    @GetMapping("/permission")
    @RequiresPermissions("system:")
    public ResponseResult listPermission() {
        return ResponseResult.e(ResponseCode.OK, resourceService.list());
    }

    @PostMapping("/role")
    public ResponseResult addRole(@RequestBody JSONObject roles) {
        SysRole role = new SysRole(roles.getString("roleName"), roles.getString("description"));
        roleService.save(role);
        return ResponseResult.e(ResponseCode.OK);
    }
}
