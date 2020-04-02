package com.management.core.service.admin.dto.param;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: zyf
 * @create: 2019-09-20
 **/

@NoArgsConstructor
@Data
public class CreateParkListParam {


    /**
     * id : 1
     * carNum : 粤ABCDEF
     * days : 111
     */

    private Integer id;
    private String carNum;
    private Integer days;
}