package com.fsll.app.investor.market.vo;

import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 首页热门投资VO
 * 
 * @author zpzhou
 * @date 2016-10-31
 */
public class AppIndexHotVO {
	private String id;
	private String name;// 名称
	private String riskLevel;// 风险等级
	private String increase;// 回报
	private String num;// 被用于组合的数量
	private String returnChart;//回报图片地址
	private String ifaName;//IFA名称
	private String iconUrl;//IFA头像地址
	private List<AppIndexHotVO> portfolioList=new ArrayList<AppIndexHotVO>();//用于最佳基金中要显示的组合列表
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getReturnChart() {
		return returnChart;
	}
	public void setReturnChart(String returnChart) {
		this.returnChart = returnChart;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public List<AppIndexHotVO> getPortfolioList() {
		return portfolioList;
	}
	public void setPortfolioList(List<AppIndexHotVO> portfolioList) {
		this.portfolioList = portfolioList;
	}
}
