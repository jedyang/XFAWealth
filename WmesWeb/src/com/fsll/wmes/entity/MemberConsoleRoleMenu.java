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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "member_console_role_menu")
public class MemberConsoleRoleMenu implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	@JsonIgnore
	private MemberConsoleRole role;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "menu_id")
	@JsonIgnore
	private MemberMenu menu;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberConsoleRole getRole() {
		return role;
	}

	public void setRole(MemberConsoleRole role) {
		this.role = role;
	}

	public MemberMenu getMenu() {
		return menu;
	}

	public void setMenu(MemberMenu menu) {
		this.menu = menu;
	}

}