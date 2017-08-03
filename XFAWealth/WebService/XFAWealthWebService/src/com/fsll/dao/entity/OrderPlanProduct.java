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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "order_plan_product")
public class OrderPlanProduct {
	@Id
	@Column(name = "id")
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "plan_id")
	@JsonIgnore
	private OrderPlan plan;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@JsonIgnore
	//@NotFound(action=NotFoundAction.IGNORE)
	private ProductInfo product;
	
	@Column(name = "unit")
	private Double unit;
	
	@Column(name = "unit_adjust")
	private Double unitAdjust;
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "amount_adjust")
	private Double amountAdjust;
	
	@Column(name = "weight")
	private Double weight;
	
	@Column(name = "weight_adjust")
	private Double weightAdjust;
	
	@Column(name = "tran_type")
	private String tranType;
	
	@Column(name = "dividend")
	private String dividend;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private InvestorAccount account;
	
	@Column(name = "account_no")
	private String accountNo;
	
	@Column(name = "original")
	private Integer original;
	
	@Column(name = "tran_fee")
	private Double tranFee;
	
	@Column(name = "tran_fee_cur")
	private String tranFeeCur;
	
	@Column(name = "tran_rate")
	private Double tranRate;
	
	@Column(name = "tran_fee_mini")
	private Double tranFeeMini;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from_product_id")
	@JsonIgnore
	private ProductInfo fromProduct;
	
	@Column(name = "switch_flag")
	private String switchFlag;

	@Column(name = "switch_group")
	private String switchGroup;
	
	@Column(name = "switch_alloc_rate")
	private Double switchAllocRate;
	
	@Column(name = "status")
	private String status;
	
	
	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public OrderPlan getPlan() {
		return plan;
	}


	public void setPlan(OrderPlan plan) {
		this.plan = plan;
	}


	public ProductInfo getProduct() {
		return product;
	}


	public void setProduct(ProductInfo product) {
		this.product = product;
	}


	public Double getUnit() {
		return unit;
	}


	public void setUnit(Double unit) {
		this.unit = unit;
	}


	public Double getUnitAdjust() {
		return unitAdjust;
	}


	public void setUnitAdjust(Double unitAdjust) {
		this.unitAdjust = unitAdjust;
	}


	public Double getAmount() {
		return amount;
	}


	public void setAmount(Double amount) {
		this.amount = amount;
	}


	public Double getAmountAdjust() {
		return amountAdjust;
	}


	public void setAmountAdjust(Double amountAdjust) {
		this.amountAdjust = amountAdjust;
	}


	public Double getWeight() {
		return weight;
	}


	public void setWeight(Double weight) {
		this.weight = weight;
	}


	public Double getWeightAdjust() {
		return weightAdjust;
	}


	public void setWeightAdjust(Double weightAdjust) {
		this.weightAdjust = weightAdjust;
	}


	public String getTranType() {
		return tranType;
	}


	public void setTranType(String tranType) {
		this.tranType = tranType;
	}


	public String getDividend() {
		return dividend;
	}


	public void setDividend(String dividend) {
		this.dividend = dividend;
	}


	public InvestorAccount getAccount() {
		return account;
	}


	public void setAccount(InvestorAccount account) {
		this.account = account;
	}


	public String getAccountNo() {
		return accountNo;
	}


	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	public Integer getOriginal() {
		return original;
	}


	public void setOriginal(Integer original) {
		this.original = original;
	}


	public Double getTranFee() {
		return tranFee;
	}


	public void setTranFee(Double tranFee) {
		this.tranFee = tranFee;
	}


	public ProductInfo getFromProduct() {
		return fromProduct;
	}


	public void setFromProduct(ProductInfo fromProduct) {
		this.fromProduct = fromProduct;
	}


	public String getSwitchFlag() {
		return switchFlag;
	}


	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}


	public String getTranFeeCur() {
		return tranFeeCur;
	}


	public void setTranFeeCur(String tranFeeCur) {
		this.tranFeeCur = tranFeeCur;
	}


	public Double getTranRate() {
		return tranRate;
	}


	public void setTranRate(Double tranRate) {
		this.tranRate = tranRate;
	}


	public Double getTranFeeMini() {
		return tranFeeMini;
	}


	public void setTranFeeMini(Double tranFeeMini) {
		this.tranFeeMini = tranFeeMini;
	}


	public String getSwitchGroup() {
		return switchGroup;
	}


	public void setSwitchGroup(String switchGroup) {
		this.switchGroup = switchGroup;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Double getSwitchAllocRate() {
		return switchAllocRate;
	}


	public void setSwitchAllocRate(Double switchAllocRate) {
		this.switchAllocRate = switchAllocRate;
	}
	
	
}
