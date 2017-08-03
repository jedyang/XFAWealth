package com.fsll.wmes.portfolio.vo;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfafirm;

/**
 * 筛选条件类
 * @author michael
 */
public class CriteriaVO {
	
	private String userId; 
	private String keyword; 
	private String sector; 
	private String geoAllocation; 
	private String period;
	private String startDate; 
	private String endDate; 
	private String orderBy;
	private String riskLevel;
	private String totalReturn;
	private String status;
	private String source;//来源：IFA,Distributor,System,Me	
	private boolean checkCount;//是否获取统计数据
	private boolean checkProduct;//是否获取产品数据
	private boolean checkIsMyFollow;//是否需要判断是我的收藏
	private String module;//使用模块
	private String issuedDate;
	private String dataFrom;
	private String dataTo;
	private MemberBase currUser;
	private MemberIfafirm firm;
	private String prefData;
	
	public CriteriaVO() {
		super();
		// TODO Auto-generated constructor stub
		userId = "";
		keyword = "";
		sector = "";
		geoAllocation = "";
		period = "";
		startDate = "";
		endDate = "";
		orderBy = "";
		source = "";
		riskLevel = "";
		checkCount = false;
		checkProduct = false;
		module = "";
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getGeoAllocation() {
		return geoAllocation;
	}
	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getPeriod() {
		return period;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(String totalReturn) {
		this.totalReturn = totalReturn;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public boolean isCheckCount() {
		return checkCount;
	}

	public void setCheckCount(boolean checkCount) {
		this.checkCount = checkCount;
	}

	public boolean isCheckProduct() {
		return checkProduct;
	}

	public void setCheckProduct(boolean checkProduct) {
		this.checkProduct = checkProduct;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public boolean isCheckIsMyFollow() {
		return checkIsMyFollow;
	}

	public void setCheckIsMyFollow(boolean checkIsMyFollow) {
		this.checkIsMyFollow = checkIsMyFollow;
	}

	public String getDataFrom() {
		return dataFrom;
	}

	public void setDataFrom(String dataFrom) {
		this.dataFrom = dataFrom;
	}

	public String getDataTo() {
		return dataTo;
	}

	public void setDataTo(String dataTo) {
		this.dataTo = dataTo;
	}

	public String getIssuedDate() {
    	return issuedDate;
    }

	public void setIssuedDate(String issuedDate) {
    	this.issuedDate = issuedDate;
    }

	public void setCurrUser(MemberBase currUser) {
	    this.currUser = currUser;
    }

	public MemberBase getCurrUser() {
	    return currUser;
    }

	public MemberIfafirm getFirm() {
		return firm;
	}

	public void setFirm(MemberIfafirm firm) {
		this.firm = firm;
	}

	public String getPrefData() {
		return prefData;
	}

	public void setPrefData(String prefData) {
		this.prefData = prefData;
	}
	
	
}
