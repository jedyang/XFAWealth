package com.fsll.wmes.crm.vo;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductInfo;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "order_history")
public class OrderHistoryVO implements java.io.Serializable {
    
    private String id;

    private MemberBase member;
	
	private MemberIfa ifa;
    
    private InvestorAccount account;
    private String accountNo;
	
    private String productId;
    private String productName;
    	
	private PortfolioInfo portfolio;
    
	private Integer commissionUnit;
    
	private Double commissionPrice;
    
	private Double commissionAmount;
    
	private Double transactionUnit;
    
	private Double transactionAmount;
    
	private Double fee;
    
    private String orderType;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date orderDate;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date closeTime;
    
	private String status;
    
    
	private String xh;    

	private String period;

	private Date beginDate;

	private Date endDate;
    

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
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
	
	public PortfolioInfo getPortfolio() {
		return this.portfolio;
	}
	
	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
	}

	public Integer getCommissionUnit() {
		return this.commissionUnit;
	}

	public void setCommissionUnit(Integer commissionUnit) {
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

	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}