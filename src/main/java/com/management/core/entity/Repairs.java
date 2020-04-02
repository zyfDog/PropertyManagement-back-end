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
 * 报修表
 * </p>
 *
 * @author zyf
 * @since 2019-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Repairs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 报修编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主id
     */
    private Long proprietorId;

    /**
     * 报修时间
     */
    private Long repairsTime;

    /**
     * 报修描述
     */
    private String summary;

    /**
     * 维修人员姓名
     */
    private String serviceman;

    /**
     * 状态（0为未处理，1为已处理）
     */
    private Boolean status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 维修地址
     */
    private String address;

    private Long serviceDate;


}
