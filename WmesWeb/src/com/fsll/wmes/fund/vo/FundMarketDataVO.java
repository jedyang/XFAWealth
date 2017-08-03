/**
 * 
 */
package com.fsll.wmes.fund.vo;

import java.util.List;


/**
 * 基金净值显示对象
 * @author michael
 * @version 1.0.0 Created On: 2016-6-20
 *	
 */
public class FundMarketDataVO {
	private String fundId;//基金ID
	private String fundName;//基金名称
	private String marketDate;//更新时间
	private String openPrice;//开市价钱
	private String lowPrice;//最低价钱
	private String hightPrice;//最高价钱
	private String closePrice;//收市价钱
	private String nav;//当日基金净值
	private String accNav;//累计净值
	private String orgCurrency;//源货币
	private String destCurrency;//目标货币
	private String lastFundReturn;//最新回报，已用returnRate替代
	private String returnRate;//日回报
	private List<FundIncomePercentageVO> fundIncomePercentageVOs;//该基金的收益信息
	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	
	public String getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}
	public String getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}
	public String getHightPrice() {
		return hightPrice;
	}
	public void setHightPrice(String hightPrice) {
		this.hightPrice = hightPrice;
	}
	public String getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
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
	public String getOrgCurrency() {
		return orgCurrency;
	}
	public void setOrgCurrency(String orgCurrency) {
		this.orgCurrency = orgCurrency;
	}
	public String getDestCurrency() {
		return destCurrency;
	}
	public void setDestCurrency(String destCurrency) {
		this.destCurrency = destCurrency;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getLastFundReturn() {
		return lastFundReturn;
	}
	public void setLastFundReturn(String lastFundReturn) {
		this.lastFundReturn = lastFundReturn;
	}
	public String getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}
	public List<FundIncomePercentageVO> getFundIncomePercentageVOs() {
		return fundIncomePercentageVOs;
	}
	public void setFundIncomePercentageVOs(
			List<FundIncomePercentageVO> fundIncomePercentageVOs) {
		this.fundIncomePercentageVOs = fundIncomePercentageVOs;
	}
	
	

}
