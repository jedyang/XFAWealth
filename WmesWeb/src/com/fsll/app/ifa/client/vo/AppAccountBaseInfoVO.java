package com.fsll.app.ifa.client.vo;

public class AppAccountBaseInfoVO {
	//基本信息
	private String firstName;//姓（英文）
	private String lastName;//名（英文）
	private String nickName;//别名
	private String nameChn;//中文姓名
	private String gender;//性别
	private String born;//生日
	private String nationality;//国籍
	private String education;//教育程度
	private String occupation;//职业
	private String employment;//就业
//	private String certType;//证件类型
	private String certNo;//证件号码
		
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
	
	public String getGender(){
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBorn(){
		return born;
	}
	public void setBorn(String born) {
		this.born = born;
	}
	public String getNationality(){
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getEducation(){
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getOccupation(){
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getEmployment(){
		return employment;
	}
	public void setEmployment(String employment) {
		this.employment = employment;
	}	
//	public String getCertType(){
//		return certType;
//	}
//	public void setCertType(String certType) {
//		this.certType = certType;
//	}
	public String getCertNo(){
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}	
}
