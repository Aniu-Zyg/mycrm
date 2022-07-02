package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {

    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);

    int saveCreateClueRemark(ClueRemark remark);

    int deleteClueRemarkById(String id);

    int saveEditClueRemark(ClueRemark remark);
}
