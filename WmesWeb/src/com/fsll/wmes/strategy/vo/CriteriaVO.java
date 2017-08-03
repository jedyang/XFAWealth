package com.fsll.wmes.strategy.vo;

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
	private String source;//来源：IFA,Distributor,System,Me
	private String riskLevel;
	private String totalReturn;
	private String status;
	private boolean checkCount;//是否获取统计数据
	private boolean checkProduct;//是否获取产品数据
	private boolean checkIsMyFollow;//是否需要判断是我的收藏
	private String module;//使用模块
	private String strategyId;//策略id
	
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

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getModule() {
		return module;
	}

	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}

	public String getStrategyId() {
		return strategyId;
	}

	public boolean isCheckIsMyFollow() {
		return checkIsMyFollow;
	}

	public void setCheckIsMyFollow(boolean checkIsMyFollow) {
		this.checkIsMyFollow = checkIsMyFollow;
	}
	
}
