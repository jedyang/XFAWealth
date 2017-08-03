package com.fsll.app.discover.vo;

import java.util.List;

import com.fsll.wmes.community.vo.FrontSearchUserVO;

/**
 * 社区栏目实体
 * @author zxtan
 * @date 2017-05-17
 */
public class AppCommunitySearchDataVO {

	private List<AppCommunityTopicVO> topicList;//帖子列表
	private List<FrontSearchUserVO> userList;//用户列表
	public List<AppCommunityTopicVO> getTopicList() {
		return topicList;
	}
	public void setTopicList(List<AppCommunityTopicVO> topicList) {
		this.topicList = topicList;
	}
	public List<FrontSearchUserVO> getUserList() {
		return userList;
	}
	public void setUserList(List<FrontSearchUserVO> userList) {
		this.userList = userList;
	}		
}
