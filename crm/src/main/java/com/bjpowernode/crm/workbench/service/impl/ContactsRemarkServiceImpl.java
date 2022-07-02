package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ContactsRemark;
import com.bjpowernode.crm.workbench.mapper.ContactsRemarkMapper;
import com.bjpowernode.crm.workbench.service.ContactsRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("contactsRemarkService")
public class ContactsRemarkServiceImpl implements ContactsRemarkService {

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Override
    public List<ContactsRemark> queryContactsRemarkForDetailByContactsId(String id) {
        return contactsRemarkMapper.selectContactsRemarkForDetailByContactsId(id);
    }

    @Override
    public int saveCreateContactsRemark(ContactsRemark remark) {
        return contactsRemarkMapper.insertContactsRemark(remark);
    }

    @Override
    public int deleteContactsRemarkById(String id) {
        return contactsRemarkMapper.deleteContactsRemarkById(id);
    }

    @Override
    public int saveEditContactsRemarkById(ContactsRemark remark) {
        return contactsRemarkMapper.updateContactsRemarkById(remark);
    }
}
