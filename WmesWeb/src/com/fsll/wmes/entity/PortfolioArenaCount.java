package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "portfolio_arena_count")
public class PortfolioArenaCount implements java.io.Serializable {
    @Id
    @Column(name="info_id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	private String id;
    
    @Column(name = "views")
	private Integer views;
	
    @Column(name = "views_month")
	private Integer viewsMonth;
	
    @Column(name = "views_week")
	private Integer viewsWeek;
	
    @Column(name = "views_day")
	private Integer viewsDay;
	
    @Column(name = "comments")
	private Integer comments;
	
    @Column(name = "comments_month")
	private Integer commentsMonth;
	
    @Column(name = "comments_week")
	private Integer commentsWeek;
	
    @Column(name = "comments_day")
	private Integer commentsDay;
	
    @Column(name = "ups")
	private Integer ups;
	
    @Column(name = "ups_month")
	private Integer upsMonth;
	
    @Column(name = "ups_week")
	private Integer upsWeek;
	
    @Column(name = "ups_day")
	private Integer upsDay;
	
    @Column(name = "downs")
	private Integer downs;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
	}

	public Integer getViewsMonth() {
		return viewsMonth;
	}

	public void setViewsMonth(Integer viewsMonth) {
		this.viewsMonth = viewsMonth;
	}

	public Integer getViewsWeek() {
		return viewsWeek;
	}

	public void setViewsWeek(Integer viewsWeek) {
		this.viewsWeek = viewsWeek;
	}

	public Integer getViewsDay() {
		return viewsDay;
	}

	public void setViewsDay(Integer viewsDay) {
		this.viewsDay = viewsDay;
	}

	public Integer getComments() {
		return comments;
	}

	public void setComments(Integer comments) {
		this.comments = comments;
	}

	public Integer getCommentsMonth() {
		return commentsMonth;
	}

	public void setCommentsMonth(Integer commentsMonth) {
		this.commentsMonth = commentsMonth;
	}

	public Integer getCommentsWeek() {
		return commentsWeek;
	}

	public void setCommentsWeek(Integer commentsWeek) {
		this.commentsWeek = commentsWeek;
	}

	public Integer getCommentsDay() {
		return commentsDay;
	}

	public void setCommentsDay(Integer commentsDay) {
		this.commentsDay = commentsDay;
	}

	public Integer getUps() {
		return ups;
	}

	public void setUps(Integer ups) {
		this.ups = ups;
	}

	public Integer getUpsMonth() {
		return upsMonth;
	}

	public void setUpsMonth(Integer upsMonth) {
		this.upsMonth = upsMonth;
	}

	public Integer getUpsWeek() {
		return upsWeek;
	}

	public void setUpsWeek(Integer upsWeek) {
		this.upsWeek = upsWeek;
	}

	public Integer getUpsDay() {
		return upsDay;
	}

	public void setUpsDay(Integer upsDay) {
		this.upsDay = upsDay;
	}

	public Integer getDowns() {
		return downs;
	}

	public void setDowns(Integer downs) {
		this.downs = downs;
	}
	
	
	
}