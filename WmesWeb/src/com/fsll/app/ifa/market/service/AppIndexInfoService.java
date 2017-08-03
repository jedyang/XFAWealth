package com.fsll.app.ifa.market.service;

import java.util.List;

import com.fsll.app.ifa.market.vo.AppFollowArenaItemVO;
import com.fsll.app.ifa.market.vo.AppHotTopicItemVO;
import com.fsll.app.ifa.market.vo.AppNoticeItemVO;
import com.fsll.app.ifa.market.vo.AppStrategyInfoItemVO;
import com.fsll.app.ifa.market.vo.AppWatchFundItemVO;
import com.fsll.app.ifa.market.vo.AppWatchTypeVO;

/**
 * IFA Market首页接口
 * @author zxtan
 * @date 2017-03-14
 *
 */
public interface AppIndexInfoService {

	/**
	 * 获取IFA 的自选分类及基金列表
	 * @param memberId Ifa member Id
	 * @param periodCode 周期
	 * @param langCode 语言
	 * @return
	 */
	public List<AppWatchTypeVO> findMyWatchTypeList(String memberId,String periodCode,String langCode);
	
	/**
	 * 获取IFA 的自选分类及基金列表
	 * @param memberId Ifa member Id
	 * @param periodCode 周期
	 * @param langCode 语言
	 * @return
	 */
	public List<AppFollowArenaItemVO> findMyFollowPortfolioList(String memberId,String periodCode,String langCode);
		
	/**
	 * 获取IFA 的自选策略列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppStrategyInfoItemVO> findMyFollowStrategyList(String memberId,String langCode);
	
	/**
	 * 获取热门的话题列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppHotTopicItemVO> findHotTopicList(String memberId,String langCode);
	
	/**
	 * 获取IFA 感兴趣的基金
	 * @param memberId
	 * @param periodCode
	 * @param langCode
	 * @return
	 */
	public List<AppWatchFundItemVO> findVisitedFundList(String memberId,String periodCode,String langCode);
	
	/**
	 * 获取IFA 感兴趣的组合列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppFollowArenaItemVO> findVisitedPortfolioList(String memberId,String periodCode,String langCode);

	/**
	 * 获取IFA 感兴趣的策略列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppStrategyInfoItemVO> findVisitedStrategyList(String memberId,String langCode);
	
	/**
	 * 获取公告列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppNoticeItemVO> findNoticeList(String memberId);
}
