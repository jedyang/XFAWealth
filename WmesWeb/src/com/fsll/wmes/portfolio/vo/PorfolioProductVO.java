package com.fsll.wmes.portfolio.vo;

import com.fsll.wmes.entity.FundInfo;

public class PorfolioProductVO {

	private FundInfo fundInfo;
	private String fundName;
	private Double lastNav;
	private String fundCurrency;
	private double weight;
	// 基金回报信息
	private double fundReturnOneWeek;// 1周
	private double fundReturnOneMonth;// 1个月
	private double fundReturnThreeMonth;// 3个月
	private double fundReturnSixMonth;// 6个月
	private double fundReturnOneYear;// 1年
	private double fundReturnThreeYear;// 3年
	private double fundReturnFiveYear;// 5年
	private double fundReturnYTD;// YTD
	private double fundReturnLaunch;// LANUCH

	public FundInfo getFundInfo() {
		return fundInfo;
	}

	public void setFundInfo(FundInfo fundInfo) {
		this.fundInfo = fundInfo;
	}
	
	

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public Double getLastNav() {
		return lastNav;
	}

	public void setLastNav(Double lastNav) {
		this.lastNav = lastNav;
	}

	public String getFundCurrency() {
		return fundCurrency;
	}

	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getFundReturnOneWeek() {
		return fundReturnOneWeek;
	}

	public void setFundReturnOneWeek(double fundReturnOneWeek) {
		this.fundReturnOneWeek = fundReturnOneWeek;
	}

	public double getFundReturnOneMonth() {
		return fundReturnOneMonth;
	}

	public void setFundReturnOneMonth(double fundReturnOneMonth) {
		this.fundReturnOneMonth = fundReturnOneMonth;
	}

	public double getFundReturnThreeMonth() {
		return fundReturnThreeMonth;
	}

	public void setFundReturnThreeMonth(double fundReturnThreeMonth) {
		this.fundReturnThreeMonth = fundReturnThreeMonth;
	}

	public double getFundReturnSixMonth() {
		return fundReturnSixMonth;
	}

	public void setFundReturnSixMonth(double fundReturnSixMonth) {
		this.fundReturnSixMonth = fundReturnSixMonth;
	}

	public double getFundReturnOneYear() {
		return fundReturnOneYear;
	}

	public void setFundReturnOneYear(double fundReturnOneYear) {
		this.fundReturnOneYear = fundReturnOneYear;
	}

	public double getFundReturnThreeYear() {
		return fundReturnThreeYear;
	}

	public void setFundReturnThreeYear(double fundReturnThreeYear) {
		this.fundReturnThreeYear = fundReturnThreeYear;
	}

	public double getFundReturnFiveYear() {
		return fundReturnFiveYear;
	}

	public void setFundReturnFiveYear(double fundReturnFiveYear) {
		this.fundReturnFiveYear = fundReturnFiveYear;
	}

	public double getFundReturnYTD() {
		return fundReturnYTD;
	}

	public void setFundReturnYTD(double fundReturnYTD) {
		this.fundReturnYTD = fundReturnYTD;
	}

	public double getFundReturnLaunch() {
		return fundReturnLaunch;
	}

	public void setFundReturnLaunch(double fundReturnLaunch) {
		this.fundReturnLaunch = fundReturnLaunch;
	}

}
