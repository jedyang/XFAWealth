package com.fsll.app.investor.me.vo;

import java.util.List;


/**
 * Ifa实体类
 * 
 * @author zxtan
 * @date 2017-01-16
 */
public class AppIfaItemVO {
	private String id;//IFA ID
	private String memberId;//IFA MemberID
	private String fullname;//IFA Name
	private String iconUrl;//IFA Icon Url
	private String companyName;//IFA firm
	private String recommendedTime;//Recommended Time
	private List<AppIfaRecommendedItemVO> recommendedCountList;//Recommended Count
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getRecommendedTime() {
		return recommendedTime;
	}
	public void setRecommendedTime(String recommendedTime) {
		this.recommendedTime = recommendedTime;
	}

	public List<AppIfaRecommendedItemVO> getRecommendedCountList() {
		return recommendedCountList;
	}
	public void setRecommendedCountList(List<AppIfaRecommendedItemVO> recommendedCountList) {
		this.recommendedCountList = recommendedCountList;
	}
}
