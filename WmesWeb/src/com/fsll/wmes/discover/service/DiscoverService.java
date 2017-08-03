package com.fsll.wmes.discover.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.discover.vo.WebDiscussReplyVO;
import com.fsll.wmes.discover.vo.WebDiscussVO;
import com.fsll.wmes.entity.WebDiscuss;
import com.fsll.wmes.entity.WebDiscussReply;

public interface DiscoverService {

	/**
     * 读取“评论”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param relateId 评论对象 Id
     * @param moduleType 对应模块，null时获取全部信息
     * @param topicType H：Hot Topic ； L：Latest Topic 排序方式
     * @return
     */
	public JsonPaging getWebDiscuss(JsonPaging jsonPaging,String relateId, String moduleType, String topicType);

	/**
	 * 发表“评论”信息
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param webDiscuss
	 * @return
	 */
	public WebDiscuss saveWebDiscuss(WebDiscuss webDiscuss);

	/**
     * 读取“回复”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param discussId
     * @return
     */
	public List<WebDiscussReplyVO> getWebDiscussReply(String discussId);

	/**
	 * 发表“回复”信息
	 * @author wwluo
	 * @date 2016-11-07 
	 * @param webDiscussReply
	 * @return
	 */
	public WebDiscussReply saveWebDiscussReply(WebDiscussReply webDiscussReply);

	/**
     * 读取一条“评论”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param discussId
     * @return
     */
	public WebDiscuss getWebDiscussById(String discussId);

	/**
     * 读取一条“回复”信息
     * @author wwluo
     * @date 2016-11-07 
     * @param discussReplyId
     * @return
     */
	public WebDiscussReply getWebDiscussReplyById(String discussReplyId);

}
