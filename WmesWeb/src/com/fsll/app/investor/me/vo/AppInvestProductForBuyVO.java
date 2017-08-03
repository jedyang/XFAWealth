package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 组合分析 产品实体类VO
 * @author zxtan
 * @date 2017-01-20
 */
public class AppInvestProductForBuyVO {
	private String productId;//产品ID
	private String fundId;//基金ID
	private String fundName;//基金名称
	private String fundCurrency;//基金货币
	private String toCurrency;//货币兑换	
	private String fundType;//基金类型
	private String riskLevel;//基金风险评级
	private String lastNav;//最新净值	
	private String minInitialAmount;//最少认购额
	private String allocationRate;//分配比率
	private List<AppInvestAccountVO> accountList;//投资账号列表
		

	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getAllocationRate() {
		return allocationRate;
	}
	public void setAllocationRate(String allocationRate) {
		this.allocationRate = allocationRate;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFundCurrency() {
		return fundCurrency;
	}
	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
	public String getLastNav() {
		return lastNav;
	}
	public void setLastNav(String lastNav) {
		this.lastNav = lastNav;
	}
	public String getMinInitialAmount() {
		return minInitialAmount;
	}
	public void setMinInitialAmount(String minInitialAmount) {
		this.minInitialAmount = minInitialAmount;
	}
	public List<AppInvestAccountVO> getAccountList() {
		return accountList;
	}
	public void setAccountList(List<AppInvestAccountVO> accountList) {
		this.accountList = accountList;
	}
	
}
