package com.fsll.app.investor.me.vo;

import java.util.List;


/**
 * 我的资产信息实体类VO
 * @author zxtan
 * @date 2016-11-29
 */
public class AppMyAssetsDetailVO {
	private AppMyAssetsMessVo assetsInfo;
	private List<AppPortfolioHoldVO> portfolioList;
	private List<AppMyAssetsHisMessVo> assetsHisList;
	
	public AppMyAssetsMessVo getAssetsInfo() {
		return assetsInfo;
	}
	public void setAssetsInfo(AppMyAssetsMessVo assetsInfo) {
		this.assetsInfo = assetsInfo;
	}

	public List<AppPortfolioHoldVO> getPortfolioList() {
		return portfolioList;
	}
	public void setPortfolioList(List<AppPortfolioHoldVO> portfolioList) {
		this.portfolioList = portfolioList;
	}
	
	public List<AppMyAssetsHisMessVo> getAssetsHisList() {
		return assetsHisList;
	}
	public void setAssetsHisList(List<AppMyAssetsHisMessVo> assetsHisList) {
		this.assetsHisList = assetsHisList;
	}
		
}
