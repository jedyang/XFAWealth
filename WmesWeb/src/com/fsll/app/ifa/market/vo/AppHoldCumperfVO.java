package com.fsll.app.ifa.market.vo;

/**
 * 组合用于展示的行情数据实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppHoldCumperfVO {
	private String id;
	private String portfolioId;
	private String valuationDate;
	private String cumulativeRate;
	private String cumulativePl;
	private String dayPl;
	private String createTime;
	private String lastUpdate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getValuationDate() {
		return valuationDate;
	}
	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
	}
	public String getCumulativeRate() {
		return cumulativeRate;
	}
	public void setCumulativeRate(String cumulativeRate) {
		this.cumulativeRate = cumulativeRate;
	}
	public String getCumulativePl() {
		return cumulativePl;
	}
	public void setCumulativePl(String cumulativePl) {
		this.cumulativePl = cumulativePl;
	}
	public String getDayPl() {
		return dayPl;
	}
	public void setDayPl(String dayPl) {
		this.dayPl = dayPl;
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
}
