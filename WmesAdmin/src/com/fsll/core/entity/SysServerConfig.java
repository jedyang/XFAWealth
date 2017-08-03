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

@Entity 
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="sys_server_config") 
public class SysServerConfig implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id", nullable = true)
	private SysServerInfo info;
    
    @Column(name="param_code")
	private String paramCode;
    
    @Column(name="param_name")
	private String paramName;
    
    @Column(name="param_value")
	private String paramValue;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysServerInfo getInfo() {
		return info;
	}

	public void setInfo(SysServerInfo info) {
		this.info = info;
	}

	public String getParamCode() {
		return paramCode;
	}

	public void setParamCode(String paramCode) {
		this.paramCode = paramCode;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

}