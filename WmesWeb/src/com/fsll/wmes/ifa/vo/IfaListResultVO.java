package com.fsll.wmes.ifa.vo;
/**
 * 根据筛选条件查询出来的IFA列表
 * 
 * @author 林文伟
 * @date 2016-8-16
 */
public class IfaListResultVO {
	private String lang;
	private String ifaId;
	private String memberId;
	private String ifaName;//IFA名字
	private String ifaBelongCountry;//IFA所属国家
	private String ifaWorkingYears;//工作年限
	private String ifaFirm;//所属IFA FIRM
	private int ifaPopularity;//人气数
	private String matchingDegree;//匹配度
	private Boolean isMatch;//是否匹配
	private String ifaHeadImg;//头像
	private String ifaPersonalCharacteristics;
	private String spaceShowPortfolio;
	private String ifaFirmLogoPath;//IFA所在公司LOGO图标
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getIfaBelongCountry() {
		return ifaBelongCountry;
	}
	public void setIfaBelongCountry(String ifaBelongCountry) {
		this.ifaBelongCountry = ifaBelongCountry;
	}
	public String getIfaWorkingYears() {
		return ifaWorkingYears;
	}
	public void setIfaWorkingYears(String ifaWorkingYears) {
		this.ifaWorkingYears = ifaWorkingYears;
	}
	public String getIfaFirm() {
		return ifaFirm;
	}
	public void setIfaFirm(String ifaFirm) {
		this.ifaFirm = ifaFirm;
	}
	public int getIfaPopularity() {
		return ifaPopularity;
	}
	public void setIfaPopularity(int ifaPopularity) {
		this.ifaPopularity = ifaPopularity;
	}
	public String getMatchingDegree() {
		return matchingDegree;
	}
	public void setMatchingDegree(String matchingDegree) {
		this.matchingDegree = matchingDegree;
	}
	public String getIfaHeadImg() {
		return ifaHeadImg;
	}
	public void setIfaHeadImg(String ifaHeadImg) {
		this.ifaHeadImg = ifaHeadImg;
	}
	public String getIfaPersonalCharacteristics() {
		return ifaPersonalCharacteristics;
	}
	public void setIfaPersonalCharacteristics(String ifaPersonalCharacteristics) {
		this.ifaPersonalCharacteristics = ifaPersonalCharacteristics;
	}
	public String getSpaceShowPortfolio() {
		return spaceShowPortfolio;
	}
	public void setSpaceShowPortfolio(String spaceShowPortfolio) {
		this.spaceShowPortfolio = spaceShowPortfolio;
	}
	public Boolean getIsMatch() {
		return isMatch;
	}
	public void setIsMatch(Boolean isMatch) {
		this.isMatch = isMatch;
	}
	public String getIfaFirmLogoPath() {
		return ifaFirmLogoPath;
	}
	public void setIfaFirmLogoPath(String ifaFirmLogoPath) {
		this.ifaFirmLogoPath = ifaFirmLogoPath;
	}
}
