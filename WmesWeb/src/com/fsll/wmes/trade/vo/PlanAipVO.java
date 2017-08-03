package com.fsll.wmes.trade.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlanAipVO {

	private String orderAipId;
	private String orderPlanAipId;
	private String orderAipTaskId;
	private String planId;
	private String holdId;
	private String historyId;
	private String returnId;
	private String memberId;
	private String customerId;
	
	private String iconUrl;
	private String nickName;
	private String mobileNumber;
	private String email;
	private String portfolioName;
	private Date createTime;
	private String createTimeStr;
	private Integer memberType;
	private String currencyCode;
	private String currencyName;
	
	private String aipState;
	private String aipStateDisplay;
	private Date aipNextTime;
	private String aipNextTimeStr;
	private String aipExecCycle;
	private String aipExecCycleDisplay;
	private Integer aipTimeDistance;
	private Double aipAmount;
	private String aipEndFlag;
	private Date aipEndDate;
	private String aipEndDateStr;
	private Double aipEndTotalAmount;
	private Integer aipEndCount;
	private String expiration;
	private Double aipTotalAmount;
	private Integer aipCount;
	private String aipEndType;
	private String aipEndTypeDisplay;
	
	private List<AipOrderHistoryVO> aipOrderHistories;
	private List<PlanProductVO> planProductVOs;
	
	public String getOrderAipId() {
		return orderAipId;
	}
	public void setOrderAipId(String orderAipId) {
		this.orderAipId = orderAipId;
	}
	public String getOrderPlanAipId() {
		return orderPlanAipId;
	}
	public void setOrderPlanAipId(String orderPlanAipId) {
		this.orderPlanAipId = orderPlanAipId;
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
	public String getHistoryId() {
		return historyId;
	}
	public void setHistoryId(String historyId) {
		this.historyId = historyId;
	}
	public String getReturnId() {
		return returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getMemberType() {
		return memberType;
	}
	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}
	public String getAipState() {
		return aipState;
	}
	public void setAipState(String aipState) {
		this.aipState = aipState;
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
	public Double getAipAmount() {
		return aipAmount;
	}
	public void setAipAmount(Double aipAmount) {
		this.aipAmount = aipAmount;
	}
	public String getAipEndFlag() {
		return aipEndFlag;
	}
	public void setAipEndFlag(String aipEndFlag) {
		this.aipEndFlag = aipEndFlag;
	}
	public Date getAipEndDate() {
		return aipEndDate;
	}
	public void setAipEndDate(Date aipEndDate) {
		this.aipEndDate = aipEndDate;
	}
	public Double getAipEndTotalAmount() {
		return aipEndTotalAmount;
	}
	public void setAipEndTotalAmount(Double aipEndTotalAmount) {
		this.aipEndTotalAmount = aipEndTotalAmount;
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
	public Date getAipNextTime() {
		return aipNextTime;
	}
	public void setAipNextTime(Date aipNextTime) {
		this.aipNextTime = aipNextTime;
	}
	public Integer getAipEndCount() {
		return aipEndCount;
	}
	public void setAipEndCount(Integer aipEndCount) {
		this.aipEndCount = aipEndCount;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public List<AipOrderHistoryVO> getAipOrderHistories() {
		return aipOrderHistories;
	}
	public void setAipOrderHistories(List<AipOrderHistoryVO> aipOrderHistories) {
		this.aipOrderHistories = aipOrderHistories;
	}
	public String getAipExecCycleDisplay() {
		return aipExecCycleDisplay;
	}
	public void setAipExecCycleDisplay(String aipExecCycleDisplay) {
		this.aipExecCycleDisplay = aipExecCycleDisplay;
	}
	public String getAipStateDisplay() {
		return aipStateDisplay;
	}
	public void setAipStateDisplay(String aipStateDisplay) {
		this.aipStateDisplay = aipStateDisplay;
	}
	public Double getAipTotalAmount() {
		return aipTotalAmount;
	}
	public void setAipTotalAmount(Double aipTotalAmount) {
		this.aipTotalAmount = aipTotalAmount;
	}
	public Integer getAipCount() {
		return aipCount;
	}
	public void setAipCount(Integer aipCount) {
		this.aipCount = aipCount;
	}
	public String getAipEndType() {
		return aipEndType;
	}
	public void setAipEndType(String aipEndType) {
		this.aipEndType = aipEndType;
	}
	public String getAipEndTypeDisplay() {
		return aipEndTypeDisplay;
	}
	public void setAipEndTypeDisplay(String aipEndTypeDisplay) {
		this.aipEndTypeDisplay = aipEndTypeDisplay;
	}
	public String getOrderAipTaskId() {
		return orderAipTaskId;
	}
	public void setOrderAipTaskId(String orderAipTaskId) {
		this.orderAipTaskId = orderAipTaskId;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getAipNextTimeStr() {
		return aipNextTimeStr;
	}
	public void setAipNextTimeStr(String aipNextTimeStr) {
		this.aipNextTimeStr = aipNextTimeStr;
	}
	public String getAipEndDateStr() {
		return aipEndDateStr;
	}
	public void setAipEndDateStr(String aipEndDateStr) {
		this.aipEndDateStr = aipEndDateStr;
	}
	public List<PlanProductVO> getPlanProductVOs() {
		return planProductVOs;
	}
	public void setPlanProductVOs(List<PlanProductVO> planProductVOs) {
		this.planProductVOs = planProductVOs;
	}
	
	
	
}
