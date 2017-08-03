package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product_setting_type")
public class ProductSettingType implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@Column(name = "type_name")
	private String typeName;
	
	@Column(name = "setting_type")
	private String settingType;
	
	@Column(name = "app_background")
	private String appBackground;
	
	@Column(name = "web_background")
	private String webBackground;
	
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

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getSettingType() {
		return settingType;
	}

	public void setSettingType(String settingType) {
		this.settingType = settingType;
	}

	public String getAppBackground() {
		return appBackground;
	}

	public void setAppBackground(String appBackground) {
		this.appBackground = appBackground;
	}

	public String getWebBackground() {
		return webBackground;
	}

	public void setWebBackground(String webBackground) {
		this.webBackground = webBackground;
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
	
}
