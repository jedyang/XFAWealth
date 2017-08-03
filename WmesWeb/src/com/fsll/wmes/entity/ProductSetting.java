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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "product_setting")
public class ProductSetting implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	private ProductInfo product;
    
	@Column(name = "product_type")
	private String productType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "setting_type")
	@NotFound(action=NotFoundAction.IGNORE)
	private ProductSettingType settingType;
	
	@Column(name = "scope_type")
	private String scopeType;
	
	@Column(name = "creator_id")
	private String creatorId;

	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "order_by")
	private Integer orderBy;
	
	@Column(name = "is_valid")
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo product) {
		this.product = product;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public ProductSettingType getSettingType() {
		return settingType;
	}

	public void setSettingType(ProductSettingType settingType) {
		this.settingType = settingType;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getScopeType() {
		return scopeType;
	}

	public void setScopeType(String scopeType) {
		this.scopeType = scopeType;
	}
	
}
