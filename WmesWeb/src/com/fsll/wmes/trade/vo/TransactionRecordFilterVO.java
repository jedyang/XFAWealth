package com.fsll.wmes.trade.vo;

public class TransactionRecordFilterVO {

	private String period;
	//筛选条件期间类型，小于一个月用D（天）表示，大于等于一个月（小于年）用M（月）表示，等于大于一年用Y表示
	private String periodType;
	private String fromDate;
	private String toDate;
	private String keyWord;
	private String finishStatus;
	private String toCurrency;
	private String langCode;
	
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	
	
}
