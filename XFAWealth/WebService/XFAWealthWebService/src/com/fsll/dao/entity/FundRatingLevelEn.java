package com.fsll.dao.entity;

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
@Table(name = "fund_rating_level_en")
public class FundRatingLevelEn implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "org_id")
	@JsonIgnore
	private FundRatingOrg orgId;
    
	@Column(name = "org_name")
	private String orgName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public FundRatingOrg getOrgId() {
		return orgId;
	}

	public void setOrgId(FundRatingOrg orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
}