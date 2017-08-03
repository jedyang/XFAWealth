package com.fsll.wmes.community.vo;

import java.util.List;

import com.fsll.wmes.ifa.vo.IfaSpaceFundVO;
import com.fsll.wmes.ifa.vo.IfaSpacePortfoliosVO;
import com.fsll.wmes.ifa.vo.IfaSpaceStrategyInfoVO;

public class CommunitySpaceVO {

	private String memberId;
	private int isCurMember;
	private int memberType;
	private String nickName;
	private String iconUrl;
	private String gender;
	private String hightlight;
	private List<String> characterList;//个人擅长与领域等数据组合
	private int followCount;//粉丝
	private int focusCount;//关注
	private int topciCount;//帖子
	private double aum;//手头掌握的AUM值
	private double totalReturn;
	private String baseCurrency;
	private String baseCurrencyName;
	private String topProtfoliosName;//个人最高收益率组合名称
	private String topProtfoliosPercentage;//个人最高收益率组合的收益百分比
	private List<IfaSpaceStrategyInfoVO> recommendedStrategies;//推荐模块之策略
	private List<IfaSpacePortfoliosVO> recommendedPortfolios;//推荐模块之组合
	private List<IfaSpaceFundVO> fundList; //推荐模块之基金
	private Boolean isSpaceShowPerformance;//是否显示自己的业绩
	private Boolean isShowAumNum;//是否显示手头掌握的基金数量
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getIsCurMember() {
		return isCurMember;
	}
	public void setIsCurMember(int isCurMember) {
		this.isCurMember = isCurMember;
	}
	public int getMemberType() {
		return memberType;
	}
	public void setMemberType(int memberType) {
		this.memberType = memberType;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getHightlight() {
		return hightlight;
	}
	public void setHightlight(String hightlight) {
		this.hightlight = hightlight;
	}
	public List<String> getCharacterList() {
		return characterList;
	}
	public void setCharacterList(List<String> characterList) {
		this.characterList = characterList;
	}
	public int getFollowCount() {
		return followCount;
	}
	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}
	public int getFocusCount() {
		return focusCount;
	}
	public void setFocusCount(int focusCount) {
		this.focusCount = focusCount;
	}
	public double getAum() {
		return aum;
	}
	public void setAum(double aum) {
		this.aum = aum;
	}
	public double getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(double totalReturn) {
		this.totalReturn = totalReturn;
	}
	public int getTopciCount() {
		return topciCount;
	}
	public void setTopciCount(int topciCount) {
		this.topciCount = topciCount;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
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
	public List<IfaSpaceStrategyInfoVO> getRecommendedStrategies() {
		return recommendedStrategies;
	}
	public void setRecommendedStrategies(
			List<IfaSpaceStrategyInfoVO> recommendedStrategies) {
		this.recommendedStrategies = recommendedStrategies;
	}
	public List<IfaSpacePortfoliosVO> getRecommendedPortfolios() {
		return recommendedPortfolios;
	}
	public void setRecommendedPortfolios(
			List<IfaSpacePortfoliosVO> recommendedPortfolios) {
		this.recommendedPortfolios = recommendedPortfolios;
	}
	public List<IfaSpaceFundVO> getFundList() {
		return fundList;
	}
	public void setFundList(List<IfaSpaceFundVO> fundList) {
		this.fundList = fundList;
	}
	public Boolean getIsSpaceShowPerformance() {
		return isSpaceShowPerformance;
	}
	public void setIsSpaceShowPerformance(Boolean isSpaceShowPerformance) {
		this.isSpaceShowPerformance = isSpaceShowPerformance;
	}
	public Boolean getIsShowAumNum() {
		return isShowAumNum;
	}
	public void setIsShowAumNum(Boolean isShowAumNum) {
		this.isShowAumNum = isShowAumNum;
	}
	public String getBaseCurrencyName() {
		return baseCurrencyName;
	}
	public void setBaseCurrencyName(String baseCurrencyName) {
		this.baseCurrencyName = baseCurrencyName;
	}
	
	
}
