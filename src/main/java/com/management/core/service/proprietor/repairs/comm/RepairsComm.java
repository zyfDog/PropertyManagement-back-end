package com.management.core.service.proprietor.repairs.comm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RepairsComm {
    /**
     * id : 1
     * repairDate : 2019-9-12
     * summary : xxxxxxxxxxxxxxxxxxxxxxxxxx
     * serviceman : 小王
     * status : 未处理
     */

    private Long id;
    private String repairDate;
    private String summary;
    private String serviceman;
    private String status;
}
