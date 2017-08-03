package com.fsll.app.investor.me.vo;

/**
 * 投资组合实体类VO
 * @author zxtan
 * @date 2017-02-06
 */
public class AppPortfolioHoldVO {
	private String id;//组合Hold ID
	private String portfolioName;//组合名称
	private String lastUpdate;//持仓表中的更新时间
	private String createTime;//创建时间
	private String baseCurrency;//货币
	private String baseCurrencyCode;//货币
	private String totalReturnRate;//累计收益率
	private String totalReturnValue;//累计收益金额
	private String totalMarketValue;//市值
	private String totalCash;//总现金
	private String totalAsset;//总资产
	private String ifaId;//Ifa Id
	private String ifaMemberId; //ifa Member Id
	private String ifaName;//Ifa名字
	private String hasOrderPlan;//是否有进行中的交易计划
	
	//当前持仓
	private String periodCode;//周期
	private String increase;//周期增长率
	
	//进行中
	private String ifFirst;//1:新创建组合，0：组合调整
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPortfolioName() {
		return portfolioName;
	}
	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getBaseCurrencyCode() {
		return baseCurrencyCode;
	}
	public void setBaseCurrencyCode(String baseCurrencyCode) {
		this.baseCurrencyCode = baseCurrencyCode;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getTotalReturnRate() {
		return totalReturnRate;
	}
	public void setTotalReturnRate(String totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
	}
	public String getTotalReturnValue() {
		return totalReturnValue;
	}
	public void setTotalReturnValue(String totalReturnValue) {
		this.totalReturnValue = totalReturnValue;
	}
	public String getTotalMarketValue() {
		return totalMarketValue;
	}
	public void setTotalMarketValue(String totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}
	public String getTotalCash() {
		return totalCash;
	}
	public void setTotalCash(String totalCash) {
		this.totalCash = totalCash;
	}
	public String getTotalAsset() {
		return totalAsset;
	}
	public void setTotalAsset(String totalAsset) {
		this.totalAsset = totalAsset;
	}
	
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getIfaMemberId() {
		return ifaMemberId;
	}
	public void setIfaMemberId(String ifaMemberId) {
		this.ifaMemberId = ifaMemberId;
	}
	public String getIfaName() {
		return ifaName;
	}
	public void setIfaName(String ifaName) {
		this.ifaName = ifaName;
	}
	public String getHasOrderPlan() {
		return hasOrderPlan;
	}
	public void setHasOrderPlan(String hasOrderPlan) {
		this.hasOrderPlan = hasOrderPlan;
	}
	
	

	public String getPeriodCode() {
		return periodCode;
	}
	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}
	public String getIncrease() {
		return increase;
	}
	public void setIncrease(String increase) {
		this.increase = increase;
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
