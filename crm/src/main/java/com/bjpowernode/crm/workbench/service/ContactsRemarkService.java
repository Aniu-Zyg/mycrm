package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ContactsRemark;

import java.util.List;

public interface ContactsRemarkService {

    List<ContactsRemark> queryContactsRemarkForDetailByContactsId(String id);

    int saveCreateContactsRemark(ContactsRemark remark);

    int deleteContactsRemarkById(String id);

    int saveEditContactsRemarkById(ContactsRemark remark);

}
