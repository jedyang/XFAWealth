package com.fsll.app.ifa.market.vo;

/**
 * 投资方案列表实体类VO
 * @author zxtan
 * @date 2017-03-24
 */
public class AppCrmProposalItemVO {
	private String id;//方案Id
	private String proposalName;//方案名称
	private String baseCurrId;//基础参照货币id
	private String initialInvestAmount;//初始投资金额
	private String status;//投资方案状态，-2 撤销；-1被退回 0：草稿，1:待签约；2：已经签约
	private String createTime;//创建时间
	private String lastUpdate;//最后更新时间
    private String memberId;//投资者member id
    private String clientName;//投资者名字
    private String iconUrl;//投资者头像
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	

}
