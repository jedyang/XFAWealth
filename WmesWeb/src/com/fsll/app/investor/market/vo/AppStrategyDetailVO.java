package com.fsll.app.investor.market.vo;

import java.util.List;

/**
 * 投资策略实体类VO
 * @author zxtan
 * @date 2016-11-15
 */
public class AppStrategyDetailVO {
	private AppStrategyInfoVO strategyInfo;//基本信息
	private List<AppStrategyAllocationVO> allocationList;//配置情况
	private List<AppStrategyProductVo> productList;//推荐基金，回报数据
	
	
	public AppStrategyInfoVO getStrategyInfo() {
		return strategyInfo;
	}
	public void setAppStrategyInfo(AppStrategyInfoVO strategyInfo) {
		this.strategyInfo = strategyInfo;
	}
	public List<AppStrategyAllocationVO> getAllocationList() {
		return allocationList;
	}
	public void setAllocationList(List<AppStrategyAllocationVO> allocationList) {
		this.allocationList = allocationList;
	}
	public List<AppStrategyProductVo> getProductList() {
		return productList;
	}
	public void setProductList(List<AppStrategyProductVo> productList) {
		this.productList = productList;
	}
	
	
}
