package com.fsll.app.investor.market.vo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.StrategyInfo;

/**
 * 投资策略配置实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppPortfolioArenaAllocationDetailVO {

	private String itemName;		
	private String itemRate;

	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	public String getItemRate() {
		return itemRate;
	}

	public void setItemRate(String itemRate) {
		this.itemRate = itemRate;
	}
	
}
