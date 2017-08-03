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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "portfolio_info")
public class PortfolioInfo implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private MemberBase member;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	private MemberIfa memberIfa;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "proposal_id")
	@JsonIgnore
	private CrmProposal proposal;
	
	@Column(name = "portfolio_name")
	private String portfolioName;
	
	@Column(name = "base_currency")
	private String baseCurrency;
	
	@Column(name = "risk_level")
	private Integer riskLevel;
	
	@Column(name = "aip_flag")
	private String aipFlag;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	private MemberBase creator;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;

	@Column(name = "if_selected")
	private String ifSelected;
	
	@Column(name = "investment_goal")
	private String investmentGoal;
	
	@Column(name = "suitability")
	private String suitability;
	
	@Column(name = "is_valid")
	private String isValid;
	
	@Transient
	private String objective;
	
	@Transient
	private Integer click;
	
	@Transient
	private Integer productCount;

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberBase getMember() {
		return member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	
	public MemberIfa getMemberIfa() {
		return memberIfa;
	}

	public void setMemberIfa(MemberIfa memberIfa) {
		this.memberIfa = memberIfa;
	}

	public void setProposal(CrmProposal proposal) {
		this.proposal = proposal;
	}

	public CrmProposal getProposal() {
		return proposal;
	}

	public void setProposalId(CrmProposal proposal) {
		this.proposal = proposal;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public String getAipFlag() {
		return aipFlag;
	}

	public void setAipFlag(String aipFlag) {
		this.aipFlag = aipFlag;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Integer getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(Integer riskLevel) {
		this.riskLevel = riskLevel;
	}

	
	public String getIfSelected() {
		return ifSelected;
	}

	public void setIfSelected(String ifSelected) {
		this.ifSelected = ifSelected;
	}

	public String getObjective() {
		return objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public String getInvestmentGoal() {
		return investmentGoal;
	}

	public void setInvestmentGoal(String investmentGoal) {
		this.investmentGoal = investmentGoal;
	}

	public String getSuitability() {
		return suitability;
	}

	public void setSuitability(String suitability) {
		this.suitability = suitability;
	}
	
	
}