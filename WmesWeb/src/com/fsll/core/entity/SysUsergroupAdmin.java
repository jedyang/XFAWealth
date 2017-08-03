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
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_usergroup_admin")
public class SysUsergroupAdmin implements java.io.Serializable{

	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "admin_id")
	@JsonIgnore
	private SysAdmin admin;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@JsonIgnore
	private SysUsergroup group;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysAdmin getAdmin() {
		return admin;
	}

	public void setAdmin(SysAdmin admin) {
		this.admin = admin;
	}

	public SysUsergroup getGroup() {
		return group;
	}

	public void setGroup(SysUsergroup group) {
		this.group = group;
	}
	
}
