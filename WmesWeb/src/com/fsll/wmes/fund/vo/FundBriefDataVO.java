/**
 * 
 */
package com.fsll.wmes.fund.vo;

/**
 * @author michael
 *	基金基础信息vo
 */
public class FundBriefDataVO {

	private String fundId;
	private String isin;
	private String fundName;
	private String fundHouse;
	private String fundCurrency;
	private String riskLevel;
	private String fundType;
	private String lastNAV;
	private String lastNAVUpdate;
	private String mgtFee;
	
	private String langCode;

	public String getFundId() {
		return fundId;
	}

	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getFundHouse() {
		return fundHouse;
	}

	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}

	public String getFundCurrency() {
		return fundCurrency;
	}

	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getLastNAV() {
		return lastNAV;
	}

	public void setLastNAV(String lastNAV) {
		this.lastNAV = lastNAV;
	}

	public String getLastNAVUpdate() {
		return lastNAVUpdate;
	}

	public void setLastNAVUpdate(String lastNAVUpdate) {
		this.lastNAVUpdate = lastNAVUpdate;
	}

	public String getMgtFee() {
		return mgtFee;
	}

	public void setMgtFee(String mgtFee) {
		this.mgtFee = mgtFee;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}



}
