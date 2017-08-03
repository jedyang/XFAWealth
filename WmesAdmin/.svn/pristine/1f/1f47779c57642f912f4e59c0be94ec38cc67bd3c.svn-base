package com.fsll.wmes.entity;

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
@Table(name = "member_company_menu")
public class MemberCompanyMenu implements java.io.Serializable {
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
	@JoinColumn(name = "menu_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private MemberMenu menu;

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

	public MemberMenu getMenu() {
		return menu;
	}

	public void setMenu(MemberMenu menu) {
		this.menu = menu;
	}
}