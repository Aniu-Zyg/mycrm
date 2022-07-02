package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkService {

    List<CustomerRemark> queryCustomerRemarkForDetailByCustomerId(String id);

    int saveCreateCustomerRemark(CustomerRemark remark);

    int deleteCustomerRemarkById(String id);

    int saveEditCustomerRemarkById(CustomerRemark remark);

}
