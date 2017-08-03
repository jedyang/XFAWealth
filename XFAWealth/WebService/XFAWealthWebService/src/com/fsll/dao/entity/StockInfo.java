package com.fsll.dao.entity;

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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "stock_info")
public class StockInfo implements java.io.Serializable {
	private String id;
	private ProductInfo product;
	private String symbolCode;
	private String exchangeCode;
	private Date createTime;
	private Date lastUpdate;
	private String overhead;
	private Date overheadTime;
	private String isValid;
	@Transient
	private String stockName;

	@GenericGenerator(name = "generator", strategy = "uuid.hex")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	@Column(name = "symbol_code")
	public String getSymbolCode() {
		return this.symbolCode;
	}

	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}

	@Column(name = "exchange_code")
	public String getExchangeCode() {
		return this.exchangeCode;
	}

	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "overhead")
	public String getOverhead() {
		return this.overhead;
	}

	public void setOverhead(String overhead) {
		this.overhead = overhead;
	}

	@Column(name = "overhead_time")
	public Date getOverheadTime() {
		return this.overheadTime;
	}

	public void setOverheadTime(Date overheadTime) {
		this.overheadTime = overheadTime;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Transient
	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

}