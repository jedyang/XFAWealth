package com.fsll.wmes.community.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.community.vo.TopicCommentVO;
import com.fsll.wmes.entity.CommunityComment;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.CommunityTopic;

public interface TopicCommentService {

	/**
	 *  帖子评论(包含问题答案)数据获取
	 * @author wwluo
	 * @date 2017-06-02
	 * @param jsonPaging
	 * @param newsCommentVO 过滤条件
	 * @param langCode 多语言标识
	 * @return jsonPaging
	 */
	public JsonPaging getTopicComments(JsonPaging jsonPaging,
			TopicCommentVO topicCommentVO, String langCode);

	/**
	 *  删除新闻评论数据（逻辑删除）
	 * @author wwluo
	 * @date 2017-06-02
	 * @param ids 删除id,可批量删除
	 * @return true:成功，false：失败
	 */
	public Boolean deleteCommunityComments(String ids);

	/**
	 *  根据ID获取评论信息,返回VO实体
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @param langCode 多语言标识
	 * @return NewsCommentVO
	 */
	public TopicCommentVO findCommentVOById(String id, String langCode);

	/**
	 *  根据ID获取评论实体
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @return CommunityComment
	 */
	public CommunityComment findById(String id);
	
	/**
	 *  根据ID获取社区帖子实体
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @return CommunityTopic
	 */
	public CommunityTopic findCommunityTopicById(String id);
	
	/**
	 *  根据ID获取社区问题实体
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @return CommunityQuestion
	 */
	public CommunityQuestion findCommunityQuestionById(String id);

	/**
	 *  保存社区评论信息
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @return CommunityComment
	 */
	public CommunityComment save(CommunityComment comment);
}
