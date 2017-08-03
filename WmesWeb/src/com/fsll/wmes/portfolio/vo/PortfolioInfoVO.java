package com.fsll.wmes.portfolio.vo;
import com.fsll.wmes.entity.PortfolioInfo;

public class PortfolioInfoVO extends PortfolioInfo{
	private Double returnRate;
	private Double totalReturn;
	
	public Double getReturnRate() {
		return returnRate;
	}
	public void setReturnRate(Double returnRate) {
		this.returnRate = returnRate;
	}
	public Double getTotalReturn() {
		return totalReturn;
	}
	public void setTotalReturn(Double totalReturn) {
		this.totalReturn = totalReturn;
	}

	
}
