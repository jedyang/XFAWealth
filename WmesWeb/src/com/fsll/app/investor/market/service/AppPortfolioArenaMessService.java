/**
 * 
 */
package com.fsll.app.investor.market.service;

import java.util.List;

import com.fsll.app.investor.market.vo.AppIndexHotVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationDetailVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaCumperfVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMarketMessVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaProductVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaReturnVO;
import com.fsll.common.util.JsonPaging;



/**
 * 投资组合用于展示的接口服务接口类
 * @author zpzhou
 * @date 2016-9-18
 */
public interface AppPortfolioArenaMessService {
	
	/**
	 * 得到首页最佳组合更多列表信息
	 * @param jsonPaging 分页信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @return	JsonPaging	分页组合数据
	 */
	public JsonPaging findBestPortfolioList(JsonPaging jsonPaging,String memberId,String langCode,String periodCode);
	/**
	 * 得到一个基金关联的组合列表信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @return	productId 产品ID
	 */
	public List<AppIndexHotVO> findProductPortfolioList(String productId,String langCode,String periodCode,int num);
	/**
	 * 得到我有权限查看的投资组合列表
	 * @param memberId 用户ID
	 * @param keyWord 搜索关键词
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @return JsonPaging 分页数据
	 */
	public JsonPaging findPortfolioList(JsonPaging jsonPaging,String memberId,String periodCode,String keyWord,String langCode);

	/**
	 * 得到某个投资组合行情数据
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPortfolioArenaCumperfVO> findPortfolioCumperfList(String portfolioId,String startDate);
	
	/**
	 * 得到投资组合的产品信息列表
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @return
	 */
	public List<AppPortfolioArenaProductVo> findPortfolioProductList(String portfolioId,String langCode);
	
	/**
	 * 得到投资组合的产品分类配置情况
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @param groupType 统计分类
	 * @return
	 */
	public List<AppPortfolioArenaAllocationDetailVO> findPortfolioAllocationList(String portfolioId,String langCode,String groupType);
	
	/**
	 * 得到投资组合基本信息
	 * @param portfolioId 组合ID
	 * @return
	 */
	public AppPortfolioArenaMessVo findPortfolioInfoMess(String memberId,String portfolioId,String langCode);

	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-18
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppPortfolioArenaReturnVO findPortfolioReturn(String portfolioId,String periodCode);
}
