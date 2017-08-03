package com.fsll.app.investor.market.service;


import java.util.List;

import com.fsll.app.investor.market.vo.AppIndexBbsTopicVO;
import com.fsll.app.investor.market.vo.AppIndexBbsTypeVO;
import com.fsll.common.util.JsonPaging;


/**
 * 首页热门话题接口服务
 * @author zpzhou
 * @date 2016-10-09
 */
public interface AppIndexTopicService {
	
	/**
	 * 热门话题模块列表信息
	 * @param langCode  语言
	 * @return
	 */
	public List<AppIndexBbsTypeVO> findIndexBbsType(String langCode);
	/**
	 * 得到热门话题列表信息
	 * @param jsonPaging 分页参数
	 * @param typeId 类型ID  0:最热门  1：推荐  2:模块
	 * @param moduleId 模块ID  当typeId=2时不能为空
	 * @return
	 */
	public JsonPaging findIndexBbsTopicList(JsonPaging jsonPaging,String typeId,String moduleId);
	/**
	 * 得到某个热门话题详细信息
	 * @param topicId 话题ID
	 * @return
	 */
	public AppIndexBbsTopicVO findIndexBbsTopicMess(String topicId);
	/**
	 * 得到某个热门话题的回复列表信息
	 * @param jsonPaging 分页参数
	 * @param topicId 话题ID
	 * @return
	 */
	public JsonPaging findIndexBbsReplyList(JsonPaging jsonPaging,String topicId);
}
