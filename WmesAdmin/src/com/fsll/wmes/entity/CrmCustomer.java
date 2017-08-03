package com.fsll.wmes.entity;

import java.sql.Timestamp;
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
@Table(name = "crm_customer")
public class CrmCustomer implements java.io.Serializable {
	private String id;
	private MemberIfa ifa;
	private MemberBase member;
	private String nickName;
	private String iconUrl;
	private String remark;
	private String isImportant;
	private String clientType;
	private Long activityCount;
	private Date createTime;
	private Date lastUpdate;
	private String salesStageId;
	private String salesRemark;
	private String isValid;
	private String character;
	private String hobby;
	private String special;
	private String dislike;

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
	@JoinColumn(name = "ifa_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public MemberBase getMember() {
		return this.member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}

	@Column(name = "nick_name")
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Column(name = "icon_url")
	public String getIconUrl() {
		return this.iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "is_important")
	public String getIsImportant() {
		return this.isImportant;
	}

	public void setIsImportant(String isImportant) {
		this.isImportant = isImportant;
	}


	@Column(name = "client_type")
	public String getClientType() {
		return this.clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	@Column(name = "activity_count")
	public Long getActivityCount() {
		return this.activityCount;
	}

	public void setActivityCount(Long activityCount) {
		this.activityCount = activityCount;
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

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Column(name = "sales_stage_id")
	public String getSalesStageId() {
		return this.salesStageId;
	}

	public void setSalesStageId(String salesStageId) {
		this.salesStageId = salesStageId;
	}

	@Column(name = "sales_remark")
	public String getSalesRemark() {
		return this.salesRemark;
	}

	public void setSalesRemark(String salesRemark) {
		this.salesRemark = salesRemark;
	}

	@Column(name = "is_valid")
	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	@Column(name = "characters")
	public String getCharacter() {
		return character;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	@Column(name = "hobby")
	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	@Column(name = "special")
	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	@Column(name = "dislike")
	public String getDislike() {
		return dislike;
	}

	public void setDislike(String dislike) {
		this.dislike = dislike;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

}