package com.fsll.app.investor.market.vo;

/**
 * 首页不容错过VO
 * @author zpzhou
 * @date 2016-9-26
 */
public class AppIndexNoMissVo {
	private String id;
	private String name; 
	private String type;//type 1:投资策略 2：投资组合 3：基金
	private String reason;//描述
	private String ifaName;//ifa名称
	private String returnRate;//回报率
	private String riskLevel;//风险级别
	private String classify;//分类
	private String iconUrl;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getClassify() {
		return classify;
	}
	public void setClassify(String classify) {
		this.classify = classify;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
}
