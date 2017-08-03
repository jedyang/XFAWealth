package com.fsll.wmes.investor.vo;

import java.util.List;

public class AccountVO {

	public String id;
	private String memberId;
	private String accountNo;
	private String fromType;
	private String accType;
	private String cies;
	private String baseCurrency;
	private String baseCurName;
	private String totalAssest;
	private String cash;
	private String productValue;
	private String cashPercent;
	private String productValuePercent;
	private String nextRPQDate;
	private String nextRPQDateStr;
	private String nextDCDate;
	private String nextDCDateStr;
	private String distributorId;
	private String distributor;
	private String distributorIcon;
	private String flowStatus;
	private String openStatus;
	private String subFlag;
	private String subRelateId;// 当subFlag等于1时有效,表示关联的主帐户
	private String faca;
	private String loginCode;
	// private String companyLogoUrl;
	private String ifaId;
	private String ifaName;
	private String ifafirmId;
	private String ifafirmName;
	private String ifafirmIcon;
	private String isValid;
	private String riskLevel;
	private String chartData;
	private String customerName;
	private String docStauts;
	private int docCheckCount;

	private List subAccounts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getFromType() {
		return fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getCies() {
		return cies;
	}

	public void setCies(String cies) {
		this.cies = cies;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getBaseCurName() {
		return baseCurName;
	}

	public void setBaseCurName(String baseCurName) {
		this.baseCurName = baseCurName;
	}

	public String getTotalAssest() {
		return totalAssest;
	}

	public void setTotalAssest(String totalAssest) {
		this.totalAssest = totalAssest;
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = cash;
	}

	public String getProductValue() {
		return productValue;
	}

	public void setProductValue(String productValue) {
		this.productValue = productValue;
	}

	public String getCashPercent() {
		return cashPercent;
	}

	public void setCashPercent(String cashPercent) {
		this.cashPercent = cashPercent;
	}

	public String getProductValuePercent() {
		return productValuePercent;
	}

	public void setProductValuePercent(String productValuePercent) {
		this.productValuePercent = productValuePercent;
	}

	public String getNextRPQDate() {
		return nextRPQDate;
	}

	public void setNextRPQDate(String nextRPQDate) {
		this.nextRPQDate = nextRPQDate;
	}

	public String getNextDCDate() {
		return nextDCDate;
	}

	public void setNextDCDate(String nextDCDate) {
		this.nextDCDate = nextDCDate;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDistributorIcon() {
		return distributorIcon;
	}

	public void setDistributorIcon(String distributorIcon) {
		this.distributorIcon = distributorIcon;
	}

	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public String getSubFlag() {
		return subFlag;
	}

	public void setSubFlag(String subFlag) {
		this.subFlag = subFlag;
	}

	public String getFaca() {
		return faca;
	}

	public void setFaca(String faca) {
		this.faca = faca;
	}

	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	/*
	 * public String getCompanyLogoUrl() { return companyLogoUrl; } public void
	 * setCompanyLogoUrl(String companyLogoUrl) { this.companyLogoUrl =
	 * companyLogoUrl; }
	 */
	public String getIfaId() {
		return ifaId;
	}

	public String getIfafirmId() {
		return ifafirmId;
	}

	public void setIfafirmId(String ifafirmId) {
		this.ifafirmId = ifafirmId;
	}

	public String getIfafirmName() {
		return ifafirmName;
	}

	public void setIfafirmName(String ifafirmName) {
		this.ifafirmName = ifafirmName;
	}

	public String getIfafirmIcon() {
		return ifafirmIcon;
	}

	public void setIfafirmIcon(String ifafirmIcon) {
		this.ifafirmIcon = ifafirmIcon;
	}

	public String getIfaName() {
		return ifaName;
	}

	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}

	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getChartData() {
		return chartData;
	}

	public void setChartData(String chartData) {
		this.chartData = chartData;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSubRelateId() {
		return subRelateId;
	}

	public void setSubRelateId(String subRelateId) {
		this.subRelateId = subRelateId;
	}

	public void setSubAccounts(List subAccounts) {
		this.subAccounts = subAccounts;
	}

	public List getSubAccounts() {
		return subAccounts;
	}

	public String getDocStauts() {
		return docStauts;
	}

	public void setDocStauts(String docStauts) {
		this.docStauts = docStauts;
	}

	public int getDocCheckCount() {
		return docCheckCount;
	}

	public void setDocCheckCount(int docCheckCount) {
		this.docCheckCount = docCheckCount;
	}

	public String getNextRPQDateStr() {
		return nextRPQDateStr;
	}

	public void setNextRPQDateStr(String nextRPQDateStr) {
		this.nextRPQDateStr = nextRPQDateStr;
	}

	public String getNextDCDateStr() {
		return nextDCDateStr;
	}

	public void setNextDCDateStr(String nextDCDateStr) {
		this.nextDCDateStr = nextDCDateStr;
	}

}
