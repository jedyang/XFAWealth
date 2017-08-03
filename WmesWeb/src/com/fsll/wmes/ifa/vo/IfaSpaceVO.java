package com.fsll.wmes.ifa.vo;

import java.util.List;

import com.fsll.wmes.entity.IfaFeeItem;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * 查询IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpaceVO {
	
	private String ifaId;
	private String ifaHeadImg;//头像
	private String ifaName;
	private String ifaFirmName;//所属公司
	private String ifaFirmLogoPath;//公司Logo
	private String memberId;
	private int popular;//人气数量
	private Integer followCount;//关注数量
	private List<String> ifaPersonalCharacteristics;//个人擅长与领域等数据组合
	private String serviceRegion;//服务区域
	private String latestHighlight;//个人最新心情
	private String latestInsight;//最新个人观点
	private double aumNum;//手头掌握的AUM值
	private Boolean isShowAumNum;//是否显示手头掌握的基金数量
	private String topProtfoliosName;//个人最高收益率组合名称
	private String topProtfoliosPercentage;//个人最高收益率组合的收益百分比
	private String latestInsights;//个人最新观点
	private String personServiceRegion;//推荐模块之个人的服务区域
	private List<GeneralDataVO> personServiceRegionList;//推荐模块之个人的服务区域
	private List<GeneralDataVO> personSectionList;//推荐模块之板块区域
	private String section;//推荐模块之个人的板块
	private List<IfaSpaceStrategyInfoVO> recommendedStrategies;//推荐模块之策略
	private List<IfaSpacePortfoliosVO> recommendedPortfolios;//推荐模块之组合
	private List<IfaSpaceFundVO> fundList; //访客列表
	private List<IfaSpaceInsightVO> insightList; //观点列表
	private List<IfaSpaceNewsVO> newsList; //新闻列表
	private List<IfaSpaceVisitVO> visitList; //访客列表
	private List<IfaSpaceLiveVO> liveList; //直播左侧列表
	private String languageDesc; //擅长语言
	private String introduction; //个人介绍
	private String nationality; //国籍
	private String address; //地址
	private String defCurrency; //个人默认货币类型
	private double totalReturnValue;
	private Boolean isSpaceShowPerformance;
	private String liveRegion; //
	private String languageSpoken; //
	private List<IfaFeeItem> ifaFeeItemList; //
	
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getIfaHeadImg() {
		return ifaHeadImg;
	}
	public void setIfaHeadImg(String ifaHeadImg) {
		this.ifaHeadImg = ifaHeadImg;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getPopular() {
		return popular;
	}
	public void setPopular(int popular) {
		this.popular = popular;
	}

	
	public Integer getFollowCount() {
		return followCount;
	}
	public void setFollowCount(Integer followCount) {
		this.followCount = followCount;
	}
	public List<String> getIfaPersonalCharacteristics() {
		return ifaPersonalCharacteristics;
	}
	public void setIfaPersonalCharacteristics(
			List<String> ifaPersonalCharacteristics) {
		this.ifaPersonalCharacteristics = ifaPersonalCharacteristics;
	}
	public String getLatestHighlight() {
		return latestHighlight;
	}
	public void setLatestHighlight(String latestHighlight) {
		this.latestHighlight = latestHighlight;
	}
	
	public double getAumNum() {
		return aumNum;
	}
	public void setAumNum(double aumNum) {
		this.aumNum = aumNum;
	}
	public String getTopProtfoliosName() {
		return topProtfoliosName;
	}
	public void setTopProtfoliosName(String topProtfoliosName) {
		this.topProtfoliosName = topProtfoliosName;
	}
	public String getTopProtfoliosPercentage() {
		return topProtfoliosPercentage;
	}
	public void setTopProtfoliosPercentage(String topProtfoliosPercentage) {
		this.topProtfoliosPercentage = topProtfoliosPercentage;
	}
	
	public String getLatestInsights() {
		return latestInsights;
	}
	public void setLatestInsights(String latestInsights) {
		this.latestInsights = latestInsights;
	}
	public String getPersonServiceRegion() {
		return personServiceRegion;
	}
	public void setPersonServiceRegion(String personServiceRegion) {
		this.personServiceRegion = personServiceRegion;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public List<IfaSpaceStrategyInfoVO> getRecommendedStrategies() {
		return recommendedStrategies;
	}
	public void setRecommendedStrategies(
			List<IfaSpaceStrategyInfoVO> recommendedStrategies) {
		this.recommendedStrategies = recommendedStrategies;
	}
	public List<IfaSpaceInsightVO> getInsightList() {
		return insightList;
	}
	public void setInsightList(List<IfaSpaceInsightVO> insightList) {
		this.insightList = insightList;
	}
	public List<IfaSpaceNewsVO> getNewsList() {
		return newsList;
	}
	public void setNewsList(List<IfaSpaceNewsVO> newsList) {
		this.newsList = newsList;
	}
	public String getIfaFirmName() {
		return ifaFirmName;
	}
	public void setIfaFirmName(String ifaFirmName) {
		this.ifaFirmName = ifaFirmName;
	}
	public List<GeneralDataVO> getPersonServiceRegionList() {
		return personServiceRegionList;
	}
	public void setPersonServiceRegionList(
			List<GeneralDataVO> personServiceRegionList) {
		this.personServiceRegionList = personServiceRegionList;
	}
	public List<GeneralDataVO> getPersonSectionList() {
		return personSectionList;
	}
	public void setPersonSectionList(List<GeneralDataVO> personSectionList) {
		this.personSectionList = personSectionList;
	}
	
	public List<IfaSpacePortfoliosVO> getRecommendedPortfolios() {
		return recommendedPortfolios;
	}
	public void setRecommendedPortfolios(
			List<IfaSpacePortfoliosVO> recommendedPortfolios) {
		this.recommendedPortfolios = recommendedPortfolios;
	}
	public List<IfaSpaceVisitVO> getVisitList() {
		return visitList;
	}
	public void setVisitList(List<IfaSpaceVisitVO> visitList) {
		this.visitList = visitList;
	}
	public List<IfaSpaceFundVO> getFundList() {
		return fundList;
	}
	public void setFundList(List<IfaSpaceFundVO> fundList) {
		this.fundList = fundList;
	}
	public List<IfaSpaceLiveVO> getLiveList() {
		return liveList;
	}
	public void setLiveList(List<IfaSpaceLiveVO> liveList) {
		this.liveList = liveList;
	}
	public String getLatestInsight() {
		return latestInsight;
	}
	public void setLatestInsight(String latestInsight) {
		this.latestInsight = latestInsight;
	}
	public String getLanguageDesc() {
		return languageDesc;
	}
	public void setLanguageDesc(String languageDesc) {
		this.languageDesc = languageDesc;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Boolean getIsShowAumNum() {
		return isShowAumNum;
	}
	public void setIsShowAumNum(Boolean isShowAumNum) {
		this.isShowAumNum = isShowAumNum;
	}
	public String getServiceRegion() {
		return serviceRegion;
	}
	public void setServiceRegion(String serviceRegion) {
		this.serviceRegion = serviceRegion;
	}
	public String getDefCurrency() {
		return defCurrency;
	}
	public void setDefCurrency(String defCurrency) {
		this.defCurrency = defCurrency;
	}
	public String getIfaFirmLogoPath() {
		return ifaFirmLogoPath;
	}
	public void setIfaFirmLogoPath(String ifaFirmLogoPath) {
		this.ifaFirmLogoPath = ifaFirmLogoPath;
	}
	public double getTotalReturnValue() {
		return totalReturnValue;
	}
	public void setTotalReturnValue(double totalReturnValue) {
		this.totalReturnValue = totalReturnValue;
	}
	public Boolean getIsSpaceShowPerformance() {
		return isSpaceShowPerformance;
	}
	public void setIsSpaceShowPerformance(Boolean isSpaceShowPerformance) {
		this.isSpaceShowPerformance = isSpaceShowPerformance;
	}
	public String getLiveRegion() {
		return liveRegion;
	}
	public void setLiveRegion(String liveRegion) {
		this.liveRegion = liveRegion;
	}
	public String getLanguageSpoken() {
		return languageSpoken;
	}
	public void setLanguageSpoken(String languageSpoken) {
		this.languageSpoken = languageSpoken;
	}
	public List<IfaFeeItem> getIfaFeeItemList() {
		return ifaFeeItemList;
	}
	public void setIfaFeeItemList(List<IfaFeeItem> ifaFeeItemList) {
		this.ifaFeeItemList = ifaFeeItemList;
	}
	
}
