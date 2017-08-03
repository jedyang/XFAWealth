package com.fsll.dao.entity;

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
@Table(name = "portfolio_hold_product")
public class PortfolioHoldProduct implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hold_id")
	@JsonIgnore
    private PortfolioHold portfolioHold;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	@NotFound(action=NotFoundAction.IGNORE)
	private ProductInfo product;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private InvestorAccount account;
	
	@Column(name = "account_no")
	private String accountNo;
	
	@Column(name = "holding_unit")
	private Double holdingUnit;
	
	@Column(name = "available_unit")
	private Double availableUnit;
	
	@Column(name = "reference_cost")
	private Double referenceCost;
	
	@Column(name = "base_currency")
	private String baseCurrency;
	
	@Column(name = "total_market_value")
	private Double totalMarketValue;
	
	@Column(name="cumperf_rate")
	private Double cumperfRate;
	
	@Column(name="total_pl")
	private Double totalPl;
	
	@Column(name="day_pl")
	private Double dayPl;
	
	@Column(name = "day_return")
	private Double dayReturn;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}

	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
	}
	
	public InvestorAccount getAccount() {
		return account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public Double getHoldingUnit() {
		return holdingUnit;
	}

	public void setHoldingUnit(Double holdingUnit) {
		this.holdingUnit = holdingUnit;
	}

	public Double getAvailableUnit() {
		return availableUnit;
	}

	public void setAvailableUnit(Double availableUnit) {
		this.availableUnit = availableUnit;
	}

	public Double getReferenceCost() {
		return referenceCost;
	}

	public void setReferenceCost(Double referenceCost) {
		this.referenceCost = referenceCost;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Double getTotalMarketValue() {
		return totalMarketValue;
	}

	public void setTotalMarketValue(Double totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}

	public Double getCumperfRate() {
		return cumperfRate;
	}

	public void setCumperfRate(Double cumperfRate) {
		this.cumperfRate = cumperfRate;
	}

	public Double getTotalPl() {
		return totalPl;
	}

	public void setTotalPl(Double totalPl) {
		this.totalPl = totalPl;
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