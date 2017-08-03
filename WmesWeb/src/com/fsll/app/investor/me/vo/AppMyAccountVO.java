package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 我的账户列表项的实体类VO
 * @author zxtan
 * @date 2017-01-12
 */
public class AppMyAccountVO {
	private String companyName;
	private String docCheckDays;
	private String rpqCheckDays;
	private String rpqRiskLevel;
	private List<AppMyAccountInfoVO> accountList;
	

	public String getCompanyName(){
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDocCheckDays(){
		return docCheckDays;
	}
	public void setDocCheckDays(String docCheckDays) {
		this.docCheckDays = docCheckDays;
	}
	public String getRpqCheckDays(){
		return rpqCheckDays;
	}
	public void setRpqCheckDays(String rpqCheckDays) {
		this.rpqCheckDays = rpqCheckDays;
	}
	public String getRpqRiskLevel(){
		return rpqRiskLevel;
	}
	public void setRpqRiskLevel(String rpqRiskLevel) {
		this.rpqRiskLevel = rpqRiskLevel;
	}
	public List<AppMyAccountInfoVO> getAccountList(){
		return accountList;
	}
	public void setAccountList(List<AppMyAccountInfoVO> accountList) {
		this.accountList = accountList;
	}
	
	
	
}
