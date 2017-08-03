package com.fsll.wmes.investor.vo;

import java.util.List;

import com.fsll.wmes.entity.PortfolioHoldCumperf;

public class InvestorMyPortfolios {
	private Double totalAsset;
	private Double totalCash;
	private Double accReturn;
	private Double totalReturn;
	
	private List<PortfolioHoldCumperf> holdCumperfs;
	private List<PortfolioAssetsVO> portfolioAssets;

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

	public List<PortfolioHoldCumperf> getHoldCumperfs() {
		return holdCumperfs;
	}

	public void setHoldCumperfs(List<PortfolioHoldCumperf> holdCumperfs) {
		this.holdCumperfs = holdCumperfs;
	}

	public List<PortfolioAssetsVO> getPortfolioAssets() {
		return portfolioAssets;
	}

	public void setPortfolioAssets(List<PortfolioAssetsVO> portfolioAssets) {
		this.portfolioAssets = portfolioAssets;
	}

	public Double getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(Double totalReturn) {
		this.totalReturn = totalReturn;
	}
	
	
}
