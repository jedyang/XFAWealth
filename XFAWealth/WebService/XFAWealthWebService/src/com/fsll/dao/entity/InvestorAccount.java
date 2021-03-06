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
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "investor_account")
public class InvestorAccount {
	private String id;
	private MemberBase member;
	private String accountNo;
	private String totalFlag;
	private String fromType;
	private String accType;
	private String subFlag;
	private InvestorAccount masterAccount;
	private String investType;
	private String cies;
	private String faca;
	private String dividend;
	private String baseCurrency;
	private String purpose;
	private String purposeOther;
	private String sentBy;
	private MemberDistributor distributor;
	private MemberIfa ifa;
	private String submitStatus;
	private String openStatus;
	private String curStep;
	private String finishStatus;
	private String flowId;
	private String flowStatus;
	private MemberBase createBy;
	private Date createTime;
	private Date lastUpdate;
	private String authorized;
	private String sourceFrom;
	private String isValid;
	private String clientType;
	private String tradeType;
	private String clientStatus;
	private String upfrontFee;
	private String hurdleRate;
	private String performanceFeeRate;
	private String advisorFee;
	private String discFlag;
	private Integer rpqLevel;
	
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
	@NotFound(action=NotFoundAction.IGNORE)
	public MemberBase getMember() {
		return this.member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	@Column(name = "account_no")
	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	@Column(name = "total_flag")
	public String getTotalFlag() {
		return this.totalFlag;
	}

	public void setTotalFlag(String totalFlag) {
		this.totalFlag = totalFlag;
	}

	@Column(name = "from_type")
	public String getFromType() {
		return this.fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}

	@Column(name = "acc_type")
	public String getAccType() {
		return this.accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	@Column(name = "invest_type")
	public String getInvestType() {
		return this.investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}

	@Column(name = "sub_flag")
	public String getSubFlag() {
		return subFlag;
	}

	public void setSubFlag(String subFlag) {
		this.subFlag = subFlag;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sub_relate_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public InvestorAccount getMasterAccount() {
		return masterAccount;
	}

	public void setMasterAccount(InvestorAccount masterAccount) {
		this.masterAccount = masterAccount;
	}

	@Column(name = "cies")
	public String getCies() {
		return this.cies;
	}

	public void setCies(String cies) {
		this.cies = cies;
	}

	
	@Column(name = "faca")
	public String getFaca() {
		return faca;
	}

	public void setFaca(String faca) {
		this.faca = faca;
	}

	@Column(name = "dividend")
	public String getDividend() {
		return this.dividend;
	}

	public void setDividend(String dividend) {
		this.dividend = dividend;
	}

	@Column(name = "base_currency")
	public String getBaseCurrency() {
		return this.baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	@Column(name = "purpose")
	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	@Column(name = "purpose_other")
	public String getPurposeOther() {
		return this.purposeOther;
	}

	public void setPurposeOther(String purposeOther) {
		this.purposeOther = purposeOther;
	}

	@Column(name = "sent_by")
	public String getSentBy() {
		return this.sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@NotFound(action=NotFoundAction.IGNORE) 
	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	@NotFound(action=NotFoundAction.IGNORE) 
	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}


	@Column(name = "open_status")
	public String getOpenStatus() {
		return this.openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	@Column(name = "cur_step")
	public String getCurStep() {
		return this.curStep;
	}

	public void setCurStep(String curStep) {
		this.curStep = curStep;
	}

	@Column(name = "finish_status")
	public String getFinishStatus() {
		return this.finishStatus;
	}

	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}

	@Column(name = "flow_id")
	public String getFlowId() {
		return this.flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	@Column(name = "flow_status")
	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
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

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Column(name = "submit_status")
	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "create_by")
	@NotFound(action=NotFoundAction.IGNORE)
	public MemberBase getCreateBy() {
		return createBy;
	}

	public void setCreateBy(MemberBase createBy) {
		this.createBy = createBy;
	}

	@Column(name="authorized")
	public String getAuthorized() {
		return authorized;
	}

	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}

	@Transient
	public String getSourceFrom() {
		return sourceFrom;
	}

	public void setSourceFrom(String sourceFrom) {
		this.sourceFrom = sourceFrom;
	}
	
	@Column(name = "client_type")
	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	@Column(name = "trade_type")
	public String getTradeType() {
		return this.tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	@Column(name = "client_status")
	public String getClientStatus() {
		return this.clientStatus;
	}

	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}

	@Column(name = "upfront_fee")
	public String getUpfrontFee() {
		return this.upfrontFee;
	}

	public void setUpfrontFee(String upfrontFee) {
		this.upfrontFee = upfrontFee;
	}

	@Column(name = "hurdle_rate")
	public String getHurdleRate() {
		return this.hurdleRate;
	}

	public void setHurdleRate(String hurdleRate) {
		this.hurdleRate = hurdleRate;
	}

	@Column(name = "performance_fee_rate")
	public String getPerformanceFeeRate() {
		return this.performanceFeeRate;
	}

	public void setPerformanceFeeRate(String performanceFeeRate) {
		this.performanceFeeRate = performanceFeeRate;
	}

	@Column(name = "advisor_fee")
	public String getAdvisorFee() {
		return this.advisorFee;
	}

	public void setAdvisorFee(String advisorFee) {
		this.advisorFee = advisorFee;
	}

	@Column(name = "disc_flag")
	public String getDiscFlag() {
    	return discFlag;
    }
	public void setDiscFlag(String discFlag) {
    	this.discFlag = discFlag;
    }

	@Column(name = "rpq_level")
	public Integer getRpqLevel() {
		return rpqLevel;
	}

	public void setRpqLevel(Integer rpqLevel) {
		this.rpqLevel = rpqLevel;
	}
}
