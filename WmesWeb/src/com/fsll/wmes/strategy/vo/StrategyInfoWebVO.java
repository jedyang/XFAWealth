package com.fsll.wmes.strategy.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebInvestorVisit;

public class StrategyInfoWebVO {
	private String id;
	private String strategyName;
	private String reason;
	private String geoAllocation;
	private String sector;
	private String investmentGoal;
	private String suitability;
	private String description;
	private MemberBase creator;
	private String createTime;
	private String lastUpdate;
	private String overhead;
	private String overheadTime;
	private String status;
	private Integer click;
	private Integer productCount;
	private String isValid;
	private String isPublic;
	private Integer fundsCount;
	private String isFollow;
	private MemberIfa ifa;
	private String riskLevel;
	private String allocationData;
	private String recommendReason;
	private String creatorName;
	
	//访问人数量
	private Integer visitCount;
	//访问客户数量
	private Integer customerVisitCount;
	//访问记录集合
	private List<WebInvestorVisitVO> viewers = new ArrayList<WebInvestorVisitVO>();
	
	public StrategyInfoWebVO() {
	}
	
	public StrategyInfoWebVO( StrategyInfo strategyInfo ) {
		this.id = strategyInfo.getId();
		this.strategyName = strategyInfo.getStrategyName();
		this.geoAllocation = strategyInfo.getGeoAllocation();
		this.sector = strategyInfo.getSector();
		this.investmentGoal = strategyInfo.getInvestmentGoal();
		this.suitability = strategyInfo.getSuitability();
		this.description = strategyInfo.getDescription();
		this.creator = strategyInfo.getCreator();
		this.createTime = null!=strategyInfo.getCreateTime()?DateUtil.dateToDateString(strategyInfo.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT):"";
		this.lastUpdate = null!=strategyInfo.getLastUpdate()?DateUtil.dateToDateString(strategyInfo.getLastUpdate(), DateUtil.DEFAULT_DATE_TIME_FORMAT):"";
		this.overhead = strategyInfo.getOverhead();
		this.overheadTime = null!=strategyInfo.getOverheadTime()?DateUtil.dateToDateString(strategyInfo.getOverheadTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT):"";
		this.status = strategyInfo.getStatus();
		this.click = strategyInfo.getClick();
		this.productCount = strategyInfo.getProductCount();
		this.isValid = strategyInfo.getIsValid();
		this.isPublic = strategyInfo.getIsPublic();		
		this.ifa = strategyInfo.getIfa();
		this.riskLevel=strategyInfo.getRiskLevel();
		this.recommendReason=strategyInfo.getReason();
		this.reason=strategyInfo.getReason();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public String getGeoAllocation() {
		return geoAllocation;
	}
	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
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
	public String getOverheadTime() {
		return overheadTime;
	}
	public void setOverheadTime(String overheadTime) {
		this.overheadTime = overheadTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Integer getFundsCount() {
		return fundsCount;
	}
	public void setFundsCount(Integer fundsCount) {
		this.fundsCount = fundsCount;
	}
	public String getIsFollow() {
		return isFollow;
	}
	public void setIsFollow(String isFollow) {
		this.isFollow = isFollow;
	}

	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getAllocationData() {
		return allocationData;
	}

	public void setAllocationData(String allocationData) {
		this.allocationData = allocationData;
	}

	public Integer getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}




	public List<WebInvestorVisitVO> getViewers() {
		return viewers;
	}

	public void setViewers(List<WebInvestorVisitVO> viewers) {
		this.viewers = viewers;
	}

	public Integer getCustomerVisitCount() {
		return customerVisitCount;
	}

	public void setCustomerVisitCount(Integer customerVisitCount) {
		this.customerVisitCount = customerVisitCount;
	}

	public String getRecommendReason() {
		return recommendReason;
	}

	public void setRecommendReason(String recommendReason) {
		this.recommendReason = recommendReason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}


}
