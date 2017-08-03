package com.fsll.wmes.strategy.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberBase;

public class WebInvestorVisitVO {
	 	private String id;
	 	private String nickName;
		private String moduleType;
		private String relateId;
		private MemberBase member;
		private Date vistiTime;
		private String iconUrl;
		
		//浏览时间区间，用于计算多少小时之前
		private Integer vistiPeriodTime;
		//浏览时间区间类型 H：小时， M：分钟
		private String vistiPeriodType;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getModuleType() {
			return moduleType;
		}
		public void setModuleType(String moduleType) {
			this.moduleType = moduleType;
		}
		public String getRelateId() {
			return relateId;
		}
		public void setRelateId(String relateId) {
			this.relateId = relateId;
		}
		public MemberBase getMember() {
			return member;
		}
		public void setMember(MemberBase member) {
			this.member = member;
		}
		public Date getVistiTime() {
			return vistiTime;
		}
		public void setVistiTime(Date vistiTime) {
			this.vistiTime = vistiTime;
		}
		public Integer getVistiPeriodTime() {
			return vistiPeriodTime;
		}
		public void setVistiPeriodTime(Integer vistiPeriodTime) {
			this.vistiPeriodTime = vistiPeriodTime;
		}
		public String getVistiPeriodType() {
			return vistiPeriodType;
		}
		public void setVistiPeriodType(String vistiPeriodType) {
			this.vistiPeriodType = vistiPeriodType;
		}
		public String getIconUrl() {
			return iconUrl;
		}
		public void setIconUrl(String iconUrl) {
			this.iconUrl = iconUrl;
		}
		
}
