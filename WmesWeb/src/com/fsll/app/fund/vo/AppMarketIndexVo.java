package com.fsll.app.fund.vo;


/**
 * Market实体类VO
 * 
 * @author zpzhou
 * @date 2016-8-25
 */
public class AppMarketIndexVo {
	private String id;
	private String code;
	private String name;
	private String curDot;
	private String increase;
	private String upsDowns;
	private String marketDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getCurDot() {
		return curDot;
	}
	public void setCurDot(String curDot) {
		this.curDot = curDot;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getUpsDowns() {
		return upsDowns;
	}
	public void setUpsDowns(String upsDowns) {
		this.upsDowns = upsDowns;
	}
	public String getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(String marketDate) {
		this.marketDate = marketDate;
	}
}
