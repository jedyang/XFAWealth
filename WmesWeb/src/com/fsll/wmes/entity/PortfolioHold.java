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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "portfolio_hold")
public class PortfolioHold implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
    private PortfolioInfo portfolio;
	
	@Column(name = "portfolio_name")
	private String portfolioName;
    	
	@Column(name = "total_return_rate")
	private Double totalReturnRate;
	
	@Column(name = "total_return_value")
	private Double totalReturnValue;

	@Column(name = "total_market_value")
	private Double totalMarketValue;
	
	@Column(name = "total_cash")
	private Double totalCash;
	
	@Column(name = "total_asset")
	private Double totalAsset;
	
	@Column(name = "day_pl")
	private Double dayPl;
	
	@Column(name = "day_return")
	private Double dayReturn;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "base_currency")
	private String baseCurrency;
	
	@Column(name = "if_first")
	private String ifFirst;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private MemberBase member;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	private MemberIfa ifa;
	
    @Column(name = "risk_level")
	private Integer riskLevel;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PortfolioInfo getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
	}

	public Double getTotalReturnRate() {
		return totalReturnRate;
	}

	public void setTotalReturnRate(Double totalReturnRate) {
		this.totalReturnRate = totalReturnRate;
	}

	public Double getTotalReturnValue() {
		return totalReturnValue;
	}

	public void setTotalReturnValue(Double totalReturnValue) {
		this.totalReturnValue = totalReturnValue;
	}

	public Double getTotalMarketValue() {
		return totalMarketValue;
	}

	public void setTotalMarketValue(Double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getIfFirst() {
		return ifFirst;
	}

	public void setIfFirst(String ifFirst) {
		this.ifFirst = ifFirst;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Double getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}

	public Double getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}
	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	public Double getDayPl() {
		return dayPl;
	}

	public void setDayPl(Double dayPl) {
		this.dayPl = dayPl;
	}

	public Double getDayReturn() {
		return dayReturn;
	}

	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}
}