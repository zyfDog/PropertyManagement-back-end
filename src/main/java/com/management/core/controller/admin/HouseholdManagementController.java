package com.management.core.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.management.common.annotation.SysLogs;
import com.management.common.bean.ResponseResult;
import com.management.core.entity.Proprietor;
import com.management.core.entity.SysUser;
import com.management.core.mapper.ProprietorMapper;
import com.management.core.service.system.SysUserService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @author: zyf
 * @create: 2019-09-25
 * @Description: 住户管理
 */
@RestController
@RequestMapping("/administrator")
public class HouseholdManagementController {

    @Resource
    ProprietorMapper proprietorMapper;

    @Resource
    SysUserService sysUserService;

    @GetMapping("/proprietor/list")
    @RequiresRoles("admin")
    @SysLogs("获取住户列表")
    public ResponseResult getProprietorList() {
        return ResponseResult.success(
                proprietorMapper.selectList(new QueryWrapper<Proprietor>()
                        .ne("real_name", "")));
    }

    @PutMapping("/proprietor")
    @RequiresRoles("admin")
    @SysLogs("修改住户信息")
    public ResponseResult modifyProprietorList(@RequestBody JSONObject object) {
        Proprietor proprietor = JSONObject.toJavaObject(object, Proprietor.class);
        proprietorMapper.updateById(proprietor);
        return ResponseResult.success(proprietor);
    }

    @GetMapping("/proprietor/{keyword}")
    @RequiresRoles("admin")
    @SysLogs("按名字搜索住户")
    public ResponseResult getProprietorListByName(@PathVariable String keyword) {
        return ResponseResult.success(proprietorMapper.selectList(new QueryWrapper<Proprietor>().eq("real_name", keyword)));
    }

    @PostMapping("/proprietor")
    @RequiresRoles("admin")
    @SysLogs("添加住户")
    public ResponseResult addProprietor(@RequestBody JSONObject object) {
        String username = object.getString("account");
        Proprietor proprietor = JSONObject.toJavaObject(object, Proprietor.class);

        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("username", username));

        if (user == null) {
            return ResponseResult.builder().code(400).msg("请先创建账号").build();
        }

        int count = proprietorMapper.update(proprietor,
                new UpdateWrapper<Proprietor>().eq("proprietor_id", user.getId()));

        return count > 0 ? ResponseResult.success(null)
                : ResponseResult.builder().code(400).msg("请先创建账号").build();
    }

    @DeleteMapping("/proprietor/{id}")
    @RequiresRoles("admin")
    @SysLogs("删除住户")
    @Transactional
    public ResponseResult deleteProprietor(@PathVariable String id) {
        Proprietor proprietor = proprietorMapper.selectById(id);
        if (proprietor == null) {
            return ResponseResult.builder().code(400).msg("账号不存在").build();
        }

        sysUserService.deleteUser(proprietor.getProprietorId());
        return ResponseResult.success(null);
    }


}
