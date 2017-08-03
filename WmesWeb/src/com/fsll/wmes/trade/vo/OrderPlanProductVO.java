package com.fsll.wmes.trade.vo;

import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.fund.vo.FundInfoDataVO;

public class OrderPlanProductVO extends OrderPlanProduct{

	private String id;
	private String planId;
	private String productId;
	private String productName;
	private String productType;
	private String cur;
	private String riskLevel;
	private String currency;
	private String currencyName;
	private double minSubscribeAmount;
	private String minSubscribeAmountStr;
	private double latestNAVPrice;
	private String latestNAVPriceStr;
	private double subscribeFee;
	private String subscribeFeeStr;
	private Double unit;
	private String unitStr;
	private Double unitAdjust;
	private String unitAdjustStr;
	private Double amount;
	private String amountStr;
	private String amountAdjustStr;
	private Double weight;
	private String weightStr;
	private Double weightAdjust;
	private String tranType;
	private String dividend;
	private String accountId;
	private String accountNo;
	private Integer original;
	private String tranFeeStr;
	private String fromProductId;
	private String fromProductName;
	private String switchFlag;
	private String warning;
	private String includeFee;
	
	private Double fromHoldingUnit;
	private Double fromAvailableUnit;
	private Double fromHoldingAmount;
	private Double fromWeight;
	private Double unitRedeem ;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public double getMinSubscribeAmount() {
		return minSubscribeAmount;
	}
	public void setMinSubscribeAmount(double minSubscribeAmount) {
		this.minSubscribeAmount = minSubscribeAmount;
	}
	public String getMinSubscribeAmountStr() {
		return minSubscribeAmountStr;
	}
	public void setMinSubscribeAmountStr(String minSubscribeAmountStr) {
		this.minSubscribeAmountStr = minSubscribeAmountStr;
	}
	public double getLatestNAVPrice() {
		return latestNAVPrice;
	}
	public void setLatestNAVPrice(double latestNAVPrice) {
		this.latestNAVPrice = latestNAVPrice;
	}
	public String getLatestNAVPriceStr() {
		return latestNAVPriceStr;
	}
	public void setLatestNAVPriceStr(String latestNAVPriceStr) {
		this.latestNAVPriceStr = latestNAVPriceStr;
	}
	public double getSubscribeFee() {
		return subscribeFee;
	}
	public void setSubscribeFee(double subscribeFee) {
		this.subscribeFee = subscribeFee;
	}
	public String getSubscribeFeeStr() {
		return subscribeFeeStr;
	}
	public void setSubscribeFeeStr(String subscribeFeeStr) {
		this.subscribeFeeStr = subscribeFeeStr;
	}
	public String getAmountStr() {
		return amountStr;
	}
	public void setAmountStr(String amountStr) {
		this.amountStr = amountStr;
	}
	public String getAmountAdjustStr() {
		return amountAdjustStr;
	}
	public void setAmountAdjustStr(String amountAdjustStr) {
		this.amountAdjustStr = amountAdjustStr;
	}
	public String getWeightStr() {
		return weightStr;
	}
	public void setWeightStr(String weightStr) {
		this.weightStr = weightStr;
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
	public Integer getOriginal() {
		return original;
	}
	public void setOriginal(Integer original) {
		this.original = original;
	}
	public String getTranFeeStr() {
		return tranFeeStr;
	}
	public void setTranFeeStr(String tranFeeStr) {
		this.tranFeeStr = tranFeeStr;
	}
	public String getFromProductId() {
		return fromProductId;
	}
	public void setFromProductId(String fromProductId) {
		this.fromProductId = fromProductId;
	}
	public String getFromProductName() {
		return fromProductName;
	}
	public void setFromProductName(String fromProductName) {
		this.fromProductName = fromProductName;
	}
	public String getSwitchFlag() {
		return switchFlag;
	}
	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	public String getIncludeFee() {
		return includeFee;
	}
	public void setIncludeFee(String includeFee) {
		this.includeFee = includeFee;
	}
	public String getUnitStr() {
		return unitStr;
	}
	public void setUnitStr(String unitStr) {
		this.unitStr = unitStr;
	}
	public String getUnitAdjustStr() {
		return unitAdjustStr;
	}
	public void setUnitAdjustStr(String unitAdjustStr) {
		this.unitAdjustStr = unitAdjustStr;
	}
	
	//基金数据
	private FundInfoDataVO fundInfoDataVO;
	//投资者账号表
	private InvestorAccount inverstorAccount;

	public FundInfoDataVO getFundInfoDataVO() {
		return fundInfoDataVO;
	}

	public void setFundInfoDataVO(FundInfoDataVO fundInfoDataVO) {
		this.fundInfoDataVO = fundInfoDataVO;
	}

	public InvestorAccount getInverstorAccount() {
		return inverstorAccount;
	}

	public void setInverstorAccount(InvestorAccount inverstorAccount) {
		this.inverstorAccount = inverstorAccount;
	}
	public Double getUnit() {
		return unit;
	}
	public void setUnit(Double unit) {
		this.unit = unit;
	}
	public Double getUnitAdjust() {
		return unitAdjust;
	}
	public void setUnitAdjust(Double unitAdjust) {
		this.unitAdjust = unitAdjust;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	
	public String getCur() {
		return cur;
	}
	public void setCur(String cur) {
		this.cur = cur;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getFromHoldingUnit() {
		return fromHoldingUnit;
	}
	public void setFromHoldingUnit(Double fromHoldingUnit) {
		this.fromHoldingUnit = fromHoldingUnit;
	}
	public Double getFromAvailableUnit() {
		return fromAvailableUnit;
	}
	public void setFromAvailableUnit(Double fromAvailableUnit) {
		this.fromAvailableUnit = fromAvailableUnit;
	}
	public Double getFromHoldingAmount() {
		return fromHoldingAmount;
	}
	public void setFromHoldingAmount(Double fromHoldingAmount) {
		this.fromHoldingAmount = fromHoldingAmount;
	}
	public Double getFromWeight() {
		return fromWeight;
	}
	public void setFromWeight(Double fromWeight) {
		this.fromWeight = fromWeight;
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
	public Double getUnitRedeem() {
		return unitRedeem;
	}
	public void setUnitRedeem(Double unitRedeem) {
		this.unitRedeem = unitRedeem;
	}

	
}
