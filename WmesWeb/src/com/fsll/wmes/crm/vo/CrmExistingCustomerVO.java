package com.fsll.wmes.crm.vo;

import java.util.List;


public class CrmExistingCustomerVO {
	private String id;
	private String ifaId;
	private String memberId;
	private String mobileCode;
	private String mobileNumber;
	private String email;
	private String nickName;
	private String pinyin;
	private String groupName;
	
	private String birthdayRemind;
	private String appointmentRemind;	
	private String createTime;
	private String lastUpdate;	
	
	//持仓信息
	List<CrmPortfolioHoldVO> holdList;
	
	//开户信息
	private String investorId;
	private String distributorId;
	private String companyName;	
	private String logofile;
	private String openStatus;
	private String accountSum;
	private String accountLastUpdate;
	
	
	//Proposal信息
	private String proposalId;
	private String proposalInvAmount;
	private String proposalCurrency;
	private String proposalStatus;
	private String proposalLastUpdate;
	
	//Portfolio信息
	private String portfolioHoldId;
	private String portfolioId;
	private String portfolioCurrency;
	private String planId;
	private String planStatus;
	private String planBuy;
	private String planSell;
	private String planLastUpdate;
	
	//not-yet-invest
	private String accountNo;
	private String openTime;
	
	//kyc
	private String rpqExpireDays;
	private String docExpireDays;
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getIfaId() {
		return this.ifaId;
	}

	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}



	public String getMobileCode() {
		return this.mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}


	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public String getPinyin() {
		return this.pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	

	public String getBirthdayRemind() {
		return this.birthdayRemind;
	}

	public void setBirthdayRemind(String birthdayRemind) {
		this.birthdayRemind = birthdayRemind;
	}
	

	public String getAppointmentRemind() {
		return this.appointmentRemind;
	}

	public void setAppointmentRemind(String appointmentRemind) {
		this.appointmentRemind = appointmentRemind;
	}
	

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public List<CrmPortfolioHoldVO> getHoldList() {
		return this.holdList;
	}

	public void setHoldList(List<CrmPortfolioHoldVO> holdList) {
		this.holdList = holdList;
	}
	
	public String getInvestorId() {
		return this.investorId;
	}

	public void setInvestorId(String investorId) {
		this.investorId = investorId;
	}

	public String getDistributorId() {
		return this.distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	public String getLogofile() {
		return this.logofile;
	}

	public void setLogofile(String logofile) {
		this.logofile = logofile;
	}
	public String getOpenStatus() {
		return this.openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
	
	public String getAccountSum() {
		return this.accountSum;
	}

	public void setAccountSum(String accountSum) {
		this.accountSum = accountSum;
	}

	public String getAccountLastUpdate() {
		return this.accountLastUpdate;
	}

	public void setAccountLastUpdate(String accountLastUpdate) {
		this.accountLastUpdate = accountLastUpdate;
	}
	
	public String getProposalId() {
		return this.proposalId;
	}

	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}

	public String getProposalInvAmount() {
		return this.proposalInvAmount;
	}

	public void setProposalInvAmount(String proposalInvAmount) {
		this.proposalInvAmount = proposalInvAmount;
	}
	
	public String getProposalCurrency() {
		return this.proposalCurrency;
	}

	public void setProposalCurrency(String proposalCurrency) {
		this.proposalCurrency = proposalCurrency;
	}

	public String getProposalStatus() {
		return this.proposalStatus;
	}

	public void setProposalStatus(String proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public String getProposalLastUpdate() {
		return this.proposalLastUpdate;
	}

	public void setProposalLastUpdate(String proposalLastUpdate) {
		this.proposalLastUpdate = proposalLastUpdate;
	}
	
	public String getPortfolioHoldId() {
		return this.portfolioHoldId;
	}

	public void setPortfolioHoldId(String portfolioHoldId) {
		this.portfolioHoldId = portfolioHoldId;
	}
	
	public String getPortfolioId() {
		return this.portfolioId;
	}

	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	
	public String getPortfolioCurrency() {
		return this.portfolioCurrency;
	}

	public void setPortfolioCurrency(String portfolioCurrency) {
		this.portfolioCurrency = portfolioCurrency;
	}
	
	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}
	
	public String getPlanStatus() {
		return this.planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}
	
	public String getPlanBuy() {
		return this.planBuy;
	}

	public void setPlanBuy(String planBuy) {
		this.planBuy = planBuy;
	}
	
	public String getPlanSell() {
		return this.planSell;
	}

	public void setPlanSell(String planSell) {
		this.planSell = planSell;
	}

	public String getPlanLastUpdate() {
		return this.planLastUpdate;
	}

	public void setPlanLastUpdate(String planLastUpdate) {
		this.planLastUpdate = planLastUpdate;
	}
	
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
	public String getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	

	public String getRpqExpireDays() {
		return this.rpqExpireDays;
	}

	public void setRpqExpireDays(String rpqExpireDays) {
		this.rpqExpireDays = rpqExpireDays;
	}

	public String getDocExpireDays() {
		return this.docExpireDays;
	}

	public void setDocExpireDays(String docExpireDays) {
		this.docExpireDays = docExpireDays;
	}
	
}
