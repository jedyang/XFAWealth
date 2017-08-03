/**
 * 
 */
package com.fsll.app.investor.market.vo;

/**
 * 首页行情数据VO
 * @author zpzhou
 * @date 2016-9-30
 */
public class AppIndexMarketDataVO {
	private String marketDate;//更新时间
	private String returnRate;//日回报
	public String getMarketDate() {
		return marketDate;
	}
	public void setMarketDate(String marketDate) {
		this.marketDate = marketDate;
	}
	public String getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}
}
