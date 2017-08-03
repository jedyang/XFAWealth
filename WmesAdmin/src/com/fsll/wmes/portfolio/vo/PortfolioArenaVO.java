package com.fsll.wmes.portfolio.vo;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioArena;

public class PortfolioArenaVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String portfolioName;

	private String geoAllocation;

	private String geoAllocationDisplay;

	private String sector;

	private String sectorDisplay;

	private String investmentGoal;

	private String suitability;

	private Integer riskLevel;

	private String description;

	private MemberBase creator;

	private String createTime;

	private String lastUpdate;

	private String overhead;

	private Date overheadTime;

	private String status;

	private String isValid;

	private String isPublic;

	private String reason;

	private Double totalReturn;

	private String aipFlag;

	private Integer click;

	private Integer productCount;

	private MemberIfa ifa;

	private MemberBase ifaMember;

	// 是否收藏
	private String isFollow;

	// 访问人数量
	private Integer visitCount;
	// 访问客户数量
	private Integer customerVisitCount;

	private double monReturn3;

	private String chartUrl;

	private Double increase;
	private String creatorName;

	public PortfolioArenaVO() {

	}

	public PortfolioArenaVO(String id) {
		this.id = id;
	}

	public PortfolioArenaVO(PortfolioArena portfolioArena) {
		this.id = portfolioArena.getId();
		this.portfolioName = StringEscapeUtils.unescapeHtml(portfolioArena.getPortfolioName());
		this.geoAllocation = portfolioArena.getGeoAllocation();
		this.sector = portfolioArena.getSector();
		this.investmentGoal = portfolioArena.getInvestmentGoal();
		this.suitability = portfolioArena.getSuitability();
		this.riskLevel = portfolioArena.getRiskLevel();
		this.description = portfolioArena.getDescription();
		this.creator = portfolioArena.getCreator();
		this.createTime = DateUtil.dateToDateString(portfolioArena.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT);
		this.lastUpdate = DateUtil.dateToDateString(portfolioArena.getLastUpdate(), DateUtil.DEFAULT_DATE_TIME_FORMAT);
		this.overhead = portfolioArena.getOverhead();
		this.overheadTime = portfolioArena.getOverheadTime();
		this.status = portfolioArena.getStatus();
		this.isValid = portfolioArena.getIsValid();
		this.isPublic = portfolioArena.getIsPublic();
		this.reason = portfolioArena.getReason();
		this.totalReturn = portfolioArena.getTotalReturn();
		this.aipFlag = portfolioArena.getAipFlag();
		this.click = portfolioArena.getClick();
		this.productCount = portfolioArena.getProductCount();
		this.ifa = portfolioArena.getIfa();
		this.geoAllocation = portfolioArena.getGeoAllocation();
		this.sector = portfolioArena.getSector();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getGeoAllocation() {
		return geoAllocation;
	}

	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	public String getGeoAllocationDisplay() {
		return geoAllocationDisplay;
	}

	public void setGeoAllocationDisplay(String geoAllocationDisplay) {
		this.geoAllocationDisplay = geoAllocationDisplay;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getSectorDisplay() {
		return sectorDisplay;
	}

	public void setSectorDisplay(String sectorDisplay) {
		this.sectorDisplay = sectorDisplay;
	}

	public String getInvestmentGoal() {
		return investmentGoal;
	}

	public void setInvestmentGoal(String investmentGoal) {
		this.investmentGoal = investmentGoal;
	}

	public String getSuitability() {
		return suitability;
	}

	public void setSuitability(String suitability) {
		this.suitability = suitability;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getOverhead() {
		return overhead;
	}

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public Date getOverheadTime() {
		return overheadTime;
	}

	public void setOverheadTime(Date overheadTime) {
		this.overheadTime = overheadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Double getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(Double totalReturn) {
		this.totalReturn = totalReturn;
	}

	public String getAipFlag() {
		return aipFlag;
	}

	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	public MemberBase getIfaMember() {
		return ifaMember;
	}

	public void setIfaMember(MemberBase ifaMember) {
		this.ifaMember = ifaMember;
	}

	public String getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(String isFollow) {
		this.isFollow = isFollow;
	}

	public Integer getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}

	public Integer getCustomerVisitCount() {
		return customerVisitCount;
	}

	public void setCustomerVisitCount(Integer customerVisitCount) {
		this.customerVisitCount = customerVisitCount;
	}

	public double getMonReturn3() {
		return monReturn3;
	}

	public void setMonReturn3(double monReturn3) {
		this.monReturn3 = monReturn3;
	}

	public String getChartUrl() {
		return chartUrl;
	}

	public void setChartUrl(String chartUrl) {
		this.chartUrl = chartUrl;
	}

	public Double getIncrease() {
		return increase;
	}

	public void setIncrease(Double increase) {
		this.increase = increase;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
