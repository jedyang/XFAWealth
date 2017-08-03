package com.fsll.wmes.ifa.vo;


import com.fsll.wmes.entity.MemberBase;

/**
 * IFA列表筛选条件
 * 
 * @author 林文伟
 * @date 2016-6-20
 */
public class IfaListfiltersVO {
	
	private String lang;
	private String keyword;//作为查询关键字
	private String ifaFirmIds;//允许多个，id用逗号分隔
	private String investmentType;
	private String serviceRegion;
	private String serviceLanguage;
	private String expertise;
	private String workingYearsFrom;
	private String workingYearsTo;
	private String belongCountry;
	private String sort;
	private String memberId;
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getIfaFirmIds() {
		return ifaFirmIds;
	}
	public void setIfaFirmIds(String ifaFirmIds) {
		this.ifaFirmIds = ifaFirmIds;
	}
	public String getInvestmentType() {
		return investmentType;
	}
	public void setInvestmentType(String investmentType) {
		this.investmentType = investmentType;
	}
	public String getServiceRegion() {
		return serviceRegion;
	}
	public void setServiceRegion(String serviceRegion) {
		this.serviceRegion = serviceRegion;
	}
	public String getServiceLanguage() {
		return serviceLanguage;
	}
	public void setServiceLanguage(String serviceLanguage) {
		this.serviceLanguage = serviceLanguage;
	}
	public String getExpertise() {
		return expertise;
	}
	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	public String getWorkingYearsFrom() {
		return workingYearsFrom;
	}
	public void setWorkingYearsFrom(String workingYearsFrom) {
		this.workingYearsFrom = workingYearsFrom;
	}
	public String getWorkingYearsTo() {
		return workingYearsTo;
	}
	public void setWorkingYearsTo(String workingYearsTo) {
		this.workingYearsTo = workingYearsTo;
	}
	public String getBelongCountry() {
		return belongCountry;
	}
	public void setBelongCountry(String belongCountry) {
		this.belongCountry = belongCountry;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
}

