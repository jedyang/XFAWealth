package com.fsll.app.ifa.client.vo;

/**
 * IFA的销售管道的账户列表项的实体类VO
 * @author zxtan
 * @date 2017-04-05
 */
public class AppPipelineAccountItemVO {
	private String customerId;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码
	private String accountId;//交易账号
	private String accountNo;//交易账号
	private String subFlag;//1：子账户 0：主账户
	private String openStatus;//开户状态
	private String lastUpdate;//最后更新时间
	
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
	
	public String getAccountId(){
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getAccountNo(){
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getSubFlag(){
		return subFlag;
	}
	public void setSubFlag(String subFlag) {
		this.subFlag = subFlag;
	}
	public String getOpenStatus(){
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public String getlastUpdate(){
		return lastUpdate;
	}
	public void setlastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}		
}
