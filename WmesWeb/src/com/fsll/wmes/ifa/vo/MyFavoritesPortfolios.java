package com.fsll.wmes.ifa.vo;

import java.util.Date;

/**
 * 显示我收藏的组合数据列表
 * @author 林文伟
 * @date 2016-9-28
 */
public class MyFavoritesPortfolios {
	private String favoritesId;
	private String portfoliosId;
	private String portfoliosName;
	private String portfoliosCreateName;
	private Date createTime;
	private Date favoritesTime;
	private Double totalReturn;
	private int riskLevel;
	private String createrHeadUrl;
	private String createMemberId;
	
	public String getFavoritesId() {
		return favoritesId;
	}
	public void setFavoritesId(String favoritesId) {
		this.favoritesId = favoritesId;
	}
	public String getPortfoliosId() {
		return portfoliosId;
	}
	public void setPortfoliosId(String portfoliosId) {
		this.portfoliosId = portfoliosId;
	}
	public String getPortfoliosName() {
		return portfoliosName;
	}
	public void setPortfoliosName(String portfoliosName) {
		this.portfoliosName = portfoliosName;
	}
	public String getPortfoliosCreateName() {
		return portfoliosCreateName;
	}
	public void setPortfoliosCreateName(String portfoliosCreateName) {
		this.portfoliosCreateName = portfoliosCreateName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getFavoritesTime() {
		return favoritesTime;
	}
	public void setFavoritesTime(Date favoritesTime) {
		this.favoritesTime = favoritesTime;
	}
	public Double getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(Double totalReturn) {
		this.totalReturn = totalReturn;
	}
	public int getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(int riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getCreaterHeadUrl() {
		return createrHeadUrl;
	}
	public void setCreaterHeadUrl(String createrHeadUrl) {
		this.createrHeadUrl = createrHeadUrl;
	}
	public String getCreateMemberId() {
		return createMemberId;
	}
	public void setCreateMemberId(String createMemberId) {
		this.createMemberId = createMemberId;
	}
	
}

