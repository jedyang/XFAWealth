package com.fsll.app.ifa.client.vo;

/**
 * IFA的销售管道的方案列表项的实体类VO
 * @author zxtan
 * @date 2017-04-05
 */
public class AppPipelineKycItemVO {
	private String customerId;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码
	private String distributorId;//代理商Id
	private String companyName;//代理商公司名称
	private String logofile;//公司logo
	private String nextRpqDate;//距离下次RPQ的天数
	private String nextDocDate;//距离下次Doc审查的天数
	
	
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLogofile() {
		return logofile;
	}
	public void setLogofile(String logofile) {
		this.logofile = logofile;
	}
	public String getNextRpqDate() {
		return nextRpqDate;
	}
	public void setNextRpqDate(String nextRpqDate) {
		this.nextRpqDate = nextRpqDate;
	}

	public String getNextDocDate() {
		return nextDocDate;
	}
	public void setNextDocDate(String nextDocDate) {
		this.nextDocDate = nextDocDate;
	}
	
}
