package com.fsll.app.ifa.market.vo;

/**
 * 组合购买、调整（确认状态） 产品实体类VO
 * @author zxtan
 * @date 2017-03-01
 */
public class AppInvestProductForCheckVO {
	private String productId;//产品ID
	private String fundId;//基金ID
	private String fundName;//基金名称
	private String fundType;//类型
	private String riskLevel;//风险评级
	private String issueCurrency;//基金发行时的货币
	private String issueCurrencyCode;//基金发行时的货币
	private String fundCurrency;//净值货币
	private String fundCurrencyCode;//净值货币
	private String toCurrency;//货币兑换	
	private String weight;//占比
	private String holdingUnit;//可用份额(减持或转出适用)
	private String unit;//份额
	private String amount;//投资金额
	private String lastNav;//最新资产净值价格
	private String fromProductId;//原有产品ID
	private String tranType;//交易方向，B:买入，S:卖出
	private String tranRate;//交易费率
	private String tranFee;//交易费	
	private String accountId;//交易账号Id
	private String accountNo;//交易账号
	private String dividend;//股息选项,R:Reinvestment再投资;D:分配到现金账户
	private String switchFlag;//转换标识，1：是，0：否
	private String switchGroup;//转换时生成的组
	private String switchWeight;//转入占比
	
	
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
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getIssueCurrency() {
		return issueCurrency;
	}
	public void setIssueCurrency(String issueCurrency) {
		this.issueCurrency = issueCurrency;
	}
	public String getIssueCurrencyCode() {
		return issueCurrencyCode;
	}
	public void setIssueCurrencyCode(String issueCurrencyCode) {
		this.issueCurrencyCode = issueCurrencyCode;
	}
	public String getFundCurrency() {
		return fundCurrency;
	}
	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}
	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}
	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
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
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getHoldingUnit() {
		return holdingUnit;
	}
	public void setHoldingUnit(String holdingUnit) {
		this.holdingUnit = holdingUnit;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getLastNav() {
		return lastNav;
	}
	public void setLastNav(String lastNav) {
		this.lastNav = lastNav;
	}
	public String getFromProductId() {
		return fromProductId;
	}
	public void setFromProductId(String fromProductId) {
		this.fromProductId = fromProductId;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getTranRate() {
		return tranRate;
	}
	public void setTranRate(String tranRate) {
		this.tranRate = tranRate;
	}
	public String getTranFee() {
		return tranFee;
	}
	public void setTranFee(String tranFee) {
		this.tranFee = tranFee;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getDividend() {
		return dividend;
	}
	public void setDividend(String dividend) {
		this.dividend = dividend;
	}
	public String getSwitchFlag() {
		return switchFlag;
	}
	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}
	public String getSwitchGroup() {
		return switchGroup;
	}
	public void setSwitchGroup(String switchGroup) {
		this.switchGroup = switchGroup;
	}
	public String getSwitchWeight() {
		return switchWeight;
	}
	public void setSwitchWeight(String switchWeight) {
		this.switchWeight = switchWeight;
	}
	
}
