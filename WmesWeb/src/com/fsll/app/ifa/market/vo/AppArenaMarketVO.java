package com.fsll.app.ifa.market.vo;

/**
 * 组合用于展示的行情数据实体类VO
 * @author zpzhou
 * @date 2016-9-18
 */
public class AppArenaMarketVO {
	private String id;
	private String marketDate;
	private String nav;
	private String accNav;
	private String returnRate;
	private String createTime;
	private String lastUpdate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(String marketDate) {
		this.marketDate = marketDate;
	}
	public String getNav() {
		return nav;
	}
	public void setNav(String nav) {
		this.nav = nav;
	}
	public String getAccNav() {
		return accNav;
	}
	public void setAccNav(String accNav) {
		this.accNav = accNav;
	}
	public String getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
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
