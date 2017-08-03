package com.fsll.app.ifa.client.vo;

import java.util.List;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-03-16
 */
public class AppEventVO {	
	
	private List<AppEventItemVO> openAccountEventList;//开户事件统计
	private List<AppEventItemVO> proposalEventList;//方案事件统计
	private List<AppEventItemVO> portfolioEventList;//组合事件统计
	private AppEventItemVO portfolioRemindEvent;//组合回报情况跟踪统计
	private AppEventItemVO kycRemindEvent;//KYC事件统计
	private AppEventItemVO notYetInvestEvent;//未投资事件统计
	
	
	public List<AppEventItemVO> getOpenAccountEventList() {
		return openAccountEventList;
	}
	public void setOpenAccountEventList(List<AppEventItemVO> openAccountEventList) {
		this.openAccountEventList = openAccountEventList;
	}
		
	public List<AppEventItemVO> getProposalEventList() {
		return proposalEventList;
	}
	public void setProposalEventList(List<AppEventItemVO> proposalEventList) {
		this.proposalEventList = proposalEventList;
	}	

	public List<AppEventItemVO> getPortfolioEventList() {
		return portfolioEventList;
	}
	public void setPortfolioEventList(List<AppEventItemVO> portfolioEventList) {
		this.portfolioEventList = portfolioEventList;
	}	
	public AppEventItemVO getPortfolioRemindEvent() {
		return portfolioRemindEvent;
	}
	public void setPortfolioRemindEvent(AppEventItemVO portfolioRemindEvent) {
		this.portfolioRemindEvent = portfolioRemindEvent;
	}		
	public AppEventItemVO getKycRemindEvent() {
		return kycRemindEvent;
	}
	public void setKycRemindEvent(AppEventItemVO kycRemindEvent) {
		this.kycRemindEvent = kycRemindEvent;
	}		
	public AppEventItemVO getNotYetInvestEvent() {
		return notYetInvestEvent;
	}
	public void setNotYetInvestEvent(AppEventItemVO notYetInvestEvent) {
		this.notYetInvestEvent = notYetInvestEvent;
	}	
}
