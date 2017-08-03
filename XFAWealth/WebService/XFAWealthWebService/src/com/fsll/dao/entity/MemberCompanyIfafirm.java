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
@Table(name = "member_company_ifafirm")
public class MemberCompanyIfafirm implements java.io.Serializable {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "company_id")
	@JsonIgnore
	private CompanyInfo company;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifafirm_id")
	@JsonIgnore
	private MemberIfafirm ifafirm;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CompanyInfo getCompany() {
		return company;
	}

	public void setCompany(CompanyInfo company) {
		this.company = company;
	}

	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}

	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
	}

}