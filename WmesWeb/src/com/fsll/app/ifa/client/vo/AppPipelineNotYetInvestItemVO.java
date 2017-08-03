package com.fsll.app.ifa.client.vo;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-04-10
 */
public class AppPipelineNotYetInvestItemVO {
	private String customerId;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码
	private String accountId;//投资账户
	private String accountNo;//投资账户
	private String distributorId;//公司Id
	private String companyName;//公司名称
	private String logofile;//logo
	
	
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
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
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
}
