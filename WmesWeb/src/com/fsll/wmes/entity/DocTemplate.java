package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "doc_template")
public class DocTemplate implements java.io.Serializable {
	private String id;
	private MemberDistributor distributor;
	private String clientType;
	private String code;
	private String title;
	private Boolean isDefault;
	private MemberBase createBy;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") 
	private Date createTime;
	private String status;
	private String langCode;
	private Integer isValid;
	
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

	@Column(name = "client_type")
	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	@Column(name = "code")
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	@Transient
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "is_default")
	public Boolean getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "create_by")
	@JsonIgnore
	public MemberBase getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(MemberBase createBy) {
		this.createBy = createBy;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "lang_code")
	public String getLangCode() {
		return this.langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	@Column(name = "is_valid")
	public Integer getIsValid() {
		return this.isValid;
	}

	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

}