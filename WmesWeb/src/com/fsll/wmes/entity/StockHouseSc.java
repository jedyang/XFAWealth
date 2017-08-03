package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "stock_house_sc")
public class StockHouseSc implements java.io.Serializable {
	private String id;
	private String houseName;
	private String pinyin;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "house_name")
	public String getHouseName() {
		return this.houseName;
	}

	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}

	@Column(name = "pinyin")
	public String getPinyin() {
		return this.pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
}