package com.fsll.app.investor.me.service;

import java.util.List;

import com.fsll.app.investor.me.vo.AppCommunityTopicItemVO;
import com.fsll.app.investor.me.vo.AppInsightInfoVo;
import com.fsll.app.investor.me.vo.AppInsightItemVo;
import com.fsll.common.util.JsonPaging;


/**
 * 观点接口
 * @author zpzhou
 * @date 2016-8-11
 */
public interface AppInsightInfoService {
	
	/**
	 * 得到观点列表信息
	 * @param jsonPaging 分页参数
	 * @param memberId 用户ID
	 * @return
	 */
//	public JsonPaging findInsightList(JsonPaging jsonPaging,String memberId,String type);
	
	/**
	 * 得到观点详细信息
	 * @param insightId
	 * @param memberId
	 * @return
	 */
	public AppCommunityTopicItemVO findInsightInfo(String topicId,String langCode);
	
	/**
	 * 【我的投资顾问】得到观点列表信息
	 * @author zxtan
	 * @date 2017-01-17
	 * @param memberId 用户ID
	 * @param ifaMemberId IfaMemberId
	 * @return
	 */
	public List<AppCommunityTopicItemVO> findMyIfaInsightList(String memberId,String ifaMemberId,String langCode);
}
