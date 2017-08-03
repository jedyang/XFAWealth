package com.fsll.app.ifa.market.vo;

public class AppAccountBaseInfoVO {
	//基本信息
	private String accType;//账户类型,I:Individual Account J:Joint Account
	private String cies;//资本投资人入境计划账户,1是,0否
	private String dividend;//股息选项,R:Reinvestment再投资;D:分配到现金账户
	private String baseCurrency;//基本货币,来源于基础数据,存放的是基础数据的name值
	private String purpose;//开户目的,I:投资,O:其他
	
	//投资用户信息
	private String firstName;//姓（英文）
	private String lastName;//名（英文）
	private String nickName;//别名
	private String nameChn;//中文姓名
	private String country;//出生国家
	
	public String getAccType(){
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public String getCies(){
		return cies;
	}
	public void setCies(String cies) {
		this.cies = cies;
	}
	public String getDividend(){
		return dividend;
	}
	public void setDividend(String dividend) {
		this.dividend = dividend;
	}
	public String getBaseCurrency(){
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getPurpose(){
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getFirstName(){
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getNickName(){
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getNameChn(){
		return nameChn;
	}
	public void setNameChn(String nameChn) {
		this.nameChn = nameChn;
	}
	public String getCountry(){
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}		
}
