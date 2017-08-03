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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "portfolio_hold_account")
public class PortfolioHoldAccount implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hold_id")
	@JsonIgnore
    private PortfolioHold portfolioHold;
	
	@Column(name = "account_type")
	private String accountType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private MemberBase member;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	@JsonIgnore
	private MemberIfa ifa;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	private InvestorAccount account;
	
	@Column(name = "account_no")
	private String accountNo;
	
	@Column(name = "sub_account_no")
	private String subAccountNo;
	
	@Column(name = "base_currency")
	private String baseCurrency;
	
	@Column(name = "market_value")
	private Double marketValue;
	
	@Column(name = "cash_available")
	private Double cashAvailable;
	
	@Column(name = "cash_hold")
	private Double cashHold;
	
	@Column(name = "cash_withdrawal")
	private Double cashWithdrawal;
	
	@Column(name = "total_cash")
	private Double totalCash;
	
	@Column(name = "buy_hold")
	private Double buyHold;
	
	@Column(name = "sell_hold")
	private Double sellHold;
	
	@Column(name = "total_asset")
	private Double totalAsset;
	
	@Column(name = "day_pl")
	private Double dayPl;
	
	@Column(name = "day_return")
	private Double dayReturn;
	
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "is_valid")
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PortfolioHold getPortfolioHold() {
		return portfolioHold;
	}

	public void setPortfolioHold(PortfolioHold portfolioHold) {
		this.portfolioHold = portfolioHold;
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

	public String getSubAccountNo() {
		return subAccountNo;
	}

	public void setSubAccountNo(String subAccountNo) {
		this.subAccountNo = subAccountNo;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Double getMarketValue() {
		return marketValue;
	}

	public void setMarketValue(Double marketValue) {
		this.marketValue = marketValue;
	}

	public Double getCashAvailable() {
		return cashAvailable;
	}

	public void setCashAvailable(Double cashAvailable) {
		this.cashAvailable = cashAvailable;
	}

	public Double getCashHold() {
		return cashHold;
	}

	public void setCashHold(Double cashHold) {
		this.cashHold = cashHold;
	}

	public Double getCashWithdrawal() {
		return cashWithdrawal;
	}

	public void setCashWithdrawal(Double cashWithdrawal) {
		this.cashWithdrawal = cashWithdrawal;
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

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	public Double getTotalCash() {
		return totalCash;
	}

	public void setTotalCash(Double totalCash) {
		this.totalCash = totalCash;
	}

	public Double getBuyHold() {
		return buyHold;
	}

	public void setBuyHold(Double buyHold) {
		this.buyHold = buyHold;
	}

	public Double getSellHold() {
		return sellHold;
	}

	public void setSellHold(Double sellHold) {
		this.sellHold = sellHold;
	}

	public Double getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(Double totalAsset) {
		this.totalAsset = totalAsset;
	}

	public Double getDayPl() {
		return dayPl;
	}

	public void setDayPl(Double dayPl) {
		this.dayPl = dayPl;
	}

	public Double getDayReturn() {
		return dayReturn;
	}

	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}
}