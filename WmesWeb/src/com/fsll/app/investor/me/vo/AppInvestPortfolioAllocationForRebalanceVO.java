package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 组合分析 实体类VO
 * @author zxtan
 * @date 2017-02-27
 */
public class AppInvestPortfolioAllocationForRebalanceVO {
	
	private AppInvestPortfolioForRebalanceVO holdDetail;//调整前（持仓产品）
	private AppInvestPortfolioForRebalanceVO adjustDetail;//调整后（持仓产品）
		

	public AppInvestPortfolioForRebalanceVO getHoldDetail() {
		return holdDetail;
	}
	public void setHoldDetail(AppInvestPortfolioForRebalanceVO holdDetail) {
		this.holdDetail = holdDetail;
	}
	public AppInvestPortfolioForRebalanceVO getAdjustDetail() {
		return adjustDetail;
	}
	public void setAdjustDetail(AppInvestPortfolioForRebalanceVO adjustDetail) {
		this.adjustDetail = adjustDetail;
	}		
}
