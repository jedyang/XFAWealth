package com.fsll.core.entity;

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
@Table(name = "sys_admin_relate")
public class SysAdminRelate implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private SysAdminRelate parent;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "admin_id")
	@JsonIgnore
	private SysAdmin admin;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysAdminRelate getParent() {
		return parent;
	}

	public void setParent(SysAdminRelate parent) {
		this.parent = parent;
	}

	public SysAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(SysAdmin admin) {
		this.admin = admin;
	}

}