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
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_account_declaration")
public class InvestorAccountDeclaration implements java.io.Serializable {
	private String id;
	private InvestorAccount account;
	private String declarationFlag;
	private String informationFlag;
	private String aimFlag;
	private String advisorFlag;
	private String qualifiedFlag;
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

	@Column(name = "declaration_flag")
	public String getDeclarationFlag() {
		return this.declarationFlag;
	}

	public void setDeclarationFlag(String declarationFlag) {
		this.declarationFlag = declarationFlag;
	}

	@Column(name = "information_flag")
	public String getInformationFlag() {
		return this.informationFlag;
	}

	public void setInformationFlag(String informationFlag) {
		this.informationFlag = informationFlag;
	}

	@Column(name = "advisor_flag")
	public String getAdvisorFlag() {
		return this.advisorFlag;
	}

	public void setAdvisorFlag(String advisorFlag) {
		this.advisorFlag = advisorFlag;
	}

	@Column(name = "qualified_flag")
	public String getQualifiedFlag() {
		return this.qualifiedFlag;
	}

	public void setQualifiedFlag(String qualifiedFlag) {
		this.qualifiedFlag = qualifiedFlag;
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
	
	@Column(name = "aim_flag")
	public String getAimFlag() {
		return aimFlag;
	}

	public void setAimFlag(String aimFlag) {
		this.aimFlag = aimFlag;
	}

}