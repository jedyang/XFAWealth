/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.OrderPlanCheck;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.trade.service.TradeCheckService;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.OrderPlanCheckVO;
import com.fsll.wmes.trade.vo.OrderPlanDetailVO;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.fsll.wmes.web.service.WebTaskListService;

/**
 * 交易:审批信息业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Service("tradeCheckService")
public class TradeCheckServiceImpl  extends BaseService implements TradeCheckService{
	@Autowired
	private TradeStepService tradeStepService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private WebTaskListService webTaskListService;
	@Autowired
	private WebReadToDoService webReadToDoService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
    private FundInfoService fundInfoService;
	
	/**
	 * 交易操作投资人确认操作（同意、退回）
	 * @param loginUser
	 * @param planId
	 * @param finishStatus
	 * @return
	 */
	public boolean saveConfirmOrderPlan(MemberBase loginUser, String planId, String finishStatus, String langCode) {
		boolean flag = false;
		if (StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(finishStatus)) {
			OrderPlan orderPlan = (OrderPlan)this.baseDao.get(OrderPlan.class,planId);
			if(orderPlan != null && CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == orderPlan.getCreator().getMemberType() && "1".equals(orderPlan.getFinishStatus())){
				 //审批中
				orderPlan.setFinishStatus(finishStatus);
				orderPlan.setLastUpdate(new Date());
				orderPlan = (OrderPlan)this.baseDao.update(orderPlan);
				PortfolioHold hold = orderPlan.getPortfolioHold();
				WebReadToDo webReadToDo = new WebReadToDo();
			 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_EXCHANGE);
			 	webReadToDo.setRelateId(orderPlan.getId());
			 	webReadToDo.setFromMember(loginUser);
			 	String key = null;
			 	List<Object> params = new ArrayList<Object>();
		 		params.add(getCommonMemberName(hold.getMember().getId(), langCode, "2"));	
			 	params.add(hold.getPortfolioName());
			 	String titleSc = null;
			 	String titleTc = null;
			 	String titleEn = null;
			 	if("2".equals(finishStatus)){ // 审批不通过
			 		webReadToDo.setMsgUrl("/front/tradeMain/previewOrderPlan.do");
			 		webReadToDo.setMsgParam("id="+orderPlan.getId());
			 		webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioConfirmActivity");
			 		webReadToDo.setAppParam("orderPlanId="+orderPlan.getId());
				 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_REJECT_INVESTOR);
				 	key = "order.reject";
				 	// 插入待办
				 	WebTaskList taskList = new WebTaskList();
				 	taskList.setCreateDate(new Date());
				 	taskList.setFromMember(loginUser);
				 	taskList.setHandleStatus("0");
				 	taskList.setIsValid("1");
				 	taskList.setModuleType(CommonConstantsWeb.WEB_TASK_MODULE_ORDER_PLAN);
				 	taskList.setRelateId(orderPlan.getId());
				 	taskList.setOwner(hold.getIfa().getMember());
				 	taskList.setTaskUrl("/front/tradeMain/previewOrderPlan.do");
				 	taskList.setTaskParam("id="+orderPlan.getId());
				 	titleSc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC,params.toArray());
				 	titleTc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC,params.toArray());
				 	titleEn = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN,params.toArray());
				 	taskList.setTitleSc(titleSc);
				 	taskList.setTitleTc(titleTc);
				 	taskList.setTitleEn(titleEn);
				 	taskList.setType(null);
				 	webTaskListService.saveTaskList(taskList);
			 	}else if("4".equals(finishStatus)){ 
			 		webReadToDo.setMsgUrl("/front/tradeMain/previewOrderPlan.do");
			 		webReadToDo.setMsgParam("id="+orderPlan.getId());
			 		webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioConfirmActivity");
			 		webReadToDo.setAppParam("orderPlanId="+orderPlan.getId());
				 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_CONFIRM_INVESTOR);
				 	key = "order.confirmed";
				 	hold.setIfFirst("0");
					hold.setLastUpdate(new Date());
					hold = (PortfolioHold)this.baseDao.update(hold);
			 	}
			 	webReadToDo.setIsApp("1");
			 	webReadToDo.setIsRead("0");
			 	webReadToDo.setOwner(hold.getIfa().getMember());
			 	webReadToDo.setCreateTime(new Date());
			 	webReadToDo.setIsValid("1");
			 	titleSc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC,params.toArray());
			 	titleTc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC,params.toArray());
			 	titleEn = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN,params.toArray());
			 	webReadToDo.setTitle(titleEn);
			 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 获取交易计划详情（提交OMS前）
	 * @param loginUser
	 * @param plan
	 * @param langCode
	 * @param currencyCode
	 * @return
	 */
	public OrderPlanDetailVO getOrderPlanProducts(MemberBase loginUser,OrderPlan plan, String langCode, String currencyCode){
		String dataFormat = loginUser.getDateFormat();
		if (StringUtils.isBlank(dataFormat)) {
			dataFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		OrderPlanDetailVO detailVO = new OrderPlanDetailVO();
		if(plan != null){
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
			detailVO.setCreateDate(plan.getCreateTime());
			detailVO.setLastUpdate(plan.getLastUpdate());
			if(plan.getLastUpdate() != null){
				detailVO.setLastUpdateStr(DateUtil.dateToDateString(plan.getLastUpdate(), dataFormat));
			}
			String status = plan.getFinishStatus();
			detailVO.setStatus(status);  //-1：初始创建 0：草稿，1：审批中,2审批不通过，3审批通过，4：交易中；5：交易完成。
			String statusDisplay = PropertyUtils.getPropertyValue(langCode, "order.plan.list.finish.status."+status, null);
			detailVO.setStatusDisplay(statusDisplay);
			detailVO.setMemberId(plan.getCreator().getId());
			detailVO.setCurrencyCode(currencyCode);
			detailVO.setCurrencyName(sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE, currencyCode, langCode));
			String iconUrl = plan.getCreator().getIconUrl();
			String email = null;
			String mobileNumber = null;
			detailVO.setMemberType(loginUser.getMemberType().toString());
			if(hold.getMember() != null){
				email = hold.getMember().getEmail();
				if (StringUtils.isNotBlank(hold.getMember().getMobileCode())) {
					mobileNumber = hold.getMember().getMobileCode();
					if (StringUtils.isNotBlank(hold.getMember().getMobileNumber())) {
						mobileNumber = mobileNumber + " " + hold.getMember().getMobileNumber();
					}
				}
				if(hold.getIfa() != null){
					detailVO.setNickName(getCommonMemberName(hold.getMember().getId(), langCode, "2"));
					iconUrl = hold.getMember().getIconUrl();
					detailVO.setIfaNickName(getCommonMemberName(hold.getIfa().getMember().getId(), langCode, "2"));
				}else{
					detailVO.setNickName(getCommonMemberName(hold.getMember().getId(), langCode, "2"));
				}
			}
			detailVO.setIconUrl(PageHelper.getUserHeadUrl(iconUrl, ""));
			detailVO.setMobileNumber(mobileNumber);
			detailVO.setEmail(email);
			detailVO.setCreatorId(plan.getCreator().getId());
			String creatorName = null;
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == plan.getCreator().getMemberType()){
				creatorName = getCommonMemberName(plan.getCreator().getId(), langCode, "2");
			}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == plan.getCreator().getMemberType()){
				creatorName = getCommonMemberName(hold.getIfa().getMember().getId(), langCode, "2");
			}else{
				creatorName = plan.getCreator().getNickName();
				if (StringUtils.isBlank(creatorName)) {
					creatorName = plan.getCreator().getLoginCode();
				}
			}
			detailVO.setCreatorName(creatorName);
			// 封装OrderPlanProduct数据
			List<PlanProductVO> planProductVOs = getPlanProducts(plan.getId(),currencyCode,langCode);
			detailVO.setPlanProductVOs(planProductVOs);
			// 封装账户信息
			List<InvestorAccountVO> investorAccounts = tradeStepService.findPortfolioHoldAccount(hold.getMember(), hold.getIfa(), langCode, currencyCode, hold.getId());
			detailVO.setInvestorAccountVOs(investorAccounts);
			// 审批信息
			List<OrderPlanCheckVO> checkVOs = getOrderPlanCheck(plan.getId(), langCode, dataFormat);
			detailVO.setCheckVOs(checkVOs);
			// 审批历史查看权限
			String approvalRecordView = "1";
			// 提交订单权限
			String planSubmitView = "0";
			if(loginUser.getId().equals(plan.getCreator().getId())){
			// investor
				if("3".equals(plan.getFinishStatus())){ // 审批通过
					planSubmitView = "1";
				}
			}else if(loginUser.getId().equals(plan.getCreator().getId()) 
					&& "3".equals(plan.getFinishStatus())){
			// IFA
				planSubmitView = "1";
			}
			detailVO.setPlanSubmitView(planSubmitView);
			List<OrderPlanCheck> checks = getApprovalForPlan(loginUser.getId(),plan.getId(),"0");
			if(checks != null && !checks.isEmpty()){
		    // 审批权限
				detailVO.setApprovalView("1");
			}else{
				detailVO.setApprovalView("0");
			}
			/*if(loginUser.getId().equals(plan.getCreator().getId())){
				// 审批历史查看权限
				approvalRecordView = "1";
			}*/
			detailVO.setApprovalRecordView(approvalRecordView);
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
				detailVO.setAipInitTime(DateUtil.StringToDate(DateUtil.getNextCycleTime(new Date(),"w",2), CommonConstants.FORMAT_DATE));
			}
		}
		return detailVO;
	}
	
	/**
	 * 获取交易详情产品(提交OMS前)
	 * @author wwluo
	 * @date 2017-02-15
	 */
	private List<PlanProductVO> getPlanProducts(String planId, String currencyCode,String langCode) {
		List<PlanProductVO> vos = null;
		if(StringUtils.isNotBlank(planId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" OrderPlanProduct h" +
					" WHERE" +
					" h.plan.id=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			List<OrderPlanProduct> orderPlanProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
				vos = new ArrayList<PlanProductVO>();
				Double exChangeRate = null;
				for (OrderPlanProduct orderPlanProduct : orderPlanProducts) {
					PlanProductVO vo = new PlanProductVO();
					vo.setPlanProductId(orderPlanProduct.getId());
					String fromCurrency = orderPlanProduct.getTranFeeCur();
					if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(orderPlanProduct.getTranFeeCur())) {
						exChangeRate = this.getExchangeRate(fromCurrency,currencyCode);
					}
					if(exChangeRate == null){
						exChangeRate = 1.00;
						currencyCode = fromCurrency;
					}
					String currencyName = this.getParamConfigName(langCode,currencyCode,CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
					vo.setCurrencyCode(currencyCode);
					vo.setCurrencyName(currencyName);
					if(orderPlanProduct.getAmount() != null){
						vo.setAmount(orderPlanProduct.getAmount()*exChangeRate);
					}
					vo.setTranType(orderPlanProduct.getTranType());
					vo.setUnit(orderPlanProduct.getUnit());
					vo.setTranRate(orderPlanProduct.getTranRate());
					if(orderPlanProduct.getTranFee() != null){
						vo.setTranFee(orderPlanProduct.getTranFee()*exChangeRate);
					}
					MemberDistributor distributor = null;
					if(orderPlanProduct.getAccount() != null){
						distributor = orderPlanProduct.getAccount().getDistributor();
						vo.setAccountId(orderPlanProduct.getAccount().getId());
						vo.setRpqRiskLevel(orderPlanProduct.getAccount().getRpqLevel());
					}
					if(distributor != null){
						vo.setDistributorId(distributor.getId());
						vo.setDistributorName(distributor.getCompanyName());
						vo.setDistributorLogoUrl(PageHelper.getLogoUrl(distributor.getLogofile(), "D"));
					}else{
						vo.setDistributorLogoUrl(PageHelper.getLogoUrl("", "D"));
					}
					vo.setAccountNo(orderPlanProduct.getAccountNo());
					String dividend = orderPlanProduct.getDividend();
					vo.setDividend(dividend);
					if(CommonConstantsWeb.ORDER_PLAN_PRODUCT_DIVIDEND_R.equals(dividend)){
						vo.setDividendDisplay(CommonConstantsWeb.ORDER_PLAN_PRODUCT_DIVIDEND_DISPLAY_R);
					}else if(CommonConstantsWeb.ORDER_PLAN_PRODUCT_DIVIDEND_D.equals(dividend)){
						vo.setDividendDisplay(CommonConstantsWeb.ORDER_PLAN_PRODUCT_DIVIDEND_DISPLAY_D);
					}
					ProductInfo product = orderPlanProduct.getProduct();
					vo.setProductId(product.getId());
					StringBuffer fundHql = new StringBuffer("" +
							" FROM" +
							" FundInfo f" +
							" WHERE" +
							" f.product.id=?" +
							"");
					params.clear();
					params.add(product.getId());
					List<FundInfo> fundInfos = this.baseDao.find(fundHql.toString(), params.toArray(), false);
					if(fundInfos != null && !fundInfos.isEmpty()){
						FundInfo fundInfo = fundInfos.get(0);
						vo.setMinSubscribeAmount(fundInfo.getMinSubscribeAmount()); // 最低订阅
						vo.setMinRedemptionAmount(fundInfo.getMinRedemptionAmount()); // 最低赎回
						vo.setMinHoldingAmount(fundInfo.getMinHoldingAmount()); // 最低持有
						vo.setMinRspAmount(fundInfo.getMinRspAmount()); // 最低定投
						vo.setFundInfoId(fundInfo.getId());
						vo.setRiskLevel(fundInfo.getRiskLevel());
						String fundCurrencyCode = null;
						if(CommonConstants.LANG_CODE_SC.equals(langCode)){
							FundInfoSc fundInfoSc = (FundInfoSc) this.baseDao.get(FundInfoSc.class, fundInfo.getId());
							vo.setFundName(fundInfoSc.getFundName());
							vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
							vo.setFundCurrencyName(fundInfoSc.getFundCurrency());
							vo.setFundType(fundInfoSc.getFundType());
							fundCurrencyCode = fundInfoSc.getFundCurrencyCode();
						}else if(CommonConstants.LANG_CODE_TC.equals(langCode)){
							FundInfoTc fundInfoTc = (FundInfoTc) this.baseDao.get(FundInfoTc.class, fundInfo.getId());
							vo.setFundName(fundInfoTc.getFundName());
							vo.setFundCurrencyCode(fundInfoTc.getFundCurrencyCode());
							vo.setFundCurrencyName(fundInfoTc.getFundCurrency());
							vo.setFundType(fundInfoTc.getFundType());
							fundCurrencyCode = fundInfoTc.getFundCurrencyCode();
						}else if(CommonConstants.LANG_CODE_EN.equals(langCode)){
							FundInfoEn fundInfoEn = (FundInfoEn) this.baseDao.get(FundInfoEn.class, fundInfo.getId());
							vo.setFundName(fundInfoEn.getFundName());
							vo.setFundCurrencyCode(fundInfoEn.getFundCurrencyCode());
							vo.setFundCurrencyName(fundInfoEn.getFundCurrency());
							vo.setFundType(fundInfoEn.getFundType());
							fundCurrencyCode = fundInfoEn.getFundCurrencyCode();
						}
						Double fundExChangeRate = null;
						if (StringUtils.isNotBlank(fundCurrencyCode) && StringUtils.isNotBlank(currencyCode)) {
							fundExChangeRate = this.getExchangeRate(fundCurrencyCode, currencyCode);
						}
						if(fundExChangeRate == null){
							fundExChangeRate = 1.00;
						}
						if(fundInfo.getLastNav() != null){
							vo.setLastNav(fundInfo.getLastNav()*fundExChangeRate);
						}
					}
					vos.add(vo);
				}
			}
		}
		return vos;
	}
	
	
	/**
	 * 获取交易计划审批记录
	 * @param planId
	 * @param langCode
	 * @return
	 */
	private List<OrderPlanCheckVO> getOrderPlanCheck(String planId, String langCode, String dataFormat) {
		List<OrderPlanCheckVO> checkVOs = null;
		if(StringUtils.isNotBlank(planId)){
			StringBuffer hql = new StringBuffer("" +" FROM OrderPlanCheck c WHERE c.plan.id=? ");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			List<OrderPlanCheck> checks = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(checks != null && !checks.isEmpty()){
				checkVOs = new ArrayList<OrderPlanCheckVO>();
				for (OrderPlanCheck orderPlanCheck : checks) {
					OrderPlanCheckVO vo = new OrderPlanCheckVO();
					BeanUtils.copyProperties(orderPlanCheck, vo);
					if(orderPlanCheck.getPlan() != null){
						vo.setPlanId(orderPlanCheck.getPlan().getId());
					}
					if(orderPlanCheck.getCheck() != null){
						vo.setApproval(getCommonMemberName(orderPlanCheck.getCheck().getId(), langCode, "2"));
						vo.setCheckId(orderPlanCheck.getCheck().getId());
					}
					vo.setCheckStatusDisplay(PropertyUtils.getPropertyValue(langCode, "order.plan.check.status." + orderPlanCheck.getCheckStatus(), null));
					if(orderPlanCheck.getCheckTime() != null){
						vo.setCheckTimeStr(DateUtil.dateToDateString(orderPlanCheck.getCheckTime(), dataFormat));
					}
					checkVOs.add(vo);
				}
			}
		}
		return checkVOs;
	}
	
	/**
	 * 交易计划该member的审批记录
	 * @param memberId
	 * @param planId
	 * @param status
	 * @return
	 */
	private List<OrderPlanCheck> getApprovalForPlan(String memberId, String planId,String status) {
		List<OrderPlanCheck> checks = null;
		if(StringUtils.isNotBlank(planId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" OrderPlanCheck c" +
					" WHERE" +
					" c.plan.id=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			if (StringUtils.isNotBlank(memberId)) {
				hql.append(" AND c.check.id=?");
				params.add(memberId);
			}
			if (StringUtils.isNotBlank(status)) {
				hql.append(" AND c.checkStatus=?");
				params.add(status);
			}
			checks = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return checks;
	}
	
	/**
	 * 交易计划审批
	 * @param memberBase
	 * @param langCode
	 * @param planId
	 * @param state
	 * @param opinon
	 * @param checkIp
	 * @return
	 */
	public boolean saveApprovalPlan(MemberBase memberBase,String langCode,String planId,String state,String opinon,String checkIp) {
		boolean flag = false;
		if (StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(state)) {
			OrderPlan plan = (OrderPlan)this.baseDao.get(OrderPlan.class,planId);
			if(plan != null && "1".equals(plan.getFinishStatus())){ // 1:审批中
				List<OrderPlanCheck> checks = this.getApprovalForPlan(memberBase.getId(), planId, "0");
				if(checks != null && !checks.isEmpty()){
					OrderPlanCheck orderPlanCheck = checks.get(0);
					orderPlanCheck.setCheckStatus(state); // 0待审核,1审核通过,2审核不通过,3未审批
					orderPlanCheck.setCheckTime(new Date());
					orderPlanCheck.setCheckResult(opinon);
					orderPlanCheck.setCheckIp(checkIp);
					orderPlanCheck = (OrderPlanCheck)this.baseDao.create(orderPlanCheck);
					String checkStatus = plan.getCheckStatus();
					if (StringUtils.isNotBlank(checkStatus) && "2".equals(state)) {
						checkStatus = checkStatus.replaceAll("1", "0");
						plan.setFinishStatus("2"); // 审批不通过
						checks = this.getApprovalForPlan(null, planId, "0");
						if(checks != null && !checks.isEmpty()){
							for (OrderPlanCheck check : checks) {
								check.setCheckStatus("3"); // 未审核
								this.baseDao.update(check);
							}
						}
						WebReadToDo webReadToDo = new WebReadToDo();
					 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_EXCHANGE);
					 	webReadToDo.setRelateId(plan.getId());
					 	webReadToDo.setFromMember(memberBase);
					 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioConfirmActivity");
					 	webReadToDo.setAppParam("orderPlanId="+plan.getId());
					 	webReadToDo.setIsApp("1");
					 	webReadToDo.setIsRead("0");
					 	webReadToDo.setOwner(plan.getCreator());
					 	webReadToDo.setCreateTime(new Date());
					 	webReadToDo.setIsValid("1");
					 	List<Object> params = new ArrayList<Object>();
					 	String titleSc = null;
					 	String titleTc = null;
					 	String titleEn = null;
				 		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberBase.getMemberType()){
				 			webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_REJECT_ADMIN);
				 			webReadToDo.setMsgUrl("/front/tradeMain/previewOrderPlan.do");
						 	webReadToDo.setMsgParam("id="+plan.getId());
				 			MemberIfa memberIfa = memberBaseService.findIfaMember(memberBase.getId());
				 			String key = null;
				 			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == plan.getCreator().getMemberType()){
				 				key = "order.reject.supervisor";
				 				params.add(getCommonMemberName(memberIfa.getMember().getId(), langCode, "2"));
					 			params.add(getCommonMemberName(plan.getPortfolioHold().getMember().getId(), langCode, "2"));
					 			params.add(plan.getPortfolioHold().getPortfolioName());
				 			}else{
				 				key = "order.reject.ifa";
				 				params.add(getCommonMemberName(memberIfa.getMember().getId(), langCode, "2"));
					 			params.add(plan.getPortfolioHold().getPortfolioName());
				 			}
				 			titleSc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC,params.toArray());
						 	titleTc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC,params.toArray());
						 	titleEn = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN,params.toArray());
				 		}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberBase.getMemberType()){
				 			webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_REJECT_INVESTOR);
				 			webReadToDo.setMsgUrl("/front/tradeMain/orderPlan.do");
						 	webReadToDo.setMsgParam("planId="+plan.getId());
				 			params.add(getCommonMemberName(plan.getPortfolioHold().getMember().getId(), langCode, "2"));
				 			params.add(plan.getPortfolioHold().getPortfolioName());
				 			titleSc = PropertyUtils.getSmsPropertyValue("order.reject",CommonConstants.LANG_CODE_SC,params.toArray());
						 	titleTc = PropertyUtils.getSmsPropertyValue("order.reject",CommonConstants.LANG_CODE_TC,params.toArray());
						 	titleEn = PropertyUtils.getSmsPropertyValue("order.reject",CommonConstants.LANG_CODE_EN,params.toArray());
				 		}
					 	webReadToDo.setTitle(titleEn);
				 		webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
					}else if (StringUtils.isNotBlank(checkStatus) && "1".equals(state)) {
						checkStatus = checkStatus.replaceFirst("0", "1");
						if(StringUtils.isNotBlank(checkStatus) && checkStatus.indexOf("0") < 0){
							plan.setFinishStatus("3"); // 审批通过
						}
						WebReadToDo webReadToDo = new WebReadToDo();
					 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_EXCHANGE);
					 	webReadToDo.setRelateId(plan.getId());
					 	webReadToDo.setFromMember(memberBase);
					 	webReadToDo.setMsgUrl("/front/tradeMain/previewOrderPlan.do");
					 	webReadToDo.setMsgParam("id="+plan.getId());
					 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioConfirmActivity");
					 	webReadToDo.setAppParam("orderPlanId="+plan.getId());
					 	webReadToDo.setIsApp("1");
					 	webReadToDo.setIsRead("0");
					 	webReadToDo.setOwner(plan.getCreator());
					 	webReadToDo.setCreateTime(new Date());
					 	webReadToDo.setIsValid("1");
					 	List<Object> params = new ArrayList<Object>();
					 	String titleSc = null;
					 	String titleTc = null;
					 	String titleEn = null;
				 		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberBase.getMemberType()){
				 			MemberIfa memberIfa = memberBaseService.findIfaMember(memberBase.getId());
				 			params.add(getCommonMemberName(memberIfa.getMember().getId(), langCode, "2"));
				 			params.add(getCommonMemberName(plan.getPortfolioHold().getMember().getId(), langCode, "2"));
				 			params.add(plan.getPortfolioHold().getPortfolioName());
				 			String key = null;
				 			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == plan.getCreator().getMemberType()){
				 				key = "order.approval.supervisor";
				 				params.add(getCommonMemberName(memberIfa.getMember().getId(), langCode, "2"));
					 			params.add(getCommonMemberName(plan.getPortfolioHold().getMember().getId(), langCode, "2"));
					 			params.add(plan.getPortfolioHold().getPortfolioName());
				 			}else{
				 				key = "order.approval.ifa";
				 				params.add(getCommonMemberName(memberIfa.getMember().getId(), langCode, "2"));
					 			params.add(plan.getPortfolioHold().getPortfolioName());
				 			}
				 			titleSc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_SC,params.toArray());
						 	titleTc = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_TC,params.toArray());
						 	titleEn = PropertyUtils.getSmsPropertyValue(key,CommonConstants.LANG_CODE_EN,params.toArray());
				 			webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_CONFIRM_ADMIN);
				 		}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberBase.getMemberType()){
				 			MemberIndividual individual = memberBaseService.findIndividualMember(memberBase.getId());
				 			params.add(this.getIndividualName(individual, langCode));
				 			params.add(plan.getPortfolioHold().getPortfolioName());
				 			titleSc = PropertyUtils.getSmsPropertyValue("order.approval",CommonConstants.LANG_CODE_SC,params.toArray());
						 	titleTc = PropertyUtils.getSmsPropertyValue("order.approval",CommonConstants.LANG_CODE_TC,params.toArray());
						 	titleEn = PropertyUtils.getSmsPropertyValue("order.approval",CommonConstants.LANG_CODE_EN,params.toArray());
						 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_CONFIRM_INVESTOR);
				 		}
					 	webReadToDo.setTitle(titleEn);
				 		webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
					}
					plan.setCheckStatus(checkStatus);
					plan.setLastUpdate(new Date());
					this.baseDao.update(plan);
					flag = true;
				}
			}
		}
		return flag;
	}
}
