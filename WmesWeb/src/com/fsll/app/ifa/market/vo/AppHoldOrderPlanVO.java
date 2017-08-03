package com.fsll.app.ifa.market.vo;

/**
 * 投资组合实体类VO
 * @author zxtan
 * @date 2017-02-06
 */
public class AppHoldOrderPlanVO {
	private String holdId;//组合Hold ID
	private String portfolioName;//组合名称
	private String riskLevel;//组合风险等级
//	private String lastUpdate;//持仓表中的更新时间
//	private String createTime;//创建时间
	private String memberId;//客户 memberId
	private String clientName;//客户名称
	private String iconUrl;//客户头像
	private String ifFirst;//1:新创建组合，0：组合调整
		
	//进行中
	private String baseCurrency;//货币
	private String toCurrency;//货币
	private String ifCheck;//是否需要当前人审核,1:是，0否
	private String orderPlanId;//计划Id
	private String orderPlanCreatorId;//计划创建人Member Id
	private String orderPlanCreateTime;//计划创建时间
	private String finishStatus;//计划状态-1：初始创建（从投资方案的组合，创建初始组合） 0：草稿（暂时保存），1：审批中,2审批不通过，3审批通过，4：交易中（提交给交易系统）；5：交易完成（交易系统返回结果，异步）
	//创建组合进行中
	private String totalBuy;//投资总金额
	private String productCount;//投资产品数量
	private String successCount;//成功数量
	private String pendCount;//交易中数量
	private String failCount;//失败数量
	//组合调整中
	private String buyCount;//买入产品数量
	private String sellCount;//卖出产品数量
//	private String switchCount;//转换产品数量
	
	public String getHoldId() {
		return holdId;
	}
	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
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

	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

//	public String getLastUpdate() {
//		return lastUpdate;
//	}
//	public void setLastUpdate(String lastUpdate) {
//		this.lastUpdate = lastUpdate;
//	}
//	public String getCreateTime() {
//		return createTime;
//	}
//	public void setCreateTime(String createTime) {
//		this.createTime = createTime;
//	}
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}	

	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}	

	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getIfFirst() {
		return ifFirst;
	}
	public void setIfFirst(String ifFirst) {
		this.ifFirst = ifFirst;
	}
	public String getIfCheck() {
		return ifCheck;
	}
	public void setIfCheck(String ifCheck) {
		this.ifCheck = ifCheck;
	}

	public String getOrderPlanId() {
		return orderPlanId;
	}
	public void setOrderPlanId(String orderPlanId) {
		this.orderPlanId = orderPlanId;
	}
	public String getOrderPlanCreatorId() {
		return orderPlanCreatorId;
	}
	public void setOrderPlanCreatorId(String orderPlanCreatorId) {
		this.orderPlanCreatorId = orderPlanCreatorId;
	}
	public String getOrderPlanCreateTime() {
		return orderPlanCreateTime;
	}
	public void setOrderPlanCreateTime(String orderPlanCreateTime) {
		this.orderPlanCreateTime = orderPlanCreateTime;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}

	public String getTotalBuy() {
		return totalBuy;
	}
	public void setTotalBuy(String totalBuy) {
		this.totalBuy = totalBuy;
	}

	public String getProductCount() {
		return productCount;
	}
	public void setProductCount(String productCount) {
		this.productCount = productCount;
	}

	public String getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(String successCount) {
		this.successCount = successCount;
	}

	public String getPendCount() {
		return pendCount;
	}
	public void setPendCount(String pendCount) {
		this.pendCount = pendCount;
	}

	public String getFailCount() {
		return failCount;
	}
	public void setFailCount(String failCount) {
		this.failCount = failCount;
	}

	public String getBuyCount() {
		return buyCount;
	}
	public void setBuyCount(String buyCount) {
		this.buyCount = buyCount;
	}

	public String getSellCount() {
		return sellCount;
	}
	public void setSellCount(String sellCount) {
		this.sellCount = sellCount;
	}

//	public String getSwitchCount() {
//		return switchCount;
//	}
//	public void setSwitchCount(String switchCount) {
//		this.switchCount = switchCount;
//	}
	
}
