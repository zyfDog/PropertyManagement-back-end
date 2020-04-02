package com.management.core.service.admin.pairs.impl;

import com.management.core.service.admin.dto.param.ReadRepairsListParam;
import com.management.core.service.admin.pairs.service.PairsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PairsServiceImplTest {

    @Autowired
    PairsAdminService pairsService;

    @Test
    public void getList() {
        ReadRepairsListParam param = new ReadRepairsListParam();
        log.debug("tt" + pairsService.getList(param).size());
    }

    @Test
    public void updateItem() {
    }

    @Test
    public void deleteItem() {
    }
}