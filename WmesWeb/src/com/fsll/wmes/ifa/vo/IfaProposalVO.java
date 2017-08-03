package com.fsll.wmes.ifa.vo;

import com.fsll.wmes.entity.CrmProposal;

public class IfaProposalVO extends CrmProposal{

	// 条件查询字段过滤
	private String filterPeriod;
	private String filterPeriodType;
	private String filterStatus;
	private String filterKeyWord; // 客户名称，方案名称，IFA名称，模糊搜索
	private String filterStartTime;
	private String filterEndTime;
	
	private String clientName;
	private String createTimeStr;
	private String ifaName;
	private String baseCurrencyName;
	private String statusDisplay;
	private String proposalDocPath;
	
	public String getFilterPeriod() {
		return filterPeriod;
	}
	public void setFilterPeriod(String filterPeriod) {
		this.filterPeriod = filterPeriod;
	}
	public String getFilterPeriodType() {
		return filterPeriodType;
	}
	public void setFilterPeriodType(String filterPeriodType) {
		this.filterPeriodType = filterPeriodType;
	}
	public String getFilterStatus() {
		return filterStatus;
	}
	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}
	public String getFilterKeyWord() {
		return filterKeyWord;
	}
	public void setFilterKeyWord(String filterKeyWord) {
		this.filterKeyWord = filterKeyWord;
	}
	public String getFilterStartTime() {
		return filterStartTime;
	}
	public void setFilterStartTime(String filterStartTime) {
		this.filterStartTime = filterStartTime;
	}
	public String getFilterEndTime() {
		return filterEndTime;
	}
	public void setFilterEndTime(String filterEndTime) {
		this.filterEndTime = filterEndTime;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getBaseCurrencyName() {
		return baseCurrencyName;
	}
	public void setBaseCurrencyName(String baseCurrencyName) {
		this.baseCurrencyName = baseCurrencyName;
	}
	public String getStatusDisplay() {
		return statusDisplay;
	}
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	public String getProposalDocPath() {
		return proposalDocPath;
	}
	public void setProposalDocPath(String proposalDocPath) {
		this.proposalDocPath = proposalDocPath;
	}
	
	
}
