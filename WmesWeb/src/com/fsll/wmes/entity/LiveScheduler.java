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

import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "live_scheduler")
public class LiveScheduler implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	private LiveInfo live;
    
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "setting_id")
    private LiveSetting setting;
    
    @Column(name = "begin_time")
	private Date beginTime;
    
    @Column(name = "end_time")
	private Date endTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LiveInfo getLive() {
		return live;
	}

	public void setLive(LiveInfo live) {
		this.live = live;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public LiveSetting getSetting() {
		return setting;
	}

	public void setSetting(LiveSetting setting) {
		this.setting = setting;
	}
}
