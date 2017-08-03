package com.fsll.wmes.entity;

import java.sql.Time;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "crm_schedule")
public class CrmSchedule implements java.io.Serializable {
	private String id;
	private CrmCustomer customer;
	private String title;
	private String content;
	private Date startDate;
	private Time startTime;
	private Date endDate;
	private Time endTime;
	private String color;
	private String remind;
	private String repeatDay;
	private String itemName;
	private String remark;
	private Date repeatFrom;
	private Date repeatTo;
	private String eventType;
	private String remindSetting;
	private CrmScheduleGroup group;
	private MemberBase creator;//creator_id
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
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
	@JoinColumn(name = "customer_id")
	@JsonIgnore
	public CrmCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(CrmCustomer customer) {
		this.customer = customer;
	}

	@Column(name = "item_name")
	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "start_time")
	public Time getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
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
	
	@Column(name = "color")
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Column(name = "end_time")
	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	@Column(name = "title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "remind")
	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}

	@Column(name = "repeat_day")
	public String getRepeatDay() {
		return repeatDay;
	}

	public void setRepeatDay(String repeatDay) {
		this.repeatDay = repeatDay;
	}
	
	@Column(name = "event_type")
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@JsonIgnore
	public CrmScheduleGroup getGroup() {
		return group;
	}
	
	public void setGroup(CrmScheduleGroup group) {
		this.group = group;
	}
	
	@Column(name = "repeat_from")
	public Date getRepeatFrom() {
		return repeatFrom;
	}

	public void setRepeatFrom(Date repeatFrom) {
		this.repeatFrom = repeatFrom;
	}

	@Column(name = "repeat_to")
	public Date getRepeatTo() {
		return repeatTo;
	}

	public void setRepeatTo(Date repeatTo) {
		this.repeatTo = repeatTo;
	}

	@Column(name = "remind_setting")
	public String getRemindSetting() {
		return remindSetting;
	}

	public void setRemindSetting(String remindSetting) {
		this.remindSetting = remindSetting;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	@JsonIgnore
	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
	}
}