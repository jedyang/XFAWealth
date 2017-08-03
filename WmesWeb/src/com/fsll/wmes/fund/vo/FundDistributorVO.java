package com.fsll.wmes.fund.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FundDistributorVO {

    private String id;
    
	private String fundId;
	

	private String companyName;

	@JsonFormat(pattern="yyyy-MM-dd")
	private Date beginDate;

	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpdate;
    
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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