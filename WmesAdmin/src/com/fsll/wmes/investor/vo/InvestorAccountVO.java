package com.fsll.wmes.investor.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;

public class InvestorAccountVO {
	private String id;
	private String memberId;
	private String loginCode;
	private String nickName;
	private String iconUrl;
	private String email;
	private String password;
	private String mobileNumber;
	private String companyName;
	private String gender;

	private String accountId;
	private String accountNo;
	private String totalFlag;
	private String fromType;
	private String accType;
	private String investType;
	private String cies;
	private String dividend;
	private String baseCurrency;
	private String currency; // 货币名称（显示）
	private String currencyCode; // 货币code
	private String purpose;
	private String purposeOther;
	private String sentBy;
	private String discFlag;
	private String distributorId;
	private String distributorName;
	private String ifaId;
	private String ifaName;
	private String submitStatus;
	private String openStatus;
	private String curStep;
	private String finishStatus;
	private String flowId;
	private String flowStatus;
	private String createBy;
	private String createByName;
	private Date createTime;
	private String authorized;
	private String subFlag;
	private String faca;
	private String lastUpdate;
	private String isValid;
	private String keyword;
	
	private String totalValue;
	private Double marketValue;
	private String cashValue;
	private String cashAvailable;
	private String cashWithdrawal;
	private String cashForPendingTran;
	private String marketRatio;
	private String cashRatio;
	private String totalCash;
	private String distributorIconUrl;
	private MemberDistributor distributor;
	
	private String ifaIconUrl;
	private MemberIfa ifa;
	
	private String time;
	private String subRelateId;
	private String subFlagDisplay;
	private String sourceFrom;
	
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
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getTotalFlag() {
		return totalFlag;
	}
	public void setTotalFlag(String totalFlag) {
		this.totalFlag = totalFlag;
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
	public String getInvestType() {
		return investType;
	}
	public void setInvestType(String investType) {
		this.investType = investType;
	}
	public String getCies() {
		return cies;
	}
	public void setCies(String cies) {
		this.cies = cies;
	}
	public String getDividend() {
		return dividend;
	}
	public void setDividend(String dividend) {
		this.dividend = dividend;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getPurposeOther() {
		return purposeOther;
	}
	public void setPurposeOther(String purposeOther) {
		this.purposeOther = purposeOther;
	}
	public String getSentBy() {
		return sentBy;
	}
	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}
	public String getDiscFlag() {
		return discFlag;
	}
	public void setDiscFlag(String discFlag) {
		this.discFlag = discFlag;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}
	public String getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
	public String getCurStep() {
		return curStep;
	}
	public void setCurStep(String curStep) {
		this.curStep = curStep;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}
	public String getFlowStatus() {
		return flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAuthorized() {
		return authorized;
	}
	public void setAuthorized(String authorized) {
		this.authorized = authorized;
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
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	public String getTotalValue() {
		return totalValue;
	}
	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}
	public String getCashValue() {
		return cashValue;
	}
	public void setCashValue(String cashValue) {
		this.cashValue = cashValue;
	}
	
	public String getCashAvailable() {
		return cashAvailable;
	}
	public void setCashAvailable(String cashAvailable) {
		this.cashAvailable = cashAvailable;
	}
	public String getCashForPendingTran() {
		return cashForPendingTran;
	}
	public void setCashForPendingTran(String cashForPendingTran) {
		this.cashForPendingTran = cashForPendingTran;
	}
	public String getMarketRatio() {
		return marketRatio;
	}
	public void setMarketRatio(String marketRatio) {
		this.marketRatio = marketRatio;
	}
	public String getCashRatio() {
		return cashRatio;
	}
	public void setCashRatio(String cashRatio) {
		this.cashRatio = cashRatio;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getDistributorIconUrl() {
		return distributorIconUrl;
	}
	public void setDistributorIconUrl(String distributorIconUrl) {
		this.distributorIconUrl = distributorIconUrl;
	}
	public MemberDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}
	public String getIfaIconUrl() {
		return ifaIconUrl;
	}
	public void setIfaIconUrl(String ifaIconUrl) {
		this.ifaIconUrl = ifaIconUrl;
	}
	public MemberIfa getIfa() {
		return ifa;
	}
	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getSubRelateId() {
		return subRelateId;
	}
	public void setSubRelateId(String subRelateId) {
		this.subRelateId = subRelateId;
	}
	public String getSubFlagDisplay() {
		return subFlagDisplay;
	}
	public void setSubFlagDisplay(String subFlagDisplay) {
		this.subFlagDisplay = subFlagDisplay;
	}
	public String getSourceFrom() {
		return sourceFrom;
	}
	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public Double getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
	}
	public String getCashWithdrawal() {
		return cashWithdrawal;
	}
	public void setCashWithdrawal(String cashWithdrawal) {
		this.cashWithdrawal = cashWithdrawal;
	}
	public String getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	
}
