package com.management.core.service.proprietor.complaint.service;

import com.management.core.service.proprietor.complaint.comm.ComplaintComm;

import java.util.List;

public interface ComplaintService {

    /**
     * 根据业主id返回所有投诉信息
     *
     * @param id 业主id
     * @return 投诉信息
     */
    List<ComplaintComm> getAllComplaintByProprietorId(Long id);

    /**
     * 根据记录id修改投诉内容
     *
     * @param id      记录id
     * @param summary 投诉内容
     * @return 修改是否成功
     */
    boolean updateComplaintByComplaintId(Long id, String summary);

    /**
     * 根据日期筛选投诉信息
     *
     * @param id   业主id
     * @param date 日期
     * @return 投诉信息
     */
    List<ComplaintComm> getComplaintRecordBydate(Long id, String date);

    /**
     * 根据业主id添加投诉记录
     *
     * @param id      业主id
     * @param summary 投诉内容
     * @return 添加是否成功
     */
    boolean addComplaintRecordByProprietorId(Long id, String summary);
}
