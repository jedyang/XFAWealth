package com.fsll.wmes.ifa.vo;

import java.util.List;

import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * 发现朋友圈-IFA圈子分享空间实体
 * @author 林文伟
 * @date 2016-10-12
 */
public class IfaCorner {
	private String ifaId;
	private String ifaHeadImg;//头像
	private String ifaNickName;
	private String memberId;
	private String gender;
	private Integer follows;//好友人数
	private Integer topicsCount;//分享主题数
	private String latestHighlight;//个人最新心情
	private List<IfaCornerInfoDetailVO> ifaCornerInfoDetailVOList;//主题列表
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getIfaHeadImg() {
		return ifaHeadImg;
	}
	public void setIfaHeadImg(String ifaHeadImg) {
		this.ifaHeadImg = ifaHeadImg;
	}
	public String getIfaNickName() {
		return ifaNickName;
	}
	public void setIfaNickName(String ifaNickName) {
		this.ifaNickName = ifaNickName;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	public Integer getFollows() {
		return follows;
	}
	public void setFollows(Integer follows) {
		this.follows = follows;
	}
	public Integer getTopicsCount() {
		return topicsCount;
	}
	public void setTopicsCount(Integer topicsCount) {
		this.topicsCount = topicsCount;
	}
	public List<IfaCornerInfoDetailVO> getIfaCornerInfoDetailVOList() {
		return ifaCornerInfoDetailVOList;
	}
	public void setIfaCornerInfoDetailVOList(
			List<IfaCornerInfoDetailVO> ifaCornerInfoDetailVOList) {
		this.ifaCornerInfoDetailVOList = ifaCornerInfoDetailVOList;
	}
	public String getLatestHighlight() {
		return latestHighlight;
	}
	public void setLatestHighlight(String latestHighlight) {
		this.latestHighlight = latestHighlight;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
