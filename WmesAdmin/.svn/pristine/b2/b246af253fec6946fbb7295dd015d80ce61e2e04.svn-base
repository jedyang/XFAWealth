package com.fsll.wmes.entity;

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

@Entity
@Table(name = "notice_group")
public class NoticeGroup implements java.io.Serializable {
	private String id;
	private Notice notice;
	private MemberGroup group;

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
	@JoinColumn(name = "notice_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public Notice getNotice() {
		return notice;
	}

	public void setNotice(Notice notice) {
		this.notice = notice;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "group_id")
	@NotFound(action=NotFoundAction.IGNORE)
	public MemberGroup getGroup() {
		return group;
	}

	public void setGroup(MemberGroup group) {
		this.group = group;
	}

}