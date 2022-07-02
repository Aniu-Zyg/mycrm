package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.mapper.CustomerRemarkMapper;
import com.bjpowernode.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("customerRemarkService")
public class CustomerRemarkServiceImpl implements CustomerRemarkService {

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Override
    public List<CustomerRemark> queryCustomerRemarkForDetailByCustomerId(String id) {
        return customerRemarkMapper.selectCustomerRemarkForDetailByCustomerId(id);
    }

    @Override
    public int saveCreateCustomerRemark(CustomerRemark remark) {
        return customerRemarkMapper.insertCustomerRemark(remark);
    }

    @Override
    public int deleteCustomerRemarkById(String id) {
        return customerRemarkMapper.deleteCustomerRemarkById(id);
    }

    @Override
    public int saveEditCustomerRemarkById(CustomerRemark remark) {
        return customerRemarkMapper.updateCustomerRemarkById(remark);
    }
}
