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
@Table(name = "insure_return")
public class InsureReturn implements java.io.Serializable {
	private String id;
	private InsureInfo insure;
	private String type;
	private String periodCode;
	private Double increase;
	private Double typeAverage;
	private Integer newRanking;
	private Integer lastRanking;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;

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
	@JoinColumn(name = "insure_id")
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	public InsureInfo getInsure() {
		return insure;
	}

	public void setInsure(InsureInfo insure) {
		this.insure = insure;
	}

	
	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "period_code")
	public String getPeriodCode() {
		return this.periodCode;
	}

	public void setPeriodCode(String periodCode) {
		this.periodCode = periodCode;
	}

	@Column(name = "increase")
	public Double getIncrease() {
		return this.increase;
	}

	public void setIncrease(Double increase) {
		this.increase = increase;
	}

	@Column(name = "type_average")
	public Double getTypeAverage() {
		return this.typeAverage;
	}

	public void setTypeAverage(Double typeAverage) {
		this.typeAverage = typeAverage;
	}

	@Column(name = "new_ranking")
	public Integer getNewRanking() {
		return this.newRanking;
	}

	public void setNewRanking(Integer newRanking) {
		this.newRanking = newRanking;
	}

	@Column(name = "last_ranking")
	public Integer getLastRanking() {
		return this.lastRanking;
	}

	public void setLastRanking(Integer lastRanking) {
		this.lastRanking = lastRanking;
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

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}