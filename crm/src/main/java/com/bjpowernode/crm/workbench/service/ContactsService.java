package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.FunnelVO;

import java.util.List;
import java.util.Map;

public interface ContactsService {

    int saveCreateContacts(Contacts contacts);

    List<Contacts> queryContactsByConditionForPage(Map<String, Object> map);

    int queryCountOfContactsByCondition(Map<String, Object> map);

    Contacts queryContactsById(String id);

    int saveEditContacts(Contacts contacts);

    int deleteContactsByIds(String[] ids);

    Contacts queryContactsForDetailById(String id);

    List<Contacts> queryContactsForDetailByCustomerId(String id);

    int deleteContactsForDetailById(String id);

    List<Contacts> queryContactsByFuzzyName(String contactsName);

    List<FunnelVO> queryCountOfCustomerAndContactsGroupByCustomer();
}
