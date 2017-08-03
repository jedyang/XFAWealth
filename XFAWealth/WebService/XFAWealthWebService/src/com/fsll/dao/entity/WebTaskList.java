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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "web_task_list")
public class WebTaskList implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;

    @Column(name = "type")
	private String type;
    
    @Column(name = "module_type")
	private String moduleType;
    
    @Column(name = "relate_id")
	private String relateId;
    
    @Column(name = "recurent_flag")
	private String recurentFlag;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from_member_id")
	@JsonIgnore
	private MemberBase fromMember;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	@JsonIgnore
	private MemberBase owner;
	
    @Column(name = "task_url")
	private String taskUrl;
    
    @Column(name = "task_param")
	private String taskParam;
    
    @Column(name = "is_app")
	private String isApp;
	
	@Column(name = "handle_status")
	private String handleStatus;
	
	@Column(name = "handle_time")
	private Date handleTime;
	
	@Column(name = "target_date")
	private Date targetDate;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "is_valid")
	private String isValid;
	
	@Column(name = "if_important")
	private String ifImportant;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "schedule_group_id")
	@JsonIgnore
	private CrmScheduleGroup group;
	
	@Transient
	private String title;
	@Transient
	private String titleSc;
	@Transient
	private String titleTc;
	@Transient
	private String titleEn;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public MemberBase getFromMember() {
		return fromMember;
	}

	public void setFromMember(MemberBase fromMember) {
		this.fromMember = fromMember;
	}

	public MemberBase getOwner() {
		return owner;
	}

	public void setOwner(MemberBase owner) {
		this.owner = owner;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRecurentFlag() {
		return recurentFlag;
	}

	public void setRecurentFlag(String recurentFlag) {
		this.recurentFlag = recurentFlag;
	}

	public String getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(String handleStatus) {
		this.handleStatus = handleStatus;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public Date getTargetDate() {
		return targetDate;
	}

	public void setTargetDate(Date targetDate) {
		this.targetDate = targetDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTaskUrl() {
		return taskUrl;
	}

	public void setTaskUrl(String taskUrl) {
		this.taskUrl = taskUrl;
	}

	public String getTaskParam() {
		return taskParam;
	}

	public void setTaskParam(String taskParam) {
		this.taskParam = taskParam;
	}

	public String getIsApp() {
		return isApp;
	}

	public void setIsApp(String isApp) {
		this.isApp = isApp;
	}
	
	public String getIfImportant() {
		return ifImportant;
	}

	public void setIfImportant(String ifImportant) {
		this.ifImportant = ifImportant;
	}

	public CrmScheduleGroup getGroup() {
		return group;
	}

	public void setGroup(CrmScheduleGroup group) {
		this.group = group;
	}

	public String getTitleSc() {
		return titleSc;
	}

	public void setTitleSc(String titleSc) {
		this.titleSc = titleSc;
	}

	public String getTitleTc() {
		return titleTc;
	}

	public void setTitleTc(String titleTc) {
		this.titleTc = titleTc;
	}

	public String getTitleEn() {
		return titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}


	
}