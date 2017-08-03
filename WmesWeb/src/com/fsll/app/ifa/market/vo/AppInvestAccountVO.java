package com.fsll.app.ifa.market.vo;

public class AppInvestAccountVO {
	private String productId;//产品ID
	private String accountId;//交易账号
	private String accountNo;//交易账号
	private String subFlag;//1：子账户 0：主账户
	private String totalFlag;//投资时，显示的总投资金额，已包含各种交易费用,1是,0否,
	private String openStatus;//开户状态
	private String companyName;//代理商
	private String companyLogo;//代理商Logo
	private String baseCurrency;//货币
	private String cashAvailable;//可用现金
	private String cashHold;//冻结现金
	private String cashWithdrawal;//可取现金
	private String transactionFeeRate;//交易费率（去除百分号）
	private String transactionFeeCur;//交易货币
	private String transactionFeeMini;//最低交易费
	private String riskWarning;//产品风险是否超过个人承受风险等级，1：是，0：否
	private String ifaId;//Ifa Id
	private String ifaMemberId; //ifa Member Id
	private String ifaName;//Ifa名字
	
	public String getProductId(){
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
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
	public String getSubFlag(){
		return subFlag;
	}
	public void setSubFlag(String subFlag) {
		this.subFlag = subFlag;
	}
	public String getTotalFlag() {
		return totalFlag;
	}
	public void setTotalFlag(String totalFlag) {
		this.totalFlag = totalFlag;
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
	public String getCompanyLogo(){
		return companyLogo;
	}
	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getBaseCurrency(){
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
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
	public String getTransactionFeeRate() {
		return transactionFeeRate;
	}
	public void setTransactionFeeRate(String transactionFeeRate) {
		this.transactionFeeRate = transactionFeeRate;
	}
	public String getTransactionFeeCur() {
		return transactionFeeCur;
	}
	public void setTransactionFeeCur(String transactionFeeCur) {
		this.transactionFeeCur = transactionFeeCur;
	}
	public String getTransactionFeeMini() {
		return transactionFeeMini;
	}
	public void setTransactionFeeMini(String transactionFeeMini) {
		this.transactionFeeMini = transactionFeeMini;
	}
	public String getRiskWarning() {
		return riskWarning;
	}
	public void setRiskWarning(String riskWarning) {
		this.riskWarning = riskWarning;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getIfaMemberId() {
		return ifaMemberId;
	}
	public void setIfaMemberId(String ifaMemberId) {
		this.ifaMemberId = ifaMemberId;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	
}
