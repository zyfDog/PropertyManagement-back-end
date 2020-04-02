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
 * 角色表
 * </p>
 *
 * @author zyf
 * @since 2019-09-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 1锁定，0不锁定
     */
    private Boolean locked;

    /**
     * 创建时间
     */
    private Date createTime;

    @TableField(exist = false)
    private List<SysResource> resources;

    public SysRole(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }
}
