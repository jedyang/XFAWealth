package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 资产分析——实体类VO
 * @author zxtan
 * @date 2017-03-03
 */
public class AppHoldProductAnalysisVO {
	private List<AppPortfolioAllocationVO> allocationList;//产品类型配置情况
	
	private List<AppHoldProductVO> fundList;//基金产品列表
	private List<AppHoldProductVO> bondList;//债券产品列表
	private List<AppHoldProductVO> stockList;//股票产品列表
	private List<AppHoldProductVO> insureList;//保险产品列表	
	
	public List<AppPortfolioAllocationVO> getAllocationList() {
		return allocationList;
	}
	public void setAllocationList(List<AppPortfolioAllocationVO> allocationList) {
		this.allocationList = allocationList;
	}
	public List<AppHoldProductVO> getFundList() {
		return fundList;
	}
	public void setFundList(List<AppHoldProductVO> fundList) {
		this.fundList = fundList;
	}
	public List<AppHoldProductVO> getBondList() {
		return bondList;
	}
	public void setBondList(List<AppHoldProductVO> bondList) {
		this.bondList = bondList;
	}
	public List<AppHoldProductVO> getStockList() {
		return stockList;
	}
	public void setStockList(List<AppHoldProductVO> stockList) {
		this.stockList = stockList;
	}
	public List<AppHoldProductVO> getInsureList() {
		return insureList;
	}
	public void setInsureList(List<AppHoldProductVO> insureList) {
		this.insureList = insureList;
	}
		
}
