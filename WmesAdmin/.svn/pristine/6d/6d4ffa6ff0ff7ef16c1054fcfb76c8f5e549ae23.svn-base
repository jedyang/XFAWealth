package com.fsll.wmes.bond.action.console;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.core.entity.SysAdmin;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.bond.service.BondInfoService;
import com.fsll.wmes.bond.vo.BondAskListVO;
import com.fsll.wmes.entity.BondAsk;

/**
 * 债券信息管理
 * @author Yan
 * @date 2016-12-09
 */
@Controller
@RequestMapping("/console/bond")
public class ConsoleBondInfoAct extends WmesBaseAct {

	@Autowired
	private BondInfoService bondInfoService;
	
	/**
	 * 债券报价列表
	 * @author mqzou 2017-06-28
	 */
	@RequestMapping(value="/bondAskList")
	public String bondAskList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
	   return "console/bond/bondask/list";
	}
	
	/**
	 * 获取债券报价列表（未报价）
	 * @author mqzou 2017-06-28
	 */
	@RequestMapping(value="/bondAskJson")
	public void findBondAskList(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyword=request.getParameter("keyword");
		String langCode=getLoginLangFlag(request);
		if(null!=keyword && !"".equals(keyword)){
			keyword=toUTF8String(keyword);
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging=bondInfoService.findBondAskList(jsonPaging, keyword, langCode);
		this.toJsonString(response, jsonPaging);
	}
	
	/**
	 * 债券报价form
	 * @author mqzou 2017-06-28
	 */
	@RequestMapping(value="/bondAsk")
	public String bondAsk(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String id=request.getParameter("id");
		String langCode=getLoginLangFlag(request);
		BondAskListVO vo=bondInfoService.findBondAskDetail(id, langCode);
		model.put("detail", vo);
	   return "console/bond/bondask/form";
	}
	
	/**
	 * 保存债券报价form
	 * @author mqzou 2017-06-28
	 */
	@RequestMapping(value="/saveBondAsk")
	public void saveBondAsk(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String, Object> result=new HashMap<String, Object>();
		String id=request.getParameter("id");
		String price=request.getParameter("price");
		String remark=request.getParameter("remark");
		SysAdmin admin=getLoginUser(request);
		if(null!=remark && !"".equals(remark)){
			remark=toUTF8String(remark);
		}
		BondAsk ask=bondInfoService.findById(id);
		if(null!=ask){
			ask.setAnswerDate(DateUtil.getCurDate());
			ask.setAnswerId(admin.getId());
			ask.setAnswerPrice(Double.parseDouble(price));
			ask.setOrderRemark(remark);
			ask.setOrderStatus("2");
			ask=bondInfoService.saveBondAsk(ask);
			if(null!=ask){
				result.put("result", true);
			}else {
				result.put("result", false);
			}
		}else {
			result.put("result", false);
		}
		JsonUtil.toWriter(result, response);
		
	}
	
	
	/**
	 * 债券报价历史列表
	 * @author mqzou 2017-06-28
	 */
	@RequestMapping(value="/bondAskHistory")
	public String bondAskHistory(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		
		return "console/bond/bondask/history";
	}
	
	/**
	 * 获取债券报价历史列表
	 * @author mqzou 2017-06-28
	 */
	@RequestMapping(value="/bondAskHistoryJson")
	public void findBondAskHistory(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String keyword=request.getParameter("keyword");
		String status=request.getParameter("status");
		String orderType=request.getParameter("orderType");
		String startDate=request.getParameter("startDate");
		String endDate=request.getParameter("endDate");
		String langCode=getLoginLangFlag(request);
		if(null!=endDate && !"".equals(endDate)){
			endDate=DateUtil.dateToDateString(DateUtil.addDate(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_FORMAT), Calendar.DATE, 1), DateUtil.DEFAULT_DATE_FORMAT); 
		}
		jsonPaging=getJsonPaging(request);
		jsonPaging=bondInfoService.findBondAskHistoryList(jsonPaging, keyword, status, orderType, langCode,startDate,endDate);
		this.toJsonString(response, jsonPaging);
	}
}
