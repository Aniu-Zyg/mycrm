package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.DicValueService;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        // 调用service层方法，查询数据字典
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        // 把数据保存到request中
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        // 请求转发到交易的主页面
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        // 调用service层方法，查询数据字典
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        // 把数据保存到request中
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        // 请求转发到创建交易的页面
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    public @ResponseBody Object getPossibilityByStage(String stageValue){
        // 通过possibility配置文件，获取阶段可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(stageValue);
        // 返回响应信息
        return possibility;
    }

    @RequestMapping("/workbench/transaction/queryCustomerNameByFuzzyName.do")
    public @ResponseBody Object queryCustomerNameByFuzzyName(String customerName) {
        List<String> customers = customerService.queryCustomerNameByFuzzyName(customerName);
        return customers;
    }

    @RequestMapping("/workbench/transaction/queryActivityByFuzzyName.do")
    public @ResponseBody Object queryActivityByFuzzyName(String activityName) {
        List<Activity> activityList = activityService.queryActivityByFuzzyName(activityName);
        return activityList;
    }

    @RequestMapping("/workbench/transaction/queryContactsByFuzzyName.do")
    public @ResponseBody Object queryContactsByFuzzyName(String contactsName) {
        List<Contacts> contactsList = contactsService.queryContactsByFuzzyName(contactsName);
        return contactsList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    public @ResponseBody Object saveCreateTran(@RequestParam Map<String, Object> map, HttpSession session) {
        // 封装参数
        map.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();
        try {
            // 调用service层方法，保存创建的交易
            tranService.saveCreateTran(map);

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/transaction/queryTranByConditionForPage.do")
    public @ResponseBody Object queryTranByConditionForPage(String owner, String name, String customerId, String stage, String type, String source, String contactsId, int pageNo, int pageSize){
        Map<String, Object> map = new HashMap<>();
        // 封装参数
        map.put("owner", owner);
        map.put("name", name);
        map.put("customerId", customerId);
        map.put("stage", stage);
        map.put("type", type);
        map.put("source", source);
        map.put("contactsId", contactsId);
        map.put("beginNo", (pageNo-1) * pageSize);
        map.put("pageSize", pageSize);
        // 调用service层，查询数据
        List<Tran> tranList = tranService.queryTranByConditionForPage(map);
        int totalRows = tranService.queryCountOfTranByCondition(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("tranList", tranList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/transaction/toEdit.do")
    public String toEdit(String id, HttpServletRequest request){
        // 调用service层方法，查询数据字典和相关信息
        List<User> userList = userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        Tran tran = tranService.queryTranById(id);

        // 解析properties配置文件，根据阶段获取可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String dicValueId = dicValueService.queryDicValueById(tran.getStage());
        String possibility = bundle.getString(dicValueId);

        // 把数据保存到request中
        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("transactionTypeList", transactionTypeList);
        request.setAttribute("sourceList", sourceList);
        request.setAttribute("tran", tran);
        request.setAttribute("possibility", possibility);
        // 请求转发到创建交易的页面
        return "workbench/transaction/edit";
    }

    @RequestMapping("/workbench/transaction/saveEditTran.do")
    public @ResponseBody Object saveEditTran(@RequestParam Map<String, Object> map, HttpSession session) {
        // 封装参数
        map.put(Contants.SESSION_USER, session.getAttribute(Contants.SESSION_USER));

        ReturnObject returnObject = new ReturnObject();
        try {
            // 调用service层方法，保存创建的交易
            tranService.saveEditTran(map);

            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/transaction/deleteTranByIds.do")
    public @ResponseBody Object deleteTranByIds(String[] id){
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.deleteTranByIds(id); // 通过联系人id数组删除所有对应的线索以及该线索的所有信息
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) { // 发生了某些异常，捕获后返回信息
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id, HttpServletRequest request){
        // 调用service层方法，查询信息
        Tran tran = tranService.queryTranForDetailById(id);
        List<TranRemark> remarkList = tranRemarkService.queryTranRemarkForDetailByTranId(id);
        List<TranHistory> historyList = tranHistoryService.queryTranHistoryForDetailForTranId(id);
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");

        // 根据交易的所处阶段的查询可能性
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());

        // 将数据保存到request作用域中
        request.setAttribute("tran", tran);
        request.setAttribute("remarkList", remarkList);
        request.setAttribute("historyList", historyList);
        request.setAttribute("possibility", possibility);
        request.setAttribute("stageList", stageList);
        // 请求转发
        return "workbench/transaction/detail";
    }

    @RequestMapping("/workbench/transaction/deleteTranById.do")
    public @ResponseBody Object deleteTranById(String tranId){
        ReturnObject returnObject = new ReturnObject();
        try {
            tranService.deleteTranById(tranId); // 通过联系人id数组删除所有对应的线索以及该线索的所有信息
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) { // 发生了某些异常，捕获后返回信息
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统繁忙，请稍后重试...");
        }
        return returnObject;
    }

}
