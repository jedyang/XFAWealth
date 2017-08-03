package com.fsll.wmes.investor.vo;

import java.util.Date;

import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;

public class InvestorAccountVO {
	private String id;
	private String memberId;
	private MemberBase member;
	private String loginCode;
	private String nickName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String mobileNumber;
	private String companyName;
	private String gender;
	private String iconUrl;

	private String accountId;
	private String accountNo;
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
	private String distributorIconUrl;
	private MemberDistributor distributor;
	
	private String ifaId;
	private String ifaName;
	private String ifaIconUrl;
	private MemberIfa ifa;
	
	private String submitStatus;
	private String openStatus;
	private String curStep;
	private String finishStatus;
	private String flowId;
	private String flowStatus;
	private String createBy;
	private String createByName;
	private Date createTime;
	private String time;
	private String authorized;
	private String subFlag;
	private String subRelateId;
	private String subFlagDisplay;
	private String faca;
	private String sourceFrom;
	
	private String totalValue;
	private Double marketValue;
	private String cashValue;
	private String cashAvailable;
	private String cashWithdrawal;
	private String cashForPendingTran;
	private String marketRatio;
	private String cashRatio;
	private String totalCash;
	
	private String ifaFirmId;
	private String ifaFirmName;
	private MemberIfafirm ifafirm;
	
	private Integer rpqRiskLevel; //RPQ 风险等级
	private String holdId;  
	private String totalFlag; //  '1是,0否,投资时，显示的总投资金额，已包含各种交易费用',
	private String totalFlagDisplay; 
	
	private String isValid;
	private String clientType;
	private String tradeType;
	private String clientStatus;
	private String upfrontFee;
	private String hurdleRate;
	private String performanceFeeRate;
	private String advisorFee;
	
	public InvestorAccountVO() {
	    super();
	}
	
	public InvestorAccountVO(InvestorAccount vo) {
	    super();
	    this.id = vo.getId();
	    if (null!=vo.getMember()){
		    this.memberId = vo.getMember().getId();
		    this.member = vo.getMember();
		    this.loginCode = vo.getMember().getLoginCode();
		    this.nickName = vo.getMember().getNickName();
		    this.iconUrl = vo.getMember().getIconUrl();
	    }
	    /*
	    this.firstName = firstName;
	    this.lastName = lastName;
	    this.email = email;
	    this.mobileNumber = mobileNumber;
	    this.companyName = companyName;
	    this.gender = gender;
	    */
	    if (null!=vo.getDistributor()){
		    this.distributorId = vo.getDistributor().getId();
		    this.distributorName = vo.getDistributor().getCompanyName();
		    this.distributorIconUrl = vo.getDistributor().getLogofile();
		    this.distributor = vo.getDistributor();
	    }
	    if (null!=vo.getIfa()){
		    this.ifaId = vo.getIfa().getId();
		    this.ifaName = vo.getIfa().getFirstName()+" "+vo.getIfa().getLastName();
		    this.ifaIconUrl = vo.getIfa().getMember().getIconUrl();
		    this.ifa = vo.getIfa();
	    }
	    /*
	    this.ifaFirmId = ifaFirmId;
	    this.ifaFirmName = ifaFirmName;
	    this.ifafirm = ifafirm;
	     */
	    this.accountId = vo.getId();
	    this.accountNo = vo.getAccountNo();
	    this.fromType = vo.getFromType();
	    this.accType = vo.getAccType();
	    this.investType = vo.getInvestType();
	    this.cies = vo.getCies();
	    this.dividend = vo.getDividend();
	    this.baseCurrency = vo.getBaseCurrency();
	    this.purpose = vo.getPurpose();
	    this.purposeOther = vo.getPurposeOther();
	    this.sentBy = vo.getSentBy();
	    this.discFlag = vo.getDiscFlag();

	    this.currency = vo.getBaseCurrency();
	    this.currencyCode = vo.getBaseCurrency();

	    this.submitStatus = vo.getSubmitStatus();
	    this.openStatus = vo.getOpenStatus();
	    this.curStep = vo.getCurStep();
	    this.finishStatus = vo.getFinishStatus();
	    this.flowId = vo.getFlowId();
	    this.flowStatus = vo.getFlowStatus();
	    if (null!=vo.getCreateBy()){
		    this.createBy = vo.getCreateBy().getId();
		    this.createByName = vo.getCreateBy().getNickName();
	    }
	    this.createTime = vo.getCreateTime();
	    this.authorized = vo.getAuthorized();
	    this.subFlag = vo.getSubFlag();
//	    this.subFlagDisplay = subFlagDisplay;
	    this.faca = vo.getFaca();
	    this.sourceFrom = vo.getSourceFrom();
	    this.totalFlag = vo.getTotalFlag();
	    this.isValid = vo.getIsValid();

	    /*
	    this.totalValue = totalValue;
	    this.marketValue = marketValue;
	    this.cashValue = cashValue;
	    this.cashAvailable = cashAvailable;
	    this.cashWithdrawal = cashWithdrawal;
	    this.cashForPendingTran = cashForPendingTran;
	    this.marketRatio = marketRatio;
	    this.cashRatio = cashRatio;
	    this.totalCash = totalCash;
	    this.rpqRiskLevel = rpqRiskLevel;
	    this.holdId = holdId;
	    this.totalFlagDisplay = totalFlagDisplay;
	    this.clientType = clientType;
	    this.tradeType = tradeType;
	    this.clientStatus = clientStatus;
	    this.upfrontFee = upfrontFee;
	    this.hurdleRate = hurdleRate;
	    this.performanceFeeRate = performanceFeeRate;
	    this.advisorFee = advisorFee;
	    */
    }
	
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	
	
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
	public String getDistributorIconUrl() {
		return distributorIconUrl;
	}
	public void setDistributorIconUrl(String distributorIconUrl) {
		this.distributorIconUrl = distributorIconUrl;
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
	
	public Double getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
	}
	public String getCashAvailable() {
		return cashAvailable;
	}
	public void setCashAvailable(String cashAvailable) {
		this.cashAvailable = cashAvailable;
	}
	public String getCashWithdrawal() {
		return cashWithdrawal;
	}
	public void setCashWithdrawal(String cashWithdrawal) {
		this.cashWithdrawal = cashWithdrawal;
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
	public String getIfaIconUrl() {
		return ifaIconUrl;
	}
	public void setIfaIconUrl(String ifaIconUrl) {
		this.ifaIconUrl = ifaIconUrl;
	}
	public String getIfaFirmId() {
		return ifaFirmId;
	}
	public void setIfaFirmId(String ifaFirmId) {
		this.ifaFirmId = ifaFirmId;
	}
	public String getIfaFirmName() {
		return ifaFirmName;
	}
	public void setIfaFirmName(String ifaFirmName) {
		this.ifaFirmName = ifaFirmName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
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
	public Integer getRpqRiskLevel() {
		return rpqRiskLevel;
	}
	public void setRpqRiskLevel(Integer rpqRiskLevel) {
		this.rpqRiskLevel = rpqRiskLevel;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getHoldId() {
		return holdId;
	}
	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
	public String getSubFlagDisplay() {
		return subFlagDisplay;
	}
	public void setSubFlagDisplay(String subFlagDisplay) {
		this.subFlagDisplay = subFlagDisplay;
	}
	public String getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	public String getTotalFlagDisplay() {
		return totalFlagDisplay;
	}
	public void setTotalFlagDisplay(String totalFlagDisplay) {
		this.totalFlagDisplay = totalFlagDisplay;
	}
	public String getIsValid() {
    	return isValid;
    }
	public void setIsValid(String isValid) {
    	this.isValid = isValid;
    }
	public String getClientType() {
    	return clientType;
    }
	public void setClientType(String clientType) {
    	this.clientType = clientType;
    }
	public String getTradeType() {
    	return tradeType;
    }
	public void setTradeType(String tradeType) {
    	this.tradeType = tradeType;
    }
	public String getClientStatus() {
    	return clientStatus;
    }
	public void setClientStatus(String clientStatus) {
    	this.clientStatus = clientStatus;
    }
	public String getUpfrontFee() {
    	return upfrontFee;
    }
	public void setUpfrontFee(String upfrontFee) {
    	this.upfrontFee = upfrontFee;
    }
	public String getHurdleRate() {
    	return hurdleRate;
    }
	public void setHurdleRate(String hurdleRate) {
    	this.hurdleRate = hurdleRate;
    }
	public String getPerformanceFeeRate() {
    	return performanceFeeRate;
    }
	public void setPerformanceFeeRate(String performanceFeeRate) {
    	this.performanceFeeRate = performanceFeeRate;
    }
	public String getAdvisorFee() {
    	return advisorFee;
    }
	public void setAdvisorFee(String advisorFee) {
    	this.advisorFee = advisorFee;
    }
	public MemberBase getMember() {
    	return member;
    }
	public void setMember(MemberBase member) {
    	this.member = member;
    }
	public MemberDistributor getDistributor() {
    	return distributor;
    }
	public void setDistributor(MemberDistributor distributor) {
    	this.distributor = distributor;
    }
	public MemberIfa getIfa() {
    	return ifa;
    }
	public void setIfa(MemberIfa ifa) {
    	this.ifa = ifa;
    }
	public MemberIfafirm getIfafirm() {
    	return ifafirm;
    }
	public void setIfafirm(MemberIfafirm ifafirm) {
    	this.ifafirm = ifafirm;
    }
	public String getSourceFrom() {
    	return sourceFrom;
    }
	public void setSourceFrom(String sourceFrom) {
    	this.sourceFrom = sourceFrom;
    }

	public void setSubRelateId(String subRelateId) {
	    this.subRelateId = subRelateId;
    }

	public String getSubRelateId() {
	    return subRelateId;
    }

	
}
