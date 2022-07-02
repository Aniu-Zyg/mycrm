package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    int saveCreateCustomer(Customer customer);

    List<Customer> queryCustomerByConditionForPage(Map<String, Object> map);

    int queryCountOfCustomerByCondition(Map<String, Object> map);

    Customer queryCustomerById(String id);

    int saveEditCustomer(Customer customer);

    int deleteCustomerByIds(String[] ids);

    Customer queryCustomerForDetailById(String id);

    List<String> queryCustomerNameByFuzzyName(String customerName);

    String queryCustomerIdByName(String customerName);
}
