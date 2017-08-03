package com.fsll.wmes.crm.action.console;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.base.CoreBaseAct;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.crm.service.InvestorClientManageService;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.member.vo.MemberSsoVO;


/*****
 * 开户账户管理
 * @author scshi_u330ps
 * @date 20161213
 */
@Controller
@RequestMapping("/console/investorClient")
public class ConsoleInvestorClientManageAct extends CoreBaseAct{
	
	@Autowired
	private InvestorClientManageService investorClientManageService;
	
	@Autowired
	private InvestorService investorService;
	@Autowired
	private SysParamService sysParamService;
	
	/**
	 * 账户页面
	 * @author scshi_u330p
	 * @date 20161223
	 * */
	@RequestMapping(value = "/clientPage", method = RequestMethod.GET)
	public String clientPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		this.isMobileDevice(request, model);
		MemberSsoVO sso = this.getLoginUserSSO(request);
		//未登录，跳转到登录页面
		if (null==sso){
			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
		}
		Integer subMemberType = sso.getSubMemberType();
		String langCode = this.getLoginLangFlag(request);
		List<GeneralDataVO>  itemList=findSysParameters("currency_type", langCode);
		model.put("currencyType", itemList);
		
		//设置查询条件
		String inUse=request.getParameter("in_use");//账户状态
		String inApproval=request.getParameter("inApproval");
		String cancellation=request.getParameter("cancellation");
		String currency=request.getParameter("cur");//基本货币
        String check=request.getParameter("check");
		
        //openStatus: -1 退回, 0 草稿, 1 等待投资人确认, 2 处理中 ,3 开户成功,4 开户失败
		String status="";
		if(null!=inUse && "1".equals(inUse)){
			status+="'3'";
		}
		if(null!=inApproval && "1".equals(inApproval)){
			status+="'-1','1','2','4'";
		}
		if(status.endsWith(",")){
			status=status.substring(0,status.length()-1);
		}
		
		String isValid="";
		if(null!=cancellation && "1".equals(cancellation)){
			isValid="0";
			
		}
		if(null==currency || "".equals(currency))
			currency=sso.getDefCurrency();
		if(null==currency || "".equals(currency))
			currency=CommonConstants.DEF_CURRENCY;
		
		if(31==subMemberType){//distributor登录
			return "console/crm/investorClient/distributorClient";
		}else if(22==subMemberType){//ifaFirm登录
			ClientSearchVO searchVo = new ClientSearchVO();
			searchVo.setIfaFirmId(sso.getIfafirmId());
//			searchVo.setDistributorId(sso.getDistributorId());
			searchVo.setOpenStatus(status);
			//account.setBaseCurrency(currency);//货币类型不作为查询条件
			searchVo.setIsValid(isValid);
			
			List distributorList=investorService.findDistributorByCriteria(searchVo);
			model.put("distributorList", distributorList);
			return "console/crm/investorClient/ifaFirmClient";
		}
		return "redirect:"+this.getFullPath(request)+"index.do";
	}
	
	
	/**
	 * 代理商查看开户账户列表json
	 * @date 20161223
	 * @author scshi_u330p
	 * */
	@RequestMapping(value="/distributorClientJson" )
	public void distributorClientJson(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException{
		Map<String, Object> result = new HashMap<String, Object>();
		
		String langCode=getLoginLangFlag(request);
		MemberSsoVO sso = this.getLoginUserSSO(request);
		
		//未登录，跳转到登录页面
		if (null==sso){
//			return "redirect:"+this.getFullPath(request)+"front/viewLogin.do";
			JsonUtil.toWriter(result, response);
		}
		
		String distributorId = sso.getDistributorId();
//		String ifafirmId = request.getParameter("ifafirmId");
		String inUse = request.getParameter("in_use");// 账户状态
		String cancellation = request.getParameter("cancellation");
		String currency = request.getParameter("cur");// 基本货币
		String period = request.getParameter("period");
		if(null==currency || "".equals(currency)) {
			currency=sso.getDefCurrency();
			currency = sysParamService.findNameByCode(currency, langCode);
		}
		
		if(null==currency || "".equals(currency)) {
			currency=CommonConstants.DEF_CURRENCY;
			currency = sysParamService.findNameByCode(currency, langCode);
		}
		
//		String status="";
		String isValid="";
		String condition="";
		
		String order=request.getParameter("order");
		String sort=request.getParameter("sort");
		String keyWord=request.getParameter("keyWord");
		String ifaName = request.getParameter("ifa");
		String status = request.getParameter("status");

		if(null!=inUse && "1".equals(inUse)){
			condition=" and  r.`open_status`='3' and r.`is_valid`='1'";
		}
		if(null!=cancellation && !"".equals(cancellation)){
			//isValid="0";
			condition=" and  r.`is_valid`='0'";
		}
		if(((null==inUse || "".equals(inUse))&& (null==cancellation || "".equals(cancellation)))|| (null!=inUse && "1".equals(inUse) && null!=cancellation && !"".equals(cancellation))){
			condition=" and ( r.`open_status`='3' or r.`is_valid`='0')";
		}
		
		if(!StrUtils.isEmpty(keyWord)){//client
			condition+=" and m.nick_name like '%"+keyWord+"%'";
		}
		if (!StrUtils.isEmpty(ifaName)){//ifa
			condition+=" AND r.`ifa_id` in (select id from member_ifa where first_name like '%"+ifaName+"%' or last_name like '%"+ifaName+"%' or name_chn like '%"+ifaName+"%')";
		}
		
//文档状态
		String docStauts = "";
		if(StringUtils.isNotBlank(status)){
			if("0".equals(status)){//需审批
				docStauts = " and d.`check_status`='0' ";
			}else if("1".equals(status)){//有效
				docStauts = " and d.`is_valid`='1' ";
			}else if("2".equals(status)){//失效
				docStauts = " and d.`is_valid`='0' ";
			}
		}
		
		AccountVO newVo = new AccountVO();
//		newVo.setIfafirmId(ifafirmId);
		newVo.setDocStauts(docStauts);
		newVo.setIfaName(ifaName);
		if (null != distributorId) {
			newVo.setDistributorId(distributorId);
		}
		if (null != period && !"".equals(period)) {
			String startDate = "";
			if ("7".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
			if ("14".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
			if ("30".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -30);
			if ("60".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -60);
			if ("90".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -90);
			newVo.setNextRPQDate(startDate);
		}
		
		newVo.setOpenStatus(condition);
		newVo.setIsValid(isValid);
	    newVo.setBaseCurrency(currency);
		jsonPaging = getJsonPaging(request);
		jsonPaging.setSort(sort);
		jsonPaging.setOrder(order);
		jsonPaging = investorClientManageService.findAccountList(jsonPaging, newVo,langCode);

		List<AccountVO> accountList = new ArrayList<AccountVO>();
		int approvalCount = 0;
		if (null!=jsonPaging.getList()){
			List it = jsonPaging.getList();
			if (!it.isEmpty()) 
			for (int i=0;i<it.size();i++){
				AccountVO vo=(AccountVO)it.get(i);
				if ("-1".equals(vo.getOpenStatus())) {//-1 退回
					approvalCount++;
				}
				
				if (!vo.getSubFlag().equals("1")){ //非从账户
					vo.setIfafirmIcon(PageHelper.getLogoUrl(vo.getIfafirmIcon(), "F"));
					vo.setDistributorIcon(PageHelper.getLogoUrl(vo.getDistributorIcon(), "D"));
					accountList.add(vo);
				}
			}
		}
		//合并各主账户中的从账户
		List<AccountVO> tmpList = new ArrayList<AccountVO>();
		List it = jsonPaging.getList();
		for (int i=0;i<accountList.size();i++){
			AccountVO vo=(AccountVO)accountList.get(i);
			tmpList = new ArrayList<AccountVO>();
			for (int j=0;j<it.size();j++){
				AccountVO child=(AccountVO)it.get(j);
				
				if (child.getSubFlag().equals("1") && child.getSubRelateId().equalsIgnoreCase(vo.getId())){
					child.setIfafirmIcon(PageHelper.getLogoUrl(child.getIfafirmIcon(), "F"));
					child.setDistributorIcon(PageHelper.getLogoUrl(child.getDistributorIcon(), "D"));
					tmpList.add(child);
				}
			}
			vo.setSubAccounts(tmpList);
		}
		
		result.put("accountList", accountList);
		result.put("total", jsonPaging.getTotal());
		result.put("currency", currency);
		result.put("approvalCount", approvalCount);
		JsonUtil.toWriter(result, response);
	}
	
	
	/**
	 * 获取未完成的账户列表
	 * @author mqzou
	 * @data 2017-01-05
	 * @return
	 */
	@RequestMapping(value = "/unCompleteAccountList", method = RequestMethod.POST)
	public void unCompleteAccountList(HttpServletRequest request,HttpServletResponse response,ModelMap mod){
		MemberSsoVO sso = this.getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=sso){
			String currency=request.getParameter("cur");//基本货币
			String distributorId=sso.getDistributorId();
			String period=request.getParameter("period");
			String keyWord=request.getParameter("keyWord");
			String ifafirmId = request.getParameter("ifafirmId");
			
			if(null==currency || "".equals(currency))
				currency=sso.getDefCurrency();
			if(null==currency || "".equals(currency))
				currency=CommonConstants.DEF_CURRENCY;
		
			String condition=" and  r.`open_status`in ('-1','-2') and r.`is_valid`='1'";
			if(null!=keyWord && !"".equals(keyWord)){
				condition+=" and m.nick_name like '%"+keyWord+"%'";
			}
			
			AccountVO newVo=new AccountVO();
			if(null!=distributorId && !"".equals(distributorId)){
				newVo.setDistributorId(distributorId);
			}
            if(null!=period && !"".equals(period)){
    			String startDate = "";//DateUtil.getCurDateStr();
    			if ("7".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 7);
    			else if ("14".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 14);
    			else if ("30".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 30);
    			else if ("60".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 60);
    			else if ("90".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 90);
    			newVo.setNextRPQDate(startDate);
			}
           
            newVo.setOpenStatus(condition);

            newVo.setBaseCurrency(currency);
			jsonPaging=getJsonPaging(request);
			jsonPaging.setSort("a.last_update");
			jsonPaging.setOrder("desc");
			jsonPaging=investorClientManageService.findAccountList(jsonPaging, newVo,langCode);
		
			
			
			result.put("accountList", jsonPaging.getList());
			result.put("total", jsonPaging.getTotal());
			result.put("currency", currency);
			
		}
		JsonUtil.toWriter(result, response);
	}
	
	
	/**
	 * ifa公司查看开户账户列表
	 * @author scshi_u330p
	 * @date 20161223
	 * */
	@RequestMapping(value = "/ifaFirmClientJson", method = RequestMethod.POST)
	public void ifaFirmClientJson(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberSsoVO sso = this.getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=sso){
			String distributorId = request.getParameter("distributorId");
			String ifafirmId = sso.getIfafirmId();
			String inUse = request.getParameter("in_use");// 账户状态
			String cancellation = request.getParameter("cancellation");
			String currency = request.getParameter("cur");// 基本货币
			String period = request.getParameter("period");
			String currencyName = "";//
			if(null==currency || "".equals(currency))
				currency=sso.getDefCurrency();
				currencyName = sysParamService.findNameByCode(currency, langCode);
			if(null==currency || "".equals(currency))
				currency=CommonConstants.DEF_CURRENCY;
				currencyName = sysParamService.findNameByCode(currency, langCode);
//			String status="";
			String isValid="";
			String condition="";
			
			String order=request.getParameter("order");
			String sort=request.getParameter("sort");
			String keyWord=request.getParameter("keyWord");
			String ifaName = request.getParameter("ifa");
			
			//都没选定或都选定
			if((StrUtils.isEmpty(inUse) && StrUtils.isEmpty(cancellation))
					|| ("1".equals(StrUtils.getString(inUse)) && "1".equals(StrUtils.getString(cancellation)))){
				condition=" and ( r.`open_status`='3' or r.`is_valid`='0')";
			}
			else if("1".equals(StrUtils.getString(inUse))){
				condition=" and  r.`open_status`='3' and r.`is_valid`='1'";
			}			
			else if("1".equals(StrUtils.getString(cancellation))){
				condition=" and  r.`is_valid`='0'";
			}
			
			if(null!=keyWord && !"".equals(keyWord)){
				condition+=" and m.nick_name like '%"+keyWord+"%'";
			}
			if (!StrUtils.isEmpty(ifaName)){//ifa
				condition+=" AND r.`ifa_id` in (select id from member_ifa where first_name like '%"+ifaName+"%' or last_name like '%"+ifaName+"%' or name_chn like '%"+ifaName+"%')";
			}
			
			AccountVO newVo = new AccountVO();
			newVo.setIfafirmId(ifafirmId);
			newVo.setIfaName(ifaName);
			if (null != distributorId) {
				newVo.setDistributorId(distributorId);
			}
			if (null != period && !"".equals(period)) {
				String startDate = "";
				if ("7".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -7);
				if ("14".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -14);
				if ("30".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -30);
				if ("60".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -60);
				if ("90".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, -90);
				newVo.setNextRPQDate(startDate);
			}
			
			newVo.setOpenStatus(condition);
			newVo.setIsValid(isValid);
		    newVo.setBaseCurrency(currency);
			jsonPaging = getJsonPaging(request);
			jsonPaging.setSort(sort);
			jsonPaging.setOrder(order);
			jsonPaging = investorClientManageService.findAccountList(jsonPaging, newVo,langCode);

			List<AccountVO> accountList = new ArrayList<AccountVO>();
			int approvalCount = 0;
			if (null!=jsonPaging.getList()){
				List it = jsonPaging.getList();
				if (!it.isEmpty()) 
				for (int i=0;i<it.size();i++){
					AccountVO vo=(AccountVO)it.get(i);
					if ("-1".equals(vo.getOpenStatus())) {//-1 退回
						approvalCount++;
					}
					
					if (!vo.getSubFlag().equals("1")){ //非从账户
						vo.setIfafirmIcon(PageHelper.getLogoUrl(vo.getIfafirmIcon(), "F"));
						vo.setDistributorIcon(PageHelper.getLogoUrl(vo.getDistributorIcon(), "D"));
						accountList.add(vo);
					}
				}
			}
			//合并各主账户中的从账户
			List<AccountVO> tmpList = new ArrayList<AccountVO>();
			List it = jsonPaging.getList();
			for (int i=0;i<accountList.size();i++){
				AccountVO vo=(AccountVO)accountList.get(i);
				tmpList = new ArrayList<AccountVO>();
				for (int j=0;j<it.size();j++){
					AccountVO child=(AccountVO)it.get(j);
					
					if (child.getSubFlag().equals("1") && child.getSubRelateId().equalsIgnoreCase(vo.getId())){
						child.setIfafirmIcon(PageHelper.getLogoUrl(child.getIfafirmIcon(), "F"));
						child.setDistributorIcon(PageHelper.getLogoUrl(child.getDistributorIcon(), "D"));
						tmpList.add(child);
					}
				}
				vo.setSubAccounts(tmpList);
			}			
			result.put("accountList", accountList);
			result.put("total", jsonPaging.getTotal());
			result.put("currency", currencyName);
			result.put("approvalCount", approvalCount);
//			JsonUtil.toWriter(result, response);
			
		}
		JsonUtil.toWriter(result, response);		
	}
	
	
	
	/**
	 * ifafirm未完成账户列表
	 * @author scshi_u330p
	 * @date 20170112
	 * **/
	@RequestMapping(value = "/ifaFirmUnComplateclientJson", method = RequestMethod.POST)
	public void firmUnComplateJson(HttpServletRequest request,HttpServletResponse response,ModelMap mod){
		MemberSsoVO sso = this.getLoginUserSSO(request);
		String langCode=getLoginLangFlag(request);
		Map<String, Object> result = new HashMap<String, Object>();
		if(null!=sso){
			String currency=request.getParameter("cur");//基本货币
			String distributorId=request.getParameter("distributorId");
			String period=request.getParameter("period");
			String keyWord=request.getParameter("keyWord");
			String ifafirmId = sso.getIfafirmId();
			
			if(null==currency || "".equals(currency))
				currency=sso.getDefCurrency();
			if(null==currency || "".equals(currency))
				currency=CommonConstants.DEF_CURRENCY;
		
			//-1 退回, 0 草稿, 1 等待投资人确认, 2 处理中
			String condition=" and  r.open_status in ('-1','1','2') and r.`is_valid`='1'";
			if(null!=keyWord && !"".equals(keyWord)){
				condition+=" and m.nick_name like '%"+keyWord+"%'";
			}
			
			AccountVO newVo=new AccountVO();
			if(null!=distributorId && !"".equals(distributorId)){
				newVo.setDistributorId(distributorId);
			}
			
            if(null!=period && !"".equals(period)){
    			String startDate = "";//DateUtil.getCurDateStr();
    			if ("7".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 7);
    			else if ("14".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 14);
    			else if ("30".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 30);
    			else if ("60".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 60);
    			else if ("90".equals(period)) startDate = DateUtil.getCurDateStr(Calendar.DATE, 90);
    			newVo.setNextRPQDate(startDate);
			}
            newVo.setIfafirmId(ifafirmId);
            newVo.setOpenStatus(condition);

            newVo.setBaseCurrency(currency);
			jsonPaging=getJsonPaging(request);
			jsonPaging.setSort("a.last_update");
			jsonPaging.setOrder("desc");
			jsonPaging=investorClientManageService.findAccountList(jsonPaging, newVo,langCode);
			
			result.put("accountList", jsonPaging.getList());
			result.put("total", jsonPaging.getTotal());
			result.put("currency", currency);
			
		}
		JsonUtil.toWriter(result, response);
	}
	
}
