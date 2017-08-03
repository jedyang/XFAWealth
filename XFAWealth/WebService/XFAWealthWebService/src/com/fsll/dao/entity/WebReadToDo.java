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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "web_read_to_do")
public class WebReadToDo implements java.io.Serializable {
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
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "from_member_id")
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private MemberBase fromMember;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "owner_id")
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	private MemberBase owner;
	
    @Column(name = "msg_url")
	private String msgUrl;
    
    @Column(name = "msg_param")
	private String msgParam;
    
    @Column(name = "app_url")
	private String appUrl;
    
    @Column(name = "app_param")
	private String appParam;
    
    @Column(name = "is_app")
	private String isApp;
	
	@Column(name = "is_read")
	private String isRead;
	
	@Column(name = "read_time")
	private Date readTime;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "is_valid")
	private String isValid;
	
	@Transient
	private String title;
	
	@Transient
	private String createTimeFmt;

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

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsgUrl() {
		return msgUrl;
	}

	public void setMsgUrl(String msgUrl) {
		this.msgUrl = msgUrl;
	}

	public String getMsgParam() {
		return msgParam;
	}

	public void setMsgParam(String msgParam) {
		this.msgParam = msgParam;
	}

	public String getIsApp() {
		return isApp;
	}

	public void setIsApp(String isApp) {
		this.isApp = isApp;
	}
	
	@Transient
	public String getCreateTimeFmt() {
		return createTimeFmt;
	}

	public void setCreateTimeFmt(String createTimeFmt) {
		this.createTimeFmt = createTimeFmt;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAppParam() {
		return appParam;
	}

	public void setAppParam(String appParam) {
		this.appParam = appParam;
	}
	
}
