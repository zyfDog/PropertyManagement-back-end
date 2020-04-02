package com.management.core.service.admin.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 添加停车位
 * @author: zyf
 * @create: 2019-09-20
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateParkParam {


    /**
     * id : 1
     * carNum : 粤ABCDEF
     * days : 111
     */

    private Long id;
    private String carNum;
    private Long days;
}