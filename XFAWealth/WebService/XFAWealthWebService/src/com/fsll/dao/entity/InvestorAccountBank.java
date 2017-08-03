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

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_account_bank")
public class InvestorAccountBank implements java.io.Serializable {
	private String id;
	private InvestorAccount account;
	private String nameOfBank;
	private String nameOfHolder;
	private String bankCode;
	private String branchCode;
	private String branchAccountNo;
	private String swiftCode;
	private String currencyType;
	private String nameOfInter;
	private String swiftOfInter;
	private String holderNameOfInter;
	private Date createTime;
	private Date lastUpdate;

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
	@JoinColumn(name = "account_id")
	@JsonIgnore
	public InvestorAccount getAccount() {
		return this.account;
	}

	
	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	@Column(name = "name_of_bank")
	public String getNameOfBank() {
		return this.nameOfBank;
	}

	public void setNameOfBank(String nameOfBank) {
		this.nameOfBank = nameOfBank;
	}

	@Column(name = "name_of_holder")
	public String getNameOfHolder() {
		return this.nameOfHolder;
	}

	public void setNameOfHolder(String nameOfHolder) {
		this.nameOfHolder = nameOfHolder;
	}

	@Column(name = "bank_code")
	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "branch_code")
	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	@Column(name = "branch_account_no")
	public String getBranchAccountNo() {
		return this.branchAccountNo;
	}

	public void setBranchAccountNo(String branchAccountNo) {
		this.branchAccountNo = branchAccountNo;
	}

	@Column(name = "swift_code")
	public String getSwiftCode() {
		return this.swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	@Column(name = "currency_type")
	public String getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	@Column(name = "name_of_inter")
	public String getNameOfInter() {
		return this.nameOfInter;
	}

	public void setNameOfInter(String nameOfInter) {
		this.nameOfInter = nameOfInter;
	}

	@Column(name = "swift_of_inter")
	public String getSwiftOfInter() {
		return this.swiftOfInter;
	}

	public void setSwiftOfInter(String swiftOfInter) {
		this.swiftOfInter = swiftOfInter;
	}

	@Column(name = "holder_name_of_inter")
	public String getHolderNameOfInter() {
		return this.holderNameOfInter;
	}

	public void setHolderNameOfInter(String holderNameOfInter) {
		this.holderNameOfInter = holderNameOfInter;
	}

	@Column(name = "create_time")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}