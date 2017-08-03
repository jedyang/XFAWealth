
package com.fsll.wmes.fund.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 基金详细信息VO
 * @author zpzhou
 * @date 2016-7-22
 */
public class FoundDetailVo {
	private FundBasicDataVO vo=new FundBasicDataVO();
	private List<FundCumulativePerformanceDataVO> ljList=new ArrayList<FundCumulativePerformanceDataVO>();
	private List<FundYearPerformanceDataVO> ndList=new ArrayList<FundYearPerformanceDataVO>();
	private ChartDataVO chartVo=new ChartDataVO(); 
	public List<FundCumulativePerformanceDataVO> getLjList() {
		return ljList;
	}
	public void setLjList(List<FundCumulativePerformanceDataVO> ljList) {
		this.ljList = ljList;
	}
	public List<FundYearPerformanceDataVO> getNdList() {
		return ndList;
	}
	public void setNdList(List<FundYearPerformanceDataVO> ndList) {
		this.ndList = ndList;
	}
	public FundBasicDataVO getVo() {
		return vo;
	}
	public void setVo(FundBasicDataVO vo) {
		this.vo = vo;
	}
	public ChartDataVO getChartVo() {
		return chartVo;
	}
	public void setChartVo(ChartDataVO chartVo) {
		this.chartVo = chartVo;
	}
}
