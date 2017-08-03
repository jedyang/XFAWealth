package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "market_index")
public class MarketIndex implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @Column(name = "code")
	private String code;
    
    @Column(name = "name")
	private String name;
	
	@Column(name = "market_date")
	private Date marketDate;
	
	@Column(name = "open_dot")
	private Double openDot;
	
	@Column(name = "max_dot")
	private Double maxDot;
	
	@Column(name = "min_dot")
	private Double minDot;
	
	@Column(name = "cur_dot")
	private Double curDot;
	
	@Column(name = "low_dot")
	private Double lowDot;
	
	@Column(name = "hight_dot")
	private Double hightDot;
	
	@Column(name = "close_dot")
	private Double closeDot;
	
	@Column(name = "aver_dot")
	private Double averDot;
	
	@Column(name = "sum_volume")
	private Integer sumVolume;
	
	@Column(name = "sum_amount")
	private Double sumAmount;
	
	@Column(name = "turnover")
	private Double turnover;
	
	@Column(name = "volume_ratio")
	private Double volumeRatio;
	
	@Column(name = "order_by")
	private Integer orderBy;
	
    @Column(name = "create_time")
	private Date createTime;
    
    @Column(name = "last_update")
	private Date lastUpdate;
    
    @Column(name = "increase")
	private String increase;
    
    @Column(name = "ups_downs")
	private String upsDowns;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getMarketDate() {
		return marketDate;
	}

	public void setMarketDate(Date marketDate) {
		this.marketDate = marketDate;
	}

	public Double getOpenDot() {
		return openDot;
	}

	public void setOpenDot(Double openDot) {
		this.openDot = openDot;
	}

	public Double getMaxDot() {
		return maxDot;
	}

	public void setMaxDot(Double maxDot) {
		this.maxDot = maxDot;
	}

	public Double getMinDot() {
		return minDot;
	}

	public void setMinDot(Double minDot) {
		this.minDot = minDot;
	}

	public Double getCurDot() {
		return curDot;
	}

	public void setCurDot(Double curDot) {
		this.curDot = curDot;
	}

	public Double getLowDot() {
		return lowDot;
	}

	public void setLowDot(Double lowDot) {
		this.lowDot = lowDot;
	}

	public Double getHightDot() {
		return hightDot;
	}

	public void setHightDot(Double hightDot) {
		this.hightDot = hightDot;
	}

	public Double getCloseDot() {
		return closeDot;
	}

	public void setCloseDot(Double closeDot) {
		this.closeDot = closeDot;
	}

	public Double getAverDot() {
		return averDot;
	}

	public void setAverDot(Double averDot) {
		this.averDot = averDot;
	}

	public Integer getSumVolume() {
		return sumVolume;
	}

	public void setSumVolume(Integer sumVolume) {
		this.sumVolume = sumVolume;
	}

	public Double getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(Double sumAmount) {
		this.sumAmount = sumAmount;
	}

	public Double getTurnover() {
		return turnover;
	}

	public void setTurnover(Double turnover) {
		this.turnover = turnover;
	}

	public Double getVolumeRatio() {
		return volumeRatio;
	}

	public void setVolumeRatio(Double volumeRatio) {
		this.volumeRatio = volumeRatio;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
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

	public String getIncrease() {
		return increase;
	}

	public void setIncrease(String increase) {
		this.increase = increase;
	}

	public String getUpsDowns() {
		return upsDowns;
	}

	public void setUpsDowns(String upsDowns) {
		this.upsDowns = upsDowns;
	}

}