package com.fsll.app.ifa.client.vo;

import java.util.List;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-03-16
 */
public class AppClientItemVO {
	private String id;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String faca;//是否美国公民,1是,0否
	private String cies;//资本投资人入境计划账户,1是,0否
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码
	private String email;//邮箱
	private String iconUrl;//头像
	private String riskLevel;//风险等级
	private String totalReturnRate;//总回报率
	private String totalReturnValue;//总回报金额
	private String totalAssets;//总资产
	private String currency;//货币
	private String investDays;//投资天数
	
	private String isBirthDay;//生日提醒
	private String hasSchedule;//日程提醒
	
	
	private List<AppPieChartItemVO> allocationList;//持仓产品分布情况（基金，保险，股票，债券 占比）
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}	
	public String getFaca() {
		return faca;
	}
	public void setFaca(String faca) {
		this.faca = faca;
	}	
	public String getCies() {
		return cies;
	}
	public void setCies(String cies) {
		this.cies = cies;
	}	
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
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
	public String getInvestDays() {
		return investDays;
	}
	public void setInvestDays(String investDays) {
		this.investDays = investDays;
	}	
	
	public String getTotalAssets() {
		return totalAssets;
	}
	public void setTotalAssets(String totalAssets) {
		this.totalAssets = totalAssets;
	}	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}	
	public String getIsBirthDay() {
		return isBirthDay;
	}
	public void setIsBirthDay(String isBirthDay) {
		this.isBirthDay = isBirthDay;
	}	
	public String getHasSchedule() {
		return hasSchedule;
	}
	public void setHasSchedule(String hasSchedule) {
		this.hasSchedule = hasSchedule;
	}
		
	public List<AppPieChartItemVO> getAllocationList() {
		return allocationList;
	}
	public void setAllocationList(List<AppPieChartItemVO> allocationList) {
		this.allocationList = allocationList;
	}	
}
