package com.fsll.core.entity;

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
@Table(name = "sys_role_usergroup")
public class SysRoleUsergroup implements java.io.Serializable {

	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@JsonIgnore
	private SysUsergroup group;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	@JsonIgnore
	private SysRole role;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysUsergroup getGroup() {
		return group;
	}

	public void setGroup(SysUsergroup group) {
		this.group = group;
	}

	public SysRole getRole() {
		return role;
	}

	public void setRole(SysRole role) {
		this.role = role;
	}
	
	
}
