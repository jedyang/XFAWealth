/**
 * 
 */
package com.fsll.wmes.fund.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基金投资组合列表
 * 
 * @author michael
 * @date 2016-6-20
 */
public class FundPortfolioListDataVO {
	// 基金投资组合列表
	private List<FundCompositionDataVO> portfolioList;
	private String type;
	private String name;
	
	public void addPortfolio(FundCompositionDataVO vo) {
		if (portfolioList==null) portfolioList = new ArrayList<FundCompositionDataVO>();
		portfolioList.add(vo);
	}
	public List<FundCompositionDataVO> getPortfolioList() {
		return portfolioList;
	}
	public void setPortfolioList(List<FundCompositionDataVO> portfolioList) {
		this.portfolioList = portfolioList;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
