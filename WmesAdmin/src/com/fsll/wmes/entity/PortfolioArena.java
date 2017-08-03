package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "portfolio_arena")
public class PortfolioArena implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@Column(name = "portfolio_name")
	private String portfolioName;

	@Column(name = "geo_allocation")
	private String geoAllocation;

	@Column(name = "sector")
	private String sector;

	@Column(name = "investment_goal")
	private String investmentGoal;

	@Column(name = "suitability")
	private String suitability;
	
	@Column(name = "risk_level")
	private Integer riskLevel;
	
	@Column(name = "description")
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	private MemberBase creator;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "overhead")
	private String overhead;
	
	@Column(name = "overhead_time")
	private Date overheadTime;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_valid")
	private String isValid;
	
	@Column(name = "is_public")
	private String isPublic;
	
	@Column(name = "reason")
	private String reason;
	
	@Column(name = "total_return")
	private Double totalReturn;
	
	@Column(name = "aip_flag")
	private String aipFlag;
	
    @Column(name = "return_chart")
	private String returnChart;
	
	@Transient
	private Integer click;
	
	@Transient
	private Integer productCount;
	
	@Transient
	private MemberIfa ifa;//creator

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getGeoAllocation() {
		return geoAllocation;
	}

	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getInvestmentGoal() {
		return investmentGoal;
	}

	public void setInvestmentGoal(String investmentGoal) {
		this.investmentGoal = investmentGoal;
	}

	public String getSuitability() {
		return suitability;
	}

	public void setSuitability(String suitability) {
		this.suitability = suitability;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}
	
	public String getOverhead() {
		return overhead;
	}

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public Date getOverheadTime() {
		return overheadTime;
	}

	public void setOverheadTime(Date overheadTime) {
		this.overheadTime = overheadTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	public Double getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(Double totalReturn) {
		this.totalReturn = totalReturn;
	}

	public String getAipFlag() {
		return aipFlag;
	}

	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
	}

	public String getReturnChart() {
		return returnChart;
	}

	public void setReturnChart(String returnChart) {
		this.returnChart = returnChart;
	}
	
}