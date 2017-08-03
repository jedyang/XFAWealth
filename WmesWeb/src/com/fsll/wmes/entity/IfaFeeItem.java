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
@Table(name = "ifa_fee_item")
public class IfaFeeItem implements java.io.Serializable {
	private String id;
	private MemberIfa ifa;
	private String aumNumber;
	private String feeNumber;
	private Date lastUpdate;
	

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	@JsonIgnore
	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	@Column(name = "aum_number")
	public String getAumNumber() {
		return this.aumNumber;
	}

	public void setAumNumber(String aumNumber) {
		this.aumNumber = aumNumber;
	}

	@Column(name = "fee_number")
	public String getFeeNumber() {
		return this.feeNumber;
	}

	public void setFeeNumber(String feeNumber) {
		this.feeNumber = feeNumber;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	
	private Integer index;
	@Transient
	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
}