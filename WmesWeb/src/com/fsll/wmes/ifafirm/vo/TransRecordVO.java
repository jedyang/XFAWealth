package com.fsll.wmes.ifafirm.vo;

import com.fsll.wmes.entity.OrderHistory;

public class TransRecordVO extends OrderHistory{

	// 条件查询字段过滤
	private String filterPeriod;
	private String filterPeriodType;
	private String filterStatus;
	private String filterKeyWord; // 客户名称，投资账户，基金名称的模糊搜索
	private String filterStartTime;
	private String filterEndTime;
	private String filterIfaFirmId;
	
	private String currencyCode;
	private String currencyName;
	private String orderDateStr; // 下单时间
	private String clientName;
	private String fundName;
	private String orderTypeDisplay;
	private String tranFeeCur; // 交易货币
	private String tranFeeCurName;
	private String closeTimeStr; // 成交时间
	private String statusDisplay;
	private String trader; // 下单人order_plan.creator_id
	private String remark; // 备注（转换信息）
	private String symbolCode; // 指定代理商产品库中产品的编码
	private String ifaFirmName;
	private String firmLogo;
	
	public String getFilterPeriod() {
		return filterPeriod;
	}
	public void setFilterPeriod(String filterPeriod) {
		this.filterPeriod = filterPeriod;
	}
	public String getFilterPeriodType() {
		return filterPeriodType;
	}
	public void setFilterPeriodType(String filterPeriodType) {
		this.filterPeriodType = filterPeriodType;
	}
	public String getFilterStatus() {
		return filterStatus;
	}
	public void setFilterStatus(String filterStatus) {
		this.filterStatus = filterStatus;
	}
	public String getFilterKeyWord() {
		return filterKeyWord;
	}
	public void setFilterKeyWord(String filterKeyWord) {
		this.filterKeyWord = filterKeyWord;
	}
	public String getOrderDateStr() {
		return orderDateStr;
	}
	public void setOrderDateStr(String orderDateStr) {
		this.orderDateStr = orderDateStr;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getTranFeeCur() {
		return tranFeeCur;
	}
	public void setTranFeeCur(String tranFeeCur) {
		this.tranFeeCur = tranFeeCur;
	}
	public String getTranFeeCurName() {
		return tranFeeCurName;
	}
	public void setTranFeeCurName(String tranFeeCurName) {
		this.tranFeeCurName = tranFeeCurName;
	}
	public String getCloseTimeStr() {
		return closeTimeStr;
	}
	public void setCloseTimeStr(String closeTimeStr) {
		this.closeTimeStr = closeTimeStr;
	}
	public String getStatusDisplay() {
		return statusDisplay;
	}
	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	public String getTrader() {
		return trader;
	}
	public void setTrader(String trader) {
		this.trader = trader;
	}
	public String getOrderTypeDisplay() {
		return orderTypeDisplay;
	}
	public void setOrderTypeDisplay(String orderTypeDisplay) {
		this.orderTypeDisplay = orderTypeDisplay;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFilterStartTime() {
		return filterStartTime;
	}
	public void setFilterStartTime(String filterStartTime) {
		this.filterStartTime = filterStartTime;
	}
	public String getFilterEndTime() {
		return filterEndTime;
	}
	public void setFilterEndTime(String filterEndTime) {
		this.filterEndTime = filterEndTime;
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
	public String getSymbolCode() {
		return symbolCode;
	}
	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}
	public String getFilterIfaFirmId() {
		return filterIfaFirmId;
	}
	public void setFilterIfaFirmId(String filterIfaFirmId) {
		this.filterIfaFirmId = filterIfaFirmId;
	}
	public String getIfaFirmName() {
		return ifaFirmName;
	}
	public void setIfaFirmName(String ifaFirmName) {
		this.ifaFirmName = ifaFirmName;
	}
	public String getFirmLogo() {
		return firmLogo;
	}
	public void setFirmLogo(String firmLogo) {
		this.firmLogo = firmLogo;
	}
	
	
}
