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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ifa_ext_info")
public class IfaExtInfo implements java.io.Serializable {
	private String id;
	private MemberIfa ifa;
	private String aumNumber;
	private String cumNumber;
	private String adviserFee;
	private String atMost;
	private String highlight;
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

	@Column(name = "cum_number")
	public String getCumNumber() {
		return this.cumNumber;
	}

	public void setCumNumber(String cumNumber) {
		this.cumNumber = cumNumber;
	}

	@Column(name = "adviser_fee")
	public String getAdviserFee() {
		return this.adviserFee;
	}

	public void setAdviserFee(String adviserFee) {
		this.adviserFee = adviserFee;
	}

	@Column(name = "at_most")
	public String getAtMost() {
		return this.atMost;
	}

	public void setAtMost(String atMost) {
		this.atMost = atMost;
	}

	@Column(name = "highlight")
	public String getHighlight() {
		return this.highlight;
	}

	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}