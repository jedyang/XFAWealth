package com.fsll.wmes.trade.vo;


public class PlanProductVO {

	private String holdId;
	private String planId;
	private String planProductId;
	private String holdProductId;
	private String orderHistoryId;
	private String orderReturnId;
	
	private String holdStatus; // (if_first) 1：该组合持仓从没投过资，0：已经投过资。投资方案确认后，为1，当发生实际交易后，变为0'
	private String planStatus; // (finish_status) -2：初始创建（从投资方案的组合，创建初始组合）：-1:被投资人否决，0：草稿（暂时保存），1：等待投资人确认，2：交易中（提交给交易系统）；3：交易完成
	private String orderHistoryStatus; // (status) -10：等待OMS返回结果,1:PendOMS等待处理,2:Part部分成交,3:Comp全部成交,4:Canc取消,5:Inac无效,6:Che审查通过,7:Conf已确认,-1:Reje审核不通过
	private String orderReturnStatus;  // (status) 1:PendOMS等待处理,2:Part部分成交,3:Comp全部成交,4:Canc取消,5:Inac无效,6:Che审查通过,7:Conf已确认,-1:Reje审核不通过'
	private String statusDisplay; // 状态信息显示
	
	private String fundInfoId;
	private String productId;
	private String fundCurrencyCode;
	private String fundCurrencyName;
	private String fundName;
	private Integer riskLevel;
	private Integer rpqRiskLevel; // RPQ测试风险等级
	private String fundType;
	private String currencyCode;
	private String currencyName;
	private Double lastNav;
	private Double minInitialAmount; // 最低首次认购
	private Double minSubscribeAmount; // 最低申购
	private Double minRedemptionAmount; // 最低赎回
	private Double minHoldingAmount; // 最低持有
	private Double minRspAmount; // 最低定投
	
	private Double amount;
	private Double unit;
	private Double referenceCost; //参考成本
	private Double holdingUnit;
	private Double availableUnit;
	
	private String accountId;
	private String accountNo;
	private String aeCode;
	private String distributorId;
	private String distributorName;
	private String distributorLogoUrl;
	private String totalFlag;
	private String totalFlagDisplay;
	private String authorized;
	private String accountIfaId;
 	
	private Double weight; // 原权重（去掉百分号）
	private Double weightAdjust; // 新权重（去掉百分号）
	private Double amountAdjust; // 调整后的金额
	private Double unitAdjust; // 调整后的份额
	private Double tranFee;
	private Double tranRate;
	private String tranFeeCur;
	private Double tranFeeMini;
	private String switchGroup;
	private String switchFlag;
	private String tranType; // B：买入/申购,S:卖出/赎回
	private String dividend; // R:Reinvestment再投资;D:分配到现金账户
	private String dividendDisplay; //股息选项信息显示
	private String original; // 是否原有产品，1：是，0：不是
	private String fromProductId; // 原产品id
	private String fromFundInfoId; // 原产品基金id
	private Double unitRedeem; // 转换占原产品份额
	private String orderType; // 交易类型，Buy：买入，Sell：卖出，Revoke撤单。
	
	private String tradable; // '交易状态,Y可交易,N不可交易'
	private String status; // '状态,0退市Delisting,1有效Active,2挂起/停用Suspend'。

	public String getHoldId() {
		return holdId;
	}

	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}

	public String getPlanId() {
		return planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPlanProductId() {
		return planProductId;
	}

	public void setPlanProductId(String planProductId) {
		this.planProductId = planProductId;
	}

	public String getOrderHistoryId() {
		return orderHistoryId;
	}

	public void setOrderHistoryId(String orderHistoryId) {
		this.orderHistoryId = orderHistoryId;
	}

	public String getOrderReturnId() {
		return orderReturnId;
	}

	public void setOrderReturnId(String orderReturnId) {
		this.orderReturnId = orderReturnId;
	}

	public String getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	public String getOrderHistoryStatus() {
		return orderHistoryStatus;
	}

	public void setOrderHistoryStatus(String orderHistoryStatus) {
		this.orderHistoryStatus = orderHistoryStatus;
	}

	public String getOrderReturnStatus() {
		return orderReturnStatus;
	}

	public void setOrderReturnStatus(String orderReturnStatus) {
		this.orderReturnStatus = orderReturnStatus;
	}

	public String getFundInfoId() {
		return fundInfoId;
	}

	public void setFundInfoId(String fundInfoId) {
		this.fundInfoId = fundInfoId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Double getLastNav() {
		return lastNav;
	}

	public void setLastNav(Double lastNav) {
		this.lastNav = lastNav;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getUnit() {
		return unit;
	}

	public void setUnit(Double unit) {
		this.unit = unit;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getWeightAdjust() {
		return weightAdjust;
	}

	public void setWeightAdjust(Double weightAdjust) {
		this.weightAdjust = weightAdjust;
	}

	public Double getAmountAdjust() {
		return amountAdjust;
	}

	public void setAmountAdjust(Double amountAdjust) {
		this.amountAdjust = amountAdjust;
	}

	public Double getUnitAdjust() {
		return unitAdjust;
	}

	public void setUnitAdjust(Double unitAdjust) {
		this.unitAdjust = unitAdjust;
	}

	public Double getTranFee() {
		return tranFee;
	}

	public void setTranFee(Double tranFee) {
		this.tranFee = tranFee;
	}

	public Double getTranRate() {
		return tranRate;
	}

	public void setTranRate(Double tranRate) {
		this.tranRate = tranRate;
	}

	public String getTranFeeCur() {
		return tranFeeCur;
	}

	public void setTranFeeCur(String tranFeeCur) {
		this.tranFeeCur = tranFeeCur;
	}

	public Double getTranFeeMini() {
		return tranFeeMini;
	}

	public void setTranFeeMini(Double tranFeeMini) {
		this.tranFeeMini = tranFeeMini;
	}

	public String getSwitchGroup() {
		return switchGroup;
	}

	public void setSwitchGroup(String switchGroup) {
		this.switchGroup = switchGroup;
	}

	public String getSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getDividend() {
		return dividend;
	}

	public void setDividend(String dividend) {
		this.dividend = dividend;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getFromProductId() {
		return fromProductId;
	}

	public void setFromProductId(String fromProductId) {
		this.fromProductId = fromProductId;
	}

	public String getFromFundInfoId() {
		return fromFundInfoId;
	}

	public void setFromFundInfoId(String fromFundInfoId) {
		this.fromFundInfoId = fromFundInfoId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getStatusDisplay() {
		return statusDisplay;
	}

	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}

	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
	}

	public String getFundCurrencyName() {
		return fundCurrencyName;
	}

	public void setFundCurrencyName(String fundCurrencyName) {
		this.fundCurrencyName = fundCurrencyName;
	}

	public String getDividendDisplay() {
		return dividendDisplay;
	}

	public void setDividendDisplay(String dividendDisplay) {
		this.dividendDisplay = dividendDisplay;
	}

	public Integer getRpqRiskLevel() {
		return rpqRiskLevel;
	}

	public void setRpqRiskLevel(Integer rpqRiskLevel) {
		this.rpqRiskLevel = rpqRiskLevel;
	}

	public Double getMinSubscribeAmount() {
		return minSubscribeAmount;
	}

	public void setMinSubscribeAmount(Double minSubscribeAmount) {
		this.minSubscribeAmount = minSubscribeAmount;
	}

	public Double getMinRedemptionAmount() {
		return minRedemptionAmount;
	}

	public void setMinRedemptionAmount(Double minRedemptionAmount) {
		this.minRedemptionAmount = minRedemptionAmount;
	}

	public Double getMinHoldingAmount() {
		return minHoldingAmount;
	}

	public void setMinHoldingAmount(Double minHoldingAmount) {
		this.minHoldingAmount = minHoldingAmount;
	}

	public Double getMinRspAmount() {
		return minRspAmount;
	}

	public void setMinRspAmount(Double minRspAmount) {
		this.minRspAmount = minRspAmount;
	}

	public String getAeCode() {
		return aeCode;
	}

	public void setAeCode(String aeCode) {
		this.aeCode = aeCode;
	}

	public String getHoldProductId() {
		return holdProductId;
	}

	public void setHoldProductId(String holdProductId) {
		this.holdProductId = holdProductId;
	}

	public Double getReferenceCost() {
		return referenceCost;
	}

	public void setReferenceCost(Double referenceCost) {
		this.referenceCost = referenceCost;
	}

	public Double getHoldingUnit() {
		return holdingUnit;
	}

	public void setHoldingUnit(Double holdingUnit) {
		this.holdingUnit = holdingUnit;
	}

	public Double getAvailableUnit() {
		return availableUnit;
	}

	public void setAvailableUnit(Double availableUnit) {
		this.availableUnit = availableUnit;
	}

	public Double getUnitRedeem() {
		return unitRedeem;
	}

	public void setUnitRedeem(Double unitRedeem) {
		this.unitRedeem = unitRedeem;
	}

	public String getDistributorLogoUrl() {
		return distributorLogoUrl;
	}

	public void setDistributorLogoUrl(String distributorLogoUrl) {
		this.distributorLogoUrl = distributorLogoUrl;
	}

	public String getTotalFlag() {
		return totalFlag;
	}

	public void setTotalFlag(String totalFlag) {
		this.totalFlag = totalFlag;
	}

	public String getTotalFlagDisplay() {
		return totalFlagDisplay;
	}

	public void setTotalFlagDisplay(String totalFlagDisplay) {
		this.totalFlagDisplay = totalFlagDisplay;
	}

	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	public String getAccountIfaId() {
		return accountIfaId;
	}

	public void setAccountIfaId(String accountIfaId) {
		this.accountIfaId = accountIfaId;
	}

	public String getTradable() {
		return tradable;
	}

	public void setTradable(String tradable) {
		this.tradable = tradable;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getMinInitialAmount() {
		return minInitialAmount;
	}

	public void setMinInitialAmount(Double minInitialAmount) {
		this.minInitialAmount = minInitialAmount;
	}


	
	
	
	
}
