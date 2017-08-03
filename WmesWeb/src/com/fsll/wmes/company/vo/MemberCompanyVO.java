package com.fsll.wmes.company.vo;

import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.MemberBase;

/**
 * 运行公司-公司用户
 * @author Yan
 * @version 1.0.0 Created On: 2016-12-05
 */
public class MemberCompanyVO {
	
	private String id;
	private CompanyInfo company;
	private MemberBase member;
	private String companyName;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public CompanyInfo getCompany() {
		return company;
	}
	public void setCompany(CompanyInfo company) {
		this.company = company;
	}
	public MemberBase getMember() {
		return member;
	}
	public void setMember(MemberBase member) {
		this.member = member;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}
