package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * IFA客户详情——投资组合列表实体类VO
 * @author zxtan
 * @date 2017-03-24
 */
public class AppOrderHistoryHoldItemVO {
	private String holdId;//组合Hold ID
	private String portfolioName;//组合名称
	private String memberId;//客户 memberId
	private String clientName;//客户名称
	private String iconUrl;//客户头像
	private String orderPlanStatus;//交易计划状态 4交易中 5完成
	private List<AppOrderHistoryVO> orderHistoryList;
	
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
	

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}	

	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}	

	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	
	public String getOrderPlanStatus() {
		return orderPlanStatus;
	}
	public void setOrderPlanStatus(String orderPlanStatus) {
		this.orderPlanStatus = orderPlanStatus;
	}
	public List<AppOrderHistoryVO> getOrderHistoryList() {
		return orderHistoryList;
	}
	public void setOrderHistoryList(List<AppOrderHistoryVO> orderHistoryList) {
		this.orderHistoryList = orderHistoryList;
	}	
	
}
