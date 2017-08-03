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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "order_history")
public class OrderHistory implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	@NotFound(action=NotFoundAction.IGNORE)
    private MemberBase member;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	@NotFound(action = NotFoundAction.IGNORE)
	private MemberIfa ifa;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private InvestorAccount account;
	
    @Column(name = "account_no")
    private String accountNo;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "product_id")
	@NotFound(action=NotFoundAction.IGNORE)
    private ProductInfo product;
    	
	 @Column(name = "currency_code")
	private String currencyCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hold_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	private PortfolioHold portfolioHold;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "plan_id")
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	private OrderPlan plan;
	
    @Column(name = "if_aip")
    private String ifAip;
	
    @Column(name = "commission_unit")
	private Double commissionUnit;
    
    @Column(name = "commission_price")
	private Double commissionPrice;
    
    @Column(name = "commission_amount")
	private Double commissionAmount;
    
    @Column(name = "transaction_unit")
	private Double transactionUnit;
    
    @Column(name = "transaction_amount")
	private Double transactionAmount;
    
    @Column(name = "transaction_price")
    private Double transactionPrice;
    
    @Column(name = "fee")
	private Double fee;
    
	@Column(name = "tran_fee_cur")
	private String tranFeeCur;
	
	@Column(name = "tran_rate")
	private Double tranRate;
	
	@Column(name = "tran_fee_mini")
	private Double tranFeeMini;
    
    @Column(name = "order_type")
    private String orderType;
    
    @Column(name = "order_date")
	private Date orderDate;

    @Column(name = "close_time")
	private Date closeTime;
    
    @Column(name = "status")
	private String status;
    
    @Column(name = "error_msg")
	private String errorMsg;
    
    @Column(name = "update_time")
	private Date updateTime;
    
    @Column(name = "its_order_number")
    private String itsOrderNumber;
    
	@Column(name = "switch_flag")
	private String switchFlag;

	@Column(name = "switch_group")
	private String switchGroup;
	
	@Column(name = "switch_alloc_rate")
	private Double switchAllocRate;
	
    @Transient
	private String xh;    

    @Transient
	private String period;

    @Transient
	private Date beginDate;

    @Transient
	private Date endDate;
    
    @Transient
	private String productName;
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProductInfo getProduct() {
		return product;
	}

	public void setProduct(ProductInfo productInfo) {
		this.product = productInfo;
	}
	
	public MemberBase getMember() {
		return this.member;
	}
	
	public void setMember(MemberBase member) {
		this.member = member;
	}
	
	public MemberIfa getIfa() {
		return this.ifa;
	}
	
	
	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}
	
	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}

	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
	}



	public Double getCommissionUnit() {
		return commissionUnit;
	}

	public void setCommissionUnit(Double commissionUnit) {
		this.commissionUnit = commissionUnit;
	}

	public Double getCommissionPrice() {
		return this.commissionPrice;
	}

	public void setCommissionPrice(Double price) {
		this.commissionPrice = price;
	}

	public Double getCommissionAmount() {
		return this.commissionAmount;
	}

	public void setCommissionAmount(Double amount) {
		this.commissionAmount = amount;
	}

	public Double getTransactionUnit() {
		return this.transactionUnit;
	}

	public void setTransactionUnit(Double transactionUnit) {
		this.transactionUnit = transactionUnit;
	}

	public Double getTransactionAmount() {
		return this.transactionAmount;
	}

	public void setTransactionAmount(Double amount) {
		this.transactionAmount = amount;
	}

	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public InvestorAccount getAccount() {
		return this.account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Date getCloseTime() {
		return this.closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getPeriod() {
		return this.period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}
	
	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getItsOrderNumber() {
		return itsOrderNumber;
	}

	public void setItsOrderNumber(String itsOrderNumber) {
		this.itsOrderNumber = itsOrderNumber;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public OrderPlan getPlan() {
		return plan;
	}

	public void setPlan(OrderPlan plan) {
		this.plan = plan;
	}

	public String getIfAip() {
		return ifAip;
	}

	public void setIfAip(String ifAip) {
		this.ifAip = ifAip;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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

	public String getSwitchFlag() {
		return switchFlag;
	}

	public void setSwitchFlag(String switchFlag) {
		this.switchFlag = switchFlag;
	}

	public String getSwitchGroup() {
		return switchGroup;
	}

	public void setSwitchGroup(String switchGroup) {
		this.switchGroup = switchGroup;
	}

	public Double getSwitchAllocRate() {
		return switchAllocRate;
	}

	public void setSwitchAllocRate(Double switchAllocRate) {
		this.switchAllocRate = switchAllocRate;
	}

	public Double getTransactionPrice() {
		return transactionPrice;
	}

	public void setTransactionPrice(Double transactionPrice) {
		this.transactionPrice = transactionPrice;
	}
	
	
	
}