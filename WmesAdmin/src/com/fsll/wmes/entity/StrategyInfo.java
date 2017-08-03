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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "strategy_info")
public class StrategyInfo implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;

	@Column(name = "strategy_name")
    private String strategyName;

	@Column(name = "risk_level")
    private String riskLevel;
	
	@Column(name = "geo_allocation")
	private String geoAllocation;

	@Column(name = "sector")
	private String sector;

	@Column(name = "investment_goal")
	private String investmentGoal;

	@Column(name = "suitability")
	private String suitability;
	
	@Column(name = "reason")
    private String reason;
	
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
	
	
	@Transient
	private Integer click;
	
	@Transient
	private Integer productCount;
	
	@Transient
	private MemberIfa ifa;//creator
	
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getStrategyName() {
		return this.strategyName;
	}
	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}


	public String getInvestmentGoal() {
		return this.investmentGoal;
	}
	public void setInvestmentGoal(String investmentGoal) {
		this.investmentGoal = investmentGoal;
	}


	public MemberBase getCreator() {
		return creator;
	}
	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return this.createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOverheadTime() {
		return overheadTime;
	}
	public void setOverheadTime(Date overheadTime) {
		this.overheadTime = overheadTime;
	}

	public String getGeoAllocation() {
		return geoAllocation;
	}
	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getSector() {
		return sector;
	}

	public void setSuitability(String suitability) {
		this.suitability = suitability;
	}
	public String getSuitability() {
		return suitability;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public Integer getClick() {
		return click;
	}
	public void setClick(Integer click) {
		this.click = click;
	}
	
	public String getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}
	
	public String getOverhead() {
		return overhead;
	}
	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public Integer getProductCount() {
		return productCount;
	}
	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}
	public MemberIfa getIfa() {
		return ifa;
	}
	
}