/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service.impl;

import it.sauronsoftware.base64.Base64;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.oms.service.ITraderSendService;
import com.fsll.oms.vo.OmsAddOrderVO;
import com.fsll.oms.vo.OmsDelOrderVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderAip;
import com.fsll.wmes.entity.OrderAipTask;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.OrderReturn;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberSsoVO;
import com.fsll.wmes.trade.service.TradeOrderService;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.trade.vo.TransactionAccountVO;
import com.fsll.wmes.web.service.WebReadToDoService;

/**
 * 交易:下单业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Service("tradeOrderService")
public class TradeOrderServiceImpl  extends BaseService implements TradeOrderService{
	@Autowired
    private TradeStepService tradeStepService;
	@Autowired
    private FundInfoService fundInfoService;
	@Autowired
	private ITraderSendService iTraderSendService;
	@Autowired
	private WebReadToDoService webReadToDoService;
	@Autowired
	private MemberBaseService memberBaseService;
	
	/**
	 * 获取交易账号,根据memberType返回investor账号或者ifa AE账号
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@Override
	public List<TransactionAccountVO> getHistoryAccountByhistoryId(String historyId, Integer memberType) {
		List<TransactionAccountVO> accounts = null;
		if(StringUtils.isNotBlank(historyId) && memberType != null){
			StringBuffer hql = new StringBuffer();
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberType){
				hql.append("" +
						" SELECT DISTINCT" +
						" a.id,b.aeCode,a.distributor.id,d.logofile" +
						" FROM" +
						" IfaDistributor b" +
						" LEFT JOIN" +
						" InvestorAccount a" +
						" ON" +
						" a.distributor.id=b.distributor.id" +
						" LEFT JOIN" +
						" OrderHistory h" +
						" ON" +
						" a.id=h.account.id" +
						" LEFT JOIN" +
						" MemberDistributor d" +
						" ON" +
						" d.id=a.distributor.id" +
						" WHERE" +
						" h.id=?" +
						" AND" +
						" b.ifa.id=a.ifa.id" +
						"");
			}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType){
				hql.append("" +
						" SELECT DISTINCT" +
						" a.id,a.accountNo,a.distributor.id,d.logofile" +
						" FROM" +
						" InvestorAccount a" +
						" LEFT JOIN" +
						" OrderHistory h" +
						" ON" +
						" a.id=h.account.id" +
						" LEFT JOIN" +
						" MemberDistributor d" +
						" ON" +
						" d.id=a.distributor.id" +
						" WHERE" +
						" h.id=?" +
						" AND" +
						" b.ifa.id=a.ifa.id" +
						"");
			}
			List<Object> params = new ArrayList<Object>();
			params.add(historyId);
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objs != null && !objs.isEmpty()){
				accounts = new ArrayList<TransactionAccountVO>();
				for (Object[] objects : objs) {
					TransactionAccountVO vo = new TransactionAccountVO();
					vo.setAccountId((String) objects[0]);
					vo.setAccountNo((String) objects[1]);
					vo.setDistributorId((String) objects[2]);
					vo.setLogoUrl(PageHelper.getLogoUrl((String) objects[3], "D"));
					vo.setMemberType(memberType);
					accounts.add(vo);
				}
			}
		}
		return accounts;
	}
	
	/**
	 * 获取交易账号,根据memberType返回investor账号或者ifa AE账号
	 * @author wwluo
	 * @date 2016-11-30
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TransactionAccountVO> getOrderPlanAccounts(String planId, Integer memberType,String planProductId) {
		List<TransactionAccountVO> accounts = null;
		if(StringUtils.isNotBlank(planId) && memberType != null){
			StringBuffer hql = new StringBuffer();
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberType){
				hql.append("" +
						" SELECT DISTINCT" +
						" a.id,b.aeCode,a.distributor.id,d.logofile" +
						" FROM" +
						" IfaDistributor b" +
						" LEFT JOIN" +
						" InvestorAccount a" +
						" ON" +
						" a.distributor.id=b.distributor.id" +
						" LEFT JOIN" +
						" OrderPlanProduct p" +
						" ON" +
						" a.id=p.account.id" +
						" LEFT JOIN" +
						" MemberDistributor d" +
						" ON" +
						" d.id=a.distributor.id" +
						" WHERE" +
						" p.account" +
						" IS NOT NULL" +
						" AND" +
						" p.plan.id=?" +
						" AND" +
						" b.ifa.id=a.ifa.id" +
						"");
			}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberType){
				hql.append("" +
						" SELECT DISTINCT" +
						" a.id,a.accountNo,a.distributor.id,d.logofile" +
						" FROM" +
						" InvestorAccount a" +
						" LEFT JOIN" +
						" OrderPlanProduct p" +
						" ON" +
						" a.id=p.account.id" +
						" LEFT JOIN" +
						" MemberDistributor d" +
						" ON" +
						" d.id=a.distributor.id" +
						" WHERE" +
						" p.account" +
						" IS NOT NULL" +
						" AND" +
						" p.plan.id=?" +
						"");
			}
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			if (StringUtils.isNotBlank(planProductId)) {
				hql.append(" AND p.id=?");
				params.add(planProductId);
			}
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objs != null && !objs.isEmpty()){
				accounts = new ArrayList<TransactionAccountVO>();
				for (Object[] objects : objs) {
					TransactionAccountVO vo = new TransactionAccountVO();
					vo.setAccountId((String) objects[0]);
					vo.setAccountNo((String) objects[1]);
					vo.setDistributorId((String) objects[2]);
					vo.setLogoUrl(PageHelper.getLogoUrl((String) objects[3], "D"));
					vo.setMemberType(memberType);
					accounts.add(vo);
				}
			}
		}
		return accounts;
	}
	
	/**
	 * 发送oms下单
	 * @param loginUser
	 * @param ssoVo
	 * @param langCode
	 * @param planId
	 * @param accountData
	 */
	public boolean addOrderPlan(HttpServletRequest request,MemberBase loginUser, String langCode,String planId,String accountData) {
		boolean flag = false;
		OrderPlan orderPlan = (OrderPlan)this.baseDao.get(OrderPlan.class,planId);
		if (orderPlan != null && "1".equals(orderPlan.getAipFlag())) {
		  //定投交易	
			flag = this.addOrderPlanAIP(request, loginUser, langCode, planId, accountData);
		}else if(orderPlan != null && StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(accountData)){
				List<Map> accountDataMap = JsonUtil.toListMap(accountData);
				if(accountDataMap != null && !accountDataMap.isEmpty()){
					for (Map map : accountDataMap) {
						String password = (String) map.get("password");
						String accountNo = (String) map.get("accountNo");
						password = Base64.decode(password, "UTF-8");
						PortfolioHold hold = orderPlan.getPortfolioHold();
						if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType() && loginUser.getId().equals(hold.getMember().getId())){
							// 为当前投资人操作
							List<OrderPlanProduct> orderPlanProducts = this.getOrderPlanProduct(planId,accountNo);
							if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
								for (OrderPlanProduct orderPlanProduct : orderPlanProducts) {
									ProductInfo productInfo = orderPlanProduct.getProduct();
									FundInfo fundInfo = fundInfoService.getFundInfoByProduct(productInfo.getId());
									FundInfoEn fundInfoEn = fundInfoService.findFundInfoEnById(fundInfo.getId());
									InvestorAccount account = orderPlanProduct.getAccount();
									String aeCode = this.findAECode(hold.getIfa().getId(),account.getDistributor().getId());
									String symbolCode = this.findSymbolCode(orderPlanProduct.getProduct().getId(),account.getDistributor().getId());
									OmsAddOrderVO omsOrder = new OmsAddOrderVO();
									omsOrder.setAccountNo(account.getAccountNo());
									omsOrder.setCreatorNo(account.getAccountNo()); // 下单人的 accountNo
									omsOrder.setAeCode(aeCode);
									omsOrder.setCurrencyCode(orderPlanProduct.getTranFeeCur());
									Double fundTranExchangeRate = null;
									if (StringUtils.isNotBlank(orderPlanProduct.getTranFeeCur()) && StringUtils.isNotBlank(fundInfoEn.getFundCurrencyCode())) {
										WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(fundInfoEn.getFundCurrencyCode(), orderPlanProduct.getTranFeeCur());
										fundTranExchangeRate = webExchangeRate.getRate();
									}
									if(fundTranExchangeRate == null){
										fundTranExchangeRate = 1.00;
									}
									// history
									OrderHistory history = new OrderHistory();  
									history.setAccount(account);
									history.setAccountNo(account.getAccountNo());
									if(orderPlanProduct.getAmount() != null){
										history.setCommissionAmount(orderPlanProduct.getAmount()); // 参考交易金额
									}
									if(fundInfo.getLastNav() != null){
										history.setCommissionPrice(fundInfo.getLastNav() * fundTranExchangeRate);  // 委托单价 
									}
									history.setCommissionUnit(orderPlanProduct.getUnit()); // 委托份额
									history.setCurrencyCode(fundInfoEn.getFundCurrencyCode()); // 基金产品货币
									history.setFee(null); // 当次交易费用,oms实际返回的交易费用
									history.setIfa(hold.getIfa());
									history.setCreatorId(orderPlan.getCreator()); // 下单人
									history.setIfAip(orderPlan.getAipFlag());
									history.setMember(hold.getMember());
									history.setOrderDate(new Date());
									String tranType = null;
									if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType())){
										history.setOrderType(CommonConstants.ORDER_TYPE_BUY);
										tranType = "0";
										if(orderPlanProduct.getAmount() != null){
											omsOrder.setLimitedPrice(orderPlanProduct.getAmount()); // 买入金额（增持时为买入总金额，减持为产品净值）
										}
										// 是否转换，计算转换分配比例
										if (StringUtils.isNotBlank(orderPlanProduct.getSwitchFlag()) && "1".equals(orderPlanProduct.getSwitchFlag())) {
											Double switchAllocRate = this.getSwitchAllocRate(orderPlan.getId(),orderPlanProduct.getSwitchGroup(),orderPlanProduct.getAmount());
											history.setSwitchAllocRate(switchAllocRate);
										}
									}else if(CommonConstants.TRAN_TYPE_SELL.equals(orderPlanProduct.getTranType())){
										history.setOrderType(CommonConstants.ORDER_TYPE_SELL);
										tranType = "1";
										Double unit = orderPlanProduct.getUnit();
										if(unit != null){
											omsOrder.setQuantity(Double.parseDouble(unit.toString())); // 赎回份额，减持有效
										}
										if(orderPlanProduct.getAmount() != null){
											omsOrder.setLimitedPrice(fundInfo.getLastNav() * fundTranExchangeRate); // 产品净值（  增持时为买入总金额，减持为产品净值）
										}
									}
									history.setPlan(orderPlan);
									history.setPortfolioHold(hold);
									history.setProduct(productInfo);
									history.setProductName(fundInfoEn.getFundName());
									history.setStatus(null);
									history.setTransactionAmount(null);
									history.setTransactionUnit(null);
									history.setUpdateTime(new Date());
									history = (OrderHistory)this.baseDao.create(history);
									omsOrder.setOrderNO(history.getId()); 
									omsOrder.setSymbolCode(symbolCode);
									omsOrder.setTradingPassword(password);
									omsOrder.setTranType(tranType);
									omsOrder.setTranFeeMini(orderPlanProduct.getTranFeeMini());
									omsOrder.setTranFeeRate(orderPlanProduct.getTranRate());
									// 调用交易接口
									iTraderSendService.addOrder(request, omsOrder);
									// 更新组合持仓帐户绑定表
									this.updatePortfolioHoldAccount(hold.getId(),hold.getMember().getId(),account.getId(),account.getAccountNo());
									orderPlanProduct.setStatus("1"); // 成功向OMS提单
									this.baseDao.update(orderPlanProduct);
								}
								orderPlan.setSubmitDate(new Date());
								orderPlan.setFinishStatus("4"); // 交易中
								this.baseDao.update(orderPlan);
								hold.setIfFirst("0"); // 是否第一次交易，0：否;1：是。
								this.baseDao.update(hold);
								flag = true;
							}
						}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType() && loginUser.getId().equals(hold.getIfa().getMember().getId()) 
								// && orderPlan.getAuthorized() == 1
								){
							// 为当前IFA操作
							IfaDistributor distributor = this.findIfaDistributorByAeCode(accountNo, hold.getIfa().getId());
							if(distributor != null && distributor.getDistributor() != null){
								List<InvestorAccount> accounts = this.findInvestorAccountsByDistributorMember(distributor.getDistributor().getId(),hold.getMember().getId());
								if(accounts != null && !accounts.isEmpty()){
									for (InvestorAccount account : accounts) {
										List<OrderPlanProduct> orderPlanProducts = null;
										if(account != null){
											orderPlanProducts = this.getOrderPlanProduct(planId,account.getAccountNo());
										}
										if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
											for (OrderPlanProduct orderPlanProduct : orderPlanProducts) {
												ProductInfo productInfo = orderPlanProduct.getProduct();
												FundInfo fundInfo = fundInfoService.getFundInfoByProduct(productInfo.getId());
												FundInfoEn fundInfoEn = fundInfoService.findFundInfoEnById(fundInfo.getId());
												account = orderPlanProduct.getAccount();
												String symbolCode = this.findSymbolCode(orderPlanProduct.getProduct().getId(),account.getDistributor().getId());
												OmsAddOrderVO omsOrder = new OmsAddOrderVO();
												omsOrder.setAccountNo(account.getAccountNo());
												omsOrder.setCreatorNo(accountNo); //下单人的 accountNo
												omsOrder.setAeCode(accountNo); // IFA时，accountNo为AE账号
												omsOrder.setCurrencyCode(orderPlanProduct.getTranFeeCur());
												Double fundTranExchangeRate = null;
												if (StringUtils.isNotBlank(orderPlanProduct.getTranFeeCur()) && StringUtils.isNotBlank(fundInfoEn.getFundCurrencyCode())) {
													WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(fundInfoEn.getFundCurrencyCode(), orderPlanProduct.getTranFeeCur());
													fundTranExchangeRate = webExchangeRate.getRate();
												}
												if(fundTranExchangeRate == null){
													fundTranExchangeRate = 1.00;
												}
												// history
												OrderHistory history = new OrderHistory();  
												history.setAccount(account);
												history.setAccountNo(account.getAccountNo());
												history.setCommissionAmount(orderPlanProduct.getAmount()); // 参考交易金额
												if(fundInfo.getLastNav() != null){
													history.setCommissionPrice(fundInfo.getLastNav() * fundTranExchangeRate);  // 委托单价 
												}
												history.setCommissionUnit(orderPlanProduct.getUnit()); // 委托份额
												history.setCurrencyCode(fundInfoEn.getFundCurrencyCode()); // 基金产品货币
												history.setFee(null); // 当次交易费用,oms实际返回的交易费用
												history.setIfa(hold.getIfa());
												history.setCreatorId(orderPlan.getCreator()); // 下单人
												history.setIfAip(orderPlan.getAipFlag());
												history.setMember(hold.getMember());
												history.setOrderDate(new Date());
												String tranType = null;
												if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType())){
													history.setOrderType("Buy");
													tranType = "0";
													omsOrder.setLimitedPrice(orderPlanProduct.getAmount()); // 买入金额（增持时为买入总金额，减持为产品净值）
													// 是否转换，计算转换分配比例
													if (StringUtils.isNotBlank(orderPlanProduct.getSwitchFlag()) && "1".equals(orderPlanProduct.getSwitchFlag())) {
														Double switchAllocRate = this.getSwitchAllocRate(orderPlan.getId(),orderPlanProduct.getSwitchGroup(),orderPlanProduct.getAmount());
														history.setSwitchAllocRate(switchAllocRate);
													}
												}else if(CommonConstants.TRAN_TYPE_SELL.equals(orderPlanProduct.getTranType())){
													history.setOrderType("Sell");
													tranType = "1";
													Double unit = orderPlanProduct.getUnit();
													if(unit != null){
														omsOrder.setQuantity(Double.parseDouble(unit.toString())); // 赎回份额，减持有效
													}
													if(orderPlanProduct.getAmount() != null){
														omsOrder.setLimitedPrice(fundInfo.getLastNav() * fundTranExchangeRate); // 产品净值（  增持时为买入总金额，减持为产品净值）
													}
												}
												history.setPlan(orderPlan);
												history.setPortfolioHold(hold);
												history.setProduct(productInfo);
												history.setSwitchFlag(orderPlanProduct.getSwitchFlag());
												history.setSwitchGroup(orderPlanProduct.getSwitchGroup());
												history.setProductName(fundInfoEn.getFundName());
												history.setStatus(null);
												history.setTransactionAmount(null);
												history.setTransactionUnit(null);
												history.setUpdateTime(new Date());
												history = (OrderHistory)this.baseDao.create(history);
												
												omsOrder.setOrderNO(history.getId()); 
												omsOrder.setSymbolCode(symbolCode);
												omsOrder.setTradingPassword(password);
												omsOrder.setTranType(tranType);
												omsOrder.setTranFeeMini(orderPlanProduct.getTranFeeMini());
												omsOrder.setTranFeeRate(orderPlanProduct.getTranRate());
												// 调用交易接口
												iTraderSendService.addOrder(request, omsOrder);
												orderPlanProduct.setStatus("1"); // 成功向OMS提单
												this.baseDao.update(orderPlanProduct);
												// 更新组合持仓帐户绑定表
												this.updatePortfolioHoldAccount(hold.getId(),hold.getMember().getId(),account.getId(),account.getAccountNo());
											}
											orderPlan.setSubmitDate(new Date());
											orderPlan.setFinishStatus("4"); // 交易中
											this.baseDao.update(orderPlan);
											hold.setIfFirst("0"); // 是否第一次交易，0：否;1：是。
											hold.setLastUpdate(new Date());
											this.baseDao.update(hold);
											
											// 消息提醒
											WebReadToDo webReadToDo = new WebReadToDo();
										 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_EXCHANGE);
										 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_COMMISSION);
										 	webReadToDo.setRelateId(orderPlan.getId());
										 	webReadToDo.setFromMember(loginUser);
										 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioDetailActivity");
										 	webReadToDo.setAppParam("portfolioHoldId="+hold.getId());
										 	webReadToDo.setIsApp("1");
										 	webReadToDo.setIsRead("0");
										 	if(hold.getPortfolio() != null){
										 		webReadToDo.setOwner(hold.getPortfolio().getMember());
										 	}else{
										 		webReadToDo.setOwner(hold.getMember());
										 	}
										 	webReadToDo.setCreateTime(new Date());
										 	webReadToDo.setIsValid("1");
										 	List<Object> params = new ArrayList<Object>();
										 	String titleSc = null;
										 	String titleTc = null;
										 	String titleEn = null;
								 			webReadToDo.setMsgUrl("/front/tradeMain/previewOrderPlan.do");
										 	webReadToDo.setMsgParam("id="+orderPlan.getId());
								 			MemberIfa memberIfa = memberBaseService.findIfaMember(loginUser.getId());
								 			params.add(getCommonMemberName(memberIfa.getMember().getId(), langCode, "2"));
								 			params.add(orderPlan.getPortfolioHold().getPortfolioName());
								 			titleSc = PropertyUtils.getSmsPropertyValue("order.commission.oms",CommonConstants.LANG_CODE_SC,params.toArray());
										 	titleTc = PropertyUtils.getSmsPropertyValue("order.commission.oms",CommonConstants.LANG_CODE_TC,params.toArray());
										 	titleEn = PropertyUtils.getSmsPropertyValue("order.commission.oms",CommonConstants.LANG_CODE_EN,params.toArray());
										 	webReadToDo.setTitle(titleEn);
									 		webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
											flag = true;
										}
									}
								}
							}
						}
					}
				}
			}
		return flag;
	}
	
	/**
	 * 定投下单
	 * @author wwluo
	 * @date 2016-11-30
	 */
	private boolean addOrderPlanAIP(HttpServletRequest request,MemberBase loginUser, String langCode,String planId,String accountData) {
		boolean flag = false;
		OrderPlan orderPlan = (OrderPlan)this.baseDao.get(OrderPlan.class,planId);
		if (orderPlan != null && "1".equals(orderPlan.getAipFlag())) {
			PortfolioHold hold = orderPlan.getPortfolioHold();
			StringBuffer hql = new StringBuffer("FROM OrderPlanAip a WHERE a.plan.id='"+planId+"' ORDER BY a.lastUpdate Desc");
			List<OrderPlanAip> orderPlanAips = this.baseDao.find(hql.toString(),null, false);
			OrderPlanAip orderPlanAip = orderPlanAips.get(0);
			OrderAip orderAip = new OrderAip();
			BeanUtils.copyProperties(orderPlanAip, orderAip);
			orderAip.setPortfolioHold(hold);
			orderAip.setAipState("1");
			orderAip.setAipNextTime(null); // 定时任务执行后回写
			orderAip.setAipCount(0);
			orderAip.setCreateTime(new Date());
			orderAip.setLastUpdate(new Date());
			// 保存定投执行计划
			orderAip = (OrderAip) this.baseDao.create(orderAip);
			if(StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(accountData)){
				List<Map> accountDataMap = JsonUtil.toListMap(accountData);
				if(accountDataMap != null && !accountDataMap.isEmpty()){
					for (Map map : accountDataMap) {
						String password = (String) map.get("password");
						String accountNo = (String) map.get("accountNo");
						password = Base64.decode(password, "UTF-8");
						if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType() && loginUser.getId().equals(hold.getMember().getId())){
							// 为当前投资人操作
							List<OrderPlanProduct> orderPlanProducts = this.getOrderPlanProduct(planId,accountNo);
							if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
								for (OrderPlanProduct orderPlanProduct : orderPlanProducts) {
									InvestorAccount account = orderPlanProduct.getAccount();
									if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType()) && !"1".equals(orderPlanProduct.getSwitchFlag())){
										// 排除转换，其他买入均作为定投
										OrderAipTask orderAipTask = new OrderAipTask();
										orderAipTask.setOrderAip(orderAip);
										orderAipTask.setProduct(orderPlanProduct.getProduct());
										orderAipTask.setAmount(orderPlanProduct.getAmount());
										orderAipTask.setDividend(orderPlanProduct.getDividend());
										orderAipTask.setAccount(orderPlanProduct.getAccount());
										orderAipTask.setAccountNo(orderPlanProduct.getAccountNo());
										orderAipTask.setTranFee(orderPlanProduct.getTranFee());
										orderAipTask.setTranFeeCur(orderPlanProduct.getTranFeeCur());
										orderAipTask.setTranRate(orderPlanProduct.getTranRate());
										orderAipTask.setTranFeeMini(orderPlanProduct.getTranFeeMini());
										orderAipTask = (OrderAipTask) this.baseDao.create(orderAipTask);
										// 更新组合持仓帐户绑定表
										this.updatePortfolioHoldAccount(hold.getId(),hold.getMember().getId(),account.getId(),account.getAccountNo());
										orderPlanProduct.setStatus("1"); // 成功向OMS提单
										this.baseDao.update(orderPlanProduct);
									}else{
									    // 正常下单
										ProductInfo productInfo = orderPlanProduct.getProduct();
										FundInfo fundInfo = fundInfoService.getFundInfoByProduct(productInfo.getId());
										FundInfoEn fundInfoEn = fundInfoService.findFundInfoEnById(fundInfo.getId());
										String aeCode = this.findAECode(hold.getIfa().getId(),account.getDistributor().getId());
										String symbolCode = this.findSymbolCode(orderPlanProduct.getProduct().getId(),account.getDistributor().getId());
										OmsAddOrderVO omsOrder = new OmsAddOrderVO();
										omsOrder.setAccountNo(account.getAccountNo());
										omsOrder.setCreatorNo(account.getAccountNo()); //下单人的 accountNo
										omsOrder.setAeCode(aeCode);
										omsOrder.setCurrencyCode(orderPlanProduct.getTranFeeCur());
										Double fundTranExchangeRate = null;
										if (StringUtils.isNotBlank(orderPlanProduct.getTranFeeCur()) && StringUtils.isNotBlank(fundInfoEn.getFundCurrencyCode())) {
											WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(fundInfoEn.getFundCurrencyCode(), orderPlanProduct.getTranFeeCur());
											fundTranExchangeRate = webExchangeRate.getRate();
										}
										if(fundTranExchangeRate == null){
											fundTranExchangeRate = 1.00;
										}
										// history
										OrderHistory history = new OrderHistory();  
										history.setAccount(account);
										history.setAccountNo(account.getAccountNo());
										if(orderPlanProduct.getAmount() != null){
											history.setCommissionAmount(orderPlanProduct.getAmount()); // 参考交易金额
										}
										if(fundInfo.getLastNav() != null){
											history.setCommissionPrice(fundInfo.getLastNav() * fundTranExchangeRate);  // 委托单价 
										}
										history.setCommissionUnit(orderPlanProduct.getUnit()); // 委托份额
										history.setCurrencyCode(fundInfoEn.getFundCurrencyCode()); // 基金产品货币
										history.setFee(null); // 当次交易费用,oms实际返回的交易费用
										history.setIfa(hold.getIfa());
										history.setCreatorId(orderPlan.getCreator()); // 下单人
										history.setIfAip(orderPlan.getAipFlag());
										history.setMember(hold.getMember());
										history.setOrderDate(new Date());
										String tranType = null;
										if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType())){
											history.setOrderType(CommonConstants.ORDER_TYPE_BUY);
											tranType = "0";
											if(orderPlanProduct.getAmount() != null){
												omsOrder.setLimitedPrice(orderPlanProduct.getAmount()); // 买入金额（增持时为买入总金额，减持为产品净值）
											}
											// 是否转换，计算转换分配比例
											if (StringUtils.isNotBlank(orderPlanProduct.getSwitchFlag()) && "1".equals(orderPlanProduct.getSwitchFlag())) {
												Double switchAllocRate = this.getSwitchAllocRate(orderPlan.getId(),orderPlanProduct.getSwitchGroup(),orderPlanProduct.getAmount());
												history.setSwitchAllocRate(switchAllocRate);
											}
										}else if(CommonConstants.TRAN_TYPE_SELL.equals(orderPlanProduct.getTranType())){
											history.setOrderType(CommonConstants.ORDER_TYPE_SELL);
											tranType = "1";
											Double unit = orderPlanProduct.getUnit();
											if(unit != null){
												omsOrder.setQuantity(Double.parseDouble(unit.toString())); // 赎回份额，减持有效
											}
											if(orderPlanProduct.getAmount() != null){
												omsOrder.setLimitedPrice(fundInfo.getLastNav() * fundTranExchangeRate); // 产品净值（  增持时为买入总金额，减持为产品净值）
											}
										}
										history.setPlan(orderPlan);
										history.setPortfolioHold(hold);
										history.setProduct(productInfo);
										history.setProductName(fundInfoEn.getFundName());
										history.setStatus(null);
										history.setTransactionAmount(null);
										history.setTransactionUnit(null);
										history.setUpdateTime(new Date());
										history = (OrderHistory) this.baseDao.create(history);
										omsOrder.setOrderNO(history.getId()); 
										omsOrder.setSymbolCode(symbolCode);
										omsOrder.setTradingPassword(password);
										omsOrder.setTranType(tranType);
										omsOrder.setTranFeeMini(orderPlanProduct.getTranFeeMini());
										omsOrder.setTranFeeRate(orderPlanProduct.getTranRate());
										// 调用交易接口
										iTraderSendService.addOrder(request, omsOrder);
										// 更新组合持仓帐户绑定表
										this.updatePortfolioHoldAccount(hold.getId(),hold.getMember().getId(),account.getId(),account.getAccountNo());
										orderPlanProduct.setStatus("1"); // 成功向OMS提单
										this.baseDao.update(orderPlanProduct);
									}
								}
								orderPlan.setSubmitDate(new Date());
								orderPlan.setFinishStatus("4"); // 交易中
								this.baseDao.update(orderPlan);
								hold.setIfFirst("0"); // 是否第一次交易，0：否;1：是。
								this.baseDao.update(hold);
								flag = true;
							}
						}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType() && loginUser.getId().equals(hold.getIfa().getMember().getId()) 
								// && orderPlan.getAuthorized() == 1
								){
							// 为当前IFA操作
							IfaDistributor distributor = this.findIfaDistributorByAeCode(accountNo, hold.getIfa().getId());
							if(distributor != null && distributor.getDistributor() != null){
								List<InvestorAccount> accounts = this.findInvestorAccountsByDistributorMember(distributor.getDistributor().getId(),hold.getMember().getId());
								if(accounts != null && !accounts.isEmpty()){
									for (InvestorAccount account : accounts) {
										List<OrderPlanProduct> orderPlanProducts = null;
										if(account != null){
											orderPlanProducts = this.getOrderPlanProduct(planId,account.getAccountNo());
										}
										if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
											for (OrderPlanProduct orderPlanProduct : orderPlanProducts) {
												account = orderPlanProduct.getAccount();
												if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType()) && !"1".equals(orderPlanProduct.getSwitchFlag())){
												// 排除转换，其他买入均作为定投
													OrderAipTask orderAipTask = new OrderAipTask();
													orderAipTask.setOrderAip(orderAip);
													orderAipTask.setProduct(orderPlanProduct.getProduct());
													orderAipTask.setAmount(orderPlanProduct.getAmount());
													orderAipTask.setDividend(orderPlanProduct.getDividend());
													orderAipTask.setAccount(orderPlanProduct.getAccount());
													orderAipTask.setAccountNo(orderPlanProduct.getAccountNo());
													orderAipTask.setTranFee(orderPlanProduct.getTranFee());
													orderAipTask.setTranFeeCur(orderPlanProduct.getTranFeeCur());
													orderAipTask.setTranRate(orderPlanProduct.getTranRate());
													orderAipTask.setTranFeeMini(orderPlanProduct.getTranFeeMini());
													orderAipTask = (OrderAipTask) this.baseDao.create(orderAipTask);
													// 更新组合持仓帐户绑定表
													this.updatePortfolioHoldAccount(hold.getId(),hold.getMember().getId(),account.getId(),account.getAccountNo());
													orderPlanProduct.setStatus("1"); // 已提单
													this.baseDao.update(orderPlanProduct);
												}else{
												// 正常下单
													ProductInfo productInfo = orderPlanProduct.getProduct();
													FundInfo fundInfo = fundInfoService.getFundInfoByProduct(productInfo.getId());
													FundInfoEn fundInfoEn = fundInfoService.findFundInfoEnById(fundInfo.getId());
													String symbolCode = this.findSymbolCode(orderPlanProduct.getProduct().getId(),account.getDistributor().getId());
													OmsAddOrderVO omsOrder = new OmsAddOrderVO();
													omsOrder.setAccountNo(account.getAccountNo());
													omsOrder.setCreatorNo(accountNo); //下单人的 accountNo
													omsOrder.setAeCode(accountNo); // IFA时，accountNo为AE账号
													omsOrder.setCurrencyCode(orderPlanProduct.getTranFeeCur());
													Double fundTranExchangeRate = null;
													if (StringUtils.isNotBlank(orderPlanProduct.getTranFeeCur()) && StringUtils.isNotBlank(fundInfoEn.getFundCurrencyCode())) {
														WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(fundInfoEn.getFundCurrencyCode(), orderPlanProduct.getTranFeeCur());
														fundTranExchangeRate = webExchangeRate.getRate();
													}
													if(fundTranExchangeRate == null){
														fundTranExchangeRate = 1.00;
													}
													// history
													OrderHistory history = new OrderHistory();  
													history.setAccount(account);
													history.setAccountNo(account.getAccountNo());
													history.setCommissionAmount(orderPlanProduct.getAmount()); // 参考交易金额
													if(fundInfo.getLastNav() != null){
														history.setCommissionPrice(fundInfo.getLastNav() * fundTranExchangeRate);  // 委托单价 
													}
													history.setCommissionUnit(orderPlanProduct.getUnit()); // 委托份额
													history.setCurrencyCode(fundInfoEn.getFundCurrencyCode()); // 基金产品货币
													history.setFee(null); // 当次交易费用,oms实际返回的交易费用
													history.setIfa(hold.getIfa());
													history.setCreatorId(orderPlan.getCreator()); // 下单人
													history.setIfAip(orderPlan.getAipFlag());
													history.setMember(hold.getMember());
													history.setOrderDate(new Date());
													String tranType = null;
													if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType())){
														history.setOrderType("Buy");
														tranType = "0";
														omsOrder.setLimitedPrice(orderPlanProduct.getAmount()); // 买入金额（增持时为买入总金额，减持为产品净值）
														// 是否转换，计算转换分配比例
														if (StringUtils.isNotBlank(orderPlanProduct.getSwitchFlag()) && "1".equals(orderPlanProduct.getSwitchFlag())) {
															Double switchAllocRate = this.getSwitchAllocRate(orderPlan.getId(),orderPlanProduct.getSwitchGroup(),orderPlanProduct.getAmount());
															history.setSwitchAllocRate(switchAllocRate);
														}
													}else if(CommonConstants.TRAN_TYPE_SELL.equals(orderPlanProduct.getTranType())){
														history.setOrderType("Sell");
														tranType = "1";
														Double unit = orderPlanProduct.getUnit();
														if(unit != null){
															omsOrder.setQuantity(Double.parseDouble(unit.toString())); // 赎回份额，减持有效
														}
														if(orderPlanProduct.getAmount() != null){
															omsOrder.setLimitedPrice(fundInfo.getLastNav() * fundTranExchangeRate); // 产品净值（  增持时为买入总金额，减持为产品净值）
														}
													}
													history.setPlan(orderPlan);
													history.setPortfolioHold(hold);
													history.setProduct(productInfo);
													history.setSwitchFlag(orderPlanProduct.getSwitchFlag());
													history.setSwitchGroup(orderPlanProduct.getSwitchGroup());
													history.setProductName(fundInfoEn.getFundName());
													history.setStatus(null);
													history.setTransactionAmount(null);
													history.setTransactionUnit(null);
													history.setUpdateTime(new Date());
													history = (OrderHistory) this.baseDao.create(history);
													
													omsOrder.setOrderNO(history.getId()); 
													omsOrder.setSymbolCode(symbolCode);
													omsOrder.setTradingPassword(password);
													omsOrder.setTranType(tranType);
													omsOrder.setTranFeeMini(orderPlanProduct.getTranFeeMini());
													omsOrder.setTranFeeRate(orderPlanProduct.getTranRate());
													// 调用交易接口
													iTraderSendService.addOrder(request, omsOrder);
													// 更新组合持仓帐户绑定表
													this.updatePortfolioHoldAccount(hold.getId(),hold.getMember().getId(),account.getId(),account.getAccountNo());
													orderPlanProduct.setStatus("1"); // 成功向OMS提单
													this.baseDao.update(orderPlanProduct);
												}
											}
											orderPlan.setSubmitDate(new Date());
											orderPlan.setFinishStatus("4"); // 交易中
											this.baseDao.update(orderPlan);
											hold.setIfFirst("0"); // 是否第一次交易，0：否;1：是。
											this.baseDao.update(hold);
											// 消息提醒
											WebReadToDo webReadToDo = new WebReadToDo();
										 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_EXCHANGE);
										 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_COMMISSION);
										 	webReadToDo.setRelateId(orderPlan.getId());
										 	webReadToDo.setFromMember(loginUser);
										 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioDetailActivity");
										 	webReadToDo.setAppParam("portfolioHoldId="+hold.getId());
										 	webReadToDo.setIsApp("1");
										 	webReadToDo.setIsRead("0");
										 	if(hold.getPortfolio() != null){
										 		webReadToDo.setOwner(hold.getPortfolio().getMember());
										 	}else{
										 		webReadToDo.setOwner(hold.getMember());
										 	}
										 	webReadToDo.setCreateTime(new Date());
										 	webReadToDo.setIsValid("1");
										 	List<Object> params = new ArrayList<Object>();
										 	String titleSc = null;
										 	String titleTc = null;
										 	String titleEn = null;
								 			webReadToDo.setMsgUrl("/front/tradeMain/previewOrderPlan.do");
										 	webReadToDo.setMsgParam("id="+orderPlan.getId());
								 			MemberIfa memberIfa = memberBaseService.findIfaMember(loginUser.getId());
								 			params.add(getCommonMemberName(memberIfa.getMember().getId(), langCode, "2"));
								 			params.add(orderPlan.getPortfolioHold().getPortfolioName());
								 			titleSc = PropertyUtils.getSmsPropertyValue("order.commission.oms",CommonConstants.LANG_CODE_SC,params.toArray());
										 	titleTc = PropertyUtils.getSmsPropertyValue("order.commission.oms",CommonConstants.LANG_CODE_TC,params.toArray());
										 	titleEn = PropertyUtils.getSmsPropertyValue("order.commission.oms",CommonConstants.LANG_CODE_EN,params.toArray());
										 	webReadToDo.setTitle(titleEn);
									 		webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
											flag = true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * 获取交易产品
	 * @author wwluo
	 * @date 2016-12-26
	 */
	private List<OrderPlanProduct> getOrderPlanProduct(String planId, String accountNo) {
		List<OrderPlanProduct> orderPlanProducts = null;
		if(StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(accountNo)){
			StringBuffer hql = new StringBuffer("" +
					" From" +
					" OrderPlanProduct p" +
					" WHERE" +
					" (p.account.accountNo=? OR p.accountNo=?)" +
					" AND" +
					" p.plan.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(accountNo);
			params.add(accountNo);
			params.add(planId);
			orderPlanProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return orderPlanProducts;
	}
	
	/**
	 * 获取 IFA AE账号
	 * @author wwluo
	 * @date 2016-12-26
	 */
	public String findAECode(String ifaId, String distributorId) {
		String aeCode = null;
		if(StringUtils.isNotBlank(ifaId) && StringUtils.isNotBlank(distributorId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" IfaDistributor i" +
					" WHERE" +
					" i.ifa.id=?" +
					" AND" +
					" i.distributor.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(ifaId);
			params.add(distributorId);
			List<IfaDistributor> distributors = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(distributors!=null && !distributors.isEmpty()){
				IfaDistributor distributor = distributors.get(0);
				aeCode = distributor.getAeCode();
			}
		}
		return aeCode;
	}

	/**
	 * 代理商产品库中产品的编码
	 * @author wwluo
	 * @date 2016-12-26
	 */
	private String findSymbolCode(String productId, String distributorId) {
		String symbolCode = null;
		if(StringUtils.isNotBlank(productId) && StringUtils.isNotBlank(distributorId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" ProductDistributor i" +
					" WHERE" +
					" i.product.id=?" +
					" AND" +
					" i.distributor.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(productId);
			params.add(distributorId);
			List<ProductDistributor> productDistributors = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(productDistributors!=null && !productDistributors.isEmpty()){
				ProductDistributor productDistributor = productDistributors.get(0);
				symbolCode = productDistributor.getSymbolCode();
			}
		}
		return symbolCode;
	}
	
	/**
	 * 获取转换分配比例
	 * @author wwluo
	 * @date 2017-02-07
	 */
	private Double getSwitchAllocRate(String planId, String switchGroup,Double amount) {
		Double switchAllocRate = null;
		if(StringUtils.isNotBlank(switchGroup) && amount != null){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" OrderPlanProduct p" +
					" WHERE" +
					" p.switchFlag=1" +
					" AND" +
					" p.tranType='S'" +
					" AND" +
					" p.switchGroup=?");
			List<Object> params = new ArrayList<Object>();
			params.add(switchGroup);
			if (StringUtils.isNotBlank(planId)) {
				hql.append(" AND p.plan.id=?");
				params.add(planId);
			}
			List<OrderPlanProduct> orderPlanProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
				OrderPlanProduct planProduct = orderPlanProducts.get(0);
				Double unit = planProduct.getUnit();
				Double lastNav = 0.00;
				StringBuffer fundHql = new StringBuffer("" +
						" FROM" +
						" FundInfo f" +
						" WHERE" +
						" f.product.id=?" +
						"");
				params.clear();
				params.add(planProduct.getProduct().getId());
				List<FundInfo> fundInfos = this.baseDao.find(fundHql.toString(), params.toArray(), false);
				if(fundInfos != null && !fundInfos.isEmpty()){
					lastNav = fundInfos.get(0).getLastNav();
				}
				switchAllocRate = amount/(unit*lastNav);
			}
		}
		return switchAllocRate;
	}
	
	/**
	 * 更新组合持仓帐户绑定表
	 * @author wwluo
	 * @date 2016-12-26
	 */
	private void updatePortfolioHoldAccount(String holdId,String memberId,String accountId,String accountNo) {
		if(StringUtils.isNotBlank(holdId) && StringUtils.isNotBlank(memberId)){
			StringBuffer hql = new StringBuffer("" +
					" UPDATE" +
					" PortfolioHoldAccount a" +
					" SET" +
					" a.portfolioHold.id=?" +
					" WHERE" +
					" a.member.id=?" +
					" ");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			params.add(memberId);
			if (StringUtils.isNotBlank(accountId)) {
				hql.append(" AND a.account.id=?");
				params.add(accountId);
				this.baseDao.updateHql(hql.toString(), params.toArray());
			}else if (StringUtils.isNotBlank(accountNo)) {
				hql.append(" AND a.accountNo=?");
				params.add(accountNo);
				this.baseDao.updateHql(hql.toString(), params.toArray());
			}
		}
	}
	
	/**
	 * 获取组合持仓帐号数据
	 * @author wwluo
	 * @date 2016-12-26
	 */
	private IfaDistributor findIfaDistributorByAeCode(String account ,String ifaId) {
		IfaDistributor ifaDistributor = null;
		if (StringUtils.isNotBlank(account) && StringUtils.isNotBlank(ifaId)) {
				StringBuffer hql = new StringBuffer("" +
						" FROM" +
						" IfaDistributor a" +
						" WHERE" +
						" a.isValid=1" +
						" AND" +
						" a.aeCode=?" +
						" AND" +
						" a.ifa.id=?" +
						" ORDER BY" +
						" a.lastUpdate" +
						" DESC");
				List<Object> params = new ArrayList<Object>();
				params.add(account);
				params.add(ifaId);
			List<IfaDistributor> distributors = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(distributors != null && !distributors.isEmpty()){
				ifaDistributor = distributors.get(0);
			}
		}
		return ifaDistributor;
	}
	
	/**
	 * 获取InvestorAccount
	 * @param distributorId
	 * @param memberId
	 * @return
	 */
	private List<InvestorAccount> findInvestorAccountsByDistributorMember(String distributorId,String memberId) {
		List<InvestorAccount> accounts = null;
		if (StringUtils.isNotBlank(distributorId) && StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" InvestorAccount i" +
					" WHERE" +
					" i.isValid=1" +
					" AND" +
					" i.member.id=?" +
					" AND" +
					" i.distributor.id=?" +
					//" AND" +
					//" i.subFlag<>1" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			params.add(distributorId);
			accounts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return accounts;
	}
	
	/**
	 * 撤单操作
	 * @param request
	 * @param loginUser
	 * @param historyId
	 * @param password
	 * @return
	 */
	public boolean deleteOrder(HttpServletRequest request,MemberBase memberBase,String historyId,String password) {
		boolean flag = false;
		OrderHistory orderHistory = (OrderHistory) this.baseDao.get(OrderHistory.class, historyId);
		if(orderHistory != null && StringUtils.isNotBlank(password)){
			password = Base64.decode(password, "UTF-8");
			orderHistory.setOrderType(CommonConstants.ORDER_TYPE_REVOKE);
			orderHistory = (OrderHistory)this.baseDao.update(orderHistory);
			OmsDelOrderVO delOrdePlan = new OmsDelOrderVO();
			delOrdePlan.setAccountNo(orderHistory.getAccount().getAccountNo());// 此订单关联的帐号
			delOrdePlan.setCreatorNo(orderHistory.getAccount().getAccountNo());// 下单人的accountNo
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberBase.getMemberType()){
				String aeCode = null;
				if(orderHistory.getIfa() != null && orderHistory.getAccount() != null && orderHistory.getAccount().getDistributor() != null){
					aeCode = this.findAECode(orderHistory.getIfa().getId(), orderHistory.getAccount().getDistributor().getId());
				}
				delOrdePlan.setAeCode(aeCode); // 此客户关系的ae code
				delOrdePlan.setCreatorNo(aeCode);// 下单人的accountNo
			}
			delOrdePlan.setOmsOrderNO(orderHistory.getItsOrderNumber());
			delOrdePlan.setOrderNO(orderHistory.getId());// 订单编号    我们系统生成的编号,等于order_history.id
			delOrdePlan.setTradingPassword(password);
			flag = iTraderSendService.deleteOrder(request, delOrdePlan);
		}
		return flag;
	}
	
	/**
	 * 获取一条交易历史记录
	 * @author wwluo
	 * @date 2017-02-07
	 * @param planId orderPlan id
	 * @param productId 产品id
	 * @param tranType 交易类型
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OrderHistory getOrderHistory(String planId, String productId, String tranType) {
		OrderHistory history = null;
		if(StringUtils.isNotBlank(planId) 
				&& StringUtils.isNotBlank(productId) 
				&& StringUtils.isNotBlank(tranType)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" OrderHistory h" +
					" WHERE" +
					" h.plan.id=?" +
					" AND" +
					" h.product.id=?" +
					" AND" +
					" h.orderType=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			params.add(productId);
			params.add(tranType);
			List<OrderHistory> orderHistorys = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderHistorys != null && !orderHistorys.isEmpty()){
				history = orderHistorys.get(0);
			}
		}
		return history;
	}

	/**
	 * 获取交易详情产品（交易中）
	 * @author wwluo
	 * @date 2017-02-15
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PlanProductVO> getHistoryProducts(String planId, String currencyCode,String langCode) {
		List<PlanProductVO> vos = null;
		if(StringUtils.isNotBlank(planId)){
			OrderPlan plan = (OrderPlan)this.baseDao.get(OrderPlan.class, planId);
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" OrderHistory h" +
					" WHERE" +
					" h.plan.id=?" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			List<OrderHistory> orderHistorys = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderHistorys != null && !orderHistorys.isEmpty()){
				vos = new ArrayList<PlanProductVO>();
				Double exChangeRate = null;
				for (OrderHistory orderHistory : orderHistorys) {
					PlanProductVO vo = new PlanProductVO();
					vo.setOrderHistoryId(orderHistory.getId());
					String fromCurrency = orderHistory.getTranFeeCur();
					String finishStatus = null; // order_plan 交易状态
					// 交易完成时
					OrderReturn orderReturn = null;
					if(plan != null && "5".equals(plan.getFinishStatus())){
						finishStatus = plan.getFinishStatus();
						orderReturn = this.getOrderReturnByHistoryId(orderHistory.getId());
						if(orderReturn != null){
							fromCurrency = orderReturn.getCurrencyCode();
						}
					}
					if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(orderHistory.getTranFeeCur())) {
						exChangeRate = this.getExchangeRate(fromCurrency,currencyCode);
					}
					if(exChangeRate == null){
						exChangeRate = 1.00;
						currencyCode = fromCurrency;
					}
					String currencyName = this.getParamConfigName(langCode,currencyCode,CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
					vo.setCurrencyCode(currencyCode);
					vo.setCurrencyName(currencyName);
					// 当交易完成时，Amount、Unit、TranFee另外处理
					if("5".equals(finishStatus) && orderReturn != null){
						if(orderReturn.getTransactionAmount() != null){
							vo.setAmount(orderReturn.getTransactionAmount()*exChangeRate);
						}
						vo.setUnit(orderReturn.getTransactionUnit());
						vo.setTranFee(orderReturn.getTransactionFee()*exChangeRate);
					}else{
						/*if(orderHistory.getCommissionAmount() != null){
							vo.setAmount(orderHistory.getCommissionAmount()*exChangeRate);
						}
						vo.setUnit(orderHistory.getCommissionUnit());*/
						vo.setTranRate(orderHistory.getTranRate());
					}
					if(orderHistory.getFee() != null){
						vo.setTranFee(orderHistory.getFee()*exChangeRate); // oms实际返回的交易费用
					}
					MemberDistributor distributor = null;
					if(orderHistory.getAccount() != null){
						distributor = orderHistory.getAccount().getDistributor();
						vo.setAccountId(orderHistory.getAccount().getId());
						vo.setRpqRiskLevel(orderHistory.getAccount().getRpqLevel());
					}
					String aeCode = null;
					if(distributor != null){
						vo.setDistributorId(distributor.getId());
						vo.setDistributorName(distributor.getCompanyName());
						if(orderHistory.getIfa() != null){
							aeCode = this.findAECode(orderHistory.getIfa().getId(),distributor.getId());
						}
					}
					vo.setAeCode(aeCode);
					vo.setAccountId(orderHistory.getAccount().getId());
					vo.setAccountNo(orderHistory.getAccountNo());
					String status = orderHistory.getStatus();
					vo.setOrderHistoryStatus(status);
					String statusDisplay = PropertyUtils.getPropertyValue(langCode, "order.plan.history.status.display."+status, null);
					vo.setStatusDisplay(statusDisplay);
					vo.setOrderType(orderHistory.getOrderType());
					ProductInfo product = orderHistory.getProduct();
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
						Double fundExChangeRate = this.getExchangeRate(fundCurrencyCode, currencyCode);
						if(fundExChangeRate == null){
							fundExChangeRate = 1.00;
						}
						vo.setLastNav(fundInfo.getLastNav()*fundExChangeRate);
					}
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * 获取一条OrderReturn信息
	 * @author wwluo
	 * @date 2017-02-15
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OrderReturn getOrderReturnByHistoryId(String historyId) {
		OrderReturn orderReturn = null;
		if(StringUtils.isNotBlank(historyId)){
			StringBuffer hql = new StringBuffer(" FROM OrderReturn r WHERE r.order.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(historyId);
			List<OrderReturn> orderReturns = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderReturns != null && !orderReturns.isEmpty()){
				orderReturn = orderReturns.get(0);
			}
		}
		return orderReturn;
	}
	
}
