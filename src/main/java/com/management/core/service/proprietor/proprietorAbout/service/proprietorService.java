package com.management.core.service.proprietor.proprietorAbout.service;

import com.alibaba.fastjson.JSONObject;

public interface proprietorService {

    /**
     * @Description: 获取业主的个人信息
     * @Param: id 业主的id
     * @return:
     * @author zyf
     * @since 2019-09-16
     */
    JSONObject getProprietor(long id);


    /**
     * @Description: 修改账户密码
     * @Param:
     * @return:
     * @author zyf
     * @since 2019-09-16
     */

    boolean updateProprietor(long id, String password);


    /**
     * @Description: 修改账户信息
     * @Param:
     * @return:
     * @author zyf
     * @since 2019-09-16
     */

    boolean updateAccount(long id, String name, String phone, String number, String address);


    /**
     * @Description: 获取投诉列表
     * @Param:
     * @return:
     * @author zyf
     * @since 2019-09-16
     */

    JSONObject getComplaintList(long id);

}
