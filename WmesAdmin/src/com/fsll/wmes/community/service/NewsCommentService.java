package com.fsll.wmes.community.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.community.vo.NewsCommentVO;
import com.fsll.wmes.entity.NewsComment;

public interface NewsCommentService {

	/**
	 *  新闻评论数据获取
	 * @author wwluo
	 * @date 2017-06-02
	 * @param jsonPaging
	 * @param newsCommentVO 过滤条件
	 * @param langCode 多语言标识
	 * @return JsonPaging
	 */
	public JsonPaging getNewsComments(JsonPaging jsonPaging,
			NewsCommentVO newsCommentVO, String langCode);

	/**
	 *  删除新闻评论数据（逻辑删除 news_comment.status=-1）
	 * @author wwluo
	 * @date 2017-06-02
	 * @param ids 删除id,可批量删除
	 * @return true:成功，false：失败
	 */
	public Boolean deleteNewsComments(String ids);

	/**
	 *  根据ID获取新闻评论实体
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @return NewsComment
	 */
	public NewsComment findById(String id);

	/**
	 *  根据ID获取新闻评论,返回VO实体
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @param langCode 多语言标识
	 * @return NewsCommentVO
	 */
	public NewsCommentVO findNewsCommentVOById(String id, String langCode);

	/**
	 *  保存新闻评论信息
	 * @author wwluo
	 * @date 2017-06-02
	 * @param id
	 * @param comment
	 * @return NewsComment
	 */
	public NewsComment save(NewsComment comment);

}
