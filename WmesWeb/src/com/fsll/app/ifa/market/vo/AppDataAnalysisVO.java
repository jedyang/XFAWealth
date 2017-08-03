package com.fsll.app.ifa.market.vo;

/**
 * IFA市场——统计与分析基础实体类VO
 * @author zxtan
 * @date 2017-03-24
 */
public class AppDataAnalysisVO {
	private String memberId;//主键 ID
	private Double totalAsset;//总资产
	private Double returnRate;//回报率
	private String baseCurrency;//货币
	private String geo;//区域（国家）
	
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public Double getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}

	public Double getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	
	public String getGeo() {
		return geo;
	}
	public void setGeo(String geo) {
		this.geo = geo;
	}
		
}
