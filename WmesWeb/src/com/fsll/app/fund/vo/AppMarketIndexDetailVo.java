package com.fsll.app.fund.vo;


/**
 * 大盘指数分时数据实体类
 * 
 * @author zpzhou
 * @date 2016-8-25
 */
public class AppMarketIndexDetailVo {
	private String id;
	private String marketTime;
	private String curDot;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMarketTime() {
		return marketTime;
	}
	public void setMarketTime(String marketTime) {
		this.marketTime = marketTime;
	}
	public String getCurDot() {
		return curDot;
	}
	public void setCurDot(String curDot) {
		this.curDot = curDot;
	}
	
	
}
