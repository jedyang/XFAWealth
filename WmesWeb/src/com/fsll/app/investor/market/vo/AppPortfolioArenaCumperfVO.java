package com.fsll.app.investor.market.vo;

/**
 * 组合用于展示的行情数据实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppPortfolioArenaCumperfVO {
	private String id;
	private String portfolioId;
	private String valuationDate;
	private String cumprefRate;
	private String totalPl;
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
	public String getCumprefRate() {
		return cumprefRate;
	}
	public void setCumprefRate(String cumprefRate) {
		this.cumprefRate = cumprefRate;
	}
	public String getTotalPl() {
		return totalPl;
	}
	public void setTotalPl(String totalPl) {
		this.totalPl = totalPl;
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
