package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.FunnelVO;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("contactsService")
public class ContactsServiceImpl implements ContactsService {

    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    public int saveCreateContacts(Contacts contacts) {
        return contactsMapper.insertContacts(contacts);
    }

    @Override
    public List<Contacts> queryContactsByConditionForPage(Map<String, Object> map) {
        return contactsMapper.selectContactsByConditionForPage(map);
    }

    @Override
    public int queryCountOfContactsByCondition(Map<String, Object> map) {
        return contactsMapper.selectCountOfContactsByCondition(map);
    }

    @Override
    public Contacts queryContactsById(String id) {
        return contactsMapper.selectContactsById(id);
    }

    @Override
    public int saveEditContacts(Contacts contacts) {
        return contactsMapper.updateContacts(contacts);
    }

    @Override
    public int deleteContactsByIds(String[] ids) {
        return contactsMapper.deleteContactsByIds(ids);
    }

    @Override
    public Contacts queryContactsForDetailById(String id) {
        return contactsMapper.selectContactsForDetailById(id);
    }

    @Override
    public List<Contacts> queryContactsForDetailByCustomerId(String id) {
        return contactsMapper.selectContactsForDetailByCustomerId(id);
    }

    @Override
    public int deleteContactsForDetailById(String id) {
        return contactsMapper.deleteContactsForDetailById(id);
    }

    @Override
    public List<Contacts> queryContactsByFuzzyName(String contactsName) {
        return contactsMapper.selectContactsByFuzzyName(contactsName);
    }

    @Override
    public List<FunnelVO> queryCountOfCustomerAndContactsGroupByCustomer() {
        return contactsMapper.selectCountOfCustomerAndContactsGroupByCustomer();
    }
}
