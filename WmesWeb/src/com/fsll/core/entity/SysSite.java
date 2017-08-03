package com.fsll.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "sys_site")
public class SysSite implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
	
	@Column(name = "domain")
	private String domain;
	
	@Column(name = "site_path")
	private String sitePath;
	
	@Column(name = "site_name")
	private String siteName;
	
	@Column(name = "short_name")
	private String shortName;
	
	@Column(name = "is_master")
	private String isMaster;
	
	@Column(name = "def_lang")
	private String defLang;
	
	@Column(name = "visit_count_div")
	private Integer visitCountDiv;
	
	@Column(name = "login_fail_count")
	private Integer loginFailCount;
	
	@Column(name = "login_retry_hour")
	private Integer loginRetryHour;
	
	@Column(name = "token_time_out")
	private Integer tokenTimeOut;
	
	/****** 以下字段不存储数据库,仅用于页面显示 *******/
	@Transient
	private String xh;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSitePath() {
		return sitePath;
	}

	public void setSitePath(String sitePath) {
		this.sitePath = sitePath;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getIsMaster() {
		return isMaster;
	}

	public void setIsMaster(String isMaster) {
		this.isMaster = isMaster;
	}

	public String getDefLang() {
		return defLang;
	}

	public void setDefLang(String defLang) {
		this.defLang = defLang;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public Integer getVisitCountDiv() {
		return visitCountDiv;
	}

	public void setVisitCountDiv(Integer visitCountDiv) {
		this.visitCountDiv = visitCountDiv;
	}

	public Integer getLoginFailCount() {
		return loginFailCount;
	}

	public void setLoginFailCount(Integer loginFailCount) {
		this.loginFailCount = loginFailCount;
	}

	public Integer getLoginRetryHour() {
		return loginRetryHour;
	}

	public void setLoginRetryHour(Integer loginRetryHour) {
		this.loginRetryHour = loginRetryHour;
	}

	public Integer getTokenTimeOut() {
		return tokenTimeOut;
	}

	public void setTokenTimeOut(Integer tokenTimeOut) {
		this.tokenTimeOut = tokenTimeOut;
	}
	
}