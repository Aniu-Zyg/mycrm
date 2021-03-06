package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Thu Jun 16 01:03:31 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Thu Jun 16 01:03:31 CST 2022
     */
    int insert(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Thu Jun 16 01:03:31 CST 2022
     */
    int insertSelective(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Thu Jun 16 01:03:31 CST 2022
     */
    CustomerRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Thu Jun 16 01:03:31 CST 2022
     */
    int updateByPrimaryKeySelective(CustomerRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer_remark
     *
     * @mbggenerated Thu Jun 16 01:03:31 CST 2022
     */
    int updateByPrimaryKey(CustomerRemark record);

    /**
     * 批量保存创建的客户备注
     */
    int insertCustomerRemarkByList(List<CustomerRemark> list);

    /**
     * 查询所有的客户
     */
    List<Customer> selectAllCustomers();

    /**
     * 根据id查询客户的备注信息
     */
    List<CustomerRemark> selectCustomerRemarkForDetailByCustomerId(String id);

    /**
     * 保存创建的客户备注
     */
    int insertCustomerRemark(CustomerRemark remark);

    /**
     * 根据id删除客户备注
     */
    int deleteCustomerRemarkById(String id);

    /**
     * 根据id更新客户备注
     */
    int updateCustomerRemarkById(CustomerRemark remark);
}