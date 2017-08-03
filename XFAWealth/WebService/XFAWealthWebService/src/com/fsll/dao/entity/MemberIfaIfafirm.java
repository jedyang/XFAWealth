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
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "member_ifa_ifafirm")
public class MemberIfaIfafirm implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	@NotFound(action=NotFoundAction.IGNORE)
	private MemberIfa ifa;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifafirm_id")
	@NotFound(action=NotFoundAction.IGNORE)
	private MemberIfafirm ifafirm;
	
	@Column(name = "check_status")
	private String checkStatus;
	
	@Column(name = "check_opinion")
	private String checkOpinion;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "check_user")
	@JsonIgnore
	private MemberBase checkUser;
	
    @Column(name="check_date")
    private Date checkDate;

    @Column(name="begin_date")
    private Date beginDate;
    
    @Column(name="end_date")
    private Date endDate;
	
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

	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}

	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
	}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getCheckOpinion() {
		return checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public MemberBase getCheckUser() {
		return checkUser;
	}

	public void setCheckUser(MemberBase checkUser) {
		this.checkUser = checkUser;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	
}