package com.fsll.wmes.crm.vo;

import java.util.Date;



public class PortfolioVO {

	private String memberId;
	private String loginCode;
	private String nickName;
	private String id;
	private String proposalId;
	private String proposalName;
	private String portfolioName;
	private String currencyType;
	private String objectiveDesc;
	private Integer riskLeve;
	private double returnRate;
	private double returnTotal;
	private double totalInvestAmount;
	private String desc;
	private String aipType;
	private String aipExecCycle;
	private Integer aipTimeDistance;
	private Date aipNextTime;
	private String status;
	private String creator;//ifaId
	private String creatorId;//memberId
	private Date createTime;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProposalId() {
		return proposalId;
	}
	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}
	
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getObjectiveDesc() {
		return objectiveDesc;
	}
	public void setObjectiveDesc(String objectiveDesc) {
		this.objectiveDesc = objectiveDesc;
	}
	public Integer getRiskLeve() {
		return riskLeve;
	}
	public void setRiskLeve(Integer riskLeve) {
		this.riskLeve = riskLeve;
	}
	public double getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(double returnRate) {
		this.returnRate = returnRate;
	}
	public double getReturnTotal() {
		return returnTotal;
	}
	public void setReturnTotal(double returnTotal) {
		this.returnTotal = returnTotal;
	}
	public double getTotalInvestAmount() {
		return totalInvestAmount;
	}
	public void setTotalInvestAmount(double totalInvestAmount) {
		this.totalInvestAmount = totalInvestAmount;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getAipType() {
		return aipType;
	}
	public void setAipType(String aipType) {
		this.aipType = aipType;
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
	public Date getAipNextTime() {
		return aipNextTime;
	}
	public void setAipNextTime(Date aipNextTime) {
		this.aipNextTime = aipNextTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
