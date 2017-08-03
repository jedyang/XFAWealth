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
@Table(name = "crm_customer_group_relationship")
public class CrmCustomerGroupRelationship implements java.io.Serializable {
	private String id;
	private CrmCustomerGroup group;
	private CrmCustomer customer;
	
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
	@JoinColumn(name = "group_id")
	@JsonIgnore
	public CrmCustomerGroup getGroup() {
		return group;
	}

	public void setGroup(CrmCustomerGroup group) {
		this.group = group;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	@JsonIgnore
	public CrmCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(CrmCustomer customer) {
		this.customer = customer;
	}
	
}