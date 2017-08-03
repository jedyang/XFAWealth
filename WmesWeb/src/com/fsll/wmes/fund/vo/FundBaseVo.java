/**
 * 
 */
package com.fsll.wmes.fund.vo;

/**
 * @author scshi
 *	基金列表查询vo
 */
public class FundBaseVo {
	private String fundName;
	private String fundSize;
	private String lang;
	
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getFundSize() {
		return fundSize;
	}
	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}

}
