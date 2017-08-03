package com.fsll.app.investor.me.vo;

/**
 * 组合行情数据实体类VO
 * @author zpzhou
 * @date 2016-9-14
 */
public class AppPortfolioMarketMessVo {
	private String id;
	private String valuationDate;
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
	public String getValuationDate() {
		return valuationDate;
	}
	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
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
