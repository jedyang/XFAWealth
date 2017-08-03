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
@Table(name = "distributor_server")
public class DistributorServer implements java.io.Serializable {
	private String id;
	private MemberDistributor distributor;
	private String platFrom;
	private String openUrl;
	private String openUser;
	private String openPwd;
	private String aimaUrl;
	private String aimaUser;
	private String aimaPwd;
	private String omsSsmIp;
	private Integer omsSsmPort;
	private String omsItsIp;
	private Integer omsItsPort;
	private String omsUser;
	private String omsPwd;
	private String isValid;
	
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@JsonIgnore
	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}

	@Column(name = "open_url")
	public String getOpenUrl() {
		return this.openUrl;
	}

	public void setOpenUrl(String openUrl) {
		this.openUrl = openUrl;
	}

	@Column(name = "open_user")
	public String getOpenUser() {
		return this.openUser;
	}

	public void setOpenUser(String openUser) {
		this.openUser = openUser;
	}

	@Column(name = "open_pwd")
	public String getOpenPwd() {
		return this.openPwd;
	}

	public void setOpenPwd(String openPwd) {
		this.openPwd = openPwd;
	}

	@Column(name = "aima_url")
	public String getAimaUrl() {
		return this.aimaUrl;
	}

	public void setAimaUrl(String aimaUrl) {
		this.aimaUrl = aimaUrl;
	}

	@Column(name = "aima_user")
	public String getAimaUser() {
		return this.aimaUser;
	}

	public void setAimaUser(String aimaUser) {
		this.aimaUser = aimaUser;
	}

	@Column(name = "aima_pwd")
	public String getAimaPwd() {
		return this.aimaPwd;
	}

	public void setAimaPwd(String aimaPwd) {
		this.aimaPwd = aimaPwd;
	}

	@Column(name = "oms_ssm_ip")
	public String getOmsSsmIp() {
		return this.omsSsmIp;
	}

	public void setOmsSsmIp(String omsSsmIp) {
		this.omsSsmIp = omsSsmIp;
	}

	@Column(name = "oms_ssm_port")
	public Integer getOmsSsmPort() {
		return this.omsSsmPort;
	}

	public void setOmsSsmPort(Integer omsSsmPort) {
		this.omsSsmPort = omsSsmPort;
	}

	@Column(name = "oms_its_ip")
	public String getOmsItsIp() {
		return this.omsItsIp;
	}

	public void setOmsItsIp(String omsItsIp) {
		this.omsItsIp = omsItsIp;
	}

	@Column(name = "oms_its_port")
	public Integer getOmsItsPort() {
		return this.omsItsPort;
	}

	public void setOmsItsPort(Integer omsItsPort) {
		this.omsItsPort = omsItsPort;
	}

	@Column(name = "oms_user")
	public String getOmsUser() {
		return this.omsUser;
	}

	public void setOmsUser(String omsUser) {
		this.omsUser = omsUser;
	}

	@Column(name = "oms_pwd")
	public String getOmsPwd() {
		return this.omsPwd;
	}

	public void setOmsPwd(String omsPwd) {
		this.omsPwd = omsPwd;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Column(name = "plat_from")
	public String getPlatFrom() {
		return platFrom;
	}

	public void setPlatFrom(String platFrom) {
		this.platFrom = platFrom;
	}

}