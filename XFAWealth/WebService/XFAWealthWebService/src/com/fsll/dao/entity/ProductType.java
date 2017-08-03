package com.fsll.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product_type")
public class ProductType implements java.io.Serializable {
	private String id;
	private String displayColor;
	private Integer orderBy;
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

	@Column(name = "display_color")
	public String getDisplayColor() {
		return displayColor;
	}

	public void setDisplayColor(String displayColor) {
		this.displayColor = displayColor;
	}

	@Column(name = "order_by")
	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

}