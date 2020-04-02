package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.common.annotation.SysLogs;
import com.management.common.bean.ResponseCode;
import com.management.common.bean.ResponseResult;
import com.management.core.entity.Administrator;
import com.management.core.entity.Proprietor;
import com.management.core.entity.SysLog;
import com.management.core.entity.SysUser;
import com.management.core.mapper.AdministratorMapper;
import com.management.core.mapper.ProprietorMapper;
import com.management.core.service.system.SysLogService;
import com.management.core.service.system.SysRoleService;
import com.management.core.service.system.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.crypto.hash.Sha384Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: zyf
 * @create: 2019-09-25
 * @Description: 基本设置
 */
@RestController
@RequestMapping("/administrator")
public class SettingController {

    @Resource
    SysUserService sysUserService;

    @Resource
    SysRoleService roleService;

    @Resource
    SysLogService sysLogService;

    @Resource
    ProprietorMapper proprietorMapper;

    @Resource
    AdministratorMapper administratorMapper;

    @GetMapping(value = {"/account/{keyword}", "/account"})
    @RequiresRoles("admin")
    @SysLogs("获取用户列表")
    public ResponseResult baseSetting(@PathVariable(required = false) String keyword) {
        Subject subject = SecurityUtils.getSubject();
        List<SysUser> user;
        if (subject.hasRole("administrator")) {
            user = sysUserService.list();
        } else {
            user = sysUserService.list(new QueryWrapper<SysUser>().ne("type", 1));
        }
        System.out.println(user);
        JSONArray array = new JSONArray();
        for (SysUser usr : user) {
            JSONObject object = (JSONObject) JSONObject.toJSON(usr);
            List<SysLog> sysLogs = sysLogService.list(new QueryWrapper<SysLog>()
                    .eq("uid", usr.getId()).orderByDesc("create_date").last("limit 10"));
            object.put("lastVisitDate", sysLogs.size() > 0 ? sysLogs.get(0).getCreateDate() : new Date());
            object.put("roles", usr.getType() == 1 ? "管理员" : "业主");

            String realName = "";
            if (usr.getType() == 2) {
                realName = proprietorMapper.selectOne(new QueryWrapper<Proprietor>()
                        .eq("proprietor_id", usr.getId())).getRealName();
            } else {
                realName = administratorMapper.selectOne(new QueryWrapper<Administrator>()
                        .eq("administrator_id", usr.getId())).getRealName();
            }

            object.put("realName", realName);

            object.remove("password");
            object.remove("salt");
            array.add(object);
        }


        if (array.size() > 0 && keyword != null && !"".equals(keyword.trim())) {
            List<Object> list = new LinkedList<>();
            for (Object o : array) {
                if (!((JSONObject) o).getString("realName").equals(keyword)) {
                    list.add(o);
                }
            }
            array.removeAll(list);
        }
        return ResponseResult.success(array);
    }

    @PutMapping("/account")
    @RequiresRoles("admin")
    @SysLogs("管理员重置用户密码角色")
    public ResponseResult resetPwd(@RequestBody JSONObject object) {
        Subject subject = SecurityUtils.getSubject();
        Long uid = object.getLong("id");
        String password = object.getString("password");
        String role = object.getString("role");
        if (uid == null || password == null || role == null) {
            return ResponseResult.e(400, "参数有空值");
        }

        SysUser user = sysUserService.getById(uid);
        if (user == null) {
            return ResponseResult.e(400, "找不到该用户");
        }


        if ((user.getType() == 1 || "管理员".equals(role)) && !subject.hasRole("administrator")) {
            return ResponseResult.e(ResponseCode.NOT_AUTH_FAIL);
        }

        if (user.getType() == 1 && "业主".equals(role)) {
            Administrator administrator = administratorMapper.selectOne(new QueryWrapper<Administrator>()
                    .eq("administrator_id", user.getId()));

            Proprietor proprietor = new Proprietor();
            proprietor.setProprietorId(uid);
            proprietor.setRealName(administrator.getRealName());
            proprietor.setPhone(administrator.getPhone());
            proprietorMapper.insert(proprietor);
            administratorMapper.deleteById(administrator.getId());
        } else if (user.getType() == 2 && "管理员".equals(role)) {
            Proprietor proprietor = proprietorMapper.selectOne(new QueryWrapper<Proprietor>()
                    .eq("proprietor_id", user.getId()));
            Administrator administrator = new Administrator();
            administrator.setAdministratorId(uid);
            administrator.setRealName(proprietor.getRealName());
            administratorMapper.insert(administrator);
            proprietorMapper.deleteById(proprietor.getId());
        }

        user.setPassword(new Sha384Hash(password + user.getUsername()).toBase64());
        user.setType("业主".equals(role) ? 2 : 1);
        roleService.updateRole(uid, Long.valueOf(user.getType()));

        if (sysUserService.updateById(user)) {
            return ResponseResult.success(null);
        }
        return ResponseResult.e(ResponseCode.FAIL);
    }

    @DeleteMapping("/account")
    @RequiresRoles("admin")
    @SysLogs("管理员删除用户")
    public ResponseResult deleteUser(@RequestBody JSONObject object) {
        Subject subject = SecurityUtils.getSubject();
        Long uid = object.getLong("id");
        if (uid == null) {
            return ResponseResult.e(400, "参数有空值");
        }

        SysUser user = sysUserService.getById(uid);

        if (user == null) {
            return ResponseResult.e(400, "找不到该用户");
        }

        if ((user.getType() == 1 && !subject.hasRole("administrator"))) {
            return ResponseResult.e(ResponseCode.NOT_AUTH_FAIL);
        }

        sysUserService.deleteUser(uid);

        return ResponseResult.success(null);
    }

}
