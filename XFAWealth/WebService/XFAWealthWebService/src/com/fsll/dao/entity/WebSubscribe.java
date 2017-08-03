package com.fsll.dao.entity;

import java.sql.Timestamp;
import java.util.Date;

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
@Table(name = "web_subscribe")
public class WebSubscribe implements java.io.Serializable {
	private String id;
	private String moduleType;
	private String relateId;
	private String keyword;
	private MemberBase owner;
	private Date createTime;
	private Date lastUpdate;
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

	@Column(name = "module_type")
	public String getModuleType() {
		return this.moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	@Column(name = "relate_id")
	public String getRelateId() {
		return this.relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	@Column(name = "keyword")
	public String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	@JsonIgnore
	public MemberBase getOwner() {
		return this.owner;
	}

	public void setOwner(MemberBase owner) {
		this.owner = owner;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

}