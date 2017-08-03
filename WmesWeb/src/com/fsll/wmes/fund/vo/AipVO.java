package com.fsll.wmes.fund.vo;

import java.util.Date;
import com.fsll.wmes.entity.PortfolioInfo;

public class AipVO {

	private String id;
    private PortfolioInfo portfolio;
	private String aipState;
	private String aipExecCycle;
	private Integer aipTimeDistance;
	private Double aipAmount;
	private String aipEndFlag;
	private Date aipEndDate;
	private Integer aipEndCount;
	private Double aipEndTotalAmount;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public PortfolioInfo getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	
}
