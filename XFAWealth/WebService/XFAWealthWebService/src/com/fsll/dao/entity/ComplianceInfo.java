package com.fsll.dao.entity;

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
@Table(name = "compliance_info")
public class ComplianceInfo implements java.io.Serializable {
	private String id;
	private MemberDistributor distributor;
	private String name;
	private String type;
	private String actionType;
	private String valueCode;
	private String instruction;
	private MemberBase createBy;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@JsonIgnore
	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}

	@Column(name = "name")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "action_type")
	public String getActionType() {
		return this.actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Column(name = "value_code")
	public String getValueCode() {
		return this.valueCode;
	}

	public void setValueCode(String valueCode) {
		this.valueCode = valueCode;
	}

	@Column(name = "instruction")
	public String getInstruction() {
		return this.instruction;
	}

	public void setInstruction(String instruction) {
		this.instruction = instruction;
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

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
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