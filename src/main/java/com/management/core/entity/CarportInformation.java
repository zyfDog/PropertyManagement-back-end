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
 * 车位基本信息表
 * </p>
 *
 * @author zyf
 * @since 2019-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CarportInformation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车位编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 车位位置
     */
    private String location;

    /**
     * 租用状态（0为未租用， 1为已租用）
     */
    private Boolean status;

    /**
     * 创建时间
     */
    private Date createTime;


}
