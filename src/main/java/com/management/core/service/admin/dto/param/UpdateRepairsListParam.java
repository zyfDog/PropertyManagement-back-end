package com.management.core.service.admin.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 修改报修管理业务参数
 * @author: zyf
 * @create: 2019-09-20
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateRepairsListParam {

    /**
     * summary : xxxxxxxxxxxxxxxxxxxxxxxxxx
     * serviceman : 张三
     * address : xxxxxxxxxxxxxxxxxxxxxxxx
     * status : 未处理
     */

    private String summary;
    private String serviceman;
    private String address;
    private String status;
}