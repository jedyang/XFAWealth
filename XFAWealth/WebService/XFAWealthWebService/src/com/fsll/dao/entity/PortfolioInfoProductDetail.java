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
@Table(name = "portfolio_info_product_detail")
public class PortfolioInfoProductDetail implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "portfolio_id")
	@JsonIgnore
    private PortfolioInfo portfolio;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductInfo product;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "candidate_product_id")
	@JsonIgnore
	private ProductInfo candidateProduct;
	
	@Column(name = "if_def")
	private String ifDef;

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

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public ProductInfo getCandidateProduct() {
		return candidateProduct;
	}

	public void setCandidateProduct(ProductInfo candidateProduct) {
		this.candidateProduct = candidateProduct;
	}

	public String getIfDef() {
		return ifDef;
	}

	public void setIfDef(String ifDef) {
		this.ifDef = ifDef;
	}

}