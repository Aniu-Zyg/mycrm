package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerRemarkService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class CustomerController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRemarkService customerRemarkService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private TranService tranService;

    @Autowired
    private DicValueService dicValueService;

    @RequestMapping("/workbench/customer/index.do")
    public String index(HttpServletRequest request){
        // 调用service层，查询所有的用户
        List<User> userList = userService.queryAllUsers();
        // 将数据保存到request
        request.setAttribute("userList", userList);
        // 请求转发到客户的主页面
        return "workbench/customer/index";
    }

    @RequestMapping("/workbench/customer/saveCreateCustomer.do")
    public @ResponseBody Object saveCreateCustomer(Customer customer, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        customer.setId(UUIDUtils.getUUID());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层保存创建的客户
            int ret = customerService.saveCreateCustomer(customer);
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

    @RequestMapping("/workbench/customer/queryCustomerByConditionForPage.do")
    public @ResponseBody Object queryCustomerByConditionForPage(String name, String owner, String phone, String website, int pageNo, int pageSize){
        Map<String, Object> map = new HashMap<>();
        // 封装参数
        map.put("name", name);
        map.put("owner", owner);
        map.put("phone", phone);
        map.put("website", website);
        map.put("beginNo", (pageNo-1) * pageSize);
        map.put("pageSize", pageSize);
        // 调用service层，查询数据
        List<Customer> customerList = customerService.queryCustomerByConditionForPage(map);
        int totalRows = customerService.queryCountOfCustomerByCondition(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("customerList", customerList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/customer/queryCustomerById.do")
    public @ResponseBody Object queryActivityById(String id){
        // 调用service层，查询市场活动
        Customer customer = customerService.queryCustomerById(id);
        // 根据市场活动，返回响应信息
        return customer;
    }

    @RequestMapping("/workbench/customer/saveEditCustomer.do")
    public @ResponseBody Object saveEditCustomer(Customer customer, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        customer.setEditBy(user.getId());
        customer.setEditTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层，更新客户数据
            int ret = customerService.saveEditCustomer(customer);

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

    @RequestMapping("/workbench/customer/deleteCustomerByIds.do")
    public @ResponseBody Object deleteCustomerByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层，更新客户数据
            int ret = customerService.deleteCustomerByIds(id);

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

    @RequestMapping("/workbench/customer/detailCustomer.do")
    public String detailCustomer(String id, HttpServletRequest request){
        // 调用service层，查询客户和客户备注，还有创建联系人所需的数据
        Customer customer = customerService.queryCustomerForDetailById(id);
        List<CustomerRemark> customerRemarkList = customerRemarkService.queryCustomerRemarkForDetailByCustomerId(id);
        List<Contacts> contactsList = contactsService.queryContactsForDetailByCustomerId(id);
        List<Tran> tranList = tranService.queryTranForDetailByCustomerId(id);
        List<User> userList = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");

        // 将数据保存到request
        request.setAttribute("customer", customer);
        request.setAttribute("customerRemarkList", customerRemarkList);
        request.setAttribute("contactsList", contactsList);
        request.setAttribute("tranList", tranList);
        request.setAttribute("userList", userList);
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("sourceList", sourceList);

        // 请求转发到客户的主页面
        return "workbench/customer/detail";
    }

    @RequestMapping("/workbench/customer/deleteContactsForDetailById.do")
    public @ResponseBody Object deleteContactsForDetailById(String contactsId){
        ReturnObject returnObject = new ReturnObject();
        try {
            // 调用service层方法，删除市场活动
            int ret = contactsService.deleteContactsForDetailById(contactsId);
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

    @RequestMapping("/workbench/contacts/saveCreateContactsForDetail.do")
    public @ResponseBody Object saveCreateContactsForDetail(Contacts contacts, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        contacts.setId(UUIDUtils.getUUID());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层保存创建的联系人
            int ret = contactsService.saveCreateContacts(contacts);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);;
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
