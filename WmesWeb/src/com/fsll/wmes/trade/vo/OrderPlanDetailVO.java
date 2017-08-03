package com.fsll.wmes.trade.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fsll.wmes.investor.vo.InvestorAccountVO;

public class OrderPlanDetailVO {
	
	private String holdId;
	private String planId;
	private String portfolioName;
	private Date submitDate; // 交易提交时间
	private Date createDate; // 交易创建时间
	private Date lastUpdate;
	private String lastUpdateStr;
	private String currencyCode;
	private String currencyName;
	private String status; // 交易状态(order_plan finish_status)
	private String statusDisplay; // 交易状态信息显示
	private String approvalView; // 审批权限（1：审批人，0：其他）
	private String approvalRecordView; // 审批记录查看权限（1：被审批人，0：其他）
	private String planSubmitView; // 交易计划提交权限
	
	private String memberId;
	private String memberType;
	private String nickName;
	private String ifaNickName;
	private String iconUrl;
	private String email;
	private String mobileNumber; // 手机号码国家区号+' '+手机号码  (+86 16888888888)
	
	private String creatorId;
	private String creatorName;
	
	private String location; // 国籍 （member_ifa nationality）
	private String ifaId;
	private String ifaFirmId;
	private String ifaFirmName;
	private String customerId; // 顾客id
	
	private String aipId; 
	private String aipFlag; // 是否定投，0否1是
	private String aipExecCycle; // 定投周期
	private Integer aipTimeDistance; // 定投间隔
	private Double aipAmount; // 定投金额
	private String aipNextTime; // 下次定投时间
	private String aipEndType; // 定投结束方式
	private Date aipEndDate; // 定投到指定时间结束
	private Integer aipEndCount; // 定投到指定次数结束
	private Double aipTotalAmount; // 定投到指定总额时结束
	private Date aipInitTime; // 首次定投时间
	
	private List<PlanProductVO> planProductVOs = new ArrayList<PlanProductVO>(); // 产品信息
	private List<InvestorAccountVO> investorAccountVOs = new ArrayList<InvestorAccountVO>(); // 账号信息
	private List<OrderPlanCheckVO> checkVOs = new ArrayList<OrderPlanCheckVO>(); // 审批信息

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIfaId() {
		return ifaId;
	}

	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
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

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
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

	public List<PlanProductVO> getPlanProductVOs() {
		return planProductVOs;
	}

	public void setPlanProductVOs(List<PlanProductVO> planProductVOs) {
		this.planProductVOs = planProductVOs;
	}

	public String getHoldId() {
		return holdId;
	}

	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<InvestorAccountVO> getInvestorAccountVOs() {
		return investorAccountVOs;
	}

	public void setInvestorAccountVOs(List<InvestorAccountVO> investorAccountVOs) {
		this.investorAccountVOs = investorAccountVOs;
	}

	public String getStatusDisplay() {
		return statusDisplay;
	}

	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
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

	public Double getAipAmount() {
		return aipAmount;
	}

	public void setAipAmount(Double aipAmount) {
		this.aipAmount = aipAmount;
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

	public Double getAipTotalAmount() {
		return aipTotalAmount;
	}

	public void setAipTotalAmount(Double aipTotalAmount) {
		this.aipTotalAmount = aipTotalAmount;
	}

	public Date getAipInitTime() {
		return aipInitTime;
	}

	public void setAipInitTime(Date aipInitTime) {
		this.aipInitTime = aipInitTime;
	}

	public String getIfaNickName() {
		return ifaNickName;
	}

	public void setIfaNickName(String ifaNickName) {
		this.ifaNickName = ifaNickName;
	}

	public List<OrderPlanCheckVO> getCheckVOs() {
		return checkVOs;
	}

	public void setCheckVOs(List<OrderPlanCheckVO> checkVOs) {
		this.checkVOs = checkVOs;
	}

	public String getApprovalView() {
		return approvalView;
	}

	public void setApprovalView(String approvalView) {
		this.approvalView = approvalView;
	}

	public String getApprovalRecordView() {
		return approvalRecordView;
	}

	public void setApprovalRecordView(String approvalRecordView) {
		this.approvalRecordView = approvalRecordView;
	}

	public String getPlanSubmitView() {
		return planSubmitView;
	}

	public void setPlanSubmitView(String planSubmitView) {
		this.planSubmitView = planSubmitView;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getLastUpdateStr() {
		return lastUpdateStr;
	}

	public void setLastUpdateStr(String lastUpdateStr) {
		this.lastUpdateStr = lastUpdateStr;
	}
	
}
