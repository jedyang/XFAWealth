package com.fsll.wmes.portfolio.vo;

public class FavouryWatchlistsVO {

	private String typeName;
	private String lastUpdate;
	private String fundsCount="0";
	private String allCount="0";
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getFundsCount() {
		return fundsCount;
	}
	public void setFundsCount(String fundsCount) {
		this.fundsCount = fundsCount;
	}
	public String getAllCount() {
		return allCount;
	}
	public void setAllCount(String allCount) {
		this.allCount = allCount;
	}
	
	
}
