package com.fsll.wmes.fund.action.console;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.entity.SysAdmin;
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
        SysAdmin admin = this.getLoginUser(request);
        String fundId = request.getParameter("fundId");
        if(null != admin){
        	model.put("fundId", fundId);
        	return "console/fund/fees/list";//页面摆放路径
        }else{
        	return "redirect:"+this.getFullPath(request)+"viewLogin.do";
        }
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
        //List<String> ids = 
//    	String langCode = this.getLoginLangFlag(request);
//        FundFeesItemEn enVO = fundFeesItemService.findById4En(id);
//        FundFeesItemSc scVO = fundFeesItemService.findById4Sc(id);
//        FundFeesItemTc tcVO = fundFeesItemService.findById4Tc(id); 
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
	
    /**
     * 
     * @param request
     * @param response
     * @param model
    
	@RequestMapping(value = "/saveEn", method = RequestMethod.POST)
	public void saveEnObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String actionMsg="global.success";
		boolean isAdd =false;
		String id = request.getParameter("id");
		String fundId = request.getParameter("fundId");
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
			FundFeesItem objFeesItem = new FundFeesItem();
			FundInfo objFundInfo = fundInfoService.findFundInfoById(fundId);
			objFeesItem.setId(null);
			objFeesItem.setFund(objFundInfo);
			objFeesItem.setIsValid("1");
			objFeesItem.setCreateTime(new Date());
			objFeesItem.setLastUpdate(new Date());	
			objFeesItem = (FundFeesItem) fundFeesItemService.saveOrUpdate(objFeesItem,true);
			
			String objFeesItemId = objFeesItem.getId();
			if(null != objFeesItemId && !"".equals(objFeesItemId)){
				//insert FundFeesItemEn
				FundFeesItemEn objEn = new FundFeesItemEn();
				objEn.setId(objFeesItemId);
				objEn.setFeesItemCode(feesItemCode);
				objEn.setFeesItem(feesItem);
				objEn.setFees(fees);
				fundFeesItemService.saveOrUpdate(objEn, isAdd);
			
				//insert FundFeesItemSc
				FundFeesItemSc objSc = new FundFeesItemSc();
				objSc.setId(objFeesItemId);
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
	} */
	
    /**
     * 保存
     * Yan
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
	public void saveEnObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
    	String actionMsg="global.success";
		String errorInsertFundInfoMsg="fund.fees.item.error.insert.fundinfo";
		Map<String, Object> obj = new HashMap<String, Object>();
		
		boolean isAdd = false;
		boolean itemIsAdd = false;
		//when update then id=FundFeesItemId, when insert then id=fundId
		String id = request.getParameter("id");
		String fundId = request.getParameter("fundId");
		String feesItemCode = request.getParameter("feesItemCode");
		String feesItemEn = request.getParameter("feesItemEn");
		String feesItemSc = request.getParameter("feesItemSc");
		String feesItemTc = request.getParameter("feesItemTc");
		String feesEn = request.getParameter("feesEn");
		String feesSc = request.getParameter("feesSc");
		String feesTc = request.getParameter("feesTc");
		
		FundFeesItem item;
		FundFeesItemEn en;
		FundFeesItemSc sc;
		FundFeesItemTc tc;
		if(StringUtils.isNotBlank(id)){
			//exit FundFeesItemId
			isAdd = false;
			//find FundFeesItem
			Object objItem = fundFeesItemService.findItemById(id);
			if(objItem!=null){
				item = (FundFeesItem)objItem;
				if(StringUtils.isNotBlank(feesEn)){
					Object objEn = fundFeesItemService.findById(id, "En");
					//if exit FundFeesItemEn update , if not exit FundFeesItemEn insert
					if(objEn != null){
						itemIsAdd = false;
						en = (FundFeesItemEn) objEn;
					}else{
						itemIsAdd = true;
						//insert FundFeesItemEn
						en = new FundFeesItemEn();
						en.setId(id);
					}
					en.setFeesItem(feesItemEn);
					en.setFeesItemCode(feesItemCode);
					en.setFees(feesEn);
					en = (FundFeesItemEn) fundFeesItemService.saveOrUpdate(en, itemIsAdd);
				}
				if(StringUtils.isNotBlank(feesSc)){
					Object objSc = fundFeesItemService.findById(id, "Sc");
					//if exit FundFeesItemSc update , if not exit FundFeesItemSc insert
					if(objSc != null){
						itemIsAdd = false;
						sc = (FundFeesItemSc) objSc;
					}else{
						itemIsAdd = true;
						//insert FundFeesItemSc
						sc = new FundFeesItemSc();
						sc.setId(id);
					}
					sc.setFeesItemCode(feesItemCode);
					sc.setFees(feesSc);
					sc = (FundFeesItemSc) fundFeesItemService.saveOrUpdate(sc, itemIsAdd);
				}
				if(StringUtils.isNotBlank(feesTc)){
					Object objTc = fundFeesItemService.findById(id, "Tc");
					//if exit FundFeesItemTc update , if not exit FundFeesItemTc insert
					if(objTc != null){
						itemIsAdd = false;
						tc = (FundFeesItemTc) objTc;
					}else{
						itemIsAdd = true;
						//insert FundFeesItemTc
						tc = new FundFeesItemTc();
						tc.setId(id);
					}
					tc.setFeesItemCode(feesItemCode);
					tc.setFees(feesSc);
					tc = (FundFeesItemTc) fundFeesItemService.saveOrUpdate(tc, itemIsAdd);
				}
				obj.put("result",true);
				obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));				
			} else {
				isAdd=true;
				//find FundInfo
				FundInfo info = fundInfoService.findFundInfoById(fundId.trim());
				item = new FundFeesItem();
				if(info != null){
					//insert FundFeesItem
					item.setFund(info);
					item.setId(null);
					item.setIsValid("1");
					item.setCreateTime(new Date());
					item.setLastUpdate(new Date());
					item = (FundFeesItem)fundFeesItemService.saveOrUpdate(item, isAdd);
					//insert FundFeesItem
					en = new FundFeesItemEn();
					en.setId(item.getId());
					en.setFeesItemCode(feesItemCode);
					en.setFeesItem(feesItemEn);
					en.setFees(feesEn);
					en = (FundFeesItemEn) fundFeesItemService.saveOrUpdate(en, isAdd);
					sc = new FundFeesItemSc();
					sc.setId(item.getId());
					sc.setFeesItemCode(feesItemCode);
					sc.setFeesItem(feesItemSc);
					sc.setFees(feesSc);
					sc = (FundFeesItemSc) fundFeesItemService.saveOrUpdate(sc, isAdd);
					tc = new FundFeesItemTc();
					tc.setId(item.getId());
					tc.setFeesItemCode(feesItemCode);
					tc.setFeesItem(feesItemTc);
					tc.setFees(feesTc);
					tc = (FundFeesItemTc) fundFeesItemService.saveOrUpdate(tc, isAdd);
					obj.put("result",true);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
				} else{
					String errorMsg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),errorInsertFundInfoMsg,null);
					obj.put("errorMsg",errorMsg);
					obj.put("result",false);
				}
			}
			item.setLastUpdate(new Date());
			item = (FundFeesItem)fundFeesItemService.saveOrUpdate(item, isAdd);		
		}		
		JsonUtil.toWriter(obj, response);
    }
    
    
	@RequestMapping(value = "/saveSc", method = RequestMethod.POST)
	public void saveScObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String actionMsg="global.success";
		String errorInsertFundInfoMsg="fund.fees.item.error.insert.fundinfo";
		Map<String, Object> obj = new HashMap<String, Object>();
		
		boolean isAdd =false;
		//when update then id=FundFeesItemId, when insert then id=fundId
		String id = request.getParameter("id");
		String fundId = request.getParameter("fundId");
		String feesItemCode = request.getParameter("feesItemCode");
		String feesItem = request.getParameter("feesItem");
		String fees = request.getParameter("fees");
		
		if(null != id && !"".equals(id)){
			//exit FundFeesItemId
			isAdd =false;
			FundFeesItem item;
			FundFeesItemSc sc;
			//find FundFeesItem
			Object objItem = fundFeesItemService.findItemById(id);
			if(objItem!=null){
				item = (FundFeesItem)objItem;
				//find FundFeesItemTc
				Object objSc = fundFeesItemService.findById(id, "Sc");
				//if exit FundFeesItemTc update , if not exit FundFeesItemTc insert
				if(objSc != null){
					item.setLastUpdate(new Date());
					item = (FundFeesItem)fundFeesItemService.saveOrUpdate(item, isAdd);			
					sc = (FundFeesItemSc) objSc;
					sc.setFees(fees);
					sc = (FundFeesItemSc) fundFeesItemService.saveOrUpdate(sc, isAdd);
					obj.put("result",true);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
				}else{
					isAdd = true;
					//insert FundFeesItemTc
					sc = new FundFeesItemSc();
					sc.setId(id);
					sc.setFeesItem(feesItem);
					sc.setFeesItemCode(feesItemCode);
					sc.setFees(fees);
					sc = (FundFeesItemSc) fundFeesItemService.saveOrUpdate(sc, isAdd);
					obj.put("result",true);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
				}
			}else{
				isAdd=true;
				//find FundInfo
				FundInfo info = fundInfoService.findFundInfoById(fundId.trim());
				if(info != null){
					//insert FundFeesItem
					item = new FundFeesItem();
					item.setFund(info);
					item.setId(null);
					item.setIsValid("1");
					item.setCreateTime(new Date());
					item.setLastUpdate(new Date());
					item = (FundFeesItem)fundFeesItemService.saveOrUpdate(item, isAdd);
					//insert FundFeesItemTc
					sc = new FundFeesItemSc();
					sc.setId(item.getId());
					sc.setFeesItem(feesItem);
					sc.setFeesItemCode(feesItemCode);
					sc.setFees(fees);
					sc = (FundFeesItemSc) fundFeesItemService.saveOrUpdate(sc, isAdd);
					obj.put("result",true);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
				} else{
					String errorMsg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),errorInsertFundInfoMsg,null);
					obj.put("errorMsg",errorMsg);
					obj.put("result",false);
				}
			}
		}		
		JsonUtil.toWriter(obj, response);
	}
	
	
	   
	@RequestMapping(value = "/saveTc", method = RequestMethod.POST)
	public void saveTcObj(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String actionMsg="global.success";
		String errorInsertFundInfoMsg="fund.fees.item.error.insert.fundinfo";
		Map<String, Object> obj = new HashMap<String, Object>();
		
		boolean isAdd =false;
		//when update then id=FundFeesItemId, when insert then id=fundId
		String id = request.getParameter("id");
		String fundId = request.getParameter("fundId");
		String feesItemCode = request.getParameter("feesItemCode");
		String feesItem = request.getParameter("feesItem");
		String fees = request.getParameter("fees");
		
		if(null != id && !"".equals(id)){
			//exit FundFeesItemId
			isAdd =false;
			FundFeesItem item;
			FundFeesItemTc tc;
			//find FundFeesItem
			Object objItem = fundFeesItemService.findItemById(id);
			if(objItem!=null){
				item = (FundFeesItem)objItem;
				//find FundFeesItemTc
				Object objTc = fundFeesItemService.findById(id, "Tc");
				//if exit FundFeesItemTc update , if not exit FundFeesItemTc insert
				if(objTc != null){
					item.setLastUpdate(new Date());
					item = (FundFeesItem)fundFeesItemService.saveOrUpdate(item, isAdd);			
					tc = (FundFeesItemTc) objTc;
					tc.setFees(fees);
					tc = (FundFeesItemTc) fundFeesItemService.saveOrUpdate(tc, isAdd);
					obj.put("result",true);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
				}else{
					isAdd = true;
					//insert FundFeesItemTc
					tc = new FundFeesItemTc();
					tc.setId(id);
					tc.setFeesItem(feesItem);
					tc.setFeesItemCode(feesItemCode);
					tc.setFees(fees);
					tc = (FundFeesItemTc) fundFeesItemService.saveOrUpdate(tc, isAdd);
					obj.put("result",true);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
				}
			}else{
				isAdd=true;
				//find FundInfo
				FundInfo info = fundInfoService.findFundInfoById(fundId.trim());
				if(info != null){
					//insert FundFeesItem
					item = new FundFeesItem();
					item.setFund(info);
					item.setId(null);
					item.setIsValid("1");
					item.setCreateTime(new Date());
					item.setLastUpdate(new Date());
					item = (FundFeesItem)fundFeesItemService.saveOrUpdate(item, isAdd);
					//insert FundFeesItemTc
					tc = new FundFeesItemTc();
					tc.setId(item.getId());
					tc.setFeesItem(feesItem);
					tc.setFeesItemCode(feesItemCode);
					tc.setFees(fees);
					tc = (FundFeesItemTc) fundFeesItemService.saveOrUpdate(tc, isAdd);
					obj.put("result",true);
					obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
				} else{
					String errorMsg = PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),errorInsertFundInfoMsg,null);
					obj.put("errorMsg",errorMsg);
					obj.put("result",false);
				}
			}
		}		
		JsonUtil.toWriter(obj, response);
	}
	
	/****
	 * 删除的方法：FundFeesItemEn/FundFeesItemSc/FundFeesItemTc/FundFeesItem
	 * @param request 用户请求对象
	 * @param response 用户请求对象
	 * @param model 存储对象
	 * @return 返回添加用户页面
	 */
	@RequestMapping(value = "/deleteItem", method = RequestMethod.GET)
	public void deleteItem(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		String id = request.getParameter("ids");
		String actionMsg="global.success";
		Object objEn = fundFeesItemService.findById(id, "En");
		if(objEn != null){
			fundFeesItemService.deleteItem(objEn);
		}
		Object objSc = fundFeesItemService.findById(id, "Sc");
		if(objSc != null){
			fundFeesItemService.deleteItem(objSc);
		}
		Object objTc = fundFeesItemService.findById(id, "Tc");
		if(objTc != null){
			fundFeesItemService.deleteItem(objTc);
		}
		Object objItem = fundFeesItemService.findItemById(id);
		if(objItem != null){
			fundFeesItemService.deleteItem(objItem);
		}
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("result",true);
		obj.put("msg",PropertyUtils.getPropertyValue(this.getLoginLangFlag(request),actionMsg,null));
		JsonUtil.toWriter(obj, response);
	}
	
	
	/****
	 * 修改状态的方法（逻辑删除）
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
