package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryService {

    List<TranHistory> queryTranHistoryForDetailForTranId(String tranId);
}
