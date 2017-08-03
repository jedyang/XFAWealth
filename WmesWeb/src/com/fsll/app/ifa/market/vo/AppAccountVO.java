package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * IFA客户详情的账户列表项的实体类VO
 * @author zxtan
 * @date 2017-03-29
 */
public class AppAccountVO {
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String faca;//是否美国公民,1是,0否
	private String cies;//资本投资人入境计划账户,1是,0否
	private String companyName;//代理商名称
	private String docCheckDays;//下次资料审查天数
	private String rpqCheckDays;//下次风险评估天数
	private String rpqRiskLevel;//目前风险等级
	private List<AppAccountItemVO> accountList;
	

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}	
	public String getFaca() {
		return faca;
	}
	public void setFaca(String faca) {
		this.faca = faca;
	}	
	public String getCies() {
		return cies;
	}
	public void setCies(String cies) {
		this.cies = cies;
	}	
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
