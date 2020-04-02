package com.management.core.service.proprietor.cost.service;

import com.management.core.service.proprietor.cost.comm.CostComm;

import java.util.List;

public interface CostService {
    /**
     * 返回某个业主的所有缴费记录
     *
     * @param id 业主唯一id
     * @return 缴费记录
     */
    List<CostComm> getAllCostRecordByProprietorId(Long id);

    /**
     * 根据缴费日期返回相应结果
     *
     * @param date 缴费日期
     * @param id   业主id
     * @return 缴费记录
     */
    List<CostComm> getCostRecordByDate(Long id, String date);
}
