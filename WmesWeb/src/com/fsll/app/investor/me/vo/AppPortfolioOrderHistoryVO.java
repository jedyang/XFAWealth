package com.fsll.app.investor.me.vo;


public class AppPortfolioOrderHistoryVO {
	
    private String id; //交易记录Id
    private String portfolioName;//组合名称
    private String fundName;   //基金名称
    private String orderNo;    //交易流水号
    private String accountNo;    //交易账号
	private String currencyCode;	//货币
	private String commissionUnit;    //委托份数
	private String commissionPrice;    //委托单价
	private String commissionAmount;    //参考交易金额
	private String transactionUnit;    //实际成交数量
	private String transactionAmount;    //实际成交单价
	private String fee;    //交易费用
    private String orderType;    //交易类型，Buy：买入，Sell：卖出，Revoke撤单。
	private String orderDate;	//发出交易请求时间
	private String closeTime;    //成交时间
	private String status;   //3:成功 1：处理中 -1：失败 
	private String updateTime; //更新时间，指后台服务根据交易结果，更新数据及状态的时间
	private String ifAip;//是否定投，0：不是，1是
	private String switchFlag; //转换标记，1：表示该下单记录与基金转换有关；0：表示无关

    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getCommissionUnit() {
		return this.commissionUnit;
	}

	public void setCommissionUnit(String commissionUnit) {
		this.commissionUnit = commissionUnit;
	}

	public String getCommissionPrice() {
		return this.commissionPrice;
	}

	public void setCommissionPrice(String price) {
		this.commissionPrice = price;
	}

	public String getCommissionAmount() {
		return this.commissionAmount;
	}

	public void setCommissionAmount(String amount) {
		this.commissionAmount = amount;
	}

	public String getTransactionUnit() {
		return this.transactionUnit;
	}

	public void setTransactionUnit(String transactionUnit) {
		this.transactionUnit = transactionUnit;
	}

	public String getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(String amount) {
		this.transactionAmount = amount;
	}

	public String getFee() {
		return this.fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}



	public String getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getCloseTime() {
		return this.closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getIfAip() {
		return ifAip;
	}

	public void setIfAip(String ifAip) {
		this.ifAip = ifAip;
	}

	public String getSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}
	
}