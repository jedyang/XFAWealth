/**
 * 
 */
package com.fsll.wmes.fund.vo;

/**
 * @author scshi
 *基金历史分红派息数据
 */
public class FundDividendDataVO {
	private String exDividendDate;
	private String dividenPerUnit;
	private String annualDividend ;
	public String getExDividendDate() {
		return exDividendDate;
	}
	public void setExDividendDate(String exDividendDate) {
		this.exDividendDate = exDividendDate;
	}
	public String getDividenPerUnit() {
		return dividenPerUnit;
	}
	public void setDividenPerUnit(String dividenPerUnit) {
		this.dividenPerUnit = dividenPerUnit;
	}
	public String getAnnualDividend() {
		return annualDividend;
	}
	public void setAnnualDividend(String annualDividend) {
		this.annualDividend = annualDividend;
	}
	
	

}