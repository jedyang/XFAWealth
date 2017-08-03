package com.fsll.wmes.trade.vo;

import java.util.Date;
import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioInfo;
/**
 * 
 * @author 林文伟
 * @date 2016-10-10
 */
public class TransactionRecordVO {
	
	private MemberBase client;
	private PortfolioInfo portfolio;
	private PortfolioHold portfolioHold;
	private OrderPlan orderPlan;
	private String toCurrency;
	//交易产品买入总金额
	private Double totalBuy;
	//交易产品卖入总金额
	private Double totalSell;

	private String currencyName;
	private String customerId;
	private String iconUrl;
	private String nickName;
	private String ifaName;
	private String ifaIconUrl;
	private Date issuedDate;
	private String issuedDateStr;
	private Date lastUpdate;
	private String lastUpdateStr;
	private String status;
	private String statusDispaly;
	private String orderPlanId;
	private String ifFirst; // 组合是否第一次交易
	
	private Integer memberType;
	
	
	public String getOrderPlanId() {
		return orderPlanId;
	}
	public void setOrderPlanId(String orderPlanId) {
		this.orderPlanId = orderPlanId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	private String portfolioName;
	private List<OrderHistory> orderHistoryList;
	
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public List<OrderHistory> getOrderHistoryList() {
		return orderHistoryList;
	}
	public void setOrderHistoryList(List<OrderHistory> orderHistoryList) {
		orderHistoryList = orderHistoryList;
	}
	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}
	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
	}
	public MemberBase getClient() {
		return client;
	}
	public void setClient(MemberBase client) {
		this.client = client;
	}
	public PortfolioInfo getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
	}
	public OrderPlan getOrderPlan() {
		return orderPlan;
	}
	public void setOrderPlan(OrderPlan orderPlan) {
		this.orderPlan = orderPlan;
	}
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
	public Double getTotalBuy() {
		return totalBuy;
	}
	public void setTotalBuy(Double totalBuy) {
		this.totalBuy = totalBuy;
	}
	public Double getTotalSell() {
		return totalSell;
	}
	public void setTotalSell(Double totalSell) {
		this.totalSell = totalSell;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public Date getIssuedDate() {
		return issuedDate;
	}
	public void setIssuedDate(Date issuedDate) {
		this.issuedDate = issuedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusDispaly() {
		return statusDispaly;
	}
	public void setStatusDispaly(String statusDispaly) {
		this.statusDispaly = statusDispaly;
	}
	public String getIssuedDateStr() {
		return issuedDateStr;
	}
	public void setIssuedDateStr(String issuedDateStr) {
		this.issuedDateStr = issuedDateStr;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getLastUpdateStr() {
		return lastUpdateStr;
	}
	public void setLastUpdateStr(String lastUpdateStr) {
		this.lastUpdateStr = lastUpdateStr;
	}
	public String getIfFirst() {
		return ifFirst;
	}
	public void setIfFirst(String ifFirst) {
		this.ifFirst = ifFirst;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public Integer getMemberType() {
		return memberType;
	}
	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}
	public String getIfaIconUrl() {
		return ifaIconUrl;
	}
	public void setIfaIconUrl(String ifaIconUrl) {
		this.ifaIconUrl = ifaIconUrl;
	}
	
}
