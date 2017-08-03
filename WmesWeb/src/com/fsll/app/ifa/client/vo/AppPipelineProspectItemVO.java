package com.fsll.app.ifa.client.vo;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-04-11
 */
public class AppPipelineProspectItemVO {
	private String customerId;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码	
	
	private String lastUpdate;//最新时间
	private String lastContact;//最后联系时间
	private String contactTimes;//联系次数
	
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
		
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getLastContact() {
		return lastContact;
	}
	public void setLastContact(String lastContact) {
		this.lastContact = lastContact;
	}		

	public String getContactTimes() {
		return contactTimes;
	}
	public void setContactTimes(String contactTimes) {
		this.contactTimes = contactTimes;
	}	
}
