package com.fsll.wmes.portfolio.vo;

import java.util.List;

import com.fsll.wmes.investor.vo.InvestorMyPortfolioVO;

public class InvestorPortfolioDataVO {

	List<PortfolioHoldCumperfSimpleVO> myPortfolioList;
	List<String> dateList;
	public List<PortfolioHoldCumperfSimpleVO> getMyPortfolioList() {
		return myPortfolioList;
	}
	public void setMyPortfolioList(List<PortfolioHoldCumperfSimpleVO> myPortfolioList) {
		this.myPortfolioList = myPortfolioList;
	}
	public List<String> getDateList() {
		return dateList;
	}
	public void setDateList(List<String> dateList) {
		this.dateList = dateList;
	}
	
	
	
	
	
}
