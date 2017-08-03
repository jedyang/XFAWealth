package com.fsll.wmes.trade.vo;

import java.util.ArrayList;
import java.util.List;

import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.OrderPlanAip;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioInfo;

public class OrderPlanVO {

	private String id;
	private String memberId;
	private String memberName;
	private String portfolioName;
	private String totalBuy;
	private String totalSell;
	private String issuDate;
	private String status;
	
	private OrderPlan orderPlan; 
	private OrderPlanAip orderPlanAip; 
	private List<OrderPlanProduct> orderPlanProducts = new ArrayList<OrderPlanProduct>();
	private PortfolioInfo portfolioInfo;
	private PortfolioHold portfolioHold;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getTotalBuy() {
		return totalBuy;
	}
	public void setTotalBuy(String totalBuy) {
		this.totalBuy = totalBuy;
	}
	public String getTotalSell() {
		return totalSell;
	}
	public void setTotalSell(String totalSell) {
		this.totalSell = totalSell;
	}
	public String getIssuDate() {
		return issuDate;
	}
	public void setIssuDate(String issuDate) {
		this.issuDate = issuDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public OrderPlan getOrderPlan() {
		return orderPlan;
	}
	public void setOrderPlan(OrderPlan orderPlan) {
		this.orderPlan = orderPlan;
	}
	public OrderPlanAip getOrderPlanAip() {
		return orderPlanAip;
	}
	public void setOrderPlanAip(OrderPlanAip orderPlanAip) {
		this.orderPlanAip = orderPlanAip;
	}
	public List<OrderPlanProduct> getOrderPlanProducts() {
		return orderPlanProducts;
	}
	public void setOrderPlanProducts(List<OrderPlanProduct> orderPlanProducts) {
		this.orderPlanProducts = orderPlanProducts;
	}
	public PortfolioInfo getPortfolioInfo() {
		return portfolioInfo;
	}
	public void setPortfolioInfo(PortfolioInfo portfolioInfo) {
		this.portfolioInfo = portfolioInfo;
	}
	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}
	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
	}
	
	
}
