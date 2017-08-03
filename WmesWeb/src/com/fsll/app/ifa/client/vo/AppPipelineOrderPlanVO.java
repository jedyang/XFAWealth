package com.fsll.app.ifa.client.vo;

/**
 * IFA的销售管道的方案列表项的实体类VO
 * @author zxtan
 * @date 2017-04-05
 */
public class AppPipelineOrderPlanVO {
	private String customerId;//客户Id
	private String memberId;//客户MemberId
	private String nickName;//客户名称
	private String mobileCode;//手机号的国家区号
	private String mobileNumber;//手机号码
	private String planId;//计划Id
	private String holdId;//组合Id
	private String baseCurrency;//基础货币
	private String totalBuy;//投资金额
	private String finishStatus;//-1：初始创建（从投资方案的组合，创建初始组合） 0：草稿（暂时保存），1：审批中,2审批不通过，3审批通过，4：交易中（提交给交易系统）；5：交易完成（交易系统返回结果，异步）。
	private String createTime;//创建时间
	private String lastUpdate;//最后更新时间
	private String successCount;//成功数量
	private String pendCount;//交易中数量
	private String failCount;//失败数量
	
	
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
	
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getHoldId() {
		return holdId;
	}
	public void setHoldId(String holdId) {
		this.holdId = holdId;
	}
	public String getBaseCurrency() {
		return baseCurrency;
	}
	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}
	public String getTotalBuy() {
		return totalBuy;
	}
	public void setTotalBuy(String totalBuy) {
		this.totalBuy = totalBuy;
	}

	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
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
	public String getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(String successCount) {
		this.successCount = successCount;
	}

	public String getPendCount() {
		return pendCount;
	}
	public void setPendCount(String pendCount) {
		this.pendCount = pendCount;
	}

	public String getFailCount() {
		return failCount;
	}
	public void setFailCount(String failCount) {
		this.failCount = failCount;
	}
}
