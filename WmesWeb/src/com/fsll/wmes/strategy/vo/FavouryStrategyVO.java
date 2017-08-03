package com.fsll.wmes.strategy.vo;

public class FavouryStrategyVO {

	private String memberId;
	private int allCount=0;
	private String strategyName;
	private int fundCount=0;
	private String lastUpdate;
	private String lastUpdateUnit;
	private String strategyId;
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public int getFundCount() {
		return fundCount;
	}
	public void setFundCount(int fundCount) {
		this.fundCount = fundCount;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getLastUpdateUnit() {
		return lastUpdateUnit;
	}
	public void setLastUpdateUnit(String lastUpdateUnit) {
		this.lastUpdateUnit = lastUpdateUnit;
	}
	public String getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	
	
}
