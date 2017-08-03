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

@Entity
@Table(name = "bond_ask")
public class BondAsk implements java.io.Serializable {
	private String id;
	private BondInfo bond;
	private String orderNo;
	private MemberBase member;
	private String orderType;
	private Double commissionAmount;
	private InvestorAccount account;
	private String accountNo;
	private Date orderDate;
	private String orderStatus;
	private String orderRemark;
	private Date answerDate;
	private String answerId;
	private Double answerPrice;
	private String baseCurrency;
	private String isValid;

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
	@JoinColumn(name = "bond_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public BondInfo getBond() {
		return this.bond;
	}

	public void setBond(BondInfo bond) {
		this.bond = bond;
	}

	@Column(name = "order_no")
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	@Column(name = "order_type")
	public String getOrderType() {
		return this.orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Column(name = "commission_amount")
	public Double getCommissionAmount() {
		return this.commissionAmount;
	}

	public void setCommissionAmount(Double commissionAmount) {
		this.commissionAmount = commissionAmount;
	}

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    @NotFound(action = NotFoundAction.IGNORE)
	public InvestorAccount getAccount() {
		return account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	@Column(name = "account_no")
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "order_date")
	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@Column(name = "order_status")
	public String getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "order_remark")
	public String getOrderRemark() {
		return this.orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	@Column(name = "answer_date")
	public Date getAnswerDate() {
		return this.answerDate;
	}

	public void setAnswerDate(Date answerDate) {
		this.answerDate = answerDate;
	}

	@Column(name = "answer_id")
	public String getAnswerId() {
		return this.answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	@Column(name = "answer_price")
	public Double getAnswerPrice() {
		return this.answerPrice;
	}

	public void setAnswerPrice(Double answerPrice) {
		this.answerPrice = answerPrice;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Column(name = "base_currency")
	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

}