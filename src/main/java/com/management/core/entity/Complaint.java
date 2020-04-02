package com.management.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * <p>
 * 投诉记录表
 * </p>
 *
 * @author zyf
 * @since 2019-09-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投诉编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业主id
     */
    private Long proprietorId;

    /**
     * 投诉日期
     */
    private Date complainDate;

    /**
     * 处理日期
     */
    private Date conductDate;

    /**
     * 投诉内容
     */
    private String summary;

    /**
     * 状态（1为已处理，0为未处理）
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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProprietorId() {
        return proprietorId;
    }

    public void setProprietorId(Long proprietorId) {
        this.proprietorId = proprietorId;
    }

    public Date getComplainDate() {
        return complainDate;
    }

    public void setComplainDate(Date complainDate) {
        this.complainDate = complainDate;
    }

    public Date getConductDate() {
        return conductDate;
    }

    public void setConductDate(Date conductDate) {
        this.conductDate = conductDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
