package com.fsll.app.ifa.client.vo;

import java.util.List;

/**
 * IFA客户详情的账户列表项的实体类VO
 * @author zxtan
 * @date 2017-03-24
 */
public class AppAccountVO {
	private String companyName;//代理公司名称
	private String docCheckDays;//下次资料审查
	private String rpqCheckDays;//下次风险评估
	private String rpqRiskLevel;//风险等级
	private List<AppAccountItemVO> accountList; //账户列表
	

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
	public List<AppAccountItemVO> getAccountList(){
		return accountList;
	}
	public void setAccountList(List<AppAccountItemVO> accountList) {
		this.accountList = accountList;
	}
	
	
	
}
