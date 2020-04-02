package com.management.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author zyf
 * @since 2019-09-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 1锁定，0不锁定
     */
    private boolean locked;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源地址
     */
    private String url;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 颜色
     */
    private String color;

    /**
     * 图标
     */
    private String icon;

    /**
     * 验证：1需要验证，0不需要验证
     */
    private Boolean verification;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 类型：按钮，菜单
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 权限标识
     */
    private String permission;

    @TableField(exist = false)
    private List<SysResource> children;
}
