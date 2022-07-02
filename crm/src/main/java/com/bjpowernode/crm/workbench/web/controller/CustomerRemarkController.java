package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class CustomerRemarkController {

    @Autowired
    private CustomerRemarkService customerRemarkService;

    @RequestMapping("/workbench/customer/saveCreateCustomerRemark.do")
    public @ResponseBody Object saveCreateCustomerRemark(CustomerRemark remark, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateBy(user.getId());
        remark.setCreateTime(DateUtils.formatDateTime(new Date()));
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_NO_EDITED);

        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层，保存创建的客户备注
            int ret = customerRemarkService.saveCreateCustomerRemark(remark);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
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

    @RequestMapping("/workbench/customer/deleteCustomerRemarkById.do")
    public @ResponseBody Object deleteCustomerRemarkById(String id){
        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层，保存创建的客户备注
            int ret = customerRemarkService.deleteCustomerRemarkById(id);

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

    @RequestMapping("/workbench/customer/saveEditCustomerRemark.do")
    public @ResponseBody Object saveEditCustomerRemark(CustomerRemark remark, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        // 封装参数
        remark.setEditFlag(Contants.REMARK_EDIT_FLAG_YES_EDITED);
        remark.setEditBy(user.getId());
        remark.setEditTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject = new ReturnObject();
        try{
            // 调用service层，更新客户备注
            int ret = customerRemarkService.saveEditCustomerRemarkById(remark);

            if (ret > 0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetData(remark);
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
