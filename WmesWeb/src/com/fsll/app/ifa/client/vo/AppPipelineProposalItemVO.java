package com.fsll.app.ifa.client.vo;

/**
 * IFA的销售管道的方案列表项的实体类VO
 * @author zxtan
 * @date 2017-04-05
 */
public class AppPipelineProposalItemVO {
	private String customerId;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码
	private String proposalId;//方案Id
	private String proposalName;//方案名称
	private String baseCurrId;//基础参照货币id
	private String initialInvestAmount;//初始投资金额
	private String status;//投资方案状态，-2 撤销；-1被退回 0：草稿，1:待签约；2：已经签约
	private String createTime;//创建时间
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
	
	public String getProposalId() {
		return proposalId;
	}
	public void setProposalId(String proposalId) {
		this.proposalId = proposalId;
	}
	public String getProposalName() {
		return proposalName;
	}
	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}
	public String getBaseCurrId() {
		return baseCurrId;
	}
	public void setBaseCurrId(String baseCurrId) {
		this.baseCurrId = baseCurrId;
	}
	public String getInitialInvestAmount() {
		return initialInvestAmount;
	}
	public void setInitialInvestAmount(String initialInvestAmount) {
		this.initialInvestAmount = initialInvestAmount;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}		
}
