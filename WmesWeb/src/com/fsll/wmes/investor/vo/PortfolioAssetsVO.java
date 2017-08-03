package com.fsll.wmes.investor.vo;

import java.util.List;

import com.fsll.wmes.entity.PortfolioHoldCumperf;

public class PortfolioAssetsVO {
	
	private Double totalAsset;
	private Double totalCash;
	private Double accReturn;
	
	private String holdId;
	private String portfolioName;
	private List<PortfolioHoldCumperf> holdCumperfs;
	
	public Double getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}
	public Double getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}
	public Double getAccReturn() {
		return accReturn;
	}
	public void setAccReturn(Double accReturn) {
		this.accReturn = accReturn;
	}
	public String getHoldId() {
		return holdId;
	}
	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public List<PortfolioHoldCumperf> getHoldCumperfs() {
		return holdCumperfs;
	}
	public void setHoldCumperfs(List<PortfolioHoldCumperf> holdCumperfs) {
		this.holdCumperfs = holdCumperfs;
	}
	
	
}
