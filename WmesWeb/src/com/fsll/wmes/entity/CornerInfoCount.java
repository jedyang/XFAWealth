package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "corner_info_count")
public class CornerInfoCount {

	private String id;
	private Integer views;
	private Integer viewsMonth;
	private Integer viewsWeek;
	private Integer viewsDay;
	private Integer comments;
	private Integer commentsMonth;
	private Short commentsWeek;
	private Short commentsDay;
	private Integer ups;
	private Integer upsMonth;
	private Short upsWeek;
	private Short upsDay;
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
	public Short getCommentsWeek() {
		return this.commentsWeek;
	}

	public void setCommentsWeek(Short commentsWeek) {
		this.commentsWeek = commentsWeek;
	}

	@Column(name = "comments_day")
	public Short getCommentsDay() {
		return this.commentsDay;
	}

	public void setCommentsDay(Short commentsDay) {
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
	public Short getUpsWeek() {
		return this.upsWeek;
	}

	public void setUpsWeek(Short upsWeek) {
		this.upsWeek = upsWeek;
	}

	@Column(name = "ups_day")
	public Short getUpsDay() {
		return this.upsDay;
	}

	public void setUpsDay(Short upsDay) {
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
