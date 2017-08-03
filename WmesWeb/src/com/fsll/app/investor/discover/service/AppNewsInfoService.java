package com.fsll.app.investor.discover.service;

import java.util.List;

import com.fsll.app.investor.discover.vo.AppNewsCategoryVO;
import com.fsll.app.investor.discover.vo.AppNewsInfoItemVO;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.NewsInfo;


/**
 * 新闻接口
 * @author zpzhou
 * @date 2016-8-11
 */
public interface AppNewsInfoService {
	
//	/**
//	 * 得到新闻列表信息
//	 * @param jsonPaging 分页参数
//	 * @param memberId 用户ID
//	 * @return
//	 */
//	public JsonPaging findNewsList(JsonPaging jsonPaging,String memberId,String type);
	
	/**
	 * 获取新闻的信息实体
	 * @author mqzou
	 * @date 2016-10-08
	 * @param id
	 * @return
	 */
	public NewsInfo findNewsInfoById(String id);
	
	/**
	 * 获取新闻栏目列表
	 * @author zxtan
	 * @date 2017-03-09
	 * @param langCode
	 * @return
	 */
	public List<AppNewsCategoryVO> findNewsCategoryList(String langCode);
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-09
	 * @param jsonPaging 分页参数
	 * @param categoryId 栏目ID
	 * @param langCode 语言
	 * @param keyword 搜索关键字
	 * @return
	 */
	public JsonPaging findNewsListByCategory(JsonPaging jsonPaging,String categoryId,String langCode,String keyword);
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @param id 新闻Id
	 * @return
	 */
	public AppNewsInfoItemVO findNewsInfo(String memberId,String id);
	
	/**
	 * 得到新闻列表信息
	 * @author zxtan
	 * @date 2017-03-10
	 * @param jsonPaging 分页参数
	 * @param newsInfoId 新闻ID
	 * @return
	 */
	public JsonPaging findNewsCommentList(JsonPaging jsonPaging,String memberId,String newsInfoId);
	
	/**
	 * 点赞、踩
	 * @author zxtan
	 * @date 2017-05-03
	 * @param memberId 当前登录人
	 * @param targetId 新闻id
	 * @param behaviorType like/unlike
	 * @return
	 */
	public boolean addNewsBehavior(String memberId,String targetId,String behaviorType);
	
	/**
	 * 添加评论
	 * @author zxtan
	 * @date 2017-05-03
	 * @param infoId
	 * @param memberId
	 * @param comment
	 * @param replyCommentId
	 * @param ip
	 * @return
	 */
	public boolean addNewsComment(String infoId, String memberId,String comment,String replyCommentId,String ip);
	
	/**
	 * 新闻评论点赞、踩
	 * @author zxtan
	 * @date 2017-05-03
	 * @param memberId 当前登录人
	 * @param targetId 新闻id
	 * @param behaviorType like/unlike
	 * @return
	 */
	public boolean addNewsCommentBehavior(String memberId,String targetId,String behaviorType);
}
