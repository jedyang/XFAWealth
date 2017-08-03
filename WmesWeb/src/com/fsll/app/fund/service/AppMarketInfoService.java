/**
 * 
 */
package com.fsll.app.fund.service;

import java.util.List;

import com.fsll.app.fund.vo.AppMarketIndexDetailVo;
import com.fsll.app.fund.vo.AppMarketIndexVo;

/**
 * 大盘指数服务接口
 * @author zpzhou
 * @date 2016-9-13
 */
public interface AppMarketInfoService {
	
	/**
	 * 得到我关注的大盘指数列表
	 * @param userId 用户ID
	 * @param langCode 语言
	 * @return
	 */
	public List<AppMarketIndexVo> findMarketFollowList(String userId,String langCode);

	/**
	 * 得到大盘指数列表
	 * @param langCode 语言
	 * @param keyWord 搜索关键词
	 * @return
	 */
	public List<AppMarketIndexVo> findMarketList(String langCode,String keyWord);
	/**
	 * 得到大盘指数分时信息数据
	 * @param indexId 语言
	 * @return
	 */
	public List<AppMarketIndexDetailVo> getMarketDetailMess(String indexId);
	/**
	 * 得到每天的大盘指数列表
	 * @param langCode 语言
	 * @param keyWord 搜索关键词
	 * @return
	 */
	public List<AppMarketIndexVo> findMarketHisList(String code);

	
}
