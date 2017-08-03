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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "distributor_org")
public class DistributorOrg {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	@JsonIgnore
	private DistributorOrg parent;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@JsonIgnore
	private MemberDistributor distributor;
	
	@Column(name = "code")	
	private String code;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "order_by")	
	private String orderBy;
	
	@Column(name = "remark")
	private String remark;
	
    @Column(name = "class_layer")
	private String classLayer;
	
    @Column(name = "is_valid")
	private String isValid;
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public DistributorOrg getParent() {
		return parent;
	}

	public void setParent(DistributorOrg parent) {
		this.parent = parent;
	}

	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getClassLayer() {
		return classLayer;
	}

	public void setClassLayer(String classLayer) {
		this.classLayer = classLayer;
	}
}
