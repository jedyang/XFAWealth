package com.fsll.app.ifa.market.vo;

/**
 * 持仓组合统计实体类VO
 * @author zxtan
 * @date 2017-04-27
 */
public class AppHoldCountVO {
	private String periodCode;//统计周期
	private String profitNum;//盈利组合数
	private String lossNum;//亏损组合数
	
	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}	
	public String getProfitNum() {
		return profitNum;
	}
	public void setProfitNum(String profitNum) {
		this.profitNum = profitNum;
	}	
	public String getLossNum() {
		return lossNum;
	}
	public void setLossNum(String lossNum) {
		this.lossNum = lossNum;
	}		
}
