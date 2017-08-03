package com.fsll.wmes.fund.action.console;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.entity.FundFeesItem;
import com.fsll.wmes.entity.FundFeesItemEn;
import com.fsll.wmes.entity.FundFeesItemSc;
import com.fsll.wmes.entity.FundFeesItemTc;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.fund.service.FundFeesItemService;
import com.fsll.wmes.fund.service.FundInfoService;

/**
 * 控制器:基金信息管理（工作台）
 * 
 * @author tan
 * @version 1.0.0 Created On: 2016-6-28
 */

@Controller
@RequestMapping("/console/fund/fees")
public class ConsoleFundFeesItemAct extends WmesBaseAct {
	
	@Autowired
	private FundFeesItemService fundFeesItemService;
	
	@Autowired
	private FundInfoService fundInfoService;
	
	@Autowired
	private SysParamService sysParamService;
	
    /**
     * 分页列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        this.isMobileDevice(request, model);
        return "console/fund/fees/list";//页面摆放路径
    }
    
    /**
     * 分页获得记录
     */
    @RequestMapping(value = "/listJson", method = RequestMethod.POST)
    public String listJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
    	String fundId = request.getParameter("fundId");
    	String feesItem = request.getParameter("feesItem");
    	String language = this.getLoginLangFlag(request);

    	
        jsonPaging = this.getJsonPaging(request);

        jsonPaging = fundFeesItemService.findAll(jsonPaging,fundId,feesItem,language);
        this.toJsonString(response, jsonPaging);
        return null;
    }
    
    /**
     * 详细信息页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String fundsdetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        String id = request.getParameter("id");
//    	String langCode = this.getLoginLangFlag(request);
//        FundFeesItemEn enVO = fundFeesItemService.findById4En(id);
//        FundFeesItemSc scVO = fundFeesItemService.findById4Sc(id);
//        FundFeesItemTc tcVO = fundFeesItemService.findById4Tc(id);        
//
//        FundFeesItemVO engVO = fundFeesItemService.findById(id,langCode);
        

        Object enVO = fundFeesItemService.findById(id,"en");
        Object scVO = fundFeesItemService.findById(id,"sc");
        Object tcVO = fundFeesItemService.findById(id,"tc");
        
        List<SysParamConfig> list = sysParamService.findParamConfigByType("fees_item");
        
        model.put("enVO", enVO);
        model.put("scVO", scVO);
        model.put("tcVO", tcVO);
        model.put("typeListVO", list);
        
//        model.addAttribute("typeListVO", list);
        
        return "console/fund/fees/input";
    }
    
    
	@RequestMapping(value = "/saveEn", method = RequestMethod.POST)
	public void saveEnObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {

		String actionMsg="global.success";
		boolean isAdd =false;
		String id = request.getParameter("id");
		String feesItemCode = request.getParameter("feesItemCode");
		String feesItem = request.getParameter("feesItem");
		String fees = request.getParameter("fees");
		
		if( null != id && !"".equals(id)){
			//update
			isAdd =false;			
			//update FundFeesItemEn
			FundFeesItemEn objEn = new FundFeesItemEn();
			objEn = (FundFeesItemEn) fundFeesItemService.findById(id, "En");
			objEn.setFees(fees);
			objEn = (FundFeesItemEn) fundFeesItemService.saveOrUpdate(objEn, isAdd);	
			
		} else {
			//add
			isAdd =true;
			//insert FeesItem
			String fundId = request.getParameter("fundId");
			FundFeesItem objFeesItem = new FundFeesItem();
			FundInfo objFundInfo = fundInfoService.findFundInfoById(fundId);
			objFeesItem.setId(null);
			objFeesItem.setFund(objFundInfo);
			objFeesItem.setIsValid("1");
			objFeesItem.setCreateTime(new Date());
			objFeesItem.setLastUpdate(new Date());	
			
//			objFeesItem = fundFeesItemService.saveOrUpdate(objFeesItem);
			objFeesItem = (FundFeesItem) fundFeesItemService.saveOrUpdate(objFeesItem,true);
			
			if(null != objFeesItem.getId() && !"".equals(objFeesItem.getId())){
				//insert FundFeesItemEn
				id = objFeesItem.getId();
				FundFeesItemEn objEn = new FundFeesItemEn();
				objEn.setId(id);
				objEn.setFeesItemCode(feesItemCode);
				objEn.setFeesItem(feesItem);
				objEn.setFees(fees);
				fundFeesItemService.saveOrUpdate(objEn, isAdd);

				
				//insert FundFeesItemSc
				FundFeesItemSc objSc = new FundFeesItemSc();
				objSc.setId(id);
				objSc.setFeesItemCode(feesItemCode);
				
				SysParamConfig objConfig = sysParamService.findByCode(feesItemCode);
				if(null != objConfig){
					objSc.setFeesItem(objConfig.getNameSc());					
				}
				
				objSc.setFees(fees);
//				objSc = fundFeesItemService.saveOrUpdate4Sc(objSc, isAdd);
				fundFeesItemService.saveOrUpdate(objSc, isAdd);
				
				//insert FundFeesItemTc
				FundFeesItemTc objTc = new FundFeesItemTc();
				objTc.setId(id);
				objTc.setFeesItemCode(feesItemCode);
				if(null != objConfig){
					objTc.setFeesItem(objConfig.getNameTc());					
				}
				objTc.setFees(fees);
//				objTc = fundFeesItemService.saveOrUpdate4Tc(objTc, isAdd);	
				fundFeesItemService.saveOrUpdate(objTc, isAdd);
			}
			
		}	
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		String msg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null);
		obj.put("msg",msg);
		obj.put("feesItemId",id);
		
		JsonUtil.toWriter(obj, response);
	}
	
    
	@RequestMapping(value = "/saveSc", method = RequestMethod.POST)
	public void saveScObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {

		String actionMsg="global.success";
		boolean isAdd =false;
		String id = request.getParameter("id");
//		String feesItemCode = request.getParameter("feesItemCode");
//		String feesItem = request.getParameter("feesItem");
		String fees = request.getParameter("fees");
		
		if( null != id && !"".equals(id)){
			//update
			isAdd =false;
			
			//update FundFeesItemEn
			FundFeesItemSc obj = new FundFeesItemSc();
			obj = (FundFeesItemSc) fundFeesItemService.findById(id, "Sc");
			obj.setFees(fees);
			obj = (FundFeesItemSc) fundFeesItemService.saveOrUpdate(obj, isAdd);				
		} 
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
		JsonUtil.toWriter(obj, response);
	}
	
	
	   
	@RequestMapping(value = "/saveTc", method = RequestMethod.POST)
	public void saveTcObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {

		String actionMsg="global.success";
		boolean isAdd =false;
		String id = request.getParameter("id");
//		String feesItemCode = request.getParameter("feesItemCode");
//		String feesItem = request.getParameter("feesItem");
		String fees = request.getParameter("fees");
		
		if( null != id && !"".equals(id)){
			//update
			isAdd =false;

			//update FundFeesItemTc
			FundFeesItemTc obj = new FundFeesItemTc();
			obj = (FundFeesItemTc) fundFeesItemService.findById(id, "Tc");
			obj.setFees(fees);
			obj = (FundFeesItemTc) fundFeesItemService.saveOrUpdate(obj, isAdd);				
		} 
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
		JsonUtil.toWriter(obj, response);
	}
	
    
	/****
	 * 修改状态的方法（删除）
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public void updatestatus(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String ids = request.getParameter("ids");
		String status = request.getParameter("status");
		String actionMsg="global.success";
		fundFeesItemService.saveStatus(ids,status);
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
		JsonUtil.toWriter(obj, response);
	}
}
