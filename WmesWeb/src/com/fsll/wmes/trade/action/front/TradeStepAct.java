/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.action.front;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.common.util.WebServiceUtil;
import com.fsll.core.CoreConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderAip;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.ifafirm.vo.IfaMyTeamListVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.product.vo.ProductVO;
import com.fsll.wmes.trade.service.TradeCheckService;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.OrderPlanDetailVO;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;
import com.fsll.wmes.trade.vo.TransactionVO;
import com.fsll.wmes.web.vo.PushAppMsgVO;

/**
 * 交易:分步管理部分
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Controller
@RequestMapping("/front/tradeStep")
public class TradeStepAct extends WmesBaseAct{
	@Autowired
	private TradeStepService tradeStepService;
	@Autowired
	private TradeCheckService tradeCheckService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private SysParamService sysParamService;
    @Autowired
    private FundInfoService fundInfoService;
	
	/**
	 * 下单第一步:重置时,获得计划的原来产品
	 * @author wwluo
	 * @data 2016-10-09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/getInitProduct")
	public void getInitProduct(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		List<PlanProductVO> planProductVOs = null;
		Boolean flag = false;
		String planId = request.getParameter("id");
		String currencyCode = request.getParameter("currencyCode");
		planProductVOs = tradeStepService.getOrderPlanProducts(planId, currencyCode, langCode);
		if(planProductVOs != null && !planProductVOs.isEmpty()){
			flag = true;
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("planProductVOs", planProductVOs);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 保存交易计划购买产品信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@RequestMapping(value = "/saveOrderPlanSubscription")
	public void saveOrderPlanSubscription(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		OrderPlan orderPlan = null;
		Integer memberType = null;
		Map<String,Object> result = new HashMap<String, Object>();
		String planId = request.getParameter("planId");
		memberType = loginUser.getMemberType();
		String currencyCode = request.getParameter("currencyCode");
		orderPlan = (OrderPlan) this.coreBaseService.get(OrderPlan.class, planId);
		if(orderPlan != null && loginUser.getId().equals(orderPlan.getCreator().getId())){
			String subscriptionDatas = this.toUTF8String(request.getParameter("subscriptionDatas"));
			tradeStepService.updateOrderPlanProduct(planId,subscriptionDatas,currencyCode);
			flag = true;
		}
		result.put("flag", flag);
		result.put("memberType", memberType);
		result.put("planId", planId);
		JsonUtil.toWriter(result, response);
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
			OrderPlan plan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,planId);
			if(plan != null){
				plan.setAipFlag(state);
				plan.setLastUpdate(new Date());
				plan = (OrderPlan)this.coreBaseService.update(plan);
			}
		}
	}
	
	/**
	 * 保存定投信息
	 * @author wwluo
	 * @data 2016-09-20
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/saveAip")
	public void saveAip(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		String refresh = null;
		String planId = request.getParameter("planId");
		if (StringUtils.isBlank(planId) && CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
			refresh = "1";
		}
		flag = tradeStepService.saveAip(request, loginUser);
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("refresh", refresh);
		result.put("planId", planId);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 修改定投计划状态(执行中)
	 * @author wwluo
	 * @data 2017-02-17
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/updateOrderAip")
	public void updateOrderAip(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		String langCode = this.getLoginLangFlag(request);
		boolean flag = false;
		OrderAip orderAip = null;
		String aipState = null;
		String aipStateDisplay = null;
		String planId = request.getParameter("planId");
		aipState = request.getParameter("aipState");
		if (StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(aipState)) {
			orderAip = tradeStepService.getOrderAipByPlan(planId);
			// 2正常结束 3手工结束
			if(orderAip != null && !"2".equals(orderAip.getAipState()) && !"3".equals(orderAip.getAipState())){
				orderAip.setAipState(aipState);
				orderAip.setLastUpdate(new Date());
				orderAip = (OrderAip)this.coreBaseService.update(orderAip);
				aipState = orderAip.getAipState();
				aipStateDisplay = PropertyUtils.getPropertyValue(langCode, "order.plan.aip.detail.state."+orderAip.getAipState(), null);
				flag = true;
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("aipState", aipState);
		result.put("aipStateDisplay", aipStateDisplay);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取我的团队上司
	 * @author wwluo
	 * @data 2017-02-17
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/getMySupervisor")
	public void getMySupervisor(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request); 
		String langCode = this.getLoginLangFlag(request);
		boolean flag = false;
		List<IfaMyTeamListVO> myTeamListVOs = null;
		String creatorId = null;
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType()){
			String planId = request.getParameter("planId");
			if (StringUtils.isNotBlank(planId)) {
				OrderPlan plan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,planId);
				if(plan != null && plan.getCreator() != null && CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == plan.getCreator().getMemberType()){
					creatorId = plan.getCreator().getId();
					myTeamListVOs = tradeStepService.getMySupervisorByMemberId(plan.getCreator().getId(),langCode);
					if(myTeamListVOs!= null && !myTeamListVOs.isEmpty()){
						flag = true;
					}
				}
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("creatorId", creatorId);
		result.put("memberType", loginUser.getMemberType());
		result.put("myTeamListVOs", myTeamListVOs);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 交易计划详情页面（提交OMS前）
	 * @author wwluo
	 * @date 2017-02-15
	 */
	@RequestMapping(value = "/confirmOrderPlan")
	public String orderPlanConfirm(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String id = request.getParameter("id"); //OrderPlan id
		OrderPlan plan = null;
		if (StringUtils.isNotBlank(id)) {
			plan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,id);
			if(plan != null){
				String currencyCode = request.getParameter("currencyCode");
				String status = plan.getFinishStatus();
				if (!"4".equals(status) && !"5".equals(status)) {
				   // 交易计划详情页面（提交OMS前)
					OrderPlanDetailVO detailVO = tradeCheckService.getOrderPlanProducts(loginUser,plan,langCode,currencyCode);
					model.put("detail", detailVO);
					return "trade/step/orderPlanConfirm";
				}else{
				// 交易中&&交易完成
					model.put("id", id);
					return "redirect:" + this.getFullPath(request)+ "front/tradeMain/previewOrderPlan";
				}
			}else{
				return "redirect:" + this.getFullPath(request)+ "front/tradeMain/orderPlanSendRedirect.do";
			}
		}else{
			return "redirect:" + this.getFullPath(request)+ "front/tradeMain/orderPlanSendRedirect.do";
		}
	}
	
	/**
	 * 交易计划-风险披露页面 step 1
	 * @author wwluo
	 * @data 2017-05-11
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/orderPlanRiskDisclosure")
	public String orderPlanRiskDisclosure(String planId, String products, String currencyCode, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		model.put("planId", planId);
		if (StringUtils.isNotBlank(products)) {
			model.put("products", products);
		}
		model.put("currencyCode", currencyCode);
		return "trade/step/orderPlanRiskDisclosure";
	}
	
	/**
	 * 交易计划-产品选择页面 step 2
	 * @author wwluo
	 * @data 2017-05-11
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/orderPlanSelectProduct")
	public String orderPlanCommissionnew(String planId, String currencyCode, String products, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException {
		MemberBase loginUser = getLoginUser(request);
		String agreeRD = request.getParameter("agreeRD");
		if("1".equals(agreeRD)){
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = loginUser.getDefCurrency();
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = CommonConstants.DEF_CURRENCY;
				}
			}
			String langCode = this.getLoginLangFlag(request);
			TransactionVO vo = new TransactionVO();
			OrderPlan orderPlan = null;
			if (StringUtils.isNotBlank(planId)) {
				orderPlan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,planId);
			}
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType() && orderPlan == null){
				return "redirect:" + this.getFullPath(request)+ "front/tradeRecord/orderPlanList.do";
			}else if(orderPlan != null && Integer.valueOf(orderPlan.getFinishStatus()) > 0){
				model.put("planId", planId);
				model.put("currencyCode", currencyCode);
				return "redirect:" + this.getFullPath(request)+ "front/tradeMain/orderPlanSendRedirect.do";
			}
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
			// investor
				vo = tradeStepService.getTransactionVOForInvestor(currencyCode, langCode, loginUser, orderPlan);
			}else{
			// ifa
				vo = tradeStepService.getTransactionVOForIfa(currencyCode, langCode, loginUser, orderPlan);
			}
			model.put("detail", vo);
			if (StringUtils.isNotBlank(products)) {
				model.put("products", products);
			}
			return "trade/step/orderPlanSelectProduct";
		}else{
			// 跳转交易列表
			return "redirect:" + this.getFullPath(request)+ "front/tradeRecord/orderPlanList.do";
		}
	}

	/**
	 * 根据ids获取产品信息
	 * @author wwluo
	 * @data 2017-03-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/getProductVOByIds")
	public void getProductVOByIds(String ids, String currencyCode, HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		Boolean flag = false;
		List<PlanProductVO> vos = null;
		if (StringUtils.isNotBlank(ids)) {
			ids = toUTF8String(ids);
			vos = tradeStepService.getPlanProductVOByIds(ids, loginUser.getId(), currencyCode, this.getLoginLangFlag(request));
			flag = true;
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		result.put("planProducts", vos);
		result.put("memberType", getLoginUser(request).getMemberType());
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 保存订单
	 * @author wwluo
	 * @data 2017-03-21
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	@RequestMapping(value = "/saveOrderPlan")
	public void saveOrderPlan(String planId, String currencyCode, String portfolioName, String productData, HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		Boolean flag = false;
		String riskLevelStr = request.getParameter("riskLevel");
		Integer riskLevel = null;
		if (StringUtils.isNotBlank(riskLevelStr)) {
			riskLevel = Integer.valueOf(riskLevelStr);
		}
		planId = tradeStepService.saveOrderPlan(loginUser, planId, currencyCode, riskLevel, toUTF8String(portfolioName), toUTF8String(productData));
		if (StringUtils.isNotBlank(planId)) {
			flag = true;
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("flag", flag);
		result.put("memberType", loginUser.getMemberType());
		result.put("planId", planId);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 发送交易推送并插入审批数据
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@RequestMapping(value = "/sendOrderPlanCheck")
	public void sendOrderPlanCheck(String planId, HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		OrderPlan orderPlan = null;
		Integer memberType = null;
		Map<String,Object> result = new HashMap<String, Object>();
		String confirmation = request.getParameter("confirmation");
		if(loginUser != null){
			memberType = loginUser.getMemberType();
			orderPlan = (OrderPlan) this.coreBaseService.get(OrderPlan.class, planId);
			if(orderPlan != null && loginUser.getId().equals(orderPlan.getCreator().getId())){
				orderPlan.setFinishStatus("1");
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberType || (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType && "1".equals(confirmation))){
					// 发送消息提醒并插入审批数据
					String langCode = this.getLoginLangFlag(request);
					String supervisorId = null;
					Set<String> ifaIds = null;
					if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType()){
						supervisorId = request.getParameter("supervisor");
					}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
						ifaIds = tradeStepService.getOrderIfas(orderPlan.getId()); 
					}
					tradeStepService.saveSendToReadForOrderPlan(loginUser, planId, supervisorId, langCode, ifaIds);
				}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType){
					orderPlan.setFinishStatus("3");
				}
				orderPlan = (OrderPlan) this.coreBaseService.update(orderPlan);
				flag = true;
			}
		}
		result.put("flag", flag);
		result.put("memberType", memberType);
		result.put("planId", planId);
		result.put("authorized", orderPlan.getAuthorized());
		result.put("confirmation", confirmation);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 交易订单提交成功页面
	 * @author wwluo
	 * @data 2016-11-25
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/orderPlanComplete")
	public String rebalanceComplete(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		MemberBase loginUser = getLoginUser(request);
		String planId = request.getParameter("planId");
		OrderPlan orderPlan = null;
		String ifFirst = null;
		if (StringUtils.isNotBlank(planId)) {
			orderPlan = (OrderPlan) this.coreBaseService.get(OrderPlan.class,planId);
			ifFirst = orderPlan.getPortfolioHold().getIfFirst();
		}
		model.put("memberType", loginUser.getMemberType());
		model.put("orderPlan", orderPlan);
		model.put("ifFirst", ifFirst);
		return "trade/step/orderPlanComplete";
	}
	
}
