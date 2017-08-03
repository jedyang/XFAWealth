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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "fund_bonus")
public class FundBonus implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fund_id")
	@JsonIgnore
	private FundInfo fund;
	
	@Column(name = "year")
	private Integer year;
	
	@Column(name = "ex_dividend_date")
	private Date exDividendDate;
	
	@Column(name = "dividend_per_unit")
	private Double dividendPerUnit;
	
	@Column(name = "annual_dividend_yield")
	private Double annualDividendYield;
	
    @Column(name = "create_time")
	private Date createTime;
    
    @Column(name = "last_update")
	private Date lastUpdate;
    
    @Column(name = "is_valid")
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FundInfo getFund() {
		return fund;
	}

	public void setFund(FundInfo fund) {
		this.fund = fund;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Date getExDividendDate() {
		return exDividendDate;
	}

	public void setExDividendDate(Date exDividendDate) {
		this.exDividendDate = exDividendDate;
	}

	public Double getDividendPerUnit() {
		return dividendPerUnit;
	}

	public void setDividendPerUnit(Double dividendPerUnit) {
		this.dividendPerUnit = dividendPerUnit;
	}

	public Double getAnnualDividendYield() {
		return annualDividendYield;
	}

	public void setAnnualDividendYield(Double annualDividendYield) {
		this.annualDividendYield = annualDividendYield;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}