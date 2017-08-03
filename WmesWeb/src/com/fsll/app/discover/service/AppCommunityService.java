package com.fsll.app.discover.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.fsll.app.discover.vo.AppCommunitySectionVO;
import com.fsll.app.discover.vo.AppCommunityTopicVO;
import com.fsll.common.util.JsonPaging;

/**
 * 社区接口
 * @author zxtan
 * @date 2017-05-17
 */
public interface AppCommunityService {

	/**
	 * 获取社区栏目列表
	 * @author zxtan
	 * @date 2017-05-17 
	 */
	public List<AppCommunitySectionVO> findSectionList(String langCode);
	
	public JsonPaging findTopicListBySection(JsonPaging jsonPaging,String memberId,String sectionId,String langCode);
	
	public JsonPaging findDynamicTopicList(JsonPaging jsonPaging,String memberId,String langCode);
	
	
	/**
	 * 获取帖子
	 * @author zxtan
	 * @date 2017-05-18
	 */
	public JsonPaging findSearchTopicList(JsonPaging jsonPaging,String memberId,String keyword, String langCode);
	
	/**
	 * 搜索页面-获取user列表
	 * @author wwlin
	 * @date 2016-03-13
	 * */
	public JsonPaging findSearchUserList(JsonPaging jsonPaging,String memberId,String keywords,String lang);
	/**
	 * 得到帖子信息
	 * @author zxtan
	 * @date 2017-05-18
	 * @return
	 */
	public AppCommunityTopicVO findTopicInfo(String memberId,String topicId,String langCode);
	
	/**
	 * 添加帖子
	 * @author zxtan
	 * @date 2017-03-10
	 * @param jsonObject
	 * @return
	 */
	public boolean saveTopic(JSONObject jsonObject);
	/**
	 * 得到回复列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @param jsonPaging 分页参数
	 * @param newsInfoId 新闻ID
	 * @return
	 */
	public JsonPaging findCommentList(JsonPaging jsonPaging,String memberId,String target,String targetId,String commentType,String langCode);
	
	/**
	 * 帖子点赞、踩
	 * @author zxtan
	 * @date 2017-05-03
	 * @param memberId 当前登录人
	 * @param targetId 新闻id
	 * @param behaviorType like/unlike
	 * @return
	 */
	public boolean saveBehavior(String memberId,String target,String targetId,String behaviorType);
	
	/**
	 * 添加评论
	 * @author zxtan
	 * @date 2017-05-03
	 */
	public boolean addComment(String memberId,String target,String targetId, String parentId,String commentType,String content);
	
}
