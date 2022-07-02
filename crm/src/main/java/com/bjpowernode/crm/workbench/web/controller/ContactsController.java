package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ContactsController {

    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ContactsRemarkService contactsRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactsActivityRelationService contactsActivityRelationService;

    @Autowired
    private TranService tranService;

    @RequestMapping("/workbench/contacts/index.do")
    public String index(HttpServletRequest request){
        // 调用service层，查询所有的用户
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        // 将数据保存到request
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("sourceList", sourceList);
        // 请求转发到客户的主页面
        return "workbench/contacts/index";
    }

    @RequestMapping("/workbench/contacts/queryCustomerNameByFuzzyName.do")
    public @ResponseBody Object queryCustomerNameByFuzzyName(String customerName) {
        List<String> customers = customerService.queryCustomerNameByFuzzyName(customerName);
        return customers;
    }

    @RequestMapping("/workbench/contacts/saveCreateContacts.do")
    public @ResponseBody Object saveCreateContacts(Contacts contacts, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        contacts.setId(UUIDUtils.getUUID());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));

        // 将前端的CustomerId作为查询条件查找客户的32位id，若没有则根据已有信息新建一个客户
        String customerName = contacts.getCustomerId();
        String customerId = customerService.queryCustomerIdByName(customerName);
        if (customerId != null){
            contacts.setCustomerId(customerId);
        }else {
            Customer customer = new Customer();
            contacts.setCustomerId(UUIDUtils.getUUID());

            // 封装新的客户信息
            customer.setId(contacts.getCustomerId());
            customer.setOwner(contacts.getOwner());
            customer.setName(customerName);
            customer.setCreateBy(contacts.getCreateBy());
            customer.setCreateTime(contacts.getCreateTime());
            customer.setDescription(contacts.getDescription());
            customer.setContactSummary(contacts.getContactSummary());
            customer.setNextContactTime(contacts.getNextContactTime());
            customer.setAddress(contacts.getAddress());
            customer.setPhone(" ");
            customer.setWebsite(" ");

            ReturnObject returnObject = new ReturnObject();
            try{
                // 执行service层，添加客户
                int ret = customerService.saveCreateCustomer(customer);

                if (ret == 0){
                    returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                    returnObject.setMessage("系统忙，请稍后重试...");
                    return returnObject;
                }
            }catch (Exception e){
                e.printStackTrace();
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
                return returnObject;
            }
        }

        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层保存创建的联系人
            int ret = contactsService.saveCreateContacts(contacts);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/contacts/queryContactsByConditionForPage.do")
    public @ResponseBody Object queryContactsByConditionForPage(String owner, String fullname, String customerId, String source, String job, int pageNo, int pageSize){
        Map<String, Object> map = new HashMap<>();
        // 封装参数
        map.put("owner", owner);
        map.put("fullname", fullname);
        map.put("customerId", customerId);
        map.put("source", source);
        map.put("job", job);
        map.put("beginNo", (pageNo-1) * pageSize);
        map.put("pageSize", pageSize);
        // 调用service层，查询数据
        List<Contacts> contactsList = contactsService.queryContactsByConditionForPage(map);
        int totalRows = contactsService.queryCountOfContactsByCondition(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("contactsList", contactsList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/contacts/queryContactsById.do")
    public @ResponseBody Object queryContactsById(String id){
        // 调用service层，查询联系人
        Contacts contacts = contactsService.queryContactsById(id);
        // 将contacts中的customerId值换成对应的名称
        Customer customer = customerService.queryCustomerById(contacts.getCustomerId());
        if (customer == null) {
            // 如果该客户不存在，则赋予空值
            contacts.setCustomerId("");
        } else {
            // 如果该客户存在，则换为客户名称
            contacts.setCustomerId(customer.getName());
        }
        // 返回响应信息
        return contacts;
    }

    @RequestMapping("/workbench/contacts/saveEditContacts.do")
    public @ResponseBody Object saveEditContacts(Contacts contacts, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        contacts.setEditBy(user.getId());
        contacts.setEditTime(DateUtils.formatDateTime(new Date()));
        // 将contacts中的customerId名称换成对应的Id
        String customerName = contacts.getCustomerId();
        contacts.setCustomerId(customerService.queryCustomerIdByName(customerName));

        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层，更新客户数据
            int ret = contactsService.saveEditContacts(contacts);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/contacts/deleteContactsByIds.do")
    public @ResponseBody Object deleteContactsByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            // 调用service层方法，删除市场活动
            int ret = contactsService.deleteContactsByIds(id);
            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/contacts/detailContacts.do")
    public String detailClue(String id, HttpServletRequest request){
        // 调用service方法，查询数据
        Contacts contacts = contactsService.queryContactsForDetailById(id);
        List<ContactsRemark> remarkList = contactsRemarkService.queryContactsRemarkForDetailByContactsId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByContactsId(id);
        List<Tran> tranList = tranService.queryTranForDetailByContactsId(id);
        // 把数据保存到request中
        request.setAttribute("contacts", contacts);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("activityList", activityList);
        request.setAttribute("tranList", tranList);
        // 请求转发
        return "workbench/contacts/detail";
    }

    @RequestMapping("/workbench/contacts/queryActivityForDetailByNameContactsId.do")
    public @ResponseBody Object queryActivityForDetailByNameContactsId(String activityName, String contactsId){
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("activityName", activityName);
        map.put("contactsId", contactsId);
        // 调用service层，查询市场活动
        List<Activity> activityList = activityService.queryActivityForDetailByNameContactsId(map);
        // 根据查询结果，返回需要信息
        return activityList;
    }

    @RequestMapping("/workbench/contacts/saveBind.do")
    public @ResponseBody Object saveBind(String[] activityId, String contactsId){
        // 封装参数
        ContactsActivityRelation car = null;
        List<ContactsActivityRelation> relationList = new ArrayList<>();
        for(String ai:activityId){
            car = new ContactsActivityRelation();
            car.setId(UUIDUtils.getUUID());
            car.setActivityId(ai);
            car.setContactsId(contactsId);
            relationList.add(car);
        }

        ReturnObject returnObject = new ReturnObject();
        try {
            // 调用service方法，批量保存线索和市场活动的关联关系
            int ret = contactsActivityRelationService.saveCreateContactsActivityRelationByList(relationList);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                List<Activity> activityList = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/contacts/saveUnbind.do")
    public @ResponseBody Object saveUnbind(ContactsActivityRelation relation){
        ReturnObject returnObject = new ReturnObject();
        try {
            // 调用service层方法，删除线索和市场活动的关联关系
            int ret = contactsActivityRelationService.deleteContactsActivityRelationByContactsIdActivityId(relation);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

}
