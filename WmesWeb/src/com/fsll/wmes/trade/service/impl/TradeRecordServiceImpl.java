/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.trade.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.PropertyUtils;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.trade.service.TradeRecordService;
import com.fsll.wmes.trade.vo.AipOrderHistoryVO;
import com.fsll.wmes.trade.vo.PlanAipVO;
import com.fsll.wmes.trade.vo.TransactionRecordFilterVO;
import com.fsll.wmes.trade.vo.TransactionRecordVO;

/**
 * 交易:交易记录业务实现
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-4-18
 */
@Service("tradeRecordService")
public class TradeRecordServiceImpl  extends BaseService implements TradeRecordService{
	/**
	 * 获取ifa的交易记录
	 * @author wwluo
	 * @data 2016-11-25
	 * @param loginUser
	 * @param filter 列表筛选条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getTradeRecord(MemberBase loginUser,JsonPaging jsonPaging,TransactionRecordFilterVO filter) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(loginUser != null){
				StringBuffer hql = new StringBuffer("" +
						" SELECT" +
						" o.id,p.portfolioName,o.totalBuy,o.totalSell,o.baseCurrency,o.createTime,o.finishStatus,c.id,m,o.lastUpdate,p.ifFirst,i.id" +
						" FROM" +
						" OrderPlan o" +
						
						" LEFT JOIN" +
						" PortfolioHold p" +
						" ON" +
						" p.id=o.portfolioHold.id" +
						
						" LEFT JOIN" +
						" MemberBase m" +
						" ON" +
						" m.id=p.member.id" +
						
						" LEFT JOIN" +
						" CrmCustomer c" +
						" ON" +
						" c.member.id=p.member.id" +
						
						" LEFT JOIN" +
						" MemberIfa i" +
						" ON" +
						" i.id=p.ifa.id" +
						
						" LEFT JOIN" +
						" OrderPlanCheck oc" +
						" ON" +
						" oc.plan.id=o.id" +
						
						" WHERE 1=1" +
						//" c.ifa.id=p.ifa.id" +
						"");
				List<Object> params = new ArrayList<Object>();
				if(filter != null){
					String period = filter.getPeriod();
					String periodType = filter.getPeriodType();
					String fromDate = filter.getFromDate();
					String toDate = filter.getToDate();
					String keyWord = filter.getKeyWord();
					// 过滤期间
					if (StringUtils.isNotBlank(period) && StringUtils.isNotBlank(periodType)){
						Calendar calendar = Calendar.getInstance();
						if(CommonConstants.DATE_TYPE_DAY.equals(periodType)){
							calendar.add(Calendar.DATE, Integer.parseInt(period));
						}else if(CommonConstants.DATE_TYPE_MONTH.equals(periodType)){
							calendar.add(Calendar.MONTH, Integer.parseInt(period));
						}else if(CommonConstants.DATE_TYPE_YEAR.equals(periodType)){
							calendar.add(Calendar.YEAR, Integer.parseInt(period));
						}
						hql.append(" AND o.createTime BETWEEN ? AND CURDATE()");
						params.add(calendar.getTime());
					}
					// 过滤指定时间
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						hql.append(" AND o.createTime BETWEEN ? AND ?");
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						params.add(format.parse(fromDate));
						params.add(format.parse(toDate));
					}					
					// 过滤关键字（客户名称、组合名称）
					if (StringUtils.isNotBlank(keyWord)) {
						hql.append(" AND (p.portfolioName LIKE ? OR m.nickName LIKE ?)");
						params.add("%"+keyWord+"%");
						params.add("%"+keyWord+"%");
					}
				}
				if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
					hql.append("" +
							" AND" +
							" (o.creator=?" +
							" OR" +
							" ((p.member=?" +
							" OR" + 
							" oc.check=?)" +
							" AND" +
							" o.finishStatus<>'-1'" +
							" AND" +
							" o.finishStatus<>'0'))" +
							" ");
					params.add(loginUser);
					params.add(loginUser);
					params.add(loginUser);
				}else{
					hql.append(" "//AND  o.creator=?  OR  ((  c.ifa.id=p.ifa.id OR oc.check=?)  AND o.finishStatus<>'-1' AND o.finishStatus<>'0' )));
							+
							" AND((" +
							" c.ifa.id=p.ifa.id and i.member=?" +
							" AND" +
							" o.finishStatus<>'-1'" +
							" AND" +
							" o.finishStatus<>'0')" +
							" OR " +
							" o.creator=?" +
							" OR" +
							" (oc.check=?" +
							" AND" +
							" o.finishStatus<>'-1'" +
							" AND" +
							" o.finishStatus<>'0')" +
							" )" +
							"");
					params.add(loginUser);
					params.add(loginUser);
					params.add(loginUser);
				}
				if (StringUtils.isNotBlank(filter.getFinishStatus())) {
					//判定是否筛选多个状态
					if(filter.getFinishStatus().indexOf(",") > -1){
						String[] status = filter.getFinishStatus().split(",");
						hql.append(" AND (");
						for (int i = 0; i < status.length; i++) {
							if(i == status.length - 1){
								hql.append(" o.finishStatus=?");
								params.add(status[i]);
							}else{
								hql.append(" o.finishStatus=? OR");
								params.add(status[i]);
							}
						}
						hql.append(" )");
					}else{
						hql.append(" AND o.finishStatus=?");
						params.add(filter.getFinishStatus());
					}
				}
				hql.append(" AND o.isValid=1 GROUP BY o.id");
				jsonPaging.setSort("o.lastUpdate");
				jsonPaging.setOrder("DESC");
				jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
				if(!jsonPaging.getList().isEmpty()){
					List<Object[]> result = jsonPaging.getList();
					List<TransactionRecordVO> recordVOs = new ArrayList<TransactionRecordVO>();
					for (Object[] objects : result) {
						TransactionRecordVO recordVO = new TransactionRecordVO();
						recordVO.setMemberType(loginUser.getMemberType());
						recordVO.setOrderPlanId((String) objects[0]);
						recordVO.setPortfolioName((String) objects[1]);
						Double totalBuy = (Double) objects[2];
						Double totalSell = (Double) objects[3];
						String currencyCode = (String)objects[4];
						Double exChangeRate = null;
						if (StringUtils.isNotBlank(currencyCode) 
								&& StringUtils.isNotBlank(filter.getToCurrency())) {
							exChangeRate = this.getExchangeRate(currencyCode, filter.getToCurrency());
						}
						if(exChangeRate == null){
							exChangeRate = 1.00;
						}
						if(totalBuy != null){
							recordVO.setTotalBuy(totalBuy*exChangeRate);
						}
						if(totalSell != null){
							recordVO.setTotalSell(totalSell*exChangeRate);
						}
						Date issuedDate = (Date)objects[5];
						recordVO.setIssuedDate(issuedDate);
						if(issuedDate != null){
							recordVO.setIssuedDateStr(dateFormat.format(issuedDate));
						}
						String status = (String) objects[6];
						String langCode = filter.getLangCode();
						String statusDisplay = PropertyUtils.getPropertyValue(langCode, "order.plan.list.finish.status."+status, null);
						recordVO.setStatus(status);
						recordVO.setStatusDispaly(statusDisplay);
						recordVO.setCustomerId((String) objects[7]);
						String iconUrl = null;
						MemberBase memberBase = (MemberBase) objects[8];
						if(memberBase != null){
							recordVO.setNickName(getCommonMemberName(memberBase.getId(), langCode, "2"));
							iconUrl = memberBase.getIconUrl();
						}
						recordVO.setIconUrl(PageHelper.getUserHeadUrl(iconUrl, ""));
						recordVO.setToCurrency(filter.getToCurrency());
						String currencyName = this.getParamConfigName(filter.getLangCode(), filter.getToCurrency(), CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY);
						recordVO.setCurrencyName(currencyName);
						Date lastUpdate = (Date)objects[9];
						recordVO.setLastUpdate(lastUpdate);
						recordVO.setLastUpdateStr(dateFormat.format(lastUpdate));
						String ifFirst = (String)objects[10];
						recordVO.setIfFirst(ifFirst);
						String ifaId = (String)objects[11];
						if (StringUtils.isNotBlank(ifaId)) {
							MemberIfa ifa = (MemberIfa) baseDao.get(MemberIfa.class, ifaId);
							if(ifa != null){
								recordVO.setIfaName(getCommonMemberName(ifa.getMember().getId(), langCode, "2"));
								recordVO.setIfaIconUrl(PageHelper.getUserHeadUrl(ifa.getMember().getIconUrl(), ""));
							}
						}
						recordVOs.add(recordVO);
					}
					jsonPaging.getList().clear();
					jsonPaging.getList().addAll(recordVOs);
					List counts = this.baseDao.find("SELECT COUNT(*) " + hql.substring(hql.toString().indexOf("FROM")), params.toArray(), false);
					if(counts != null){
						jsonPaging.setTotal(counts.size());
					}
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonPaging;
	}
	
	/**
	 * 获取执行中的定投计划
	 * @author wwluo
	 * @data 2017-03-21
	 * @return
	 */	
	@Override
	public JsonPaging getAipTask(MemberBase loginUser, JsonPaging jsonPaging,TransactionRecordFilterVO filter) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currencyCode = filter.getToCurrency();
		try {
			if(loginUser != null){
				StringBuffer hql = new StringBuffer(""
						+ " SELECT"
						+ " p.id,c,h.portfolioName,oa.aipCount,oa.aipTotalAmount,oa.aipNextTime,oa.createTime,oa.aipState,o.tranFeeCur,oa.aipAmount"
						+ " FROM"
						+ " OrderAipTask o"
						
						+ " LEFT JOIN"
						+ " OrderAip oa"
						+ " ON"
						+ " o.orderAip.id=oa.id"
						
						+ " LEFT JOIN"
						+ " PortfolioHold h"
						+ " ON"
						+ " h.id=oa.portfolioHold.id"
						
						+ " LEFT JOIN"
						+ " OrderPlan p"
						+ " ON"
						+ " p.id=oa.plan.id"
						
						+ " LEFT JOIN"
						+ " MemberBase c"
						+ " ON"
						+ " c.id=h.member.id"
						
						+ " LEFT JOIN"
						+ " MemberIfa i"
						+ " ON"
						+ " i.id=h.ifa.id"
						
						+ " WHERE"
						+ " 1=1" +
						"");
				List<Object> params = new ArrayList<Object>();
				if(filter != null){
					String period = filter.getPeriod();
					String periodType = filter.getPeriodType();
					String fromDate = filter.getFromDate();
					String toDate = filter.getToDate();
					String keyWord = filter.getKeyWord();
					// 过滤期间
					if (StringUtils.isNotBlank(period) && StringUtils.isNotBlank(periodType)){
						Calendar calendar = Calendar.getInstance();
						if(CommonConstants.DATE_TYPE_DAY.equals(periodType)){
							calendar.add(Calendar.DATE, Integer.parseInt(period));
						}else if(CommonConstants.DATE_TYPE_MONTH.equals(periodType)){
							calendar.add(Calendar.MONTH, Integer.parseInt(period));
						}else if(CommonConstants.DATE_TYPE_YEAR.equals(periodType)){
							calendar.add(Calendar.YEAR, Integer.parseInt(period));
						}
						hql.append(" AND oa.createTime BETWEEN ? AND CURDATE()");
						params.add(calendar.getTime());
					}
					// 过滤指定时间
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						hql.append(" AND oa.createTime BETWEEN ? AND ?");
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						params.add(format.parse(fromDate));
						params.add(format.parse(toDate));
					}
					// 过滤关键字（客户名称、组合名称）
					if (StringUtils.isNotBlank(keyWord)) {
						hql.append(" AND (h.portfolioName LIKE ? OR c.nickName LIKE ?)");
						params.add("%"+keyWord+"%");
						params.add("%"+keyWord+"%");
					}
					if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_INVESTOR == loginUser.getMemberType()){
						hql.append(" AND (h.member=? OR p.creator=?)");
						params.add(loginUser);
						params.add(loginUser);
					}else if(CommonConstantsWeb.MEMBERBASE_MEMBERTYPE_IFA == loginUser.getMemberType()){
						hql.append(" AND (i.member=? OR p.creator=?)");
						params.add(loginUser);
						params.add(loginUser);
					}
				}
				hql.append(" GROUP BY p.id");
				jsonPaging.setSort("oa.createTime");
				jsonPaging.setOrder("desc");
				jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
				if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
					List<PlanAipVO> vos = new ArrayList<PlanAipVO>();
					List<Object[]> objs = jsonPaging.getList();
					for (Object[] objects : objs) {
						PlanAipVO vo = new PlanAipVO();
						String id = (String) objects[0];
						String iconUrl = null;
						String nickName = null;
						MemberBase memberBase = (MemberBase) objects[1];
						if(memberBase != null){
							nickName = getCommonMemberName(memberBase.getId(), filter.getLangCode(), "2");
							iconUrl = memberBase.getIconUrl();
						}
						String portfolioName = (String) objects[2];
						Integer aipCount = (Integer) objects[3];
						Double aipTotalAmount = (Double) objects[4];
						Date aipNextTime = (Date) objects[5];
						Date createTime = (Date) objects[6];
						String aipState = (String) objects[7];
						String tranFeeCur = (String) objects[8];
						Double aipAmount = (Double) objects[9];
						if(aipNextTime !=null){
							vo.setAipNextTimeStr(dateFormat.format(aipNextTime));
						}
						if(createTime !=null){
							vo.setCreateTimeStr(dateFormat.format(createTime));
						}
						Double exChangeRete = null;
						if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(tranFeeCur)) {
							exChangeRete = this.getExchangeRate(tranFeeCur, currencyCode);
						}
						if(exChangeRete == null){
							exChangeRete = 1.00;
						}
						vo.setPlanId(id);
						vo.setIconUrl(PageHelper.getUserHeadUrl(iconUrl, ""));
						vo.setNickName(nickName);
						vo.setPortfolioName(portfolioName);
						vo.setAipCount(aipCount);
						if(aipTotalAmount != null){
							vo.setAipTotalAmount(aipTotalAmount*exChangeRete);
						}
						if(aipAmount != null){
							vo.setAipAmount(aipAmount*exChangeRete);
						}
						vo.setAipNextTime(aipNextTime);
						vo.setCreateTime(createTime);
						vo.setAipState(aipState);
						vo.setAipStateDisplay(PropertyUtils.getPropertyValue(filter.getLangCode(), "order.plan.aip.detail.state." + aipState, null));
						vo.setCurrencyCode(currencyCode);
						vo.setCurrencyName(this.getParamConfigName(filter.getLangCode(), currencyCode, CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY));
						vos.add(vo);
					}
					jsonPaging.getList().clear();
					jsonPaging.getList().addAll(vos);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return jsonPaging;
	}
	
	/**
	 * 删除交易计划,逻辑删除
	 * @author wwluo
	 * @data 2017-03-21
	 * @return
	 */	
	@Override
	public void delOrderPlan(String id) {
		if (StringUtils.isNotBlank(id)) {
			OrderPlan plan = (OrderPlan) this.baseDao.get(OrderPlan.class, id);
			plan.setIsValid("0");
			this.baseDao.update(plan);
		}
	}
	
	/**
	 * 获取定投交易记录
	 * @author wwluo
	 * @data 2017-02-23
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<AipOrderHistoryVO> getAipOrderHistory(String holdId,
			String currencyCode, String langCode) {
		List<AipOrderHistoryVO> vos = null;
		if(StringUtils.isNotBlank(holdId)
				&& StringUtils.isNotBlank(currencyCode)
					&& StringUtils.isNotBlank(langCode)){
			StringBuffer hql = new StringBuffer("" +
					" SELECT" +
					" l.fundName,l.fundCurrencyCode,h,i.id" +
					" FROM" +
					" OrderHistory h" +
					" LEFT JOIN" +
					" OrderPlan p" +
					" ON" +
					" p.id=h.plan.id" +
					" LEFT JOIN" +
					" FundInfo i" +
					" ON" +
					" i.product.id=h.product.id" +
					" LEFT JOIN" +
					" FundInfo" + this.getLangFirstCharUpper(langCode) + " l" +
					" ON" +
					" l.id=i.id" +
					" WHERE" +
					" p.aipFlag=1" +
					" AND" +
					" h.portfolioHold.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objs != null && !objs.isEmpty()){
				vos = new ArrayList<AipOrderHistoryVO>();
				for (Object[] obj : objs) {
					AipOrderHistoryVO vo = new AipOrderHistoryVO();
					String fundName = (String) obj[0];
					String fundCurrencyCode = (String) obj[1];
					OrderHistory history = (OrderHistory) obj[2];
					String fundId = (String) obj[3];
					vo.setOrderDate(history.getOrderDate());
					vo.setProductId(history.getProduct().getId());
					vo.setFundInfoId(fundId);
					vo.setFundName(fundName);
					vo.setFundCurrencyCode(fundCurrencyCode);
					String fundCurrencyName = null;
					if (StringUtils.isNotBlank(fundCurrencyCode)) {
						fundCurrencyName = this.getParamConfigName(langCode, fundCurrencyCode, CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY);
						vo.setFundCurrencyName(fundCurrencyName);
					}
					String hisCurrencyCode = history.getCurrencyCode();
					Double hisExchangeRate = null;  // 货币汇率
					if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(hisCurrencyCode)) {
						hisExchangeRate = this.getExchangeRate(hisCurrencyCode, currencyCode);
					}
					if(hisExchangeRate == null){
						hisExchangeRate = 1.00;
					}
					if(history.getCommissionPrice() != null){
						vo.setOrderPrice(history.getCommissionPrice()*hisExchangeRate);
					}
					if(history.getCommissionAmount() != null){
						vo.setOrderAmount(history.getCommissionAmount()*hisExchangeRate);
					}
					vo.setTransactionUnit(history.getTransactionUnit());
					String tranFeeCur = history.getTranFeeCur(); 
					Double tranFeeCurExchangeRate = null;  // 交易货币货币汇率
					if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(tranFeeCur)) {
						tranFeeCurExchangeRate = this.getExchangeRate(tranFeeCur, currencyCode);
					}
					if(tranFeeCurExchangeRate == null){
						tranFeeCurExchangeRate = 1.00;
					}
					if(history.getCommissionAmount() != null){
						vo.setTransactionAmount(history.getCommissionAmount()*tranFeeCurExchangeRate);
					}
					if(history.getTransactionPrice() != null){
						vo.setTransactionPrice(history.getTransactionPrice()*tranFeeCurExchangeRate);
					}
					vo.setCloseTime(history.getCloseTime()); // 成交时间
					vo.setFee(history.getFee());
					if(history.getAccount() != null){
						vo.setAccountId(history.getAccount().getId());
					}
					vo.setAccountNO(history.getAccountNo());
					vo.setStatus(history.getStatus());
					String statusDisplay = PropertyUtils.getPropertyValue(langCode, "order.plan.history.status.display."+history.getStatus(), null);
					vo.setStatusDisplay(statusDisplay);
					vos.add(vo);
				}
			}
		}
		return vos;
	}

}
