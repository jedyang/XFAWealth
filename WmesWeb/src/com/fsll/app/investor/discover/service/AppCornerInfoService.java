package com.fsll.app.investor.discover.service;

import com.fsll.app.investor.discover.vo.AppCornerInfoVO;
import com.fsll.app.investor.discover.vo.AppIfaInfoVO;
import com.fsll.app.investor.discover.vo.AppIfaPerformanceVO;
import com.fsll.app.investor.discover.vo.AppIfaRecommendedVO;
import com.fsll.common.util.JsonPaging;


/**
 * 圈子信息接口
 * @author zxtan
 * @date 2016-11-21
 */
public interface AppCornerInfoService {
	
	/**
	 * 得到投资者圈子列表信息
	 * @param jsonPaging 分页参数
	 * @param memberId 投资用户ID
	 * @param ifaMemberId IfaId
	 * @return
	 */
	public JsonPaging findCornerInfoList(JsonPaging jsonPaging,String memberId,String ifaMemberId);
	
	/**
	 * 获取某条圈子的信息实体
	 * @param cornerId
	 * @return
	 */
	public AppCornerInfoVO findCornerInfoById(String cornerId);
	
	/**
	 * 得到投资者圈子列表信息
	 * @param jsonPaging 分页参数
	 * @param cornerId 
	 * @return
	 */
	public JsonPaging findCornerReplyList(JsonPaging jsonPaging,String cornerId);
	
	/**
	 * 得到投资者圈子列表信息
	 * @param jsonPaging 分页参数
	 * @param cornerId 
	 * @return
	 */
	public JsonPaging findCornerLikedList(JsonPaging jsonPaging,String cornerId);
	
	/**
	 * 获取Ifa基本信息
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public AppIfaInfoVO findIfaInfo(String ifaMemberId,String langCode);

	/**
	 * 获取Ifa空间的业绩
	 * @param ifaMemberId
	 * @param toCurrency
	 * @param periodCode
	 * @param rows
	 * @return
	 */
	public AppIfaPerformanceVO findIfaPerformance(String ifaMemberId,String toCurrency,String periodCode,int rows);
	
	/**
	 * 获取Ifa空间的推荐内容
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param langCode
	 * @param toCurrency
	 * @param periodCode
	 * @return
	 */
	public AppIfaRecommendedVO findIfaRecommended(JsonPaging jsonPaging, String ifaMemberId,String langCode,String toCurrency,String periodCode);

}
