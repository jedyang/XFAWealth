package com.fsll.wmes.portfolio.vo;

import com.fsll.common.util.DateUtil;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioInfo;

public class PortfolioHoldCumperfVO {
	private String id;
	
    private PortfolioHold portfolioHold;
	
	private String valuationDate;
	
	private double cumulativeRate;
	
	private double cumulativePl;
	
	private String cumulativePlStr;
	
	private double dayPl;
	
	private String dayPlStr;
	
	private String createTime;
	
	private String lastUpdate;
	public PortfolioHoldCumperfVO(){
		
	}
	

	public PortfolioHoldCumperfVO(PortfolioHoldCumperf vo) {
		this.id=vo.getId();
		this.portfolioHold=vo.getPortfolioHold();
		this.valuationDate=DateUtil.getDateStr(vo.getValuationDate());
		this.cumulativeRate=vo.getCumulativeRate();
		this.cumulativePl=vo.getCumulativePl();
		this.cumulativePlStr=StrUtils.getNumberString(cumulativePl);
		this.dayPl=vo.getDayPl();
		this.dayPlStr=StrUtils.getNumberString(dayPl);
		this.createTime=DateUtil.getDateStr(vo.getCreateTime());
		this.lastUpdate=DateUtil.getDateStr(vo.getLastUpdate());
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}
	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
	}
	public String getValuationDate() {
		return valuationDate;
	}

	public void setValuationDate(String valuationDate) {
		this.valuationDate = valuationDate;
	}

	public double getCumulativeRate() {
		return cumulativeRate;
	}

	public void setCumulativeRate(double cumulativeRate) {
		this.cumulativeRate = cumulativeRate;
	}

	public double getCumulativePl() {
		return cumulativePl;
	}

	public void setCumulativePl(double cumulativePl) {
		this.cumulativePl = cumulativePl;
	}

	public String getCumulativePlStr() {
		return cumulativePlStr;
	}

	public void setCumulativePlStr(String cumulativePlStr) {
		this.cumulativePlStr = cumulativePlStr;
	}

	public double getDayPl() {
		return dayPl;
	}

	public void setDayPl(double dayPl) {
		this.dayPl = dayPl;
	}

	public String getDayPlStr() {
		return dayPlStr;
	}

	public void setDayPlStr(String dayPlStr) {
		this.dayPlStr = dayPlStr;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	
}
