package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;
import com.bjpowernode.crm.workbench.mapper.ContactsActivityRelationMapper;
import com.bjpowernode.crm.workbench.service.ContactsActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("contactsActivityRelationService")
public class ContactsActivityRelationServiceImpl implements ContactsActivityRelationService {

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Override
    public int saveCreateContactsActivityRelationByList(List<ContactsActivityRelation> list) {
        return contactsActivityRelationMapper.insertContactsActivityRelationByList(list);
    }

    @Override
    public int deleteContactsActivityRelationByContactsIdActivityId(ContactsActivityRelation relation) {
        return contactsActivityRelationMapper.deleteContactsActivityRelationByContactsIdActivityId(relation);
    }
}
