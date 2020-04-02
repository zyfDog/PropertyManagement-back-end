package com.management.core.service.admin.dto.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 停车场列表参数
 * @author: zyf
 * @create: 2019-09-20
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReadParkListParam {


    //住户编号
    private Long id;
    //车牌号
    private String num;
    private String status;
}