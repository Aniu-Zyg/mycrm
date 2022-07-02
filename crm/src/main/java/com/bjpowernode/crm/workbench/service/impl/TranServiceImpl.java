package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TranHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TranMapper;
import com.bjpowernode.crm.workbench.mapper.TranRemarkMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public void saveCreateTran(Map<String, Object> map) {
        String customerName = (String) map.get("customerId");
        User user = (User) map.get(Contants.SESSION_USER);
        // 根据customerName查询客户ID
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName((String) map.get("customerId"));
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setContactSummary((String) map.get("contactSummary"));
            customer.setNextContactTime((String) map.get("nextContactTime"));
            customer.setDescription((String) map.get("description"));
            customerMapper.insertCustomer(customer);
        }

        // 保存交易的信息
        Tran tran = new Tran();
        tran.setStage((String) map.get("stage"));
        tran.setOwner((String) map.get("owner"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setId(UUIDUtils.getUUID());
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setCreateTime(DateUtils.formatDateTime(new Date()));
        tran.setCreateBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));
        tranMapper.insertTran(tran);

        // 保存交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public List<Tran> queryTranByConditionForPage(Map<String, Object> map) {
        return tranMapper.selectTranByConditionForPage(map);
    }

    @Override
    public int queryCountOfTranByCondition(Map<String, Object> map) {
        return tranMapper.selectCountOfTranByCondition(map);
    }

    @Override
    public Tran queryTranById(String id) {
        return tranMapper.selectTranById(id);
    }

    @Override
    public void saveEditTran(Map<String, Object> map) {
        String customerName = (String) map.get("customerId");
        User user = (User) map.get(Contants.SESSION_USER);
        // 根据customerName查询客户ID
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if (customer == null){
            customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName((String) map.get("customerId"));
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formatDateTime(new Date()));
            customer.setContactSummary((String) map.get("contactSummary"));
            customer.setNextContactTime((String) map.get("nextContactTime"));
            customer.setDescription((String) map.get("description"));
            customerMapper.insertCustomer(customer);
        }

        // 保存交易的信息
        Tran tran = new Tran();
        tran.setId((String) map.get("id"));
        tran.setStage((String) map.get("stage"));
        tran.setOwner((String) map.get("owner"));
        tran.setNextContactTime((String) map.get("nextContactTime"));
        tran.setName((String) map.get("name"));
        tran.setMoney((String) map.get("money"));
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setEditTime(DateUtils.formatDateTime(new Date()));
        tran.setEditBy(user.getId());
        tran.setContactSummary((String) map.get("contactSummary"));
        if ((String) map.get("contactsId") != null){
            tran.setContactsId((String) map.get("contactsId"));
        }else {
            map.remove(map.get("contactsId"));
        }
        if ((String) map.get("activityId") != null) {
            tran.setActivityId((String) map.get("activityId"));
        }else {
            map.remove(map.get("activityId"));
        }
        tran.setDescription((String) map.get("description"));
        tran.setSource((String) map.get("source"));
        tran.setType((String) map.get("type"));

        tranMapper.updateTranById(tran);

        // 保存交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setCreateBy(user.getId());
        tranHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setId(UUIDUtils.getUUID());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
        tranHistory.setTranId(tran.getId());
        tranHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public int deleteTranByIds(String[] id) {
        tranMapper.deleteTranByIds(id);
        tranHistoryMapper.deleteTranHistoryByTranIds(id);
        return tranRemarkMapper.deleteTranRemarkByTranIds(id);
    }

    @Override
    public Tran queryTranForDetailById(String id) {
        return tranMapper.selectTranForDetailById(id);
    }

    @Override
    public List<Tran> queryTranForDetailByCustomerId(String customerId) {
        return tranMapper.selectTranForDetailByCustomerId(customerId);
    }

    @Override
    public int deleteTranById(String id) {
        tranMapper.deleteTranById(id);
        tranHistoryMapper.deleteTranHistoryByTranId(id);
        return tranRemarkMapper.deleteTranRemarkByTranId(id);
    }

    @Override
    public List<Tran> queryTranForDetailByContactsId(String contactsId) {
        return tranMapper.selectTranForDetailByContactsId(contactsId);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return tranMapper.selectCountOfTranGroupByStage();
    }
}
