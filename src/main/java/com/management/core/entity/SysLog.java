package com.management.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author zyf
 * @since 2019-09-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户ID
     */
    private String uid;

    /**
     * 操作IP
     */
    private String ip;

    /**
     * 是否异步
     */
    private Boolean ajax;

    /**
     * URL
     */
    private String uri;

    /**
     * 参数
     */
    private String params;

    /**
     * HTTP方法
     */
    private String httpMethod;

    /**
     * 类名
     */
    private String classMethod;

    /**
     * 动作名
     */
    private String actionName;

    /**
     * 响应时间
     */
    private Long responseTimes;

    /**
     * 异常
     */
    private String throwing;

    /**
     * 时间
     */
    private Date createDate;


}
