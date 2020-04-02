package com.management.core.service.admin.park.impl;

import com.management.core.service.admin.dto.param.ReadParkListParam;
import com.management.core.service.admin.park.service.ParkAdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ParkServiceImplTest {

    @Autowired
    ParkAdminService parkAdminService;

    @Test
    public void getList() {

        System.out.println("tt" + parkAdminService.getList(new ReadParkListParam()));
    }

    @Test
    public void updateItem() {
    }

    @Test
    public void deleteItem() {
        Long a = 24 * 60 * 60 * 1000L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        System.out.println(System.currentTimeMillis());
        System.out.println(sdf.format(ts));
        try {
            System.out.println(sdf.parse("2019-07-18").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createItem() {
    }
}