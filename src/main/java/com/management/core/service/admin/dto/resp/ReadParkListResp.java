package com.management.core.service.admin.dto.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: zyf
 * @create: 2019-09-20
 **/

@NoArgsConstructor
@Data
public class ReadParkListResp {

    /**
     * id : 1
     * proprietorId : 2016030403104
     * carNum : 粤ABCDE
     * usePeriod : ["2016-5-6","2016-7-8"]
     * amount : 520.1
     * status : 未处理
     */

    private Long id;
    private Long proprietorId;
    private String carNum;
    private Double amount;
    private String status;
    private List<String> usePeriod;
}