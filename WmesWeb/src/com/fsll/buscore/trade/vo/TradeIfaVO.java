package com.fsll.buscore.trade.vo;

public class TradeIfaVO {
	private double totalNum=0;
	private double profitNum=0;
	private double lossNum=0;
	private double aum=0;
	public double getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(double totalNum) {
		this.totalNum = totalNum;
	}
	public double getProfitNum() {
		return profitNum;
	}
	public void setProfitNum(double profitNum) {
		this.profitNum = profitNum;
	}
	public double getLossNum() {
		return lossNum;
	}
	public void setLossNum(double lossNum) {
		this.lossNum = lossNum;
	}
	public double getAum() {
		return aum;
	}
	public void setAum(double aum) {
		this.aum = aum;
	}
}
