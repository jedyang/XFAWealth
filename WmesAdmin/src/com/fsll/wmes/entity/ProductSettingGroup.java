package com.fsll.wmes.entity;

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
@Table(name = "product_setting_group")
public class ProductSettingGroup implements java.io.Serializable {
	private String id;
	private ProductSetting setting;
	private MemberGroup group;

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
	@JoinColumn(name = "setting_id")
	@JsonIgnore
	public ProductSetting getSetting() {
		return setting;
	}

	public void setSetting(ProductSetting setting) {
		this.setting = setting;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@JsonIgnore
	public MemberGroup getGroup() {
		return group;
	}

	public void setGroup(MemberGroup group) {
		this.group = group;
	}

}