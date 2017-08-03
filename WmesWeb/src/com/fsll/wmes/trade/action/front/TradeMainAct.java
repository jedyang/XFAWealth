/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.action.front;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.yaml.snakeyaml.util.UriEncoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.OrderAip;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.trade.service.TradeOrderService;
import com.fsll.wmes.trade.service.TradeRecordService;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.AipOrderHistoryVO;
import com.fsll.wmes.trade.vo.OrderPlanDetailVO;
import com.fsll.wmes.trade.vo.PlanAipVO;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.trade.vo.TransactionVO;

/**
 * 交易:主页面及负责跳转的页面
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Controller
@RequestMapping("/front/tradeMain")
public class TradeMainAct extends WmesBaseAct{
	@Autowired
	private TradeStepService tradeStepService;
	@Autowired
	private TradeOrderService tradeOrderService;
	@Autowired
	private TradeRecordService tradeRecordService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
    private FundInfoService fundInfoService;
	
	/**
	 * 交易计划按状态重定向
	 * @author wwluo
	 * @data 2016-10-09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@SuppressWarnings("unused")
	@RequestMapping(value = "/orderPlanSendRedirect")
	public String orderPlanSendRedirect(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String planId = request.getParameter("planId");
		String currencyCode = request.getParameter("currencyCode");
		String ifEdit = request.getParameter("ifEdit");
		if(StringUtils.isNotBlank(planId)){
			OrderPlan plan = (OrderPlan) this.coreBaseService.get(OrderPlan.class,planId);
			PortfolioHold hold = plan.getPortfolioHold();
			if(plan != null){
				// 4交易中  5交易完成
				if(("4".equals(plan.getFinishStatus()) || "5".equals(plan.getFinishStatus())) && loginUser.getId().equals(plan.getCreator().getId())){ 
					model.put("id", planId);
					model.put("currencyCode", currencyCode);
					// 跳转详情页面
					return "redirect:" + this.getFullPath(request)+ "front/tradeMain/previewOrderPlan.do";
				}else if("1".equals(plan.getFinishStatus()) ||"2".equals(plan.getFinishStatus()) ||"3".equals(plan.getFinishStatus())){ // 审批阶段
					// 跳转确认页面
					model.put("id", planId);
					model.put("currencyCode", currencyCode);
					return "redirect:" + this.getFullPath(request)+ "front/tradeStep/confirmOrderPlan.do";
				}else if(hold != null && "0".equals(hold.getIfFirst()) && loginUser.getId().equals(plan.getCreator().getId())){
					List<PortfolioHoldProduct> holdProducts = tradeStepService.getPortfolioHoldProducts(hold.getId());
					if(holdProducts != null && !holdProducts.isEmpty()){
						//rebalance 页面
						model.put("planId", planId);
						model.put("id", hold.getId());
						model.put("currencyCode", currencyCode);
						return "redirect:" + this.getFullPath(request)+ "front/tradeRebalance/rebalance.do";
					}else if(loginUser.getId().equals(plan.getCreator().getId())){
						// 跳购买页面
						model.put("planId", planId);
						model.put("currencyCode", currencyCode);
						model.put("ifEdit", ifEdit);
						return "redirect:" + this.getFullPath(request)+ "front/tradeMain/orderPlan.do";
					}else{
						// 跳转列表页面
						return "redirect:" + this.getFullPath(request)+ "front/tradeRecord/orderPlanList.do";
					}
				}else if(loginUser.getId().equals(plan.getCreator().getId())){
					// 跳购买页面
					model.put("planId", planId);
					model.put("currencyCode", currencyCode);
					model.put("ifEdit", ifEdit);
					return "redirect:" + this.getFullPath(request)+ "front/tradeMain/orderPlan.do";
				}else{
					// 跳转列表页面
					return "redirect:" + this.getFullPath(request)+ "front/tradeRecord/orderPlanList.do";
				}
			}else if(hold != null){
				model.put("id", planId);
				model.put("currencyCode", currencyCode);
				return "redirect:" + this.getFullPath(request)+ "front/tradeStep/confirmOrderPlan.do";
			}else{
				// 跳转列表页面
				return "redirect:" + this.getFullPath(request)+ "front/tradeRecord/orderPlanList.do";
			}
		}else{
			// 跳转列表页面
			return "redirect:" + this.getFullPath(request)+ "front/tradeRecord/orderPlanList.do";
		}
	}
	
	/**
	 * 交易计划详情页面（提交OMS后）
	 * @author wwluo
	 * @date 2017-02-15
	 */
	@RequestMapping(value = "/previewOrderPlan")
	public String orderPlanDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String id = request.getParameter("id"); //OrderPlan id
		String currencyCode = request.getParameter("currencyCode");
		OrderPlan plan = null;
		if (StringUtils.isNotBlank(id)) {
			plan = (OrderPlan) this.coreBaseService.get(OrderPlan.class,id);
			if(plan != null){
				String status = plan.getFinishStatus();
				if ("4".equals(status) || "5".equals(status)) { //-1：初始创建 0：草稿，1：审批中,2审批不通过，3审批通过，4：交易中；5：交易完成。
				// 交易中&&交易完成
					model = this.getOrderPlanDetailVO(loginUser, plan,langCode,currencyCode,model);
					return "trade/main/orderPlanDetail";
				}else{
				// 交易计划详情页面（提交OMS前)
					model.put("id", id);
					model.put("currencyCode", currencyCode);
					return "redirect:" + this.getFullPath(request)+ "front/tradeStep/confirmOrderPlan.do";
				}
			}else{
				return "redirect:" + this.getFullPath(request)+ "front/tradeMain/orderPlanSendRedirect.do";
			}
		}else{
			return "redirect:" + this.getFullPath(request)+ "front/tradeMain/orderPlanSendRedirect.do";
		}
	}
	
	/**
	 * 获取交易计划详情（提交OMS后）
	 * @author wwluo
	 * @date 2017-02-15
	 */
	private ModelMap getOrderPlanDetailVO(MemberBase loginUser, OrderPlan plan, String langCode, String currencyCode, ModelMap model) {
		if(plan != null){
			OrderPlanDetailVO detailVO = new OrderPlanDetailVO();
			PortfolioHold hold = plan.getPortfolioHold();
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = plan.getBaseCurrency();
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = hold.getBaseCurrency();
				}
			}
			detailVO.setPlanId(plan.getId());
			detailVO.setHoldId(hold.getId());
			detailVO.setPortfolioName(hold.getPortfolioName());
			detailVO.setSubmitDate(plan.getSubmitDate());
			String status = plan.getFinishStatus();
			detailVO.setStatus(status);
			if("4".equals(status)){   //-1：初始创建 0：草稿，1：审批中,2审批不通过，3审批通过，4：交易中；5：交易完成。
				detailVO.setStatusDisplay(this.getProperty(langCode, "order.plan.history.status.display.processing"));
			}else if("5".equals(status)){
				detailVO.setStatusDisplay(this.getProperty(langCode, "order.plan.history.status.display.completed"));
			}
			detailVO.setMemberId(plan.getCreator().getId());
			detailVO.setCurrencyCode(currencyCode);
			detailVO.setCurrencyName(sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE, currencyCode, langCode));
			String iconUrl = null;
			String name = null;
			Integer memberType = loginUser.getMemberType();
			String email = null;
			String mobileNumber = null;
			if(memberType != null){
				detailVO.setMemberType(memberType.toString());
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberType){
					iconUrl = hold.getMember().getIconUrl();
					name = getCommonMemberName(hold.getMember().getId(), langCode, "2");
					email = hold.getMember().getEmail();
					if (StringUtils.isNotBlank(hold.getMember().getMobileCode())) {
						mobileNumber = hold.getMember().getMobileCode();
						if (StringUtils.isNotBlank(hold.getMember().getMobileNumber())) {
							mobileNumber = mobileNumber + " " + hold.getMember().getMobileNumber();
						}
					}
				}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType){
					MemberIfafirm ifafirm = ifafirmManageService.findIfafirmByIfa(hold.getIfa(), langCode);
					detailVO.setIfaFirmId(ifafirm.getId());
					detailVO.setIfaFirmName(ifafirm.getCompanyName());
					if(hold.getIfa() != null){
						iconUrl = hold.getIfa().getMember().getIconUrl();
						name = getCommonMemberName(hold.getIfa().getMember().getId(), langCode, "2");
						email = hold.getIfa().getMember().getEmail();
						if(hold.getIfa().getNationality() != null){
							detailVO.setLocation(this.getCountryString(hold.getIfa().getNationality(), langCode));
						}
						if (StringUtils.isNotBlank(hold.getIfa().getMember().getMobileCode())) {
							mobileNumber = hold.getIfa().getMember().getMobileCode();
							if (StringUtils.isNotBlank(hold.getIfa().getMember().getMobileNumber())) {
								mobileNumber = mobileNumber + " " + hold.getMember().getMobileNumber();
							}
						}
					}
				}
			}
			detailVO.setIconUrl(PageHelper.getUserHeadUrl(iconUrl, ""));
			detailVO.setNickName(name);
			detailVO.setMobileNumber(mobileNumber);
			detailVO.setEmail(email);
			if(hold.getIfa() != null){
				detailVO.setIfaId(hold.getIfa().getId());
			}
			// 封装OrderHistory数据(当交易完成时，Amount、Unit、TranFee另外处理,取OrderReturn返回数据)
			List<PlanProductVO> planProductVOs = tradeOrderService.getHistoryProducts(plan.getId(),currencyCode,langCode);
			detailVO.setPlanProductVOs(planProductVOs);
			List<InvestorAccountVO> investorAccounts = tradeStepService.findPortfolioHoldAccount(hold.getMember(), hold.getIfa(), langCode, currencyCode, hold.getId());
			detailVO.setInvestorAccountVOs(investorAccounts);
			// 定投部分
			detailVO.setAipFlag(plan.getAipFlag());
			OrderPlanAip planAip = tradeStepService.getOrderPlanAipByOrderId(plan.getId());
			if(planAip != null){
				detailVO.setAipId(planAip.getId());
				Double exChangeRate = null;
				if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(plan.getBaseCurrency())) {
					WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(plan.getBaseCurrency(),currencyCode);
					if(webExchangeRate != null){
						exChangeRate = webExchangeRate.getRate();
					}
				}
				if(exChangeRate == null){
					exChangeRate = 1.00;
				}
				if(planAip.getAipAmount() != null){
					detailVO.setAipAmount(planAip.getAipAmount() * exChangeRate);
				}
				detailVO.setAipEndCount(planAip.getAipEndCount());
				detailVO.setAipEndDate(planAip.getAipEndDate());
				detailVO.setAipEndType(planAip.getAipEndType());
				detailVO.setAipExecCycle(planAip.getAipExecCycle());
				detailVO.setAipInitTime(planAip.getAipInitTime());
				detailVO.setAipTimeDistance(planAip.getAipTimeDistance());
			}else{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					detailVO.setAipInitTime(dateFormat.parse(DateUtil.getNextCycleTime(new Date(),"w",2)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			model.put("detail", detailVO);
		}
		return model;
	}
	
	/**
	 * 交易判断
	 * @author wwluo
	 * @data 2017-05-11
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/orderPlan")
	public String orderPlan(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String planId = request.getParameter("planId");
		String currencyCode = request.getParameter("currencyCode");
		MemberBase loginUser = getLoginUser(request);
		OrderPlan orderPlan = null;
		if (StringUtils.isNotBlank(planId)) {
			orderPlan = (OrderPlan) this.coreBaseService.get(OrderPlan.class,planId);
		}
		if(orderPlan != null && orderPlan.getCreator() != null 
				&& loginUser.getId().equals(orderPlan.getCreator().getId()) 
				&& ("-1".equals(orderPlan.getFinishStatus()) 
						|| "0".equals(orderPlan.getFinishStatus())
						|| "2".equals(orderPlan.getFinishStatus()))){ //-1：初始创建 0：草稿，1：审批中,2审批不通过，3审批通过，4：交易中；5：交易完成。
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = orderPlan.getBaseCurrency();
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = CommonConstants.DEF_CURRENCY;
				}
			}
			PortfolioHold hold = orderPlan.getPortfolioHold();
			List<PortfolioHoldProduct> holdProducts = tradeStepService.getPortfolioHoldProducts(hold.getId());
			// 是否拥有持仓
			if(holdProducts != null && !holdProducts.isEmpty()){
				model.put("currencyCode", currencyCode);
				model.put("planId", planId); 
				model.put("id", hold.getId()); // rebalance
				return "redirect:" + this.getFullPath(request) + "front/tradeRebalance/rebalance.do";
			}
			model.put("currencyCode", currencyCode);
			model.put("planId", planId);
			return "redirect:" + this.getFullPath(request) + "front/tradeStep/orderPlanRiskDisclosure.do";
		}else{
			model.put("currencyCode", currencyCode);
			model.put("planId", planId);
			return "redirect:" + this.getFullPath(request) + "front/tradeMain/orderPlanSendRedirect.do";
		}
	}
	
	/**
	 * 接收基金信息创建组合
	 * @author wwluo
	 * @data 2017-02-17
	 * @param products 基金ids
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/createPorfolioByProducts")
	public String createPorfolioByProducts(String holdId, String products, HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		PortfolioHold hold = null;
		OrderPlan plan = null;
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
			if (StringUtils.isNotBlank(products)) {
				if (StringUtils.isNotBlank(holdId)) { 
				// add
					hold = (PortfolioHold) coreBaseService.get(PortfolioHold.class, holdId);
					if(hold != null){
						plan = new OrderPlan();
						plan.setCreator(loginUser);
						plan.setBaseCurrency(hold.getBaseCurrency());
						plan.setCreateTime(new Date());
						plan.setFinishStatus("0");
						plan.setLastUpdate(new Date());
						plan.setPortfolioHold(hold);
						plan = (OrderPlan) coreBaseService.create(plan);
					}
				}
				if(hold != null && plan != null){
					tradeStepService.addPlanProduct(plan.getId(), products);
					model.put("planId", plan.getId());
					return "redirect:" + this.getFullPath(request)
					+ "front/tradeStep/orderPlanRiskDisclosure.do";
				}else{
					model.put("products", UriEncoder.encode(products));
					return "redirect:" + this.getFullPath(request)
					+ "front/tradeStep/orderPlanRiskDisclosure.do";
				}
			}else{
				return "redirect:" + this.getFullPath(request)
				+ "/front/fund/info/list.do";
			}
		}else{
			return "redirect:" + this.getFullPath(request)
			+ "/front/fund/info/list.do";
		}
	}
	
	/**
	 * 定投详情页面
	 * @author wwluo
	 * @data 2017-02-22
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/aipDetail")
	public String aipDetail(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		MemberBase loginUser = this.getLoginUser(request);
		String holdId = request.getParameter("holdId");
		String planId = request.getParameter("planId");
		String currencyCode = request.getParameter("currencyCode");
		PlanAipVO vo = new PlanAipVO();
		PortfolioHold hold = null;
		OrderPlan plan = null;
		if (StringUtils.isNotBlank(holdId)) {
			 hold = (PortfolioHold) coreBaseService.get(PortfolioHold.class, holdId);
		}else if (StringUtils.isNotBlank(planId)) {
			plan = (OrderPlan) coreBaseService.get(OrderPlan.class, planId);
			if(plan != null){
				hold = plan.getPortfolioHold();
				// 封装OrderHistory数据(当交易完成时，Amount、Unit、TranFee另外处理,取OrderReturn返回数据)
				List<PlanProductVO> planProductVOs = tradeOrderService.getHistoryProducts(plan.getId(), currencyCode, langCode);
				vo.setPlanProductVOs(planProductVOs);
			}
		}
		if(hold != null){
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = hold.getBaseCurrency();
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = loginUser.getDefCurrency();
				}
			}
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType() 
					&& loginUser.getId().equals(hold.getIfa().getMember().getId())){
				MemberBase member = hold.getMember();
				if(member != null){
					vo.setEmail(member.getEmail());
					String mobileNumber = null;
					if(member.getMobileCode() != null){
						mobileNumber = member.getMobileCode();
						if(member.getMobileNumber() != null){
							mobileNumber =  mobileNumber + " " + member.getMobileNumber();
						}
					}else if(member.getMobileNumber() != null){
						mobileNumber = member.getMobileNumber();
					}
					vo.setMobileNumber(mobileNumber);
				}
				CrmCustomer customer = new CrmCustomer();
				String clientIconUrl = null;
				if(hold.getIfa() != null && member != null){
					customer = crmCustomerService.findByIfaAndMember(hold.getIfa().getId(), hold.getMember().getId());
					if(customer != null){
						clientIconUrl = customer.getIconUrl();
						vo.setNickName(customer.getNickName());
					}
				}
				clientIconUrl = PageHelper.getUserHeadUrl(clientIconUrl, "");
				vo.setIconUrl(clientIconUrl);
			}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()
					&& (loginUser.getId().equals(hold.getMember().getId()) || loginUser.getId().equals(hold.getMember().getId()))){
				MemberBase member = hold.getMember();
				if(member != null){
					vo.setEmail(member.getEmail());
					String mobileNumber = null;
					if(member.getMobileCode() != null){
						mobileNumber = member.getMobileCode();
						if(member.getMobileNumber() != null){
							mobileNumber =  mobileNumber + " " + member.getMobileNumber();
						}
					}else if(member.getMobileNumber() != null){
						mobileNumber = member.getMobileNumber();
					}
					vo.setMobileNumber(mobileNumber);
				}
				CrmCustomer customer = new CrmCustomer();
				String clientIconUrl = null;
				if(hold.getIfa() != null && member != null){
					customer = crmCustomerService.findByIfaAndMember(hold.getIfa().getId(), hold.getMember().getId());
					if(customer != null){
						clientIconUrl = customer.getIconUrl();
						vo.setNickName(customer.getNickName());
					}
				}
				clientIconUrl = PageHelper.getUserHeadUrl(clientIconUrl, "");
				vo.setIconUrl(clientIconUrl);
			}
			vo.setCurrencyCode(currencyCode);
			String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
			vo.setCurrencyName(currencyName);
			vo.setPortfolioName(hold.getPortfolioName());
			OrderAip orderAip = tradeStepService.getOrderAipByPlan(plan.getId());
			if(orderAip != null){
				vo.setAipState(orderAip.getAipState());
				String aipStateDisplay = PropertyUtils.getPropertyValue(langCode, "order.plan.aip.detail.state."+orderAip.getAipState(), null);
				vo.setAipStateDisplay(aipStateDisplay);
				vo.setCreateTime(orderAip.getCreateTime()); // 定投计划创建时间
				vo.setAipExecCycle(orderAip.getAipExecCycle());
				String execCycle = PropertyUtils.getPropertyValue(langCode, "order.plan.rebalance.AIP.execCycle."+orderAip.getAipExecCycle(), null);
				vo.setAipExecCycleDisplay(execCycle);
				vo.setAipTimeDistance(orderAip.getAipTimeDistance());
				vo.setAipNextTime(orderAip.getAipNextTime());
				
				Double exChangeRate = null;
				if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(plan.getBaseCurrency())) {
					WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(plan.getBaseCurrency(),currencyCode);
					if(webExchangeRate != null){
						exChangeRate = webExchangeRate.getRate();
					}
				}
				if(exChangeRate == null){
					exChangeRate = 1.00;
				}
				if(orderAip.getAipAmount() != null){
					vo.setAipAmount(orderAip.getAipAmount() * exChangeRate);
				}
				if(orderAip.getAipTotalAmount() != null){
					vo.setAipTotalAmount(orderAip.getAipTotalAmount() * exChangeRate);
				}
				if(orderAip.getAipEndTotalAmount() != null){
					vo.setAipEndTotalAmount(orderAip.getAipEndTotalAmount() * exChangeRate);
				}
				vo.setAipCount(orderAip.getAipCount());
				vo.setAipEndCount(orderAip.getAipEndCount());
				vo.setAipEndDate(orderAip.getAipEndDate());
				String aipEndType = orderAip.getAipEndType();
				List<Object> params = new ArrayList<Object>();
				if("1".equals(aipEndType)){
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					if(orderAip.getAipEndDate() != null){
						params.add(dateFormat.format(orderAip.getAipEndDate()));
					}
				}else if("2".equals(aipEndType)){
					params.add(orderAip.getAipEndCount());
				}else if("3".equals(aipEndType)){
					params.add(vo.getAipEndTotalAmount());
					params.add(currencyName);
				}
				String aipEndDateDisplay = PropertyUtils.getPropertyValue(langCode, "order.plan.aip.detail.expiration." + aipEndType, params.toArray());
				vo.setAipEndTypeDisplay(aipEndDateDisplay);
				vo.setAipEndType(orderAip.getAipEndType());
			}
			List<AipOrderHistoryVO> aipOrderHistories = tradeRecordService.getAipOrderHistory(hold.getId(), currencyCode, langCode);
			vo.setAipOrderHistories(aipOrderHistories);
		}
		model.put("detail", vo);
		return "trade/main/aipDetail";
	}
	
	/**
	 * 修改定投状态
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateAipState")
	public void updateAipState(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String planId = request.getParameter("planId");
		String state = request.getParameter("state");
		if (StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(state)) {
			OrderPlan plan = (OrderPlan) coreBaseService.get(OrderPlan.class, planId);
			if(plan != null){
				plan.setAipFlag(state);
				plan.setLastUpdate(new Date());
				plan = (OrderPlan) coreBaseService.update(plan);
			}
		}
	}
	
}
