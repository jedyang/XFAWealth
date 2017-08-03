package com.fsll.app.investor.me.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Ifa 推荐（新闻，基金）统计实体类
 * 
 * @author zxtan
 * @date 2017-02-15
 */
public class AppIfaRecommendedItemVO {
	private String moduleType;//推荐类型
	private String count;//统计数量
	@JsonIgnore
	private String lastUpdate;//最新时间
	
	public String getModuleType() {
		return moduleType;
	}
	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
