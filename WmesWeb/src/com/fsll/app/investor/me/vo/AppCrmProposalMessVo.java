package com.fsll.app.investor.me.vo;

/**
 * 投资方案实体类VO
 * @author zpzhou
 * @date 2016-9-22
 */
public class AppCrmProposalMessVo {
	private String id;
	private String proposalName;//方案名称
	private String baseCurrId;//基础参照货币id
	private String initialInvestAmount;//初始投资金额
	private String liquidityNeed;//流动性约束
	private String timeFrame;//投资年限描述
	private String taxConcerns;//相关费用描述
	private String lrf;//法律和监管因素描述
	private String unp;//特殊要求描述
	private String remark;//备注说明
	private String status;//投资方案状态，-2 撤销；-1被退回 0：草稿，1:待签约；2：已经签约
	private String createTime;//创建时间
	private String lastUpdate;//最后更新时间
	private String creator;//IFA名称
	private String iconUrl;//IFA头像
	private String pdfUrl;//PDF Url
	
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
	public String getLiquidityNeed() {
		return liquidityNeed;
	}
	public void setLiquidityNeed(String liquidityNeed) {
		this.liquidityNeed = liquidityNeed;
	}
	public String getTimeFrame() {
		return timeFrame;
	}
	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}
	public String getTaxConcerns() {
		return taxConcerns;
	}
	public void setTaxConcerns(String taxConcerns) {
		this.taxConcerns = taxConcerns;
	}
	public String getLrf() {
		return lrf;
	}
	public void setLrf(String lrf) {
		this.lrf = lrf;
	}
	public String getUnp() {
		return unp;
	}
	public void setUnp(String unp) {
		this.unp = unp;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getPdfUrl() {
		return pdfUrl;
	}
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}
}
