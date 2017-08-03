package com.fsll.wmes.portfolio.vo;

import com.fsll.common.util.DateUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductInfo;

public class PortfolioHoldProductCumperfVO {

	private String id;
    private PortfolioHold portfolioHold;
	private ProductInfo product;
	private InvestorAccount account;
	private String accountNo;
	private String valuationDate;
	private double cumperfRate;
	private double totalPl;
	private String totalPlStr;
	private double dayPl;
	private String dayPlStr;
	private String createTime;
	private String lastUpdate;
	
	public PortfolioHoldProductCumperfVO(PortfolioHoldProductCumperf vo){
		this.id=vo.getId();
		this.portfolioHold=vo.getPortfolioHold();
		this.product=vo.getProduct();
		this.account=vo.getAccount();
		this.accountNo=vo.getAccountNo();
		this.valuationDate=DateUtil.getDateStr(vo.getValuationDate());
		this.cumperfRate=vo.getCumperfRate();
		this.totalPl=vo.getTotalPl();
		this.totalPlStr=StrUtils.getNumberString(totalPl);
		this.dayPl=vo.getDayPl();
		this.dayPlStr=StrUtils.getNumberString(dayPl);
		this.createTime=DateUtil.getDateStr(vo.getCreateTime());
		this.lastUpdate=DateUtil.getDateStr(vo.getLastUpdate());
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}

	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
	}

	public ProductInfo getProduct() {
		return product;
	}
	public void setProduct(ProductInfo product) {
		this.product = product;
	}
	public InvestorAccount getAccount() {
		return account;
	}
	public void setAccount(InvestorAccount account) {
		this.account = account;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getValuationDate() {
		return valuationDate;
	}
	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
	}
	public double getCumperfRate() {
		return cumperfRate;
	}
	public void setCumperfRate(double cumperfRate) {
		this.cumperfRate = cumperfRate;
	}
	public double getTotalPl() {
		return totalPl;
	}
	public void setTotalPl(double totalPl) {
		this.totalPl = totalPl;
	}
	public double getDayPl() {
		return dayPl;
	}
	public void setDayPl(double dayPl) {
		this.dayPl = dayPl;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getTotalPlStr() {
		return totalPlStr;
	}

	public void setTotalPlStr(String totalPlStr) {
		this.totalPlStr = totalPlStr;
	}

	public String getDayPlStr() {
		return dayPlStr;
	}

	public void setDayPlStr(String dayPlStr) {
		this.dayPlStr = dayPlStr;
	}
	
	
	
}
