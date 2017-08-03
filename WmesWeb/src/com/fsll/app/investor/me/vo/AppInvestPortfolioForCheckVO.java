package com.fsll.app.investor.me.vo;

import java.util.List;

/**
 * 组合购买、调整 （确认状态）  实体类VO
 * @author zxtan
 * @date 2017-03-01
 */
public class AppInvestPortfolioForCheckVO {
	private String portfolioName;//组合名称
	private String riskLevel;//组合风险评级
	private String aipFlag;//是否定投，1：定投，0：一次性购买
	private String totalBuy;//总买入
	private String totalSell;//总卖出
	private String baseCurrency;//货币
	private String rebalanceFlag;//组合调整标识，1：调整，0：购买
	private String totalFee;//总费用
	private String readyForOMS;//是否可以提交OMS，1:是， 0:否
	private String finishStatus;//计划状态-1：初始创建（从投资方案的组合，创建初始组合） 0：草稿（暂时保存），1：审批中,2审批不通过，3审批通过，4：交易中（提交给交易系统）；5：交易完成（交易系统返回结果，异步）
	private String creatorId;//交易计划创建人Member Id
	private List<AppInvestProductForCheckVO> productList;//组合产品
	private List<AppOrderPlanCheckVO> checkList;//审批历史
		
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getAipFlag() {
		return aipFlag;
	}
	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
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
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getRebalanceFlag(){
		return rebalanceFlag;
	}
	public void setRebalanceFlag(String rebalanceFlag){
		this.rebalanceFlag = rebalanceFlag;
	}
	public String getTotalFee(){
		return totalFee;
	}
	public void setTotalFee(String totalFee){
		this.totalFee = totalFee;
	}
	public String getReadyForOMS(){
		return readyForOMS;
	}
	public void setReadyForOMS(String readyForOMS){
		this.readyForOMS = readyForOMS;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	public List<AppInvestProductForCheckVO> getProductList() {
		return productList;
	}
	public void setProductList(List<AppInvestProductForCheckVO> productList) {
		this.productList = productList;
	}
	public List<AppOrderPlanCheckVO> getCheckList() {
		return checkList;
	}
	public void setCheckList(List<AppOrderPlanCheckVO> checkList) {
		this.checkList = checkList;
	}		
}
