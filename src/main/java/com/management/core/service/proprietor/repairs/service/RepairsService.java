package com.management.core.service.proprietor.repairs.service;

import com.management.core.service.proprietor.repairs.comm.RepairsComm;

import java.util.List;

public interface RepairsService {

    /**
     * 获取某业主的所有报修记录
     *
     * @param proprietorId 业主id
     * @return 报修记录列表
     */
    List<RepairsComm> getAllRepairsRecord(Long proprietorId);

    /**
     * 添加报修记录
     *
     * @param proprietorId 业主id
     * @param summary      报修内容
     * @return 添加是否成功
     */
    boolean addRepairsRecord(Long proprietorId, String summary);

    /**
     * 修改报修记录
     *
     * @param id      保修编号
     * @param summary 报修内容
     * @return 修改是否成功
     */
    boolean updateRepairsRecord(Long id, String summary);

    /**
     * 根据报修内容和业主id关键字查询报修记录
     *
     * @param id      业主id
     * @param keyword 关键字
     * @return 报修记录
     */
    List<RepairsComm> getRepairsRecordByKeyword(Long id, String keyword);
}
