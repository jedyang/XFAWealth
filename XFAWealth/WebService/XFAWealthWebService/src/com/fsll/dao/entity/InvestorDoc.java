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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_doc")
public class InvestorDoc implements java.io.Serializable {
	private String id;
	private MemberBase member;
	private MemberDistributor distributor;
	private MemberIfafirm ifafirm;
	private InvestorAccount account;
	private InvestorAccountContact contact;
	private DocTemplate docTemplate;
	private String docListId;
	private String isNecessary;
	private Integer updateCycle;
	private String status;
	private Date submitDate;
	private Date expireDate;
	private String checkStatus;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	
	private String docName;
	

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
	@JsonIgnore
	public MemberBase getMember() {
		return this.member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "distributor_id")
	@JsonIgnore
	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "doc_template_id")
	@JsonIgnore
	public DocTemplate getDocTemplate() {
		return docTemplate;
	}

	public void setDocTemplate(DocTemplate docTemplate) {
		this.docTemplate = docTemplate;
	}

	@Column(name = "is_necessary")
	public String getIsNecessary() {
		return this.isNecessary;
	}

	public void setIsNecessary(String isNecessary) {
		this.isNecessary = isNecessary;
	}

	@Column(name = "update_cycle")
	public Integer getUpdateCycle() {
		return this.updateCycle;
	}

	public void setUpdateCycle(Integer updateCycle) {
		this.updateCycle = updateCycle;
	}

	@Column(name = "status", length = 10)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "submit_date")
	public Date getSubmitDate() {
		return this.submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	@Column(name = "expire_date")
	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	@Column(name = "check_status")
	public String getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "contact_id")
	@JsonIgnore
	public InvestorAccountContact getContact() {
		return contact;
	}

	public void setContact(InvestorAccountContact contact) {
		this.contact = contact;
	}
	
	@Transient
	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifafirm_id")
	@JsonIgnore
	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}

	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id")
	@JsonIgnore
	public InvestorAccount getAccount() {
		return account;
	}

	public void setAccount(InvestorAccount account) {
		this.account = account;
	}

	@Column(name = "doc_list_id")
	public String getDocListId() {
		return docListId;
	}

	public void setDocListId(String docListId) {
		this.docListId = docListId;
	}

}