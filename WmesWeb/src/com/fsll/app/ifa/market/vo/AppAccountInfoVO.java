package com.fsll.app.ifa.market.vo;

public class AppAccountInfoVO {
	private String accountId;//交易账号
	private String accountNo;//交易账号
	private String accountCurrency;
	private String subFlag;//1：子账户 0：主账户
	private String cies;
	private String openStatus;//开户状态
	private String companyName;//代理商
	private String portfolioName;//组合名称
	private String baseCurrency;//货币
	private String totalAsset;//账户总资产
	private String marketValue;//组合市值
	private String totalCash;//总现金
	private String cashAvailable;//可用现金
	private String cashHold;//冻结现金
	private String cashWithdrawal;//可取现金
	private String marketValueWeight;//组合市值占比
	private String totalCashWeight;//总现金占比
	
	public String getAccountId(){
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNo(){
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountCurrency() {
		return accountCurrency;
	}
	public void setAccountCurrency(String accountCurrency) {
		this.accountCurrency = accountCurrency;
	}
	public String getSubFlag(){
		return subFlag;
	}
	public void setSubFlag(String subFlag) {
		this.subFlag = subFlag;
	}
	public String getCies() {
		return cies;
	}
	public void setCies(String cies) {
		this.cies = cies;
	}
	public String getOpenStatus(){
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
	public String getCompanyName(){
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPortfolioName(){
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getBaseCurrency(){
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getMarketValue(){
		return marketValue;
	}
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}
	public String getCashAvailable(){
		return cashAvailable;
	}
	public void setCashAvailable(String cashAvailable) {
		this.cashAvailable = cashAvailable;
	}
	public String getCashHold(){
		return cashHold;
	}
	public void setCashHold(String cashHold) {
		this.cashHold = cashHold;
	}
	public String getCashWithdrawal(){
		return cashWithdrawal;
	}
	public void setCashWithdrawal(String cashWithdrawal) {
		this.cashWithdrawal = cashWithdrawal;
	}
	public String getTotalAsset(){
		return totalAsset;
	}
	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}
	public String getTotalCash(){
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	public String getTotalCashWeight(){
		return totalCashWeight;
	}
	public void setTotalCashWeight(String totalCashWeight) {
		this.totalCashWeight = totalCashWeight;
	}
	public String getMarketValueWeight(){
		return marketValueWeight;
	}
	public void setMarketValueWeight(String marketValueWeight) {
		this.marketValueWeight = marketValueWeight;
	}
	
}
