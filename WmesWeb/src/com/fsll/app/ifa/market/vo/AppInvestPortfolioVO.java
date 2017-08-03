package com.fsll.app.ifa.market.vo;

import java.util.List;

import com.fsll.buscore.fund.vo.CorePieChartItemVO;

/**
 * 组合分析 实体类VO
 * @author zxtan
 * @date 2017-02-08
 */
public class AppInvestPortfolioVO {
	private String riskLevel;//组合风险评级
	private List<AppInvestProductVO> productList;//组合产品
	private List<CorePieChartItemVO> sectorTypeList;//组合产品行业分布
	private List<CorePieChartItemVO> geoAllocationList;//组合产品地区分布
		

	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public List<AppInvestProductVO> getProductList() {
		return productList;
	}
	public void setProductList(List<AppInvestProductVO> productList) {
		this.productList = productList;
	}
	public List<CorePieChartItemVO> getSectorTypeList() {
		return sectorTypeList;
	}
	public void setSectorTypeList(List<CorePieChartItemVO> sectorTypeList) {
		this.sectorTypeList = sectorTypeList;
	}	
	public List<CorePieChartItemVO> getGeoAllocationList() {
		return geoAllocationList;
	}
	public void setGeoAllocationList(List<CorePieChartItemVO> geoAllocationList) {
		this.geoAllocationList = geoAllocationList;
	}			
}
