package com.fsll.app.ifa.market.vo;

import java.util.List;

/**
 * 自选基金的自选分类实体类VO
 * @author zxtan
 * @date 2017-03-14
 */
public class AppWatchTypeVO {
	private String typeId;//分类Id
	private String typeName;//分类名称
	private List<AppWatchFundItemVO> fundList;//分类基金列表
	

	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public List<AppWatchFundItemVO> getFundList() {
		return fundList;
	}
	public void setFundList(List<AppWatchFundItemVO> fundList) {
		this.fundList = fundList;
	}	
}
