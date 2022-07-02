package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationService {

    int saveCreateContactsActivityRelationByList(List<ContactsActivityRelation> list);

    int deleteContactsActivityRelationByContactsIdActivityId(ContactsActivityRelation relation);
}
