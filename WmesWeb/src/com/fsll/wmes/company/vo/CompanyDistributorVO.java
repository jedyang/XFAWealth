package com.fsll.wmes.company.vo;

import java.util.Date;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductInfo;

/**
 * 运营与代理商关系管理
 * @author Yan
 * @date 2017-01-20
 */
public class CompanyDistributorVO {
   
	private String id;
	private CompanyInfo company; 
	private MemberDistributor distributor;
	private Date createTime;
	private String companyName;
	private String distributorName;
	
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
	public MemberDistributor getDistributor() {
		return distributor;
	}
	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getDistributorName() {
		return distributorName;
	}
	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	
}
