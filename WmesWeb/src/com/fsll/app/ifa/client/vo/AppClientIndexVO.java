package com.fsll.app.ifa.client.vo;

import java.util.List;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-03-16
 */
public class AppClientIndexVO {
	private String totalNum;//总客户数
	private String profitNum;//盈利客户数
	private String lossNum;//亏损客户数
	private String notYetInvestNum;//未投资客户数	
//	private List<AppClientItemVO> clientList;//客户列表
	

	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}	
	public String getProfitNum() {
		return profitNum;
	}
	public void setProfitNum(String profitNum) {
		this.profitNum = profitNum;
	}	
	public String getLossNum() {
		return lossNum;
	}
	public void setLossNum(String lossNum) {
		this.lossNum = lossNum;
	}	
	public String getNotYetInvestNum() {
		return notYetInvestNum;
	}
	public void setNotYetInvestNum(String notYetInvestNum) {
		this.notYetInvestNum = notYetInvestNum;
	}	
		
//	public List<AppClientItemVO> getClientList() {
//		return clientList;
//	}
//	public void setClientList(List<AppClientItemVO> clientList) {
//		this.clientList = clientList;
//	}	
}
