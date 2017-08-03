/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.service.CrmCustomerService;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberCompany;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderAip;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.OrderPlanCheck;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebReadToDo;
import com.fsll.wmes.entity.WebTaskList;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.ifafirm.vo.IfaMyTeamListVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.trade.service.TradeStepService;
import com.fsll.wmes.trade.vo.OrderPlanProductVO;
import com.fsll.wmes.trade.vo.OrderPlanVO;
import com.fsll.wmes.trade.vo.PlanProductVO;
import com.fsll.wmes.trade.vo.TransactionVO;
import com.fsll.wmes.web.service.WebReadToDoService;
import com.fsll.wmes.web.service.WebTaskListService;

/**
 * 交易:分步管理业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Service("tradeStepService")
public class TradeStepServiceImpl  extends BaseService implements TradeStepService{
	@Autowired
    private FundInfoService fundInfoService;
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private WebReadToDoService webReadToDoService;
	@Autowired
	private CrmCustomerService crmCustomerService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private WebTaskListService webTaskListService;
	
	/**
	 * 获取 portfolio 产品持仓数据
	 * @author wwluo
	 * @data 2016-10-09
	 * @param 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<PortfolioHoldProduct> getPortfolioHoldProducts(String holdId) {
		List<PortfolioHoldProduct> holdProducts = null;
		if(StringUtils.isNotBlank(holdId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldProduct p" +
					" WHERE" +
					" p.portfolioHold.id=?" +
					" AND" +
					" p.product.type='fund'");
			List params = new ArrayList();
			params.add(holdId);
			holdProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return holdProducts;
	}
	
	/**
	 * 获取组合可用的账号
	 * @author wwluo
	 * @date 2016-01-07
	 * @param 
	 * @return
	 */
	@Override
	public List<InvestorAccountVO> findPortfolioHoldAccount(MemberBase member,MemberIfa memberIfa,String langCode,String toCurrency,String holdId) {
		List<InvestorAccountVO> accounts = null;
		if(member != null){
			//获得没有被组合占用或者空仓状态下的帐户
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldAccount a" +
					" WHERE" +
					" a.isValid=1" +
					" AND" +
					" a.member=?" +
					" AND" +
					" (a.portfolioHold IS NULL" +
					" OR" +
					" NOT EXISTS" +
					" (FROM" +
					" PortfolioHoldProduct p" +
					" WHERE" +
					" p.portfolioHold.id=a.portfolioHold.id" +
					"");
			if (StringUtils.isNotBlank(holdId)) {
				hql.append(" AND p.portfolioHold.id<>'"+holdId+"'");			
			}
			hql.append(" ))");
			List<Object> params = new ArrayList<Object>();
			params.add(member);
			if(memberIfa != null){
				hql.append(" AND a.ifa=?");
				params.add(memberIfa);
			}
			List<PortfolioHoldAccount> portfolioHoldAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
			accounts = new ArrayList<InvestorAccountVO>();
			if(portfolioHoldAccounts != null && !portfolioHoldAccounts.isEmpty()){
				for (PortfolioHoldAccount portfolioHoldAccount : portfolioHoldAccounts) {
					InvestorAccountVO target = new InvestorAccountVO();
					BeanUtils.copyProperties(portfolioHoldAccount, target);
					if(portfolioHoldAccount.getPortfolioHold() != null){
						target.setHoldId(portfolioHoldAccount.getPortfolioHold().getId());
					}
					InvestorAccount account = portfolioHoldAccount.getAccount();
					if(account != null){
						target.setId(account.getId());
						MemberDistributor distributor = account.getDistributor();
						target.setDistributorIconUrl(PageHelper.getLogoUrl(distributor.getLogofile(), "D"));
						target.setDistributorName(distributor.getCompanyName());
						target.setDistributorId(distributor.getId());
						target.setAccountId(account.getId());
						target.setAccountNo(account.getAccountNo());
						target.setAuthorized(account.getAuthorized());
						target.setSubFlag(account.getSubFlag());
						target.setSubFlagDisplay(PropertyUtils.getPropertyValue(langCode, "order.plan.rebalance.table.inv.account.subFlag."+account.getSubFlag(), null));
						target.setTotalFlag(account.getTotalFlag());
						target.setTotalFlagDisplay(PropertyUtils.getPropertyValue(langCode, "order.plan.commission.account.total.flag."+account.getTotalFlag(), null));
						if(account.getIfa() != null){
							target.setIfaId(account.getIfa().getId());
						}
					}
					Double exChangeRate = null;
					if(StringUtils.isNotBlank(portfolioHoldAccount.getBaseCurrency()) && StringUtils.isNotBlank(toCurrency)){
						exChangeRate = this.getExchangeRate(portfolioHoldAccount.getBaseCurrency(), toCurrency);
					}
					if(exChangeRate == null){
						exChangeRate = 1.00;
					}
					String currencyName = this.getParamConfigName(langCode, toCurrency, CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
					target.setBaseCurrency(toCurrency);
					target.setCurrency(currencyName);
					target.setRpqRiskLevel(account.getRpqLevel());
					Double cashAvailable = null;
					if(portfolioHoldAccount.getCashAvailable() != null){
						cashAvailable = portfolioHoldAccount.getCashAvailable()*exChangeRate;
					}
					Double cashHold = null;
					if(portfolioHoldAccount.getCashHold() != null){
						cashHold = portfolioHoldAccount.getCashHold()*exChangeRate;
					}
					Double totalCash = 0.00;
					if(cashAvailable != null){
						target.setCashAvailable(cashAvailable.toString());
						totalCash += cashAvailable;
					}
					if(cashHold != null){
						totalCash += cashHold;
					}
					target.setTotalCash(totalCash.toString());
					if(portfolioHoldAccount.getMarketValue() != null){
						target.setMarketValue(portfolioHoldAccount.getMarketValue());
					}
					accounts.add(target);
				}
			}
		}
		return accounts;
	}		
	
	/**
	 * 获取某个会员的所有可用现金总金额
	 * @author wwluo
	 * @date 2017-02-07
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Double getCashAvailableTotal(String memberId,String ifaId, String defCurrency,String holdId) {
		Double cashAvailableTotal = 0.00;
		if(StringUtils.isNotBlank(memberId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldAccount a" +
					" WHERE" +
					" a.isValid=1" +
					" AND" +
					" a.member.id=?" +
					" AND" +
					" (a.portfolioHold IS NULL" +
					" OR" +
					" NOT EXISTS" +
					" (FROM" +
					" PortfolioHoldProduct p" +
					" WHERE" +
					" p.portfolioHold.id=a.portfolioHold.id" +
					"");
			if (StringUtils.isNotBlank(holdId)) {
				hql.append(" AND p.portfolioHold.id<>'"+holdId+"'");			
			}
			hql.append(" )" +
			" )" +
			"");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			if (StringUtils.isNotBlank(ifaId)) {
				hql.append(" AND a.ifa.id=?");
				params.add(ifaId);
			}
			List<PortfolioHoldAccount> portfolioHoldAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(portfolioHoldAccounts != null && !portfolioHoldAccounts.isEmpty()){
				for (PortfolioHoldAccount portfolioHoldAccount : portfolioHoldAccounts) {
					if(portfolioHoldAccount.getCashAvailable() != null){
						Double rate = null;
						if (StringUtils.isNotBlank(defCurrency) && StringUtils.isNotBlank(portfolioHoldAccount.getBaseCurrency())) {
							rate = this.getExchangeRate(portfolioHoldAccount.getBaseCurrency(), defCurrency);
						}
						if(rate == null){
							rate = 1.00;
						}
						cashAvailableTotal = cashAvailableTotal + portfolioHoldAccount.getCashAvailable()*rate;
					}
				}
			}
		}
		return cashAvailableTotal;
	}
	
	/**
	 * 获取交易计划的产品列表
	 * @author mqzou
	 * @data 2016-10-14
	 * @param planId
	 * @return
	 */
	@Override
	public List<OrderPlanProductVO> getOrderPlanProducts(String planId,String langCode) {
		String hql=" from OrderPlanProduct r left join FundInfo f on r.product.id=f.product.id left join "+this.getLangString("FundInfo",langCode)+" ";
		hql+=" l on f.id=l.id left join RpqExam e on r.plan.portfolioHold.member.id=e.member.id  left join ProductDistributor d ";
		hql+=" on f.product.id=d.product.id and e.distributor.id=d.distributor.id  where r.plan.id=?" +
				//" and r.original<>1" +
				"";
		hql+=" order by e.createTime desc";
		List params=new ArrayList();
		params.add(planId);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		List<OrderPlanProductVO> list=new ArrayList<OrderPlanProductVO>();
		List<OrderPlanProduct> productList=new ArrayList<OrderPlanProduct>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				Object[] obj=(Object[])it.next();
				OrderPlanProductVO vo=new OrderPlanProductVO();
				OrderPlanProduct product = (OrderPlanProduct) obj[0];
				if(productList.contains(product)){
					continue;
				}
				productList.add(product);
				FundInfo fundInfo=(FundInfo)obj[1];
				RpqExam rpq=(RpqExam)obj[3];
				InvestorAccount account=product.getAccount();
				vo.setAccountId(null!=account?account.getId():"");
				vo.setAccountNo(null!=account?account.getAccountNo():product.getAccountNo());
				String totalFlag=null!=account?account.getTotalFlag():"";
				vo.setIncludeFee("1".equals(totalFlag)?"1":"0");
				vo.setAmount(product.getAmount());
				if(product.getAmount() != null){
					vo.setAmountStr(StrUtils.getNumberString(product.getAmount()));
				}
				vo.setAmountAdjust(product.getAmountAdjust());
				if(product.getAmountAdjust() != null){
					vo.setAmountAdjustStr(StrUtils.getNumberString(product.getAmountAdjust()));
				}
				String dividend="";
				if("R".equals(product.getDividend())){
					dividend="Relnv.";
				}else{
					dividend="Cash Acc";
				}
				vo.setDividend(dividend);
			    vo.setId(product.getId());
			    vo.setLatestNAVPrice(null!=fundInfo.getLastNav()?fundInfo.getLastNav():0);
			    vo.setLatestNAVPriceStr(StrUtils.getNumberString(vo.getLatestNAVPrice()));
			    vo.setMinSubscribeAmount(null!=fundInfo.getMinSubscribeAmount()?fundInfo.getMinSubscribeAmount():0);
			    vo.setMinSubscribeAmountStr(StrUtils.getNumberString(vo.getMinSubscribeAmount()));
			    vo.setOriginal(product.getOriginal());
			    vo.setPlanId(product.getPlan().getId());
			    vo.setProductId(fundInfo.getId());
			   
			    vo.setSubscribeFee(null!=fundInfo.getSubscribeFee()?fundInfo.getSubscribeFee():0);
			    vo.setSwitchFlag(product.getSwitchFlag());
			    vo.setTranFee(product.getTranFee());
			    vo.setCurrency(product.getTranFeeCur());
			    vo.setCurrencyName(product.getTranFeeCur());
			    vo.setTranFee(product.getTranFee());
			    vo.setTranType(product.getTranType());
			    if(product.getUnit() != null){
			    	 vo.setUnit(product.getUnit());
			    }
			    if(product.getUnit() != null){
			    	vo.setUnitStr(StrUtils.getNumberString(product.getUnit()));
			    }
			    vo.setUnitAdjust(product.getUnitAdjust());
			    if(product.getUnitAdjust() != null){
			    	vo.setUnitAdjustStr(StrUtils.getNumberString(product.getUnitAdjust()));
			    }
			    vo.setWeight(product.getWeight());
				
				if(CommonConstants.LANG_CODE_EN.equals(langCode)){
					FundInfoEn fundInfoEn=(FundInfoEn)obj[2];
					vo.setCur(fundInfoEn.getFundCurrencyCode());
					vo.setProductName(fundInfoEn.getFundName());
					vo.setProductType(fundInfoEn.getFundType());
				}else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					FundInfoSc fundInfoSc=(FundInfoSc)obj[2];
					vo.setCur(fundInfoSc.getFundCurrencyCode());
					vo.setProductName(fundInfoSc.getFundName());
					vo.setProductType(fundInfoSc.getFundType());
				}else {
					FundInfoTc fundInfoTc=(FundInfoTc)obj[2];
					vo.setCur( fundInfoTc.getFundCurrencyCode());
					vo.setProductName(fundInfoTc.getFundName());
					vo.setProductType(fundInfoTc.getFundType());
				}
				int riskLevel=null!=fundInfo.getRiskLevel()?fundInfo.getRiskLevel():0;
				int risk = 0;
				if(rpq!=null){
					risk=null!=rpq.getRiskLevel()?rpq.getRiskLevel():0;
				}
				if(riskLevel>risk)
					vo.setWarning("1");
				vo.setRiskLevel(String.valueOf(riskLevel));
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 获取交易产品
	 * @author wwluo
	 * @date 2017-02-07
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PlanProductVO> getOrderPlanProducts(String planId,String currencyCode, String langCode) {
		List<PlanProductVO> vos = null;
		if(StringUtils.isNotBlank(planId)){
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " p,i.fundName,f.riskLevel,i.fundType,i.fundCurrencyCode,f.lastNav,f.id,f.minSubscribeAmount,f.minRedemptionAmount"
					+ " FROM"
					+ " OrderPlanProduct p"
					+ " LEFT JOIN"
					+ " FundInfo f"
					+ " ON"
					+ " p.product.id=f.product.id"
					+ " LEFT JOIN"
					+ " FundInfo" + this.getLangFirstCharUpper(langCode) + " i"
					+ " ON"
					+ " f.id=i.id"
					+ " WHERE"
					+ " p.status=?"  // 新增字段,获取提交订单产品 ,1：已提交OMS,0：未提交OMS
					+ " AND"
					+ " p.plan.id=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add("0");
			params.add(planId);
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objs != null && !objs.isEmpty()){
				vos = new ArrayList<PlanProductVO>();
				for (Object[] objects : objs) {
					PlanProductVO vo = new PlanProductVO();
					OrderPlanProduct orderPlanProduct = (OrderPlanProduct) objects[0];
					String fundName = (String) objects[1];
					Integer riskLevel = (Integer) objects[2];
					String fundType = (String) objects[3];
					String fundCurrencyCode = (String) objects[4];
					Double lastNav = (Double) objects[5];
					String fundId = (String) objects[6];
					Double minSubscribeAmount = (Double) objects[7];
					Double minRedemptionAmount = (Double) objects[8];
					vo.setSwitchFlag(orderPlanProduct.getSwitchFlag());
					vo.setSwitchGroup(orderPlanProduct.getSwitchGroup());
					vo.setFundInfoId(fundId);
					vo.setPlanProductId(orderPlanProduct.getId());
					vo.setTranType(orderPlanProduct.getTranType());
					vo.setFundName(fundName);
					vo.setRiskLevel(riskLevel);
					vo.setFundType(fundType);
					vo.setDividend(orderPlanProduct.getDividend());
					if(orderPlanProduct.getAccount() != null){
						vo.setAccountId(orderPlanProduct.getAccount().getId());
						vo.setTotalFlag(orderPlanProduct.getAccount().getTotalFlag());
						vo.setTotalFlagDisplay(PropertyUtils.getPropertyValue(langCode, "order.plan.commission.account.total.flag."+orderPlanProduct.getAccount().getTotalFlag(), null));
						vo.setAuthorized(orderPlanProduct.getAccount().getAuthorized());
						if(orderPlanProduct.getAccount().getIfa() != null){
							vo.setAccountIfaId(orderPlanProduct.getAccount().getIfa().getId());
						}
						vo.setRpqRiskLevel(orderPlanProduct.getAccount().getRpqLevel());
					}
					vo.setAccountNo(orderPlanProduct.getAccountNo());
					vo.setFundCurrencyCode(fundCurrencyCode);
					String fundCurrencyName = this.getParamConfigName(langCode, fundCurrencyCode, CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY);
					vo.setFundCurrencyName(fundCurrencyName);
					Double fundCurrencyRate = null;  // 与exchangeRate不同
					if(fundCurrencyCode != null){
						fundCurrencyRate = this.getExchangeRate(fundCurrencyCode, currencyCode);
					}
					if(fundCurrencyRate == null){
						fundCurrencyRate = 1.00;
					}
					if(lastNav != null){
						vo.setLastNav(lastNav*fundCurrencyRate);
					}
					vo.setFundInfoId(fundId);
					String feeCur = orderPlanProduct.getTranFeeCur();
					Double exchangeRate = null;
					if (StringUtils.isNotBlank(feeCur) && StringUtils.isNotBlank(currencyCode)) {
						exchangeRate = this.getExchangeRate(feeCur, currencyCode);
					}
					if(exchangeRate == null){
						exchangeRate = 1.00;
					}
					if(orderPlanProduct.getAmount() != null){
						vo.setAmount(orderPlanProduct.getAmount()*exchangeRate);	
					}
					if(orderPlanProduct.getFromProduct() != null){
						vo.setFromProductId(orderPlanProduct.getFromProduct().getId());
						FundInfo fundInfos = this.getFundInfoByProduct(orderPlanProduct.getFromProduct().getId());
						if(fundInfos != null){
							vo.setFromFundInfoId(fundInfos.getId());
							if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType()) && orderPlanProduct.getAmount() != null){
								vo.setUnitRedeem(orderPlanProduct.getAmount()*exchangeRate/fundInfos.getLastNav()*fundCurrencyRate);
							}
						}
					}
					vo.setUnit(orderPlanProduct.getUnit());
					vo.setUnitAdjust(orderPlanProduct.getUnitAdjust());
					if(orderPlanProduct.getWeight() != null){
						vo.setWeight(orderPlanProduct.getWeight() * 100);
					}
					if(orderPlanProduct.getWeightAdjust() != null){
						vo.setWeightAdjust(orderPlanProduct.getWeightAdjust() * 100);
					}else{
						vo.setWeightAdjust(orderPlanProduct.getWeight() * 100);
					}
					if(orderPlanProduct.getProduct() != null){
						vo.setProductId(orderPlanProduct.getProduct().getId());
					}
					Double tranFee = orderPlanProduct.getTranFee();
					Double feeMini = orderPlanProduct.getTranFeeMini();
					Double tranRate = orderPlanProduct.getTranRate();
					vo.setTranFeeCur(feeCur);
					if(tranFee == null && tranRate != null && minSubscribeAmount != null){
						tranFee = tranRate*minSubscribeAmount/100;
					}
					if(feeMini != null && (tranFee == null || tranFee < feeMini)){  // 交易费用不能小于最小费用
						tranFee = feeMini;
					}
					if (StringUtils.isNotBlank(feeCur) && exchangeRate != null){
						tranFee = tranFee * exchangeRate;
						feeMini = feeMini * exchangeRate;
					}
					vo.setTranRate(tranRate);
					vo.setTranFee(tranFee);
					vo.setTranFeeMini(feeMini);
					vo.setMinSubscribeAmount(minSubscribeAmount);
					vo.setMinRedemptionAmount(minRedemptionAmount);
					OrderPlan plan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
					if(plan != null){
						ProductDistributor productDistributor = getProductDistributorByMemberIdAndProductId(plan.getPortfolioHold().getMember().getId(),orderPlanProduct.getProduct().getId());
						if(productDistributor != null){
							vo.setTradable(productDistributor.getTradable());
							vo.setStatus(productDistributor.getStatus());
						}
					}
					vo.setCurrencyName(sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE, currencyCode, langCode));
					vos.add(vo);
				}
			}
		}
		return vos;
	}
	
	/**
	 * 通过产品id获得产品对应的基金信息
	 * @param productId
	 * @return
	 */
	private FundInfo getFundInfoByProduct(String productId) {
		FundInfo fundInfo = null;
		if (StringUtils.isNotBlank(productId)) {
			StringBuffer fundHql = new StringBuffer("" +
					" FROM" +
					" FundInfo f" +
					" WHERE" +
					" f.product.id=?" +
					"");
			List<Object> params = new ArrayList<Object>(); 
			params.add(productId);
			List<FundInfo> fundInfos = this.baseDao.find(fundHql.toString(), params.toArray(), false);
			if(fundInfos != null && !fundInfos.isEmpty()){
				fundInfo = fundInfos.get(0);
			}
		}
		return fundInfo;
	}
	
	/**
	 * 保存交易订单
	 * @author wwluo
	 * @data 2016-10-17
	 * @param planId
	 * @param holdId
	 * @param currencyCode
	 * @param portfolioName
	 * @param orderPlanProducts
	 * @return true成功,false失败
	 */
	public String saveOrderPlan(MemberBase memberBase,String planId,String holdId,String currencyCode,String portfolioName,String orderPlanProducts){
		PortfolioHold hold = null;
		OrderPlan orderPlan = null;
		if (StringUtils.isBlank(planId) && StringUtils.isNotBlank(holdId)) {
			hold = (PortfolioHold)baseDao.get(OrderPlan.class,holdId);
			if(hold != null){
				orderPlan = new OrderPlan();
				orderPlan.setBaseCurrency(hold.getBaseCurrency());
				orderPlan.setCreateTime(new Date());
				orderPlan.setCreator(memberBase);
				orderPlan.setFinishStatus("0");
				orderPlan.setLastUpdate(new Date()); 
				orderPlan.setPortfolioHold(hold);
				/*
				orderPlan.setTotalBuy(totalBuy);
				orderPlan.setTotalSell(totalSell);
				orderPlan.setAuthorized(authorized);
				orderPlan.setAipFlag(aipFlag);
				orderPlan.setFinishStatus(finishStatus);
				orderPlan.setCheckStatus(checkStatus);
				orderPlan.setSubmitDate(submitDate);
				*/
				orderPlan.setIsValid("1");
				orderPlan = (OrderPlan)baseDao.create(orderPlan);
				planId = orderPlan.getId();
			}
		}
		// investor 下单
		if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberBase.getMemberType() && StringUtils.isNotBlank(planId)){
		     //investor 再次编辑订单
			 orderPlan = (OrderPlan)this.baseDao.get(OrderPlan.class, planId);
			 hold = orderPlan.getPortfolioHold();
			 if(orderPlan != null && orderPlan.getCreator() != null && memberBase.getId().equals(orderPlan.getCreator().getId())
					&& !"1".equals(orderPlan.getFinishStatus()) // 审批中
					&& !"3".equals(orderPlan.getFinishStatus()) // 审批通过
					&& !"4".equals(orderPlan.getFinishStatus()) // 交易中
					&& !"5".equals(orderPlan.getFinishStatus()) // 交易完成
					){
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = orderPlan.getBaseCurrency();
				}
				hold.setLastUpdate(new Date());
				hold.setPortfolioName(portfolioName);
				hold = (PortfolioHold)this.baseDao.update(hold);
				saveOrderPlanProduct(orderPlan, orderPlanProducts, null, currencyCode);
				orderPlan.setFinishStatus("0"); // 状态修改 ,0:草稿
				orderPlan.setLastUpdate(new Date());
				orderPlan = (OrderPlan)this.baseDao.update(orderPlan);
				planId = orderPlan.getId();
			}
		}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberBase.getMemberType() ){
		    //investor 新建订单
			hold = new PortfolioHold();
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = memberBase.getDefCurrency();
				if (StringUtils.isBlank(currencyCode)) {
					currencyCode = CommonConstants.DEF_CURRENCY;
				}
			}
			hold.setBaseCurrency(currencyCode);
			hold.setCreateTime(new Date());
			hold.setIfFirst("1");
			hold.setLastUpdate(new Date());
			hold.setMember(memberBase);
			hold.setPortfolioName(portfolioName);
			/*
			hold.setPortfolio(portfolio);
			hold.setIfa(ifa);
			hold.setTotalMarketValue(totalMarketValue);
			hold.setTotalReturnRate(totalReturnRate);
			hold.setTotalReturnValue(totalReturnValue);
			hold.setTotalAsset(totalAsset);
			hold.setTotalCash(totalCash);
			hold.setRiskLevel(riskLevel);
			*/
			hold = (PortfolioHold)baseDao.create(hold);
			orderPlan = new OrderPlan();
			orderPlan.setBaseCurrency(currencyCode);
			orderPlan.setCreateTime(new Date());
			orderPlan.setCreator(memberBase);
			orderPlan.setFinishStatus("0");
			orderPlan.setLastUpdate(new Date()); 
			orderPlan.setPortfolioHold(hold);
			orderPlan.setIsValid("1");
			/*
			orderPlan.setTotalBuy(totalBuy);
			orderPlan.setTotalSell(totalSell);
			orderPlan.setAuthorized(authorized);
			orderPlan.setAipFlag(aipFlag);
			orderPlan.setFinishStatus(finishStatus);
			orderPlan.setCheckStatus(checkStatus);
			orderPlan.setSubmitDate(submitDate);
			*/
			orderPlan = (OrderPlan)baseDao.create(orderPlan);
			saveOrderPlanProduct(orderPlan, orderPlanProducts, null, currencyCode);
			orderPlan.setFinishStatus("0"); // 状态修改 ,0:草稿
			orderPlan.setLastUpdate(new Date());
			orderPlan = (OrderPlan)this.baseDao.update(orderPlan);
			planId = orderPlan.getId();
		}else{
		// ifa 下单
			if (StringUtils.isNotBlank(planId)) {
				orderPlan = (OrderPlan)this.baseDao.get(OrderPlan.class, planId);
			}
			if(orderPlan != null
					&& memberBase.getId().equals(orderPlan.getCreator().getId())
					&& !"1".equals(orderPlan.getFinishStatus()) // 审批中
					&& !"3".equals(orderPlan.getFinishStatus()) // 审批通过
					&& !"4".equals(orderPlan.getFinishStatus()) // 交易中
					&& !"5".equals(orderPlan.getFinishStatus()) // 交易完成
					){
				saveOrderPlanProduct(orderPlan, orderPlanProducts, null, currencyCode);
				orderPlan.setFinishStatus("0");// 状态修改 ,0:草稿
				orderPlan.setLastUpdate(new Date());
				orderPlan = (OrderPlan)this.baseDao.update(orderPlan);
				planId = orderPlan.getId();
			}
		}
		return planId;
	}
	
	
	/**
	 * 保存交易订单
	 * @author wwluo
	 * @data 2016-10-17
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	private OrderPlanVO saveOrderPlanProduct(OrderPlan orderPlan, String orderPlanProducts,String orderPlanAip,String currencyCode){
		OrderPlanVO vo = new OrderPlanVO();
		if(orderPlan != null){
			PortfolioHold hold = orderPlan.getPortfolioHold();
			//保存 OrderPlan
			List<Map> orderPlanProductList = JsonUtil.toListMap(orderPlanProducts);
			if(orderPlanProductList != null && !orderPlanProductList.isEmpty()){
				//0.获得用户所在的companyId
				String hql = "FROM MemberCompany r where r.member.id='"+hold.getMember().getId()+"'";
				List<MemberCompany> mcList = this.baseDao.find(hql.toString(),null, false);
				String companyId = "";
				if(mcList != null && !mcList.isEmpty()){
					MemberCompany memberCompany = mcList.get(0);
					companyId = memberCompany.getCompany().getId();
				}
				//1.清空原产品
				hql = " DELETE FROM OrderPlanProduct p WHERE p.plan.id='"+orderPlan.getId()+"'";
				this.baseDao.updateHql(hql.toString(),null);
				List<OrderPlanProduct> planProducts = new ArrayList<OrderPlanProduct>();
				//判断是否自动授权标识
				boolean authorizedFlag = false;
				Double totalBuy = 0.00; 
				Double totalSell = 0.00;
				Map<String,String> groupMap = new TreeMap<String, String>(); 
				for (Map map : orderPlanProductList) {
					Object fundId = map.get("fundId");
					Object amount = map.get("amount");
					Object fromProductId = map.get("fromProductId");
					Object original = map.get("original");
					Object switchFlag = map.get("switchFlag");
					Object switchGroup = map.get("switchGroup");
					Object tranType = map.get("tranType");
					Object unit = map.get("unit");
					Object weight = map.get("weight");
					Object weightAdjust = map.get("weightAdjust");
					Object amountAdjust = map.get("amountAdjust");
					Object unitAdjust = map.get("unitAdjust");
					Object accountId = map.get("accountId");
					Object dividend = map.get("dividend");
					OrderPlanProduct orderPlanProduct = new OrderPlanProduct();
					orderPlanProduct.setPlan(orderPlan);
					Double amountValue = 0.00;
					String feeCur = null;
					if(null != fromProductId && StringUtils.isNotBlank(fromProductId.toString())){
						FundInfo fromFundInfo = (FundInfo) baseDao.get(FundInfo.class, fromProductId.toString());
						if(fromFundInfo != null){
							ProductInfo productInfo = (ProductInfo) this.baseDao.get(ProductInfo.class, fromFundInfo.getProduct().getId());
							orderPlanProduct.setFromProduct(productInfo);
							ProductDistributor distributor = this.getProductDistributorByProduct(productInfo.getId(),companyId);
							if(distributor != null){
								// 交易费用信息
								Double feeRate = distributor.getTransactionFeeRate();
								Double feeMini = distributor.getTransactionFeeMini();
								feeCur = distributor.getTransactionFeeCur();
								orderPlanProduct.setTranRate(feeRate);
								orderPlanProduct.setTranFeeMini(feeMini);
								orderPlanProduct.setTranFeeCur(feeCur);
								Double tranFee = feeMini;
								if(distributor.getTransactionFeeRate() != null && amountValue != null){
									tranFee = distributor.getTransactionFeeRate()*amountValue;
								}
								orderPlanProduct.setTranFee(tranFee);
							}
						}
					}
					Double exChangeRate = null;
					if(StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(feeCur)){
						WebExchangeRate rate = fundInfoService.findExchangeRate(currencyCode, feeCur);
						if(rate != null){
							exChangeRate = rate.getRate();
						}
					}
					if(exChangeRate == null){
						exChangeRate = 1.00;
					}
					if(null != amount && StringUtils.isNotBlank(amount.toString())){
						amountValue = Double.parseDouble(amount.toString());
						orderPlanProduct.setAmount(amountValue * exChangeRate);
					}
					FundInfo fundInfo = (FundInfo) baseDao.get(FundInfo.class,fundId.toString());
					Double lastNav = 0.00;
					String fundCurrencyCode = null;
					if(null != fundId && StringUtils.isNotBlank(fundId.toString()) && fundInfo != null){
						orderPlanProduct.setProduct(fundInfo.getProduct());
						lastNav = fundInfo.getLastNav();
						FundInfoEn fundInfoEn = (FundInfoEn) baseDao.get(FundInfoEn.class,fundId.toString());
						fundCurrencyCode = fundInfoEn.getFundCurrencyCode();
					}
					if(null != tranType && StringUtils.isNotBlank(tranType.toString())){
						orderPlanProduct.setTranType(tranType.toString());
						if(fundInfo != null){
							PortfolioHoldProduct holdProduct = this.getHoldProduct(hold.getId(),fundInfo.getProduct().getId());
							if(holdProduct != null){
								orderPlanProduct.setAccount(holdProduct.getAccount());
								orderPlanProduct.setAccountNo(holdProduct.getAccountNo());
							}
						}
					}
					InvestorAccount account = null;
					if(null != accountId && StringUtils.isNotBlank(accountId.toString())){
						account = (InvestorAccount) baseDao.get(InvestorAccount.class,accountId.toString());
						orderPlanProduct.setAccount(account);
						// 判断是否自动授权
						if(account == null || !"1".equals(account.getAuthorized())){
							authorizedFlag = true;
						}
					}else{
						authorizedFlag = true;
					}
					if(null != switchFlag && StringUtils.isNotBlank(switchFlag.toString())){
						orderPlanProduct.setSwitchFlag(switchFlag.toString());
						if(null != switchGroup && StringUtils.isNotBlank(switchGroup.toString())){
							String groupStr = switchGroup.toString();
							if(!groupMap.containsKey(groupStr)){
								groupMap.put(groupStr, groupStr);
								//转换分组编码校验
								boolean hasGroup = false;
								hasGroup = this.validSwitchGroup(groupStr);
								String groupNew = null;
								while (hasGroup){
									groupNew = StrUtils.getRandomNum();
									groupMap.put(groupStr, groupNew);
									hasGroup = this.validSwitchGroup(groupNew);
								}
							}
							orderPlanProduct.setSwitchGroup(groupMap.get(groupStr));
						}
					}
					Double unitValue = 0.00;
					if(null != unit && StringUtils.isNotBlank(unit.toString())){
						unitValue = Double.parseDouble(unit.toString());
						orderPlanProduct.setUnit(unitValue);
					}
					if(null != weight && StringUtils.isNotBlank(weight.toString())){
						orderPlanProduct.setWeight(Double.parseDouble(weight.toString())/100);
					}
					if(null != weightAdjust && StringUtils.isNotBlank(weightAdjust.toString()) ){
						orderPlanProduct.setWeightAdjust(Double.parseDouble(weightAdjust.toString())/100);
					}
					if(null != amountAdjust && StringUtils.isNotBlank(amountAdjust.toString()) ){
						orderPlanProduct.setAmountAdjust((Double.parseDouble(amountAdjust.toString()))*exChangeRate);
					}
					if(null != unitAdjust && StringUtils.isNotBlank(unitAdjust.toString()) ){
						orderPlanProduct.setUnitAdjust(Double.parseDouble(unitAdjust.toString()));
					}
					if(null != original && StringUtils.isNotBlank(original.toString())){
						orderPlanProduct.setOriginal(Integer.parseInt(original.toString()));
				    }
					if(null != dividend && StringUtils.isNotBlank(dividend.toString())){
						orderPlanProduct.setDividend(dividend.toString());
					}
					// 交易总额累加
					if(CommonConstants.TRAN_TYPE_SELL.equals(tranType)){
						WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(fundCurrencyCode, orderPlan.getBaseCurrency());
						if(exchangeRate != null){
							lastNav = lastNav * exchangeRate.getRate();
						}
						totalSell = totalSell + lastNav * unitValue;
					}else if(CommonConstants.TRAN_TYPE_BUY.equals(tranType)){
						totalBuy = totalBuy + amountValue * exChangeRate;
					}
					orderPlanProduct.setStatus("0");
					orderPlanProduct = (OrderPlanProduct) this.baseDao.create(orderPlanProduct);
					planProducts.add(orderPlanProduct);
				}
				orderPlan.setTotalSell(totalSell);
				orderPlan.setTotalBuy(totalBuy);
				//是否自动授权
				if(authorizedFlag){
					orderPlan.setAuthorized("0");
				}else{
					orderPlan.setAuthorized("1");
				}
				this.baseDao.update(orderPlan);
				vo.setOrderPlanProducts(planProducts);
			}
		}
		return vo;
	}
	
	/**
	 * 获取一条持仓产品数据
	 * @author wwluo
	 * @date 2017-02-07
	 */
	private PortfolioHoldProduct getHoldProduct(String holdId, String productId) {
		PortfolioHoldProduct holdProduct = null;
		if(StringUtils.isNotBlank(holdId) && StringUtils.isNotBlank(productId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldProduct p" +
					" WHERE" +
					" p.portfolioHold.id=?" +
					" AND" +
					" p.product.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			params.add(productId);
			List<PortfolioHoldProduct> holdProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(holdProducts != null && !holdProducts.isEmpty()){
				holdProduct = holdProducts.get(0);
			}
		}
		return holdProduct;
	}
	
	/**
	 * 获取产品与代理商的对应表
	 * @author wwluo
	 * @date 2017-01-18
	 */
	private ProductDistributor getProductDistributorByProduct(String productId,String companyId) {
		ProductDistributor distributor = null;
		//一个产品可能有多个代码在代理
		if(StringUtils.isNotBlank(productId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" ProductDistributor d" +
					" WHERE" +
					" d.product.id=? and " +
					" EXISTS" +
					" (" +
					"  FROM ProductCompany p WHERE p.product.id=d.product.id and p.company.id = ? " +
					"  ) ");
			List<Object> params = new ArrayList<Object>();
			params.add(productId);
			params.add(companyId);
			List<ProductDistributor> distributors = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(distributors != null && !distributors.isEmpty()){
				distributor = distributors.get(0);
			}
		}
		return distributor;
	}
	
	/**
	 * 基金转换分组编码校验
	 * @author wwluo
	 * @date 2017-02-07
	 */
	private boolean validSwitchGroup(String groupStr) {
		boolean flag = false;
		if(StringUtils.isNotBlank(groupStr)){
			StringBuffer hql = new StringBuffer(" FROM OrderPlanProduct WHERE switchGroup=?");
			List<Object> params = new ArrayList<Object>();
			params.add(groupStr);
			List<OrderPlanProduct> orderPlanProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 保存交易计划股权选项信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@SuppressWarnings({ "unchecked"})
	@Override
	public void updateOrderPlanProduct(String planId,String subscriptionDatas,String currencyCode) {
		if(StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(subscriptionDatas)){
			OrderPlan plan = (OrderPlan) this.baseDao.get(OrderPlan.class, planId);
			if(plan != null){
				List<Map> subscriptionMaps = JsonUtil.toListMap(subscriptionDatas);
				for (Map map : subscriptionMaps) {
					String planProduct = (String) map.get("planProduct");
					String investorId = (String) map.get("investorId");
					String dividend = (String) map.get("dividend");
					String tranRate = (String) map.get("tranRate");
					String amountStr = (String) map.get("amount");
					String weightAdjustStr = (String) map.get("weightAdjust");
					InvestorAccount investorAccount = (InvestorAccount) this.baseDao.get(InvestorAccount.class, investorId);
					StringBuffer hql = new StringBuffer("" +
							" FROM" +
							" OrderPlanProduct p" +
							" WHERE" +
							" p.id =?" +
							"");
					List<Object> params = new ArrayList<Object>();
					params.add(planProduct);
					List<OrderPlanProduct> orderPlanProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
					if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
						OrderPlanProduct orderPlanProduct = orderPlanProducts.get(0);
						String tranFeeCur = orderPlanProduct.getTranFeeCur();
						Double exchangeRate = null;
						if(StringUtils.isNotBlank(tranFeeCur) && StringUtils.isNotBlank(currencyCode)){
							exchangeRate = this.getExchangeRate(currencyCode, tranFeeCur);
						}
						if(exchangeRate == null){
							exchangeRate = 1.00;
						}
						orderPlanProduct.setDividend(dividend);
						orderPlanProduct.setAccount(investorAccount);
						if(investorAccount != null){
							orderPlanProduct.setAccountNo(investorAccount.getAccountNo());
						}
						if (StringUtils.isNotBlank(tranRate)) {
							orderPlanProduct.setTranRate(Double.parseDouble(tranRate));
						}
						if (StringUtils.isNotBlank(amountStr)) {
							Double amount = Double.parseDouble(amountStr);
							if(amount != null){
								amount = amount * exchangeRate;
							}
							orderPlanProduct.setAmount(amount);
							orderPlanProduct.setAmountAdjust(amount);
						}
						if (StringUtils.isNotBlank(weightAdjustStr)) {
							orderPlanProduct.setWeightAdjust(Double.parseDouble(weightAdjustStr));
						}
						if(orderPlanProduct.getTranRate() != null){
							Double rate = orderPlanProduct.getTranRate();
							Double amount = orderPlanProduct.getAmount();
							orderPlanProduct.setTranFee(amount * (rate / 100) * exchangeRate); // TranFee = amount*rate*exchangeRate;
						}
						this.baseDao.update(orderPlanProduct);
					}
				}
			}
		}
	}
	
	/**
	 * 交易金额汇总（计算 order_plan.total_buy、order_plan.total_sell）
	 * @author wwluo
	 * @data 2017-02-27
	 * @param 
	 * @return
	 */
	@Override
	public OrderPlan saveTotalAmountSummary(String planId) {
		OrderPlan plan = null;
		if (StringUtils.isNotBlank(planId)) {
			plan = (OrderPlan) this.baseDao.get(OrderPlan.class, planId);
			if(plan != null){
				StringBuffer hql = new StringBuffer("" +
						" FROM" +
						" OrderPlanProduct p" +
						" WHERE" +
						" p.original<>1" +
						" AND" +
						" p.plan.id=?");
				List<Object> params = new ArrayList<Object>();
				params.add(planId);
				List<OrderPlanProduct> orderPlanProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(orderPlanProducts != null && !orderPlanProducts.isEmpty()){
					Double totalBuy = 0.00;
					Double totalSell = 0.00;
					String planCurrency = null;
					String productCurrency = null;
					for (OrderPlanProduct orderPlanProduct : orderPlanProducts) {
						planCurrency = plan.getBaseCurrency();
						productCurrency = orderPlanProduct.getTranFeeCur();
						Double rate = null;
						if (StringUtils.isNotBlank(planCurrency) 
								&& StringUtils.isNotBlank(productCurrency)) {
							rate = this.getExchangeRate(productCurrency, planCurrency);
						}
						if(rate == null){
							rate = 1.00;
						}
						if(CommonConstants.TRAN_TYPE_BUY.equals(orderPlanProduct.getTranType())){
							Double amount = orderPlanProduct.getAmount();
							if(amount != null){
								totalBuy += amount*rate;
							}
						}else if(CommonConstants.TRAN_TYPE_SELL.equals(orderPlanProduct.getTranType())){
							Double unit = orderPlanProduct.getUnit();
							String productId = orderPlanProduct.getProduct().getId();
							FundInfo fundInfo = this.getFundInfoByProduct(productId);
							Double lastNav = null;
							if(fundInfo != null){
								lastNav = fundInfo.getLastNav();
								if(lastNav != null){
									totalSell += lastNav*unit;
								}
							}
						}
					}
					plan.setTotalBuy(totalBuy);
					plan.setTotalSell(totalSell);
					plan.setLastUpdate(new Date());
					plan = (OrderPlan)this.baseDao.update(plan);
				}
			}
		}
		return plan;
	}
	
	/**
	 * 下单成功后发送待办待阅
	 * @param memberBase
	 * @param planId
	 * @param supervisorId
	 * @param langCode
	 * @param ifaIds
	 */
	public void saveSendToReadForOrderPlan(MemberBase memberBase,String planId,String supervisorId,String langCode,Set<String> ifaIds){
		OrderPlan plan = (OrderPlan) this.baseDao.get(OrderPlan.class, planId);
		if(plan != null){
			String checkStatus = "";
			if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == memberBase.getMemberType()){
				//消息提醒
				WebReadToDo webReadToDo = new WebReadToDo();
			 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_EXCHANGE);
			 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_SUMMIT_IFA1);
			 	webReadToDo.setRelateId(plan.getId());
			 	webReadToDo.setFromMember(memberBase);
			 	webReadToDo.setMsgUrl("/front/tradeStep/confirmOrderPlan.do");
			 	webReadToDo.setMsgParam("id="+plan.getId());
			 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioConfirmActivity");
			 	webReadToDo.setAppParam("orderPlanId="+plan.getId());
			 	webReadToDo.setIsApp("1");
			 	webReadToDo.setIsRead("0");
			 	webReadToDo.setOwner(plan.getPortfolioHold().getMember());
			 	webReadToDo.setCreateTime(new Date());
			 	webReadToDo.setIsValid("1");
			 	List<Object> params = new ArrayList<Object>();
			 	if(plan.getPortfolioHold().getIfa() != null){
			 		params.add(getCommonMemberName(plan.getPortfolioHold().getIfa().getMember().getId(), langCode, "2"));
			 		params.add(plan.getPortfolioHold().getPortfolioName());
			 	}
			 	String titleSc = PropertyUtils.getSmsPropertyValue("order.commission", CommonConstants.LANG_CODE_SC, params.toArray());
			 	String titleTc = PropertyUtils.getSmsPropertyValue("order.commission", CommonConstants.LANG_CODE_TC, params.toArray());
			 	String titleEn = PropertyUtils.getSmsPropertyValue("order.commission", CommonConstants.LANG_CODE_EN, params.toArray());
			 	webReadToDo.setTitle(titleEn);
			 	if("1" != plan.getAuthorized()){
			 		// 发送investor
			 		webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
			 		// 插入审批数据
				 	OrderPlanCheck check = new OrderPlanCheck();
				 	check.setCheck(plan.getPortfolioHold().getMember());
				 	check.setPlan(plan);
				 	check.setCheckStatus("0");
				 	this.baseDao.create(check);
				 	checkStatus += "0";
			 	}
			 	if (StringUtils.isNotBlank(supervisorId)) {
			 		//发送supervisor
			 		MemberBase supervisor = (MemberBase) baseDao.get(MemberBase.class,supervisorId);
			 		if(supervisor != null){
			 			WebReadToDo webReadNew = new WebReadToDo();
				 		BeanUtils.copyProperties(webReadToDo, webReadNew);
				 		webReadNew.setId(null);
				 		webReadNew.setOwner(supervisor);
				 		params.clear();
				 		params.add(this.getIfaName(plan.getPortfolioHold().getIfa(), langCode));
				 		params.add(getCommonMemberName(plan.getPortfolioHold().getMember().getId(), langCode, "2"));
				 		params.add(plan.getPortfolioHold().getPortfolioName());
				 		titleSc = PropertyUtils.getSmsPropertyValue("order.check.supervisor",CommonConstants.LANG_CODE_SC,params.toArray());
					 	titleTc = PropertyUtils.getSmsPropertyValue("order.check.supervisor",CommonConstants.LANG_CODE_TC,params.toArray());
					 	titleEn = PropertyUtils.getSmsPropertyValue("order.check.supervisor",CommonConstants.LANG_CODE_EN,params.toArray());
				 		webReadNew = webReadToDoService.saveWebReadToDo(webReadNew,titleEn,titleSc,titleTc);
				 		// 插入待办
					 	WebTaskList taskList = new WebTaskList();
					 	taskList.setCreateDate(new Date());
					 	taskList.setFromMember(memberBase);
					 	taskList.setHandleStatus("0");
					 	taskList.setIsValid("1");
					 	taskList.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_SUMMIT_IFA1);
					 	taskList.setRelateId(plan.getId());
					 	taskList.setOwner(supervisor);
					 	taskList.setTaskUrl("/front/tradeStep/confirmOrderPlan.do");
					 	taskList.setTaskParam("id="+plan.getId());
					 	taskList.setTitleSc(titleSc);
					 	taskList.setTitleTc(titleTc);
					 	taskList.setTitleEn(titleEn);
					 	taskList.setType(null);
					 	webTaskListService.saveTaskList(taskList);
				 		// 插入审批数据
					 	OrderPlanCheck check = new OrderPlanCheck();
					 	check.setCheck(supervisor);
					 	check.setPlan(plan);
					 	check.setCheckStatus("0");
					 	this.baseDao.create(check);
				 		checkStatus += "0";
			 		}
			 	}
			 	// 更新审批状态
			 	plan.setCheckStatus(checkStatus); 
			 	plan = (OrderPlan)this.baseDao.update(plan);
			}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberBase.getMemberType()
					&& ifaIds != null && !ifaIds.isEmpty()) {
				for (String ifaId : ifaIds) {
					MemberIfa memberIfa = (MemberIfa) this.baseDao.get(MemberIfa.class, ifaId);
					if(memberIfa != null){
						// 消息提醒
						WebReadToDo webReadToDo = new WebReadToDo();
					 	webReadToDo.setType(CommonConstantsWeb.WEB_READ_MESSAGE_TYPE_EXCHANGE);
					 	webReadToDo.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_SUMMIT_INVESTOR1);
					 	webReadToDo.setRelateId(plan.getId());
					 	webReadToDo.setFromMember(memberBase);
					 	webReadToDo.setMsgUrl("/front/tradeStep/confirmOrderPlan.do");
					 	webReadToDo.setMsgParam("id="+plan.getId());
					 	webReadToDo.setAppUrl("com.xinfinance.xfawealthInvestor.business.me.activity.MePortfolioConfirmActivity");
					 	webReadToDo.setAppParam("orderPlanId="+plan.getId());
					 	webReadToDo.setIsApp("1");
					 	webReadToDo.setIsRead("0");
					 	webReadToDo.setOwner(memberIfa.getMember());
					 	webReadToDo.setCreateTime(new Date());
					 	webReadToDo.setIsValid("1");
					 	List<Object> params = new ArrayList<Object>();
				 		params.add(getCommonMemberName(plan.getPortfolioHold().getMember().getId(), langCode, "2"));
				 		params.add(plan.getPortfolioHold().getPortfolioName());
					 	String titleSc = PropertyUtils.getSmsPropertyValue("order.commission.investor", CommonConstants.LANG_CODE_SC, params.toArray());
					 	String titleTc = PropertyUtils.getSmsPropertyValue("order.commission.investor", CommonConstants.LANG_CODE_TC, params.toArray());
					 	String titleEn = PropertyUtils.getSmsPropertyValue("order.commission.investor", CommonConstants.LANG_CODE_EN, params.toArray());
					 	webReadToDo.setTitle(titleEn); 
					 	webReadToDo = webReadToDoService.saveWebReadToDo(webReadToDo,titleEn,titleSc,titleTc);
					 	// 插入待办
					 	WebTaskList taskList = new WebTaskList();
					 	taskList.setCreateDate(new Date());
					 	taskList.setFromMember(memberBase);
					 	taskList.setHandleStatus("0");
					 	taskList.setIsValid("1");
					 	taskList.setModuleType(CommonConstantsWeb.WEB_READ_MODULE_ORDER_SUMMIT_INVESTOR1);
					 	taskList.setRelateId(plan.getId());
					 	taskList.setOwner(memberIfa.getMember());
					 	taskList.setTaskUrl("/front/tradeStep/confirmOrderPlan.do");
					 	taskList.setTaskParam("id="+plan.getId());
					 	taskList.setTitleSc(titleSc);
					 	taskList.setTitleTc(titleTc);
					 	taskList.setTitleEn(titleEn);
					 	taskList.setType(null);
					 	webTaskListService.saveTaskList(taskList);
					 	// 插入审批数据
					 	OrderPlanCheck check = new OrderPlanCheck();
					 	check.setCheck(memberIfa.getMember());
					 	check.setPlan(plan);
					 	check.setCheckStatus("0");
					 	this.baseDao.create(check);
					 	checkStatus += "0";
					}
				}
				// 更新审批状态
			 	plan.setCheckStatus(checkStatus); 
			 	plan.setLastUpdate(new Date());
			 	plan = (OrderPlan) this.baseDao.update(plan);
			}
		}
	}
	
	/**
	 * 获取定投信息
	 * @author wwluo
	 * @date 2016-12-26
	 */
	@Override
	public OrderPlanAip getOrderPlanAipByOrderId(String planId) {
		OrderPlanAip  aip = null;
		if(StringUtils.isNotBlank(planId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" OrderPlanAip a" +
					" WHERE" +
					" a.plan.id=?" +
					" ORDER BY" +
					" a.lastUpdate" +
					" Desc" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			List<OrderPlanAip> orderPlanAips = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderPlanAips != null && !orderPlanAips.isEmpty()){
				aip = orderPlanAips.get(0);
			}
		}
		return aip;
	}
	
	/**
	 * 获取定投计划（执行中）
	 * @author wwluo
	 * @data 2017-02-23
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public OrderAip getOrderAipByPlan(String planId) {
		OrderAip orderAip = null;
		if (StringUtils.isNotBlank(planId)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" OrderAip o" +
					" WHERE" +
					" o.plan.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			List<OrderAip> orderAips = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(orderAips != null && !orderAips.isEmpty()){
				orderAip = orderAips.get(0);
			}
		}
		return orderAip;
	}
	
	/**
	 * 保存定投信息
	 * @param request
	 * @param memberBase
	 * @return
	 */
	public boolean saveAip(HttpServletRequest request,MemberBase memberBase) {
		boolean flag = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		String planId = request.getParameter("planId");
		String currencyCode = request.getParameter("currencyCode");
		String aipExecCycle = request.getParameter("aipExecCycle");
		String aipInitTime = request.getParameter("aipInitTime");
		String chargeDay = request.getParameter("chargeDay");
		String aipAmount = request.getParameter("aipAmount");
		String aipEndType = request.getParameter("aipEndType");
		String aipEndDate = request.getParameter("aipEndDate");
		String aipEndCount = request.getParameter("aipEndCount");
		String aipEndTotalAmount = request.getParameter("aipEndTotalAmount");
		OrderPlan plan = null;
		if (StringUtils.isNotBlank(planId)) {
			plan = (OrderPlan)this.baseDao.get(OrderPlan.class,planId);
		}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == memberBase.getMemberType()){
			PortfolioHold hold = new PortfolioHold();
			hold.setBaseCurrency(memberBase.getDefCurrency());
			hold.setCreateTime(new Date());
			hold.setIfFirst("1");
			hold.setLastUpdate(new Date());
			hold.setMember(memberBase);
			hold = (PortfolioHold) this.baseDao.create(hold);
			plan = new OrderPlan();
			plan.setCreator(memberBase);
			plan.setBaseCurrency(memberBase.getDefCurrency());
			plan.setCreateTime(new Date());
			plan.setFinishStatus("0");
			plan.setLastUpdate(new Date());
			plan.setPortfolioHold(hold);
			plan.setIsValid("1");
			plan = (OrderPlan) this.baseDao.create(plan);
			planId = plan.getId();
		}
		if(plan != null){
			Double exChangeRate = null;
			if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(plan.getBaseCurrency())) {
				WebExchangeRate webExchangeRate = fundInfoService.findExchangeRate(currencyCode, plan.getBaseCurrency());
				if(webExchangeRate != null){
					exChangeRate = webExchangeRate.getRate();
				}
			}
			if(exChangeRate == null){
				exChangeRate = 1.00;
			}
			OrderPlanAip aip = this.getOrderPlanAipByOrderId(plan.getId());
			if(aip == null){
				aip = new OrderPlanAip();
				aip.setCreateTime(new Date());
				aip.setPlan(plan);
			}
			try {
				if (StringUtils.isNotBlank(aipInitTime)) {
					aip.setAipInitTime(dateFormat.parse(aipInitTime));
				}else{
					aip.setAipInitTime(null);
				}
				if (StringUtils.isNotBlank(chargeDay)) {
					aip.setAipTimeDistance(Integer.parseInt(chargeDay));
				}else{
					aip.setAipTimeDistance(null);
				}
				if (StringUtils.isNotBlank(aipAmount)) {
					aip.setAipAmount(Double.parseDouble(aipAmount) * exChangeRate);
				}else{
					aip.setAipAmount(null);
				}
				if (StringUtils.isNotBlank(aipEndDate)) {
					aip.setAipEndDate(dateFormat.parse(aipEndDate));
				}else{
					aip.setAipEndDate(null);
				}
				if (StringUtils.isNotBlank(aipEndCount)) {
					aip.setAipEndCount(Integer.parseInt(aipEndCount));
				}else{
					aip.setAipEndCount(null);
				}
				if (StringUtils.isNotBlank(aipEndTotalAmount)) {
					aip.setAipEndTotalAmount(Double.parseDouble(aipEndTotalAmount) * exChangeRate);
				}else{
					aip.setAipEndTotalAmount(null);
				}
				aip.setAipEndType(aipEndType);
				aip.setAipExecCycle(aipExecCycle);
				aip.setLastUpdate(new Date());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(aip.getId() == null){
				aip = (OrderPlanAip) this.baseDao.create(aip);
			}else{
				aip = (OrderPlanAip) this.baseDao.update(aip);
			}
			plan.setAipFlag("1");
			plan.setLastUpdate(new Date());
			this.baseDao.update(plan);
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 获取我的团队上司
	 * @author wwluo
	 * @data 2017-02-23
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IfaMyTeamListVO> getMySupervisorByMemberId(String memberId,String langCode) {
		List<IfaMyTeamListVO> teamListVOs = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer("" +
					" SELECT" +
					" t.id,b.id,t.ifa,t.team.id" +
					" FROM" +
					" IfafirmTeamIfa t,IfafirmTeam d" +
					" LEFT JOIN" +
					" MemberIfa m" +
					" ON" +
					" m.id=t.ifa.id" +
					" LEFT JOIN" +
					" MemberBase b" +
					" ON" +
					" b.id=m.member.id" +
					" WHERE" +
					" t.isSupervisor=1" +
					" AND" +
					" d.id=t.team.id" +
					" AND" +
					" d.id" +
					" IN" +
					" (SELECT" +
					" f.team.id" +
					" FROM" +
					" IfafirmTeamIfa f" +
					" LEFT JOIN" +
					" MemberIfa i" +
					" ON" +
					" i.id=f.ifa.id" +
					" WHERE" +
					" i.member.id=?" +
					" )" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			List<Object[]> objects = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objects != null && !objects.isEmpty()){
				teamListVOs = new ArrayList<IfaMyTeamListVO>();
				for (Object[] obj : objects) {
					IfaMyTeamListVO vo = new IfaMyTeamListVO();
					String id = (String) obj[0];
					String member = (String) obj[1];
					/*if(memberId.equals(member)){
						continue;
					}*/
					MemberIfa ifa = (MemberIfa) obj[2];
					String teamId = (String) obj[3];
					vo.setId(id);
					vo.setMemberId(member);
					vo.setTeamId(teamId);
			    	vo.setNickname(getCommonMemberName(ifa.getMember().getId(), langCode, "2"));
					teamListVOs.add(vo);
				}
			}
		}
		return teamListVOs;
	}
	
	/**
	 * 封装交易页面IFA展示信息
	 * @author wwluo
	 * @data 2017-05-11
	 * @param currencyCode
	 * @param langCode
	 * @param loginUser
	 * @param orderPlan
	 * @return
	 */	
	@Override
	public TransactionVO getTransactionVOForIfa(String currencyCode, String langCode, MemberBase loginUser,
			OrderPlan orderPlan) {
		TransactionVO vo = new TransactionVO();
		vo.setMemberType(loginUser.getMemberType()); // '账户类型：1-投资人，2-IFA,3-代理商',
		PortfolioHold hold = orderPlan.getPortfolioHold();
		vo.setHoldId(hold.getId());
		vo.setPortfolioName(hold.getPortfolioName());
		List<InvestorAccountVO> investorAccounts = findPortfolioHoldAccount(hold.getMember(), hold.getIfa(),langCode,currencyCode,hold.getId());
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
		String clientIconUrl = null;
		if(ifa != null && member != null){
			clientIconUrl = hold.getMember().getIconUrl();
			vo.setNickName(getCommonMemberName(hold.getMember().getId(), langCode, "2"));
			Double cashAvailable = getCashAvailableTotal(member.getId(),ifa.getId(),currencyCode,hold.getId());
			vo.setCashAvailable(cashAvailable);
		}
		clientIconUrl = PageHelper.getUserHeadUrl(clientIconUrl, "");
		vo.setClientIconUrl(clientIconUrl);
		vo.setCurrencyCode(currencyCode);
		String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
		vo.setCurrencyName(currencyName);
		List<PlanProductVO> planProductVOs = getOrderPlanProducts(orderPlan.getId(), currencyCode, langCode);
		vo.setPlanProductVOs(planProductVOs);
		// 定投部分
		vo.setAipFlag(orderPlan.getAipFlag());
		OrderPlanAip planAip = getOrderPlanAipByOrderId(orderPlan.getId());
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
			vo.setAipInitTime(DateUtil.StringToDate(DateUtil.getNextCycleTime(new Date(),"w",2), CommonConstants.FORMAT_DATE));
		}
		return vo;
	}

	/**
	 * 封装交易页面Investor展示信息
	 * @author wwluo
	 * @data 2017-05-11
	 * @param currencyCode
	 * @param langCode
	 * @param loginUser
	 * @param orderPlan
	 * @return
	 */
	@Override
	public TransactionVO getTransactionVOForInvestor(String currencyCode,
			String langCode, MemberBase loginUser, OrderPlan orderPlan) {
		TransactionVO vo = new TransactionVO();
		if(orderPlan != null && loginUser.getId().equals(orderPlan.getCreator().getId())){
			PortfolioHold hold = orderPlan.getPortfolioHold();
			vo.setMemberType(loginUser.getMemberType()); // '账户类型：1-投资人，2-IFA,3-代理商',
			vo.setHoldId(hold.getId());
			vo.setPortfolioName(hold.getPortfolioName());
			List<InvestorAccountVO> investorAccounts = findPortfolioHoldAccount(hold.getMember(), hold.getIfa(), langCode, currencyCode, hold.getId());
			vo.setInvestorAccountVOs(investorAccounts);
			if(hold.getMember() != null){
				Double cashAvailable = getCashAvailableTotal(hold.getMember().getId(), null, currencyCode, hold.getId());
				vo.setCashAvailable(cashAvailable);
			}
			vo.setCurrencyCode(currencyCode);
			String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
			vo.setCurrencyName(currencyName);
			List<PlanProductVO> planProductVOs = getOrderPlanProducts(orderPlan.getId(), currencyCode, langCode);
			vo.setPlanProductVOs(planProductVOs);
			vo.setClientIconUrl(PageHelper.getUserHeadUrl(loginUser.getIconUrl(), ""));
			vo.setNickName(getCommonMemberName(loginUser.getId(), langCode, "2"));
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
		}else if(orderPlan == null){
			vo.setMemberType(loginUser.getMemberType()); // '账户类型：1-投资人，2-IFA,3-代理商',
			if (StringUtils.isBlank(currencyCode)) {
				currencyCode = CommonConstants.DEF_CURRENCY;
			}
			List<InvestorAccountVO> investorAccounts = findPortfolioHoldAccount(loginUser, null, langCode, currencyCode, null);
			vo.setInvestorAccountVOs(investorAccounts);
			Double cashAvailable = getCashAvailableTotal(loginUser.getId(),null,currencyCode,null);
			vo.setCashAvailable(cashAvailable);
			vo.setCurrencyCode(currencyCode);
			String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY, currencyCode, langCode);
			vo.setCurrencyName(currencyName);
			vo.setClientIconUrl(PageHelper.getUserHeadUrl(loginUser.getIconUrl(), ""));
			vo.setNickName(getCommonMemberName(loginUser.getId(), langCode, "2"));
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
		}
		return vo;
	}

	/**
	 * 根据ids获取产品VO
	 * @author wwluo
	 * @data 2017-05-11
	 * @param ids 基金id
	 * @param currencyCode 货币编码
	 * @param langCode 多语言编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PlanProductVO> getPlanProductVOByIds(String ids, String memberId, String currencyCode, String langCode) {
		List<PlanProductVO> vos = null;
		if (StringUtils.isNotBlank(ids)) {
			vos = new ArrayList<PlanProductVO>();
			ids = StrUtils.reHeavy(ids);
			String[] idStr = null;
			if(ids.indexOf(",") > -1){
				idStr = ids.split(",");
			}else{
				idStr = new String[]{ids};
			}
			for (int i = 0; i < idStr.length; i++) {
				StringBuffer hql = new StringBuffer(""
						+ " SELECT"
						+ " f,l,p"
						+ " FROM"
						+ " FundInfo f"
						+ "	LEFT JOIN"
						+ " FundInfo" + this.getLangFirstCharUpper(langCode) + " l"
						+ " ON"
						+ " f.id=l.id"
						+ " LEFT JOIN"
						+ " ProductDistributor p"
						+ " ON"
						+ " p.product.id=f.product.id"
						+ "	AND"
						+ " EXISTS"
						+ " (FROM"
						+ " ProductCompany c"
						+ " WHERE"
						+ " c.product.id=f.product.id"
						+ " AND"
						+ " c.company=(SELECT r.company FROM MemberCompany r WHERE r.member.id=?))"
						+ " WHERE"
						+ " f.isValid=1"
						+ " AND"
						+ " f.id=?"
						+ " ORDER BY"
						+ " f.lastUpdate"
						+ " DESC"
						+ "");
				List<Object> params = new ArrayList<Object>();
				params.add(memberId);
				params.add(idStr[i]);
				List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(objs != null && !objs.isEmpty()){
					// 基金行情货币转换页面货币（currencyCode）汇率
					Double fundCurrencyExchangeRate = null;
					// 基金发行货币转换页面货币（currencyCode）汇率
					Double issueCurrencyExchangeRate = null;
					// 产品交易货币转换页面货币（currencyCode）汇率
					Double tranCurrencyExchangeRate = null;
					String fundCurrencyCode = null;
					String issueCurrencyCode = null;
					String tranCurrencyCode = null;
					PlanProductVO vo = new PlanProductVO();
					Object[] objects = objs.get(0);
					FundInfo fundInfo = (FundInfo) objects[0];
					vo.setFundInfoId(fundInfo.getId());
					vo.setRiskLevel(fundInfo.getRiskLevel());
					vo.setLastNav(fundInfo.getLastNav());
					vo.setMinInitialAmount(fundInfo.getMinInitialAmount());
					if(objects[1] instanceof FundInfoSc){
						FundInfoSc fundInfoSc = (FundInfoSc) objects[1];
						vo.setFundName(fundInfoSc.getFundName());
						vo.setFundType(fundInfoSc.getFundType());
						vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
						vo.setFundCurrencyName(fundInfoSc.getFundCurrency());
						fundCurrencyCode = fundInfoSc.getFundCurrencyCode();
						issueCurrencyCode = fundInfoSc.getIssueCurrencyCode();
					}else if(objects[1] instanceof FundInfoTc){
						FundInfoTc fundInfoTc = (FundInfoTc) objects[1];
						vo.setFundName(fundInfoTc.getFundName());
						vo.setFundType(fundInfoTc.getFundType());
						vo.setFundCurrencyCode(fundInfoTc.getFundCurrencyCode());
						vo.setFundCurrencyName(fundInfoTc.getFundCurrency());
						fundCurrencyCode = fundInfoTc.getFundCurrencyCode();
						issueCurrencyCode = fundInfoTc.getIssueCurrencyCode();
					}else if(objects[1] instanceof FundInfoEn){
						FundInfoEn fundInfoEn = (FundInfoEn) objects[1];
						vo.setFundName(fundInfoEn.getFundName());
						vo.setFundType(fundInfoEn.getFundType());
						vo.setFundCurrencyCode(fundInfoEn.getFundCurrencyCode());
						vo.setFundCurrencyName(fundInfoEn.getFundCurrency());
						fundCurrencyCode = fundInfoEn.getFundCurrencyCode();
						issueCurrencyCode = fundInfoEn.getIssueCurrencyCode();
					}
					ProductDistributor productDistributor = (ProductDistributor) objects[2];
					vo.setTranRate(productDistributor.getTransactionFeeRate());
					tranCurrencyCode = productDistributor.getTransactionFeeCur();
					vo.setTranFeeCur(tranCurrencyCode);
					vo.setTranFeeMini(productDistributor.getTransactionFeeMini());
					vo.setStatus(productDistributor.getStatus()); // '状态,0退市Delisting,1有效Active,2挂起/停用Suspend'
					vo.setTradable(productDistributor.getTradable()); // '交易状态,Y可交易,N不可交易'
					vo.setCurrencyCode(currencyCode);
					String currencyName = sysParamService.findNameByValue(CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE, currencyCode, langCode);
					vo.setCurrencyName(currencyName);
					// 货币相关转换
					if (StringUtils.isNotBlank(fundCurrencyCode) && !fundCurrencyCode.equals(currencyCode)) {
						fundCurrencyExchangeRate = this.getExchangeRate(fundCurrencyCode, currencyCode);
					}
					if (StringUtils.isNotBlank(issueCurrencyCode) && !issueCurrencyCode.equals(currencyCode)) {
						issueCurrencyExchangeRate = this.getExchangeRate(issueCurrencyCode, currencyCode);
					}
					if (StringUtils.isNotBlank(tranCurrencyCode) && !tranCurrencyCode.equals(currencyCode)) {
						tranCurrencyExchangeRate = this.getExchangeRate(tranCurrencyCode, currencyCode);
					}
					if(fundCurrencyExchangeRate != null && vo.getLastNav() != null){
						vo.setLastNav(vo.getLastNav() * fundCurrencyExchangeRate);
					}
					if(issueCurrencyExchangeRate != null && vo.getMinInitialAmount() != null){
						vo.setMinInitialAmount(vo.getMinInitialAmount() * issueCurrencyExchangeRate);
					}
					if(tranCurrencyExchangeRate != null && vo.getTranFeeMini() != null){
						vo.setTranFeeMini(vo.getTranFeeMini() * tranCurrencyExchangeRate);
					}
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * 根据ids获取产品VO
	 * @author wwluo
	 * @data 2017-05-11
	 * @param loginUser
	 * @param planId 
	 * @param currencyCode 货币编码
	 * @param portfolioName 组合名称
	 * @param productData 产品信息
	 * @return planId 
	 */
	@Override
	public String saveOrderPlan(MemberBase loginUser, String planId,
			String currencyCode, Integer riskLevel, String portfolioName, String productData) {
		PortfolioHold hold = null;
		OrderPlan plan = null;
		if (StringUtils.isBlank(planId) 
				&& CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()) {
			hold = new PortfolioHold();
			hold.setBaseCurrency(currencyCode);
			hold.setCreateTime(new Date());
			hold.setIfFirst("1");
			hold.setLastUpdate(new Date());
			hold.setMember(loginUser);
			hold.setPortfolioName(portfolioName);
			hold.setRiskLevel(riskLevel);
			hold = (PortfolioHold) baseDao.create(hold);
			plan = new OrderPlan();
			plan.setBaseCurrency(currencyCode);
			plan.setCreateTime(new Date());
			plan.setCreator(loginUser);
			plan.setFinishStatus("0");
			plan.setLastUpdate(new Date()); 
			plan.setPortfolioHold(hold);
			plan.setIsValid("1");
			plan = (OrderPlan) baseDao.create(plan);
		}
		if (plan == null && StringUtils.isNotBlank(planId)) {
			plan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
		}
		if(plan != null){
			hold = plan.getPortfolioHold();
			hold.setPortfolioName(portfolioName);
			hold.setLastUpdate(new Date());
			hold = (PortfolioHold) baseDao.saveOrUpdate(hold);
			saveOrderPlanProduct(plan.getId(), productData, currencyCode);
			planId = plan.getId();
		}
		return planId;
	}

	/**
	 * 交易产品信息保存
	 * @author wwluo
	 * @data 2017-05-11
	 * @param planId 
	 * @param currencyCode 货币编码
	 * @param productData 产品信息
	 * @return
	 */
	private void saveOrderPlanProduct(String planId, String productData,
			String currencyCode) {
		OrderPlan plan = (OrderPlan) baseDao.get(OrderPlan.class, planId);
		PortfolioHold hold = plan.getPortfolioHold();
		List<Map> productDatas = JsonUtil.toListMap(productData);
		if(productDatas != null && !productDatas.isEmpty()){
			// 0.获得用户所在的companyId
			String hql = " FROM MemberCompany r WHERE r.member.id=?";
			List<MemberCompany> mcList = baseDao.find(hql.toString(), new Object[]{hold.getMember().getId()}, false);
			CompanyInfo companyInfo = null;
			if(mcList != null && !mcList.isEmpty()){
				MemberCompany memberCompany = mcList.get(0);
				companyInfo = memberCompany.getCompany();
			}
			// 1.清空原产品
			hql = " DELETE FROM OrderPlanProduct p WHERE p.plan.id=?";
			baseDao.updateHql(hql.toString(), new Object[]{plan.getId()});
			Double totalAmount = 0.00;
			Boolean authorized = true;
			// 2.保存新产品
			for (Map productDataMap : productDatas) {
				String fundId = (String) productDataMap.get("fundId");
				String amountStr = (String) productDataMap.get("amount");
				String weightStr = (String) productDataMap.get("weight");
				String accountId = (String) productDataMap.get("accountId");
				String dividend = (String) productDataMap.get("dividend");
				String tranRateStr = (String) productDataMap.get("tranRate");
				String tranFeeStr = (String) productDataMap.get("tranFee");
				FundInfo fundInfo = (FundInfo) baseDao.get(FundInfo.class, fundId);
				ProductInfo productInfo = fundInfo.getProduct();
				Double amount = null;
				if (StringUtils.isNotBlank(amountStr)) {
					amount = Double.valueOf(amountStr);
				}
				Double weight = null;
				if (StringUtils.isNotBlank(weightStr)) {
					weight = Double.valueOf(weightStr)/100;
				}
				InvestorAccount account = (InvestorAccount) baseDao.get(InvestorAccount.class, accountId);
				Double tranRate = null;
				if (StringUtils.isNotBlank(tranRateStr)) {
					tranRate = Double.valueOf(tranRateStr);
				}
				Double tranFee = null;
				if (StringUtils.isNotBlank(tranFeeStr)) {
					tranFee = Double.valueOf(tranFeeStr);
				}
				ProductDistributor productDistributor = this.getProductDistributorByProduct(productInfo.getId(), companyInfo.getId());
				OrderPlanProduct orderPlanProduct = new OrderPlanProduct();
				orderPlanProduct.setPlan(plan);
				orderPlanProduct.setProduct(fundInfo.getProduct());
				orderPlanProduct.setAmount(amount);
				orderPlanProduct.setWeight(weight);
				orderPlanProduct.setAccount(account);
				orderPlanProduct.setAccountNo(account.getAccountNo());
				orderPlanProduct.setDividend(dividend);
				orderPlanProduct.setTranFeeCur(productDistributor.getTransactionFeeCur());
				orderPlanProduct.setTranFeeMini(productDistributor.getTransactionFeeMini());
				orderPlanProduct.setTranRate(tranRate);
				orderPlanProduct.setTranFee(tranFee);
				orderPlanProduct.setTranType(CommonConstants.TRAN_TYPE_BUY);
				orderPlanProduct.setOriginal(0);
				orderPlanProduct.setTranFee(amount * tranRate / 100);
				orderPlanProduct.setStatus("0");
				// Exchange
				Double exchangeRate = getExchangeRate(currencyCode, orderPlanProduct.getTranFeeCur());
				orderPlanProduct.setAmount(orderPlanProduct.getAmount() * exchangeRate);
				orderPlanProduct.setTranFee(orderPlanProduct.getTranFee() * exchangeRate);
				orderPlanProduct = (OrderPlanProduct) baseDao.create(orderPlanProduct);
				// Total Amount
				Double planExchangeRate = null;
				if (StringUtils.isNotBlank(orderPlanProduct.getTranFeeCur())) {
					planExchangeRate = getExchangeRate(orderPlanProduct.getTranFeeCur(), plan.getBaseCurrency());
				}
				if(planExchangeRate == null){
					planExchangeRate = 1.0;
				}
				totalAmount += orderPlanProduct.getAmount() * planExchangeRate;
				// Authorized
				if(!"1".equals(account.getAuthorized())){
					authorized = false;
				}
			}
			// 3.更新交易计划
			if(authorized){
				plan.setAuthorized("1");
			}else{
				plan.setAuthorized("0");
			}
			plan.setTotalBuy(totalAmount);
			plan.setLastUpdate(new Date());
			baseDao.saveOrUpdate(plan);
		}
	}

	/**
	 * 获取交易账号关联的IFA ID
	 * @author wwluo
	 * @data 2017-05-11
	 * @param planId 
	 * @return 
	 */
	@Override
	public Set<String> getOrderIfas(String planId) {
		Set<String> ifaIds = null;
		if (StringUtils.isNotBlank(planId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " i.ifa.id"
					+ " FROM"
					+ " OrderPlanProduct o"
					+ " LEFT JOIN"
					+ " InvestorAccount i"
					+ " ON"
					+ " o.account.id=i.id"
					+ " WHERE"
					+ " o.plan.id=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(planId);
			List<String> ids = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(ids != null){
				ifaIds = new HashSet(ids);
			}
		}
		return ifaIds;
	}
	
	/**
	 * 往OrderPlan插入基金
	 * @author wwluo
	 * @date 2017-02-20
	 * @param planId
	 * @param fundIds
	 */
	@Override
	public void addPlanProduct(String planId, String fundIds) {
		if (StringUtils.isNotBlank(planId) && StringUtils.isNotBlank(fundIds)) {
			OrderPlan plan = (OrderPlan) this.baseDao.get(OrderPlan.class, planId);
			if(plan != null){
				String[] objs = null;
				if(fundIds.indexOf(",") > -1){
					fundIds = StrUtils.reHeavy(fundIds);
					objs = fundIds.split(",");
				}else{
					objs = new String[]{fundIds};
				}
				if(objs != null){
					for (int i = 0; i < objs.length; i++) {
						FundInfo fundInfo = (FundInfo) baseDao.get(FundInfo.class, objs[i]);
						ProductDistributor productDistributor = 
								getProductDistributorByMemberIdAndProductId(plan.getPortfolioHold().getMember().getId(), fundInfo.getProduct().getId());
						List<OrderPlanProduct> planProducts = 
								getPlanProducts(plan.getId(), objs[i]);
						if(productDistributor != null 
								&& "1".equals(productDistributor.getStatus()) 
								&& "Y".equals(productDistributor.getTradable()) 
								&& (planProducts == null || planProducts.isEmpty())){
							OrderPlanProduct planProduct = new OrderPlanProduct();
							planProduct.setPlan(plan);
							planProduct.setProduct(fundInfo.getProduct());
							planProduct.setTranType(CommonConstants.TRAN_TYPE_BUY);
							planProduct.setStatus("0");
							planProduct.setAmount(0.00);
							planProduct.setWeight(0.00);
							planProduct.setTranFeeCur(productDistributor.getTransactionFeeCur());
							planProduct.setTranFeeMini(productDistributor.getTransactionFeeMini());
							planProduct.setTranRate(productDistributor.getTransactionFeeRate());
							planProduct = (OrderPlanProduct) this.baseDao.create(planProduct);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 获取产品代理商关系表
	 * @author wwluo
	 * @date 2017-02-20
	 * @param memberId
	 * @param productId
	 */
	@Override
	public ProductDistributor getProductDistributorByMemberIdAndProductId(String memberId,String productId) {
		ProductDistributor productDistributor = null;
		ProductInfo product = (ProductInfo) baseDao.get(ProductInfo.class, productId);
		String hql = "SELECT r.company FROM MemberCompany r WHERE r.member.id=?";
		List<CompanyInfo> companyInfos = baseDao.find(hql.toString(), new Object[]{memberId}, false, 1);
		CompanyInfo companyInfo = null;
		if(companyInfos != null && !companyInfos.isEmpty()){
			companyInfo = companyInfos.get(0);
		}
		productDistributor = this.getProductDistributorByProduct(product.getId(), companyInfo.getId());
		return productDistributor;
	}
	
	/**
	 * 获取OrderPlanProduct
	 * @author wwluo
	 * @date 2017-02-20
	 */
	private List<OrderPlanProduct> getPlanProducts(String planId,
			String fundId) {
		StringBuffer hql = new StringBuffer("" +
				" SELECT p" +
				" FROM" +
				" OrderPlanProduct p" +
				" LEFT JOIN" +
				" FundInfo f" +
				" ON" +
				" f.product.id=p.product.id" +
				" WHERE" +
				" p.plan.id=?" +
				" AND" +
				" f.id=?" +
				"");
		List<Object> params = new ArrayList<Object>();
		params.add(planId);
		params.add(fundId);
		List<OrderPlanProduct> planProducts = this.baseDao.find(hql.toString(), params.toArray(), false);
		return planProducts;
	}
	
}
