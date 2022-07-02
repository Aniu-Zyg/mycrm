package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranService {

    void saveCreateTran(Map<String, Object> map);

    List<Tran> queryTranByConditionForPage(Map<String, Object> map);

    int queryCountOfTranByCondition(Map<String, Object> map);

    Tran queryTranById(String id);

    void saveEditTran(Map<String, Object> map);

    int deleteTranByIds(String[] id);

    Tran queryTranForDetailById(String id);

    List<Tran> queryTranForDetailByCustomerId(String customerId);

    int deleteTranById(String id);

    List<Tran> queryTranForDetailByContactsId(String ContactsId);

    List<FunnelVO> queryCountOfTranGroupByStage();
}
