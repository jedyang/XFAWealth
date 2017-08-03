package com.fsll.app.ifa.client.vo;

import java.util.List;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-03-22
 */
public class AppProspectEventVO {	

	private AppEventItemVO newClientEvent;//新客户统计
	private List<AppEventItemVO> contactEventList;//接触事件统计
	private List<AppEventItemVO> proposalEventList;//方案事件统计
	private AppEventItemVO closeEvent;//完成客户事件统计
	
	public AppEventItemVO getNewClientEvent() {
		return newClientEvent;
	}
	public void setNewClientEvent(AppEventItemVO newClientEvent) {
		this.newClientEvent = newClientEvent;
	}
	
	public List<AppEventItemVO> getContactEventList() {
		return contactEventList;
	}
	public void setContactEventList(List<AppEventItemVO> contactEventList) {
		this.contactEventList = contactEventList;
	}
		
	public List<AppEventItemVO> getProposalEventList() {
		return proposalEventList;
	}
	public void setProposalEventList(List<AppEventItemVO> proposalEventList) {
		this.proposalEventList = proposalEventList;
	}	

	public AppEventItemVO getCloseEvent() {
		return closeEvent;
	}
	public void setCloseEvent(AppEventItemVO closeEvent) {
		this.closeEvent = closeEvent;
	}	
	
}
