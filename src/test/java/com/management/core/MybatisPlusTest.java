package com.management.core;

import com.management.core.entity.Administrator;
import com.management.core.entity.Cost;
import com.management.core.entity.SysLog;
import com.management.core.entity.SysRoleResource;
import com.management.core.mapper.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc
public class MybatisPlusTest {
    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private CostMapper costMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysLogMapper sysLogMapper;

    @Autowired
    private SysRoleResourceMapper sysRoleResourceMapper;

    @Test
    public void test1() {
        List<Administrator> list = administratorMapper.selectList(null);
        list.forEach(System.out::println);
        List<Cost> costs = costMapper.selectList(null);
        costs.forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<Cost> list = costMapper.selectList(null);
        list.forEach(System.out::println);
    }

    @Test
    public void test3() {
        List<SysLog> sysLogs = sysLogMapper.selectList(null);
        sysLogs.forEach(System.out::println);
    }

    @Test
    public void test4() {
        List<SysRoleResource> sysRoleResources = sysRoleResourceMapper.selectList(null);
        sysRoleResources.forEach(System.out::println);
    }

    @Test
    public void test5() {
        Long now = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = format.format(1560842968000L);
        String nowTime = format.format(now);
        Long daysBetween = 0L;
        try {
            Date jobStartDate;
            Date nowDate;
            jobStartDate = format.parse(startTime);
            nowDate = format.parse(nowTime);
            daysBetween = (nowDate.getTime() - jobStartDate.getTime() + 1000000) / (60 * 60 * 24 * 1000);
        } catch (Exception e) {

        }

        System.out.println(daysBetween);
    }
}
