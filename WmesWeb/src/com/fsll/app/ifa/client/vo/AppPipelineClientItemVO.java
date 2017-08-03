package com.fsll.app.ifa.client.vo;

/**
 * IFA Market 首页实体类VO
 * @author zxtan
 * @date 2017-04-10
 */
public class AppPipelineClientItemVO {
	private String customerId;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码	
	private String clientType;//客户类型，1正式  0潜在
	
	private Object dataList;//
	
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
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}	
	public Object getDataList() {
		return dataList;
	}
	public void setDataList(Object dataList) {
		this.dataList = dataList;
	}	
}
