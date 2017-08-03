package com.fsll.app.investor.discover.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * 发现朋友圈发表主题评论实体
 * @author zxtan
 * @date 2016-11-21
 */
public class AppCornerLikedVO {
	private String id;
	private String nickName;
	private String memberId;//评论人ID
	private String memberIconUrl;//评论人头像Url
	private String createDate;//评论日期
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberIconUrl() {
		return memberIconUrl;
	}
	public void setMemberIconUrl(String memberIconUrl) {
		this.memberIconUrl = memberIconUrl;
	}
	
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	
}
