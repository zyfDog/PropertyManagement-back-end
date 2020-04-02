package com.management.core.service.proprietor.complaint.comm;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ComplaintComm {
    /**
     * id : 1
     * complaintDate : 2019-6-12
     * summary : xxxxxxxxxxxxxxxxxxxxxxxxxx
     * dealDate : 2019-6-13
     * status : 未处理
     */

    private Long id;
    private String complaintDate;
    private String summary;
    private String dealDate;
    private String status;
}
