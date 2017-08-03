package com.fsll.core.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="sys_server_info") 
public class SysServerInfo implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @Column(name="server_code")
	private String serverCode;
    
    @Column(name="server_name")
	private String serverName;
	
    @Column(name="server_class")
	private String serverClass;
    
    @Column(name="remark")
	private String remark;
    
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "info")
	@OrderBy("id asc")
	private Set<SysServerConfig> configSet = new HashSet<SysServerConfig>(0);
    
	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "info")
	@OrderBy("id asc")
	private Set<SysServerControl> controlSet = new HashSet<SysServerControl>(0);

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerClass() {
		return serverClass;
	}

	public void setServerClass(String serverClass) {
		this.serverClass = serverClass;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<SysServerConfig> getConfigSet() {
		return configSet;
	}

	public void setConfigSet(Set<SysServerConfig> configSet) {
		this.configSet = configSet;
	}

	public Set<SysServerControl> getControlSet() {
		return controlSet;
	}

	public void setControlSet(Set<SysServerControl> controlSet) {
		this.controlSet = controlSet;
	}

}