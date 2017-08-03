package com.fsll.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "strategy_count")
public class StrategyCount implements java.io.Serializable {
	private String id;
	private Integer views;
	private Integer viewsMonth;
	private Integer viewsWeek;
	private Integer viewsDay;
	private Integer comments;
	private Integer commentsMonth;
	private Integer commentsWeek;
	private Integer commentsDay;
	private Integer ups;
	private Integer upsMonth;
	private Integer upsWeek;
	private Integer upsDay;
	private Integer downs;

    @Id
    @Column(name="info_id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "views")
	public Integer getViews() {
		return this.views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	@Column(name = "views_month")
	public Integer getViewsMonth() {
		return this.viewsMonth;
	}

	public void setViewsMonth(Integer viewsMonth) {
		this.viewsMonth = viewsMonth;
	}

	@Column(name = "views_week")
	public Integer getViewsWeek() {
		return this.viewsWeek;
	}

	public void setViewsWeek(Integer viewsWeek) {
		this.viewsWeek = viewsWeek;
	}

	@Column(name = "views_day")
	public Integer getViewsDay() {
		return this.viewsDay;
	}

	public void setViewsDay(Integer viewsDay) {
		this.viewsDay = viewsDay;
	}

	@Column(name = "comments")
	public Integer getComments() {
		return this.comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	@Column(name = "comments_month")
	public Integer getCommentsMonth() {
		return this.commentsMonth;
	}

	public void setCommentsMonth(Integer commentsMonth) {
		this.commentsMonth = commentsMonth;
	}

	@Column(name = "comments_week")
	public Integer getCommentsWeek() {
		return this.commentsWeek;
	}

	public void setCommentsWeek(Integer commentsWeek) {
		this.commentsWeek = commentsWeek;
	}

	@Column(name = "comments_day")
	public Integer getCommentsDay() {
		return this.commentsDay;
	}

	public void setCommentsDay(Integer commentsDay) {
		this.commentsDay = commentsDay;
	}

	@Column(name = "ups")
	public Integer getUps() {
		return this.ups;
	}

	public void setUps(Integer ups) {
		this.ups = ups;
	}

	@Column(name = "ups_month")
	public Integer getUpsMonth() {
		return this.upsMonth;
	}

	public void setUpsMonth(Integer upsMonth) {
		this.upsMonth = upsMonth;
	}

	@Column(name = "ups_week")
	public Integer getUpsWeek() {
		return this.upsWeek;
	}

	public void setUpsWeek(Integer upsWeek) {
		this.upsWeek = upsWeek;
	}

	@Column(name = "ups_day")
	public Integer getUpsDay() {
		return this.upsDay;
	}

	public void setUpsDay(Integer upsDay) {
		this.upsDay = upsDay;
	}

	@Column(name = "downs")
	public Integer getDowns() {
		return this.downs;
	}

	public void setDowns(Integer downs) {
		this.downs = downs;
	}

}