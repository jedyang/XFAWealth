
package com.fsll.app.fund.vo;

import java.util.List;

/**
 * 基金基本信息VO
 * @author zxtan
 * @date 2016-11-29
 */
public class AppFundGeneralDataVO {
	private AppFundBasicDataVO basicData;//基本信息
	private List<AppFundCompositionDataVO> sectorList; //行业分布
	private List<AppFundCompositionDataVO> marketList; //地区分布

	public AppFundBasicDataVO getBasicData() {
		return basicData;
	}
	public void setBasicData(AppFundBasicDataVO basicData) {
		this.basicData = basicData;
	}
	
	public List<AppFundCompositionDataVO> getSectorList() {
		return sectorList;
	}
	public void setSectorList(List<AppFundCompositionDataVO> sectorList) {
		this.sectorList = sectorList;
	}
	
	public List<AppFundCompositionDataVO> getMarketList() {
		return marketList;
	}
	public void setMarketList(List<AppFundCompositionDataVO> marketList) {
		this.marketList = marketList;
	}
}
