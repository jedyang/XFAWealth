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
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "rpq_quest")
public class RpqQuest implements java.io.Serializable {
	private String id;
	private String type;
	private String questType;
	private String creatorId;
	private MemberBase member;
	private MemberDistributor distributor;
	private MemberIfafirm ifafirm;
	@Transient
	private String title;
	@Transient
	private String remark;
	private String isPublic;
	private String status;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	private String adaptType;

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

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "creator_id")
	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	//@JsonIgnore
	public MemberBase getMember() {
		return member;
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
	
	@Transient
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Transient
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "is_public")
	public String getIsPublic() {
		return this.isPublic;
	}

	public void setIsPublic(String isPublic) {
		this.isPublic = isPublic;
	}

	@Column(name = "status")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Column(name = "quest_type")
	public String getQuestType() {
		return questType;
	}

	public void setQuestType(String questType) {
		this.questType = questType;
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
	
	@Transient
	public String getAdaptType() {
		return adaptType;
	}

	public void setAdaptType(String adaptType) {
		this.adaptType = adaptType;
	}

	
}