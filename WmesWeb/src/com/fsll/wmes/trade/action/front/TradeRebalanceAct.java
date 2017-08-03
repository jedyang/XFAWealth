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
import java.util.HashSet;
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
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.base.WmesBaseAct;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.FundHouse;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.trade.service.TradeRebalanceService;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.OrderPlanProductVO;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.trade.vo.TransactionVO;

/**
 * 交易:持仓调整部分
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Controller
@RequestMapping("/front/tradeRebalance")
public class TradeRebalanceAct extends WmesBaseAct{
	@Autowired
	private TradeRebalanceService tradeRebalanceService;
	@Autowired
	private TradeStepService tradeStepService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private SysParamService sysParamService;
    @Autowired
    private FundInfoService fundInfoService;
	@Autowired
	private InvestorService investorService;
	
	/**
	 * 产品调整校验
	 * @author wwluo
	 * @data 2016-10-09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/rebalanceMixer")
	public String rebalanceMixer(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		
		return "trade/rebalance/rebalanceMixer";
	}
	
	/**
	 * 产品调整页面
	 * @author wwluo
	 * @data 2016-10-09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/rebalance")
	public String rebalance(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		String langCode = getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		String holdId = request.getParameter("id");
		String planId = request.getParameter("planId");
		String currencyCode = request.getParameter("currencyCode");
		OrderPlan orderPlan = null;
		PortfolioHold hold = null;
		if (StringUtils.isNotBlank(planId)) {
			orderPlan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,planId);
			if(orderPlan != null){
				hold = orderPlan.getPortfolioHold();
			}
		}else if(StringUtils.isNotBlank(holdId)){
			hold = (PortfolioHold) this.coreBaseService.get(PortfolioHold.class, holdId);
			if(hold != null){
				orderPlan = new OrderPlan();
				orderPlan.setBaseCurrency(hold.getBaseCurrency());
				orderPlan.setCreator(loginUser);
				orderPlan.setFinishStatus("0");
				orderPlan.setPortfolioHold(hold);
				orderPlan.setCreateTime(new Date());
				orderPlan.setLastUpdate(new Date());
				orderPlan.setIsValid("1");
				orderPlan = (OrderPlan) this.coreBaseService.create(orderPlan);
				planId = orderPlan.getId();
			}
		}
		if(orderPlan != null && (orderPlan.getCreator() != null && loginUser.getId().equals(orderPlan.getCreator().getId()))){
			hold = orderPlan.getPortfolioHold();
			TransactionVO vo = new TransactionVO();
			vo.setHoldId(hold.getId());
			vo.setPortfolioName(hold.getPortfolioName());
			vo.setMemberType(loginUser.getMemberType()); // '账户类型：1-投资人，2-IFA,3-代理商',
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = orderPlan.getBaseCurrency();
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = loginUser.getDefCurrency();
					if (StringUtils.isBlank(currencyCode)) {
						currencyCode = CommonConstants.DEF_CURRENCY;
					}
				}
			}
			List<InvestorAccountVO> investorAccounts = tradeStepService.findPortfolioHoldAccount(hold.getMember(), hold.getIfa(),langCode,currencyCode,hold.getId());
			vo.setInvestorAccountVOs(investorAccounts);
			MemberIfa ifa = hold.getIfa();
			vo.setIfaId(ifa.getId());
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
			if(ifa != null && member != null){
				customer = crmCustomerService.findByIfaAndMember(hold.getIfa().getId(), hold.getMember().getId());
				if(customer != null){
					clientIconUrl = customer.getIconUrl();
					vo.setNickName(customer.getNickName());
				}
				Double cashAvailable = tradeStepService.getCashAvailableTotal(member.getId(),ifa.getId(),currencyCode,hold.getId());
				vo.setCashAvailable(cashAvailable);
			}
			clientIconUrl = PageHelper.getUserHeadUrl(clientIconUrl, "");
			vo.setClientIconUrl(clientIconUrl);
			vo.setCurrencyCode(currencyCode);
			String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
			vo.setCurrencyName(currencyName);
			List<PlanProductVO> planProductVOs = tradeRebalanceService.getPortfolioHoldProducts(hold.getId(), currencyCode, langCode);
			if(planProductVOs == null || planProductVOs.isEmpty()){
			   //空仓
				model.put("currencyCode", currencyCode);
				model.put("planId", orderPlan.getId());
				return "redirect:" + this.getFullPath(request) + "front/tradeMain/orderPlan.do";
			}
			vo.setPlanProductVOs(planProductVOs);
			// 定投部分
			vo.setAipFlag(orderPlan.getAipFlag());
			OrderPlanAip planAip = tradeStepService.getOrderPlanAipByOrderId(orderPlan.getId());
			if(planAip != null){
				vo.setAipId(planAip.getId());
				Double exChangeRate = null;
				if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(orderPlan.getBaseCurrency())) {
					WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(orderPlan.getBaseCurrency(),currencyCode);
					if(webExchangeRate != null){
						exChangeRate = webExchangeRate.getRate();
					}
				}
				if(exChangeRate == null){
					exChangeRate = 1.00;
				}
				if(planAip.getAipAmount() != null){
					vo.setAipAmount(planAip.getAipAmount() * exChangeRate);
				}
				vo.setAipEndCount(planAip.getAipEndCount());
				vo.setAipEndDate(planAip.getAipEndDate());
				vo.setAipEndType(planAip.getAipEndType());
				vo.setAipExecCycle(planAip.getAipExecCycle());
				vo.setAipInitTime(planAip.getAipInitTime());
				vo.setAipTimeDistance(planAip.getAipTimeDistance());
			}else{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				try {
					vo.setAipInitTime(dateFormat.parse(DateUtil.getNextCycleTime(new Date(),"w",2)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			model.put("planId", planId);
			model.put("detail", vo);
			return "trade/rebalance/rebalanceMixer";
		}else{
		   // 查看交易跳转路由
			model.put("currencyCode", currencyCode);
			model.put("planId", planId); 
			model.put("holdId", holdId); // rebalance
			return "redirect:" + this.getFullPath(request) + "front/tradeMain/orderPlanSendRedirect.do";
		}
	}
	
	/**
	 * 获取 portfolio 产品持仓数据
	 * @author wwluo
	 * @data 2016-10-09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getPortfolioHoldProducts")
	public void getPortfolioHoldProducts(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = getLoginLangFlag(request);
		boolean flag = false;
		List<PlanProductVO> planProductVOs = null;
		String planId = request.getParameter("planId");
		if (StringUtils.isNotBlank(planId)) {
			OrderPlan orderPlan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,planId);
			if(orderPlan != null){
				String currencyCode = request.getParameter("currencyCode");
				if(StringUtils.isBlank(currencyCode)){
					currencyCode = orderPlan.getBaseCurrency();
					if(StringUtils.isBlank(currencyCode)){
						currencyCode = loginUser.getDefCurrency();
						if(StringUtils.isBlank(currencyCode)){
							currencyCode = CommonConstants.DEF_CURRENCY;
						}
					}
				}
				planProductVOs = tradeStepService.getOrderPlanProducts(orderPlan.getId(), currencyCode, langCode);
				flag = true;
			}
		}
		result.put("flag", flag);
		result.put("planProductVOs", planProductVOs);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取基金转出数据
	 * @author wwluo
	 * @data 2016-10-09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/getFundOut")
	public void getFundOut(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode = getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		boolean flag = false;
		try {
			FundInfoDataVO fundInfoDataVO = new FundInfoDataVO();
			String fundId = request.getParameter("fundId");
			if (StringUtils.isNotBlank(fundId)) {
				fundInfoDataVO = getFundDataVO(fundInfoDataVO,fundId);
				fundInfoDataVO = fundInfoService.setFundInfos(fundInfoDataVO,langCode,loginUser.getId(),null,null);
			}
			String holdId = request.getParameter("holdId");
			String productId = request.getParameter("productId");
			// 获取 portfolio各产品持仓
			PortfolioHoldProduct holdProduct = tradeRebalanceService.getPortfolioHoldProduct(holdId,productId);
			fundInfoDataVO.setPortfolioHoldProduct(holdProduct);
			result.put("fundInfoDataVO", fundInfoDataVO);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取可转换的基金数据
	 * @author wwluo
	 * @data 2016-10-12
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@RequestMapping(value = "/getFundIn")
	public void getFundIn(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode = getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		boolean flag = false;
		try {
			List<FundInfoDataVO> fundInfoDataVOs = new ArrayList<FundInfoDataVO>();
			FundInfoSc fundInfoSc = new FundInfoSc(); 
			String fundId = request.getParameter("fundId");
			String holdId = request.getParameter("holdId");
			String toCurrency = request.getParameter("toCurrency");
			if (StringUtils.isBlank(toCurrency)) {
				toCurrency = loginUser.getDefCurrency();
			}
			if (StringUtils.isNotBlank(fundId)) {
				FundInfo fundInfo = fundInfoService.findFundInfoById(fundId);
				FundInfoEn fundInfoEn = fundInfoService.findFundInfoEnById(fundId);
				FundHouse house = fundInfoEn.getFundHouseId();
				//获取 portfolio各产品持仓
				PortfolioHoldProduct holdProduct = tradeRebalanceService.getPortfolioHoldProduct(holdId,fundInfo.getProduct().getId());
				if(house != null){
					//获取fund数据
					List<GeneralDataVO> generalDataVOs= fundInfoService.findFundListByHouse(house.getId(), null, langCode);
					if(null != generalDataVOs && !generalDataVOs.isEmpty()){
						for (GeneralDataVO generalDataVO : generalDataVOs) {
							FundInfoDataVO fundInfoDataVO = new FundInfoDataVO();
							fundInfoDataVO.setPortfolioHoldProduct(holdProduct);
							String fund = generalDataVO.getFundId();
							fundInfoDataVO = getFundDataVO(fundInfoDataVO,fund);
							fundInfoSc = fundInfoService.findFundInfoScById(fund);
							fundInfoDataVO = fundInfoService.setFundInfos(fundInfoDataVO,langCode,loginUser.getId(),toCurrency,null);
							//基金所属公司ID
							if(null != fundInfoSc && null != fundInfoSc.getFundHouseId()){
								fundInfoDataVO.setFundHouseId(fundInfoSc.getFundHouseId().getId());
							}
							fundInfoDataVO.setDefaultInfoByLang(langCode);
							fundInfoDataVOs.add(fundInfoDataVO);
						}
						flag = true;
					}
				}
			}
			result.put("fundInfoDataVOs", fundInfoDataVOs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取订单基金数据
	 * @author wwluo
	 * @data 2016-10-13
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */							
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getOrderPlan")
	public void getOrderPlan(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		String langCode = getLoginLangFlag(request);
		MemberBase loginUser = getLoginUser(request);
		boolean flag = false;
		try {
			List<OrderPlanProductVO> orderPlanProductVOs = new ArrayList<OrderPlanProductVO>();
			String portfolioId = request.getParameter("portfolioId");
			String orderPlanData = this.getDecodeStr(request.getParameter("orderPlanData"));
			List<Map> orderPlans = JsonUtil.toListMap(orderPlanData);
			for (Map map : orderPlans) {
				FundInfoDataVO fundInfoDataVO = new FundInfoDataVO();
				OrderPlanProductVO orderPlanProductVO = new OrderPlanProductVO();
				String fundId = (String) map.get("fundId");
				if (StringUtils.isNotBlank(fundId)) {
					//获取基金多语言
					fundInfoDataVO = getFundDataVO(fundInfoDataVO,fundId);
					//设置基金信息vo中的回报及评级信息
					fundInfoDataVO = fundInfoService.setFundInfos(fundInfoDataVO,langCode,loginUser.getId(),null,null);
				}
				orderPlanProductVO.setFundInfoDataVO(fundInfoDataVO);
				Integer original = (Integer) map.get("original");
				orderPlanProductVO.setOriginal(original);
				String amount = (String) map.get("amount");
				if (StringUtils.isNotBlank(amount)) {
					orderPlanProductVO.setAmount(Double.parseDouble(amount));
				}
				Object amountAdjust = (Object) map.get("amountAdjust");
				if (amountAdjust != null) {
					orderPlanProductVO.setAmountAdjust(Double.parseDouble(amountAdjust.toString()));
				}
				Object unit = (Object) map.get("unit");
				if (unit != null) {
					orderPlanProductVO.setUnit(Double.parseDouble(unit.toString()));
				}
				Object unitAdjust = (Object) map.get("unitAdjust");
				if (unitAdjust != null) {
					orderPlanProductVO.setUnitAdjust(Double.parseDouble(unitAdjust.toString()));
				}
				String tranType = (String) map.get("tranType");
				orderPlanProductVO.setTranType(tranType);
				Object weight = (Object) map.get("weight");
				if (weight != null) {
					orderPlanProductVO.setWeight(Double.parseDouble(weight.toString()));
				}
				Object weightAdjust = (Object) map.get("weightAdjust");
				if (weight != null) {
					orderPlanProductVO.setWeightAdjust(Double.parseDouble(weightAdjust.toString()));
				}
				String switchFlag = (String) map.get("switchFlag");
				orderPlanProductVO.setSwitchFlag(switchFlag);
				String fromProductId = (String) map.get("fromProductId");
				if (StringUtils.isNotBlank(fromProductId)) {
					ProductInfo productInfo = new ProductInfo();
					productInfo.setId(fromProductId);
					orderPlanProductVO.setFromProduct(productInfo);
					orderPlanProductVO.setFromProductId(fromProductId);
				}
				orderPlanProductVO.setTranType(tranType);
				//获取 Account
				if (StringUtils.isNotBlank(portfolioId)) {
					List<PortfolioHoldProduct>  portfolioHoldProducts = tradeRebalanceService.getPortfolioHoldProducts(portfolioId,"fund");
					if(null != portfolioHoldProducts && !portfolioHoldProducts.isEmpty()){
						for (PortfolioHoldProduct portfolioHoldProduct : portfolioHoldProducts) {
							if(fundInfoDataVO.getFundInfo() !=null && fundInfoDataVO.getFundInfo().getProduct().equals(portfolioHoldProduct.getProduct())){
								orderPlanProductVO.setInverstorAccount(portfolioHoldProduct.getAccount());
							}
						}
					}
					if(original == 0){//新增基金时
						PortfolioInfo portfolioInfo = (PortfolioInfo)this.coreBaseService.get(PortfolioInfo.class,portfolioId);
						//管理该组合的IFA
						MemberIfa memberIfa = portfolioInfo.getMemberIfa();
						//组合所属的客户
						MemberBase customer = portfolioInfo.getMember();
						if(memberIfa!=null && customer!=null){
							List<InvestorAccount> investorAccounts = investorService.findInvestorAccounts(memberIfa.getId(),customer.getId());
							result.put("InvestorAccounts", investorAccounts);
						}
					}
				}
				orderPlanProductVOs.add(orderPlanProductVO);
			}
			result.put("orderPlanProductVOs", orderPlanProductVOs);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("flag", flag);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 封装基金多语言
	 * @author wwluo
	 * @data 2016-10-12
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */	
	private FundInfoDataVO getFundDataVO(FundInfoDataVO fundInfoDataVO,String fundId) {
		FundInfo fundInfo = fundInfoService.findFundInfoById(fundId);
		//获取fund多语言数据
		FundInfoSc fundInfoSc = fundInfoService.findFundInfoScById(fundId);
		FundInfoTc fundInfoTc = fundInfoService.findFundInfoTcById(fundId);
		FundInfoEn fundInfoEn = fundInfoService.findFundInfoEnById(fundId);
		//设置基金信息vo中的回报及评级信息
		fundInfoDataVO.setFundId(fundId);
		fundInfoDataVO.setFundInfo(fundInfo);
		fundInfoDataVO.setFundInfoSc(fundInfoSc);
		fundInfoDataVO.setFundInfoTc(fundInfoTc);
		fundInfoDataVO.setFundInfoEn(fundInfoEn);
		return fundInfoDataVO;
	}
	
	/**
	 * rebalance 订单生成页面
	 * @author wwluo
	 * @data 2016-11-25
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException 
	 */							
	@RequestMapping(value = "/rebalanceCommission")
	public String rebalanceCommission(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		String planId = request.getParameter("planId");
		if (StringUtils.isNotBlank(planId)) {
			OrderPlan plan = (OrderPlan)this.coreBaseService.get(OrderPlan.class,planId);
			if(plan != null && loginUser.getId().equals(plan.getCreator().getId()) && "0".equals(plan.getFinishStatus())){ // 草稿状态
				TransactionVO vo = new TransactionVO();
				String currencyCode = request.getParameter("currencyCode");
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = plan.getBaseCurrency();
					if (StringUtils.isBlank(currencyCode)) {
						currencyCode = CommonConstants.DEF_CURRENCY;
					}
				}
				String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE, currencyCode, langCode);
				vo.setCurrencyCode(currencyCode);
				vo.setCurrencyName(currencyName);
				PortfolioHold hold = plan.getPortfolioHold();
				vo.setPortfolioName(hold.getPortfolioName());
				vo.setMemberType(loginUser.getMemberType());
				List<InvestorAccountVO> investorAccounts = tradeStepService.findPortfolioHoldAccount(hold.getMember(), hold.getIfa(),langCode,currencyCode,hold.getId());
				vo.setInvestorAccountVOs(investorAccounts);
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType()){
					CrmCustomer customer = null;
					String clientIconUrl = null;
					if(hold.getMember() != null){
						vo.setEmail(hold.getMember().getEmail());
						String mobileNumber = null;
						if(hold.getMember().getMobileCode() != null){
							mobileNumber = hold.getMember().getMobileCode();
							if(hold.getMember().getMobileNumber() != null){
								mobileNumber =  mobileNumber + " " + hold.getMember().getMobileNumber();
							}
						}else if(hold.getMember().getMobileNumber() != null){
							mobileNumber = hold.getMember().getMobileNumber();
						}
						vo.setMobileNumber(mobileNumber);
						if(hold.getIfa() != null){
							customer = crmCustomerService.findByIfaAndMember(hold.getIfa().getId(), hold.getMember().getId());
							if(customer != null){
								clientIconUrl = customer.getIconUrl();
								vo.setCustomerId(customer.getId());
								vo.setNickName(customer.getNickName());
							}
							Double cashAvailable = tradeStepService.getCashAvailableTotal(hold.getMember().getId(),hold.getIfa().getId(),currencyCode,hold.getId());
							vo.setCashAvailable(cashAvailable);
						}
					}
					clientIconUrl = PageHelper.getUserHeadUrl(clientIconUrl, "");
					vo.setClientIconUrl(clientIconUrl);
				}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
					vo.setClientIconUrl(PageHelper.getUserHeadUrl(loginUser.getIconUrl(), ""));
					vo.setNickName(loginUser.getNickName());
					vo.setEmail(loginUser.getEmail());
					String mobileNumber = null;
					if(loginUser.getMobileCode() != null){
						mobileNumber = loginUser.getMobileCode();
						if(loginUser.getMobileNumber() != null){
							mobileNumber =  mobileNumber + " " + loginUser.getMobileNumber();
						}
					}else if(loginUser.getMobileNumber() != null){
						mobileNumber = loginUser.getMobileNumber();
					}
					vo.setMobileNumber(mobileNumber);
					Double cashAvailable = tradeStepService.getCashAvailableTotal(loginUser.getId(),null,currencyCode,hold.getId());
					vo.setCashAvailable(cashAvailable);
				}
				List<PlanProductVO> planProductVOs = tradeStepService.getOrderPlanProducts(plan.getId(), currencyCode, langCode);
				vo.setPlanProductVOs(planProductVOs);
				// 定投部分
				vo.setAipFlag(plan.getAipFlag());
				OrderPlanAip planAip = tradeStepService.getOrderPlanAipByOrderId(planId);
				if(planAip != null){
					vo.setAipId(planAip.getId());
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
						vo.setAipAmount(planAip.getAipAmount() * exChangeRate);
					}
					if(planAip.getAipEndTotalAmount() != null){
						vo.setAipEndTotalAmount(planAip.getAipEndTotalAmount() * exChangeRate);
					}
					vo.setAipEndCount(planAip.getAipEndCount());
					vo.setAipEndDate(planAip.getAipEndDate());
					vo.setAipEndType(planAip.getAipEndType());
					vo.setAipExecCycle(planAip.getAipExecCycle());
					vo.setAipInitTime(planAip.getAipInitTime());
					vo.setAipTimeDistance(planAip.getAipTimeDistance());
				}else{
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					try {
						vo.setAipInitTime(dateFormat.parse(DateUtil.getNextCycleTime(new Date(),"w",2)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				model.put("detail", vo);
				return "trade/rebalance/rebalanceCommission";
			}else{
			// 查看交易详情
				model.put("id", planId);
				return "redirect:" + this.getFullPath(request)+ "front/tradeMain/previewOrderPlan.do";
			}
		}else{
		// OderPlan为空或者OderPlan创建人非当前登录账号
			return "redirect:" + this.getFullPath(request)+ "front/tradeRecord/orderPlanList.do";
		}
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
	@RequestMapping(value = "/rebalanceComplete")
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
		return "trade/rebalance/rebalanceComplete";
	}
	
	/**
	 * 获取投资组合客户账户信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@RequestMapping(value = "/getPortfolioAccountData")
	public void getPortfolioAccountData(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		String langCode = this.getLoginLangFlag(request);
		List<InvestorAccountVO> investorAccounts = new ArrayList<InvestorAccountVO>();
		boolean flag = false;
		if(loginUser != null){
			String portfolioId = request.getParameter("portfolioId");
			String toCurrency = request.getParameter("toCurrency");
			//基金信息
			if (StringUtils.isBlank(toCurrency)) {
				toCurrency = loginUser.getDefCurrency();
			}
			PortfolioInfo portfolioInfo = (PortfolioInfo)this.coreBaseService.get(PortfolioInfo.class,portfolioId);
			if(portfolioInfo != null){
				investorAccounts = tradeStepService.findPortfolioHoldAccount(portfolioInfo.getMember(), portfolioInfo.getMemberIfa(),langCode,toCurrency,null);
				flag = true;
			}
		}
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("flag", flag);
		result.put("investorAccounts", investorAccounts);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 校验持仓产品
	 * @author wwluo
	 * @data 2016-10-09
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/valiHoldProduct")
	public void valiHoldProduct(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean flag = false;
		String holdId = request.getParameter("id");
		PortfolioHold hold = null;
		String ifHold = "0";  // 0:空仓，1：有持仓
		String ifPlan = "0";  // 0:所有交易订单已提交，1：尚有未提交订单
		String ifFirst = "1";  // 0:投资过，1：未投资过。（区分草稿状态的订单来源）
		String planId = null;
		String status = null;
		if (StringUtils.isNotBlank(holdId)) {
			hold = (PortfolioHold)this.coreBaseService.get(PortfolioHold.class,holdId);
		}
		if(hold != null){
			List<PortfolioHoldProduct> holdProducts = tradeStepService.getPortfolioHoldProducts(hold.getId());
			if(holdProducts != null && !holdProducts.isEmpty()){
			// 拥有持仓
				ifHold = "1";
				List<OrderPlan> orderPalns = tradeRebalanceService.getOrderPlansByHold(hold.getId(),null);
				if(orderPalns != null && !orderPalns.isEmpty()){
					for (OrderPlan orderPlan : orderPalns) {
						if("1".equals(orderPlan.getFinishStatus()) 
								|| "3".equals(orderPlan.getFinishStatus())){
						// 拥有未完成的订单(尚未提交)	
							ifPlan = "1";
							planId = orderPlan.getId();
							status = orderPlan.getFinishStatus();
							ifFirst = hold.getIfFirst();
							break;
						}
					}
				}
			}
			flag = true;
		}
		result.put("flag", flag);
		result.put("ifHold", ifHold);
		result.put("ifPlan", ifPlan);
		result.put("planId", planId);
		result.put("status", status);
		result.put("ifFirst", ifFirst);
		JsonUtil.toWriter(result, response);
	}
	
	
	/**
	 * rebalance 保存交易订单
	 * @author wwluo
	 * @data 2016-11-25
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException 
	 */							
	@RequestMapping(value = "/saveOrderPlanData")
	public void saveOrderPlanData(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws ParseException {
		Map<String,Object> result = new HashMap<String, Object>();
		MemberBase loginUser = this.getLoginUser(request);
		String currencyCode = request.getParameter("currencyCode");
		String planId = request.getParameter("planId");
		String holdId = request.getParameter("holdId");
		String orderPlanProducts = this.getDecodeStr(request.getParameter("orderPlanData"));
		String portfolioName = request.getParameter("portfolioName");
		planId = tradeStepService.saveOrderPlan(loginUser, planId, holdId, currencyCode,portfolioName,orderPlanProducts);
		boolean flag = false;
		if(planId != null && !"".equals(planId)){
			flag = true;
		}
		result.put("flag", flag);
		result.put("planId", planId);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 保存交易计划股权选项信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@RequestMapping(value = "/saveOrderPlanCommission")
	public void saveOrderPlanCommission(HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		MemberBase loginUser = this.getLoginUser(request);
		boolean flag = false;
		OrderPlan orderPlan = null;
		Integer memberType = null;
		Map<String,Object> result = new HashMap<String, Object>();
		String planId = request.getParameter("planId");
		String confirmation = request.getParameter("confirmation");
		String authorized = request.getParameter("authorized");
		if (StringUtils.isNotBlank(planId)) {
			memberType = loginUser.getMemberType();
			String currencyCode = request.getParameter("currencyCode");
			orderPlan = (OrderPlan)this.coreBaseService.get(OrderPlan.class, planId);
			if(orderPlan != null && loginUser.getId().equals(orderPlan.getCreator().getId())){
				String subscriptionDatas = this.toUTF8String(request.getParameter("subscriptionDatas"));
				tradeStepService.updateOrderPlanProduct(planId, subscriptionDatas,currencyCode);
				orderPlan.setFinishStatus("1");
				if (StringUtils.isNotBlank(authorized)) {
					orderPlan.setAuthorized(authorized); // 授权状态
				}
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberType || (CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType && "1".equals(confirmation))){
					//发送消息提醒并插入审批数据
					String langCode = this.getLoginLangFlag(request);
					String supervisorId = null;
					Set<String> ifaIds = null;
					if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType()){
						supervisorId = request.getParameter("supervisor");//如果是ifa
					}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
						ifaIds = tradeStepService.getOrderIfas(orderPlan.getId());//如果是investor
					}
					tradeStepService.saveSendToReadForOrderPlan(loginUser, planId, supervisorId, langCode, ifaIds);
				}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType){
					orderPlan.setFinishStatus("3");
				}
				orderPlan = (OrderPlan)this.coreBaseService.update(orderPlan);
				// 交易金额汇总
				orderPlan = tradeStepService.saveTotalAmountSummary(orderPlan.getId());
				flag = true;
			}
		}
		result.put("flag", flag);
		result.put("memberType", memberType);
		result.put("planId", planId);
		result.put("authorized", authorized);
		result.put("confirmation", confirmation);
		JsonUtil.toWriter(result, response);
	}
	
	/**
	 * 获取持仓产品信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@RequestMapping(value = "/getHoldProductInfo")
	public void getHoldProductInfo(String holdId, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Map<String, Object> result = tradeRebalanceService.getHoldProductInfo(holdId);
		JsonUtil.toWriter(result, response);
	}
}
