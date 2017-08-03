package com.fsll.wmes.ifa.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.ifa.vo.InsightInfoVo;
import com.fsll.wmes.ifa.vo.InsightSearchParamVo;
import com.fsll.wmes.ifa.vo.InsightVistorVo;


/**
 * 观点接口
 * @author zpzhou
 * @date 2016-8-11
 */
public interface InsightInfoService {
	
	/**
	 * 得到观点列表信息
	 * @param jsonPaging 分页参数
	 * @param memberId 用户ID
	 * @return
	 */
	public JsonPaging findInsightList(JsonPaging jsonPaging,String memberId,String type);
	
	/**
	 * 得到观点详细信息
	 * @param insightId
	 * @param memberId
	 * @return
	 */
	public InsightInfoVo findInsightInfo(String insightId,String memberId);
	
	
	/**
	 * 获取列表
	 * @param jsonPaging
	 * @param keyword
	 * @param lang
	 * @return
	 */
	public JsonPaging findAllInsight(JsonPaging jsonPaging,String keyword,String lang);
	
	/**
	 * 获取内容
	 * @param id
	 * @param lang
	 * @return
	 */
	public InsightInfo findInsightById(String id,String lang);
	
	/**
	 * web前台获取IFA观点列表
	 * @author scshi
	 * @param jsonPaging 分页实体
	 * @param loginUserId 登陆用户
	 * @param searchParam 搜索条件vo
	 * 
	 * */
	public JsonPaging findIfaInsightListForWeb(JsonPaging jsonPaging,String loginUserId, InsightSearchParamVo searchParam);

	/**
	 * 观点访客列表
	 * @author scshi
	 * @date 20160822
	 * @param insightId 观点唯一标识
	 * @param vistType	对应模块,ifa空间访客,insight观点访客,news新闻访客,strategy策略,portfolio_arena组合竞技场访客,portfolio_info组合访客
	 * */
	public List<InsightVistorVo> loadVistorList(String insightId,String vistType);
	/**
	 * 保存/创建观点实例
	 * @author scshi
	 * @date 20160823
	 * @param insight 观点实例
	 * */
	public InsightInfo createOrUpdateInsight(InsightInfo insight) throws Exception;

	/**
	 * 删除观点
	 * @author scshi
	 * @date 20160823
	 * @param insightId 观点唯一标识
	 * */
	public void delInsightById(String insightId);

	
	
}
