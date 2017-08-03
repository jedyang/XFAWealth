package com.fsll.wmes.trade.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fsll.wmes.investor.vo.InvestorAccountVO;

public class TransactionVO {
	private Integer memberType;
	private String clientIconUrl;
	private String nickName;
	private String customerId;
	private String email;
	private String mobileNumber;
	private String currencyCode; // 当前页面货币编码
	private String currencyName;
	private String memberId;  // 当前登录 member
	private String clientMemberId;
	private String ifaId;
	
	private String planId;
	private String holdId;
	private String portfolioName;
	private Double cashAvailable; // 可用现金
	
	private String aipId; 
	private String aipFlag; // 是否定投，0否1是
	private String aipExecCycle; // 定投周期
	private Integer aipTimeDistance; // 定投间隔
	private Double aipAmount; // 定投金额
	private String aipNextTime; // 下次定投时间
	private String aipEndType; // 定投结束方式
	private Date aipEndDate; // 定投到指定时间结束
	private Integer aipEndCount; // 定投到指定次数结束
	private Double aipEndTotalAmount; // 定投到指定总额时结束
	private Date aipInitTime; // 首次定投时间
	
	private List<InvestorAccountVO> investorAccountVOs = new ArrayList<InvestorAccountVO>(); // 账号信息
	private List<PlanProductVO> planProductVOs = new ArrayList<PlanProductVO>(); // 产品信息
	
	
	public Integer getMemberType() {
		return memberType;
	}
	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}
	public String getClientIconUrl() {
		return clientIconUrl;
	}
	public void setClientIconUrl(String clientIconUrl) {
		this.clientIconUrl = clientIconUrl;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getClientMemberId() {
		return clientMemberId;
	}
	public void setClientMemberId(String clientMemberId) {
		this.clientMemberId = clientMemberId;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getHoldId() {
		return holdId;
	}
	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public Double getCashAvailable() {
		return cashAvailable;
	}
	public void setCashAvailable(Double cashAvailable) {
		this.cashAvailable = cashAvailable;
	}
	public String getAipId() {
		return aipId;
	}
	public void setAipId(String aipId) {
		this.aipId = aipId;
	}
	public String getAipFlag() {
		return aipFlag;
	}
	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
	}
	public String getAipExecCycle() {
		return aipExecCycle;
	}
	public void setAipExecCycle(String aipExecCycle) {
		this.aipExecCycle = aipExecCycle;
	}
	public Integer getAipTimeDistance() {
		return aipTimeDistance;
	}
	public void setAipTimeDistance(Integer aipTimeDistance) {
		this.aipTimeDistance = aipTimeDistance;
	}
	public String getAipNextTime() {
		return aipNextTime;
	}
	public void setAipNextTime(String aipNextTime) {
		this.aipNextTime = aipNextTime;
	}
	public String getAipEndType() {
		return aipEndType;
	}
	public void setAipEndType(String aipEndType) {
		this.aipEndType = aipEndType;
	}
	public Date getAipEndDate() {
		return aipEndDate;
	}
	public void setAipEndDate(Date aipEndDate) {
		this.aipEndDate = aipEndDate;
	}
	public Integer getAipEndCount() {
		return aipEndCount;
	}
	public void setAipEndCount(Integer aipEndCount) {
		this.aipEndCount = aipEndCount;
	}
	
	public Double getAipEndTotalAmount() {
		return aipEndTotalAmount;
	}
	public void setAipEndTotalAmount(Double aipEndTotalAmount) {
		this.aipEndTotalAmount = aipEndTotalAmount;
	}
	public List<InvestorAccountVO> getInvestorAccountVOs() {
		return investorAccountVOs;
	}
	public void setInvestorAccountVOs(List<InvestorAccountVO> investorAccountVOs) {
		this.investorAccountVOs = investorAccountVOs;
	}
	public List<PlanProductVO> getPlanProductVOs() {
		return planProductVOs;
	}
	public void setPlanProductVOs(List<PlanProductVO> planProductVOs) {
		this.planProductVOs = planProductVOs;
	}
	public Date getAipInitTime() {
		return aipInitTime;
	}
	public void setAipInitTime(Date aipInitTime) {
		this.aipInitTime = aipInitTime;
	}
	public Double getAipAmount() {
		return aipAmount;
	}
	public void setAipAmount(Double aipAmount) {
		this.aipAmount = aipAmount;
	}
	
	
	
	
	
}
