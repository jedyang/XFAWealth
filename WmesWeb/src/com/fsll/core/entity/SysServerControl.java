package com.fsll.core.entity;

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

@Entity 
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="sys_server_control") 
public class SysServerControl implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id", nullable = true)
	private SysServerInfo info;
	
    @Column(name="alloc_tag")
	private String allocTag;
    
    @Column(name="last_time")
    private Date lastTime;//最后访问时间
    
    @Column(name="man_control")
	private String manControl;//人工设置起停
    
    @Column(name="exec_cycle")
	private String execCycle;
    
    @Column(name="begin_date")
    private Date beginDate;
    
    @Column(name="end_date")
    private Date endDate;
    
    @Column(name="exec_time")
    private String execTime;
    
    @Column(name="next_time")
    private Date nextTime;//下次运行时间
    
    @Column(name="end_date_flag")
	private String endDateFlag;//是否有结束日期
    
    @Column(name="time_distance")
	private Long timeDistance;
    
    @Column(name="week_day")
	private String weekDay;//周几执行
    
    @Column(name="month_day")
	private String monthDay;//每月第几天
    
    @Column(name="which_month")
	private String whichMonth;//执行月份

    @Column(name="log_flag")
	private String logFlag;
    
    @Column(name="is_valid")
	private String isValid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public SysServerInfo getInfo() {
		return info;
	}

	public void setInfo(SysServerInfo info) {
		this.info = info;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getManControl() {
		return manControl;
	}

	public void setManControl(String manControl) {
		this.manControl = manControl;
	}

	public String getExecCycle() {
		return execCycle;
	}

	public void setExecCycle(String execCycle) {
		this.execCycle = execCycle;
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

	public String getExecTime() {
		return execTime;
	}

	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}

	public Date getNextTime() {
		return nextTime;
	}

	public void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}

	public String getEndDateFlag() {
		return endDateFlag;
	}

	public void setEndDateFlag(String endDateFlag) {
		this.endDateFlag = endDateFlag;
	}

	public Long getTimeDistance() {
		return timeDistance;
	}

	public void setTimeDistance(Long timeDistance) {
		this.timeDistance = timeDistance;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getMonthDay() {
		return monthDay;
	}

	public void setMonthDay(String monthDay) {
		this.monthDay = monthDay;
	}

	public String getWhichMonth() {
		return whichMonth;
	}

	public void setWhichMonth(String whichMonth) {
		this.whichMonth = whichMonth;
	}

	public String getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(String logFlag) {
		this.logFlag = logFlag;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getAllocTag() {
		return allocTag;
	}

	public void setAllocTag(String allocTag) {
		this.allocTag = allocTag;
	}
}