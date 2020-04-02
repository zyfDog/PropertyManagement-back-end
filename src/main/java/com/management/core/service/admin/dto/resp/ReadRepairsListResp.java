package com.management.core.service.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 报修业务列表模板
 * @author: zyf
 * @create: 2019-09-20
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReadRepairsListResp {


    /**
     * id : 1
     * proprietorId : 1
     * repairDate : 2019-6-12
     * address : xxxxxxx
     * summary : xxxxxxxxxxxxxxxxxxxxxxxxxx
     * serviceDate : 2019-6-13
     * serviceman : 小王
     * status : 未处理
     */

    private Long id;
    private Long proprietorId;
    private String repairDate;
    private String address;
    private String summary;
    private String serviceDate;
    private String serviceman;
    private String status;
}