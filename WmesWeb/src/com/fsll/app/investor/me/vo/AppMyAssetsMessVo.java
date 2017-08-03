package com.fsll.app.investor.me.vo;

import java.util.List;


/**
 * 我的资产信息实体类VO
 * @author zpzhou
 * @date 2016-9-13
 */
public class AppMyAssetsMessVo {
	private String totalAsset;//总资产
	private String totalMarket;//持仓金额
	private String totalCash;//现金
	private String currencyType;//货币
	private String accReturn;//累计收益率
	private String totalPl;//总收益金额
	private String dayPl;//与上一天收益的变化情况
	private String lastUpdate;//最后更新时间
	
	private String cashHold;//冻结金额(未有来源)
	private String iconUrl;
	private List<AppIfaItemVO> myIfaList;//我的IFA
	
	public String getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}
	public String getTotalMarket() {
		return totalMarket;
	}
	public void setTotalMarket(String totalMarket) {
		this.totalMarket = totalMarket;
	}
	public String getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	public String getAccReturn() {
		return accReturn;
	}
	public void setAccReturn(String accReturn) {
		this.accReturn = accReturn;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
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
	public List<AppIfaItemVO> getMyIfaList() {
		return myIfaList;
	}
	public void setMyIfaList(List<AppIfaItemVO> myIfaList) {
		this.myIfaList = myIfaList;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	//以下字段暂未有数据源
	public String getCashHold() {
		return cashHold;
	}
	public void setCashHold(String cashHold) {
		this.cashHold = cashHold;
	}
	
}
