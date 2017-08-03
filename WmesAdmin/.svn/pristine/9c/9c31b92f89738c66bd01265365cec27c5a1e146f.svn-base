package com.fsll.wmes.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "strategy_allocation")
public class StrategyAllocation implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "strategy_id")
	@JsonIgnore
    private StrategyInfo strategy;
    
	@Column(name = "type")
	private String type;
	
	@Column(name = "method_type")
	private String methodType;

	@Column(name = "item_code")
	private String itemCode;
	
	@Column(name = "item_weight")
	private Integer itemWeight;
	
	@Column(name = "layer_level")
	private Integer layerLevel;

	@Transient
	private String name;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public StrategyInfo getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyInfo strategy) {
		this.strategy = strategy;
	}

	public String getMethodType() {
		return methodType;
	}

	public void setMethodType(String methodType) {
		this.methodType = methodType;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(Integer itemWeight) {
		this.itemWeight = itemWeight;
	}

	public Integer getLayerLevel() {
		return layerLevel;
	}

	public void setLayerLevel(Integer layerLevel) {
		this.layerLevel = layerLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	
}