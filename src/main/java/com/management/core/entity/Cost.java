package com.management.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 缴费记录表
 * </p>
 *
 * @author zyf
 * @since 2019-09-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Cost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 缴费编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主id
     */
    private Long proprietorId;

    /**
     * 缴费日期
     */
    private Long costDate;

    /**
     * 缴费项目id
     */
    private Long itemId;

    /**
     * 缴费总额
     */
    private Double money;

    /**
     * 缴费状态（0为未缴费，1为已缴费）
     */
    private Boolean status;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 更新时间
     */
    private Long auditorId;

    /**
     * 发票编号
     */
    private String invoice;


}
