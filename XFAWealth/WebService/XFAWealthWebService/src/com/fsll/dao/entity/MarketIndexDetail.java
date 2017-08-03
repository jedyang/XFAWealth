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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "market_index_detail")
public class MarketIndexDetail implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "index_id")
	@JsonIgnore
	private MarketIndex index;
    
    @Column(name = "market_time")
	private Date marketTime;
	
	@Column(name = "cur_dot")
	private Double curDot;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MarketIndex getIndex() {
		return index;
	}

	public void setIndex(MarketIndex index) {
		this.index = index;
	}

	public Date getMarketTime() {
		return marketTime;
	}

	public void setMarketTime(Date marketTime) {
		this.marketTime = marketTime;
	}

	public Double getCurDot() {
		return curDot;
	}

	public void setCurDot(Double curDot) {
		this.curDot = curDot;
	}
	
}