package com.fsll.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "fund_fees_item_sc")
public class FundFeesItemSc implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
    private String id;
    
    @Column(name = "fees_item_code")
	private String feesItemCode;
    
    @Column(name = "fees_item")
	private String feesItem;
    
    @Column(name = "fees")
	private String fees;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFeesItemCode() {
		return feesItemCode;
	}

	public void setFeesItemCode(String feesItemCode) {
		this.feesItemCode = feesItemCode;
	}

	public String getFeesItem() {
		return feesItem;
	}

	public void setFeesItem(String feesItem) {
		this.feesItem = feesItem;
	}

	public String getFees() {
		return fees;
	}

	public void setFees(String fees) {
		this.fees = fees;
	}
}