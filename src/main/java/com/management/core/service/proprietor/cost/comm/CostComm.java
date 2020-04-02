package com.management.core.service.proprietor.cost.comm;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CostComm {
    /**
     * id : 1
     * costDate : 2019-9-12
     * item : 物业费
     * money : 520.1
     * status : 未缴费
     */

    private Long id;
    private String costDate;
    private String item;
    private double money;
    private String status;
}
