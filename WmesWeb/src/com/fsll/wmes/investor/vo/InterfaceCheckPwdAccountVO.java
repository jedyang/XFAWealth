package com.fsll.wmes.investor.vo;

import com.fsll.wmes.entity.MemberDistributor;

public class InterfaceCheckPwdAccountVO {
	private String id;
	private String accountNo;
	private String accountType;
	private MemberDistributor distributor;
	private String distributorLogo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public MemberDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}
	public String getDistributorLogo() {
		return distributorLogo;
	}
	public void setDistributorLogo(String distributorLogo) {
		this.distributorLogo = distributorLogo;
	}
	
	
}
