package com.fsll.wmes.ifa.vo;

/**
 * 首个投资策略，用于IFA空间数据实体
 * @author 林文伟
 * @date 2016-8-19
 */
public class IfaSpaceStrategyInfoVO {
	
	private String id;
	private String strategyName;
	private int containProCount;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStrategyName() {
		return strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}
	public int getContainProCount() {
		return containProCount;
	}
	public void setContainProCount(int containProCount) {
		this.containProCount = containProCount;
	}
}
