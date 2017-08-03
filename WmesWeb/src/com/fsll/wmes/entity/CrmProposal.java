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
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "crm_proposal")
public class CrmProposal implements java.io.Serializable {
	private String id;
	private MemberBase member;
	private MemberIfa ifaMember;
	private String proposalName;
	private String baseCurrId;
	private Double initialInvestAmount;
	private String liquidityNeed;
	private String timeFrame;
	private String taxConcerns;
	private String lrf;
	private String unp;
	private StrategyInfo strategy;
	private String remark;
	private String status;
	private String curStep;
	private WebEmailLog emailLog;
	private Integer printCount;
	private Date printLastTime;
	private MemberBase creator;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	private String investmentGoal;

	private String createTimeStr;
	
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
	@JoinColumn(name = "member_id")
	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "Ifa_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public MemberIfa getIfaMember() {
		return ifaMember;
	}

	public void setIfaMember(MemberIfa ifa) {
		this.ifaMember = ifa;
	}

	@Column(name = "proposal_name")
	public String getProposalName() {
		return this.proposalName;
	}

	public void setProposalName(String proposalName) {
		this.proposalName = proposalName;
	}

	@Column(name = "base_curr_id")
	public String getBaseCurrId() {
		return this.baseCurrId;
	}

	public void setBaseCurrId(String currencyType) {
		this.baseCurrId = currencyType;
	}

	@Column(name = "initial_invest_amount")
	public Double getInitialInvestAmount() {
		return this.initialInvestAmount;
	}

	public void setInitialInvestAmount(Double initialInvestAmount) {
		this.initialInvestAmount = initialInvestAmount;
	}

	/*@Column(name = "total_invest_amount")
	public Double getTotalInvestAmount() {
		return this.totalInvestAmount;
	}

	public void setTotalInvestAmount(Double totalInvestAmount) {
		this.totalInvestAmount = totalInvestAmount;
	}*/

	@Column(name = "liquidity_need")
	public String getLiquidityNeed() {
		return this.liquidityNeed;
	}

	public void setLiquidityNeed(String liquidityNeed) {
		this.liquidityNeed = liquidityNeed;
	}

	@Column(name = "time_frame")
	public String getTimeFrame() {
		return this.timeFrame;
	}

	public void setTimeFrame(String timeFrame) {
		this.timeFrame = timeFrame;
	}

	@Column(name = "tax_concerns")
	public String getTaxConcerns() {
		return this.taxConcerns;
	}

	public void setTaxConcerns(String taxConcerns) {
		this.taxConcerns = taxConcerns;
	}

	@Column(name = "lrf")
	public String getLrf() {
		return this.lrf;
	}

	public void setLrf(String lrf) {
		this.lrf = lrf;
	}

	@Column(name = "unp")
	public String getUnp() {
		return this.unp;
	}

	public void setUnp(String unp) {
		this.unp = unp;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "strategy_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public StrategyInfo getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyInfo strategy) {
		this.strategy = strategy;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "cur_step")
	public String getCurStep() {
		return this.curStep;
	}

	public void setCurStep(String curStep) {
		this.curStep = curStep;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "email_log_id")
	@JsonIgnore
	public WebEmailLog getEmailLog() {
		return emailLog;
	}

	public void setEmailLog(WebEmailLog emailLog) {
		this.emailLog = emailLog;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	@Column(name = "create_time")
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "last_update")
	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Column(name = "print_count")
	public Integer getPrintCount() {
		return printCount;
	}

	public void setPrintCount(Integer printCount) {
		this.printCount = printCount;
	}
	
	@Column(name = "print_last_time")
	public Date getPrintLastTime() {
		return printLastTime;
	}

	public void setPrintLastTime(Date printLastTime) {
		this.printLastTime = printLastTime;
	}

	@Column(name = "investment_goal")
	public String getInvestmentGoal() {
		return investmentGoal;
	}
	
	public void setInvestmentGoal(String investmentGoal) {
		this.investmentGoal = investmentGoal;
	}

	

}