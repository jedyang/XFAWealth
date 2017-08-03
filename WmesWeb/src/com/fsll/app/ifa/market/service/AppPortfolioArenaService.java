package com.fsll.app.ifa.market.service;


import java.util.List;

import net.sf.json.JSONObject;

import com.fsll.app.ifa.market.vo.AppArenaAllocationDetailVO;
import com.fsll.app.ifa.market.vo.AppArenaCumperfVO;
import com.fsll.app.ifa.market.vo.AppArenaProductVO;
import com.fsll.app.ifa.market.vo.AppArenaReturnVO;
import com.fsll.app.ifa.market.vo.AppArenaVO;
import com.fsll.common.util.JsonPaging;

/**
 * IFA 组合库管理服务
 * @author zxtan
 * @date 2017-03-23
 */
public interface AppPortfolioArenaService {

	/**
	 * 获取我的策略
	 * @author zxtan
	 * @date 2017-03-29
	 * @param jsonPaging
	 * @param memberId
	 * @param status
	 * @param isValid
	 * @param langCode
	 * @return
	 */
	public JsonPaging findMyPortfolioList(JsonPaging jsonPaging,String memberId,String keyword,String status,String isValid,String periodCode,String langCode);
	
	/**
	 * 获取我有权限查看的组合列表
	 * @author zxtan
	 * @date 2017-04-25
	 */
	public JsonPaging findAllPortfolioList(JsonPaging jsonPaging,JSONObject jsonObject);
	
	/**
	 * 更新或删除Arena
	 * @author zxtan
	 * @date 2017-03-29
	 * @param updateType update/delete
	 * @param id Arena Id
	 * @param isValid 是否有效（update时用到）
	 * @return
	 */
	public boolean updatePortfolioArena(String updateType,String id,String isValid);
	
	/**
	 * 得到某个投资组合行情数据
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppArenaCumperfVO> findPortfolioCumperfList(String portfolioId,String startDate);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @return
	 */
	public List<AppArenaProductVO> findPortfolioProductList(String portfolioId,String langCode);
	
	/**
	 * 得到投资组合的产品分类配置情况
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @param groupType 统计分类
	 * @return
	 */
	public List<AppArenaAllocationDetailVO> findPortfolioAllocationList(String portfolioId,String langCode,String groupType);
	
	/**
	 * 得到投资组合基本信息
	 * @param portfolioId 组合ID
	 * @return
	 */
	public AppArenaVO findPortfolioInfoMess(String memberId,String portfolioId,String langCode);

	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-18
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppArenaReturnVO findPortfolioReturn(String portfolioId,String periodCode);
}
