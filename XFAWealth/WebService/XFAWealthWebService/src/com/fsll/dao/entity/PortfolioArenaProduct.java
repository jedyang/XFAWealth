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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "portfolio_arena_product")
public class PortfolioArenaProduct implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
    private PortfolioArena portfolio;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductInfo product;

	@Column(name = "allocation_rate")
	private Double allocationRate;
	
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

	public PortfolioArena getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(PortfolioArena portfolio) {
		this.portfolio = portfolio;
	}

	public Double getAllocationRate() {
		return allocationRate;
	}

	public void setAllocationRate(Double allocationRate) {
		this.allocationRate = allocationRate;
	}

}