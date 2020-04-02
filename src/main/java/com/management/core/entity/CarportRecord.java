package com.management.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 车位记录表
 * </p>
 *
 * @author zyf
 * @since 2019-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarportRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主id
     */
    private Long proprietorId;

    /**
     * 车位id
     */
    private Long carportId;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 开始停车日期
     */
    private Long stopStartDate;

    /**
     * 停车结束日期
     */
    private Long stopEndDate;

    /**
     * 总额
     */
    private Double money;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 车位是否空闲（0为空闲，1为占用）
     */
    private Boolean status;


}
