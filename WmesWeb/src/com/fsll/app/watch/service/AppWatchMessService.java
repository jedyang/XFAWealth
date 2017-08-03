/**
 * 
 */
package com.fsll.app.watch.service;


import java.util.List;

import net.sf.json.JSONArray;

import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;
import com.fsll.app.investor.market.vo.AppStrategyMessVo;
import com.fsll.app.watch.vo.AppFundInfoVO;
import com.fsll.app.watch.vo.AppPortfolioInfoVO;
import com.fsll.app.watch.vo.AppWatchFundVO;
import com.fsll.app.watch.vo.AppWatchProductVo;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.entity.WebWatchlistAlert;
import com.fsll.wmes.entity.WebWatchlistType;

/**
 * 自选接口服务接口类
 * @author zpzhou
 * @date 2016-9-30
 */

public interface AppWatchMessService {
	
	/**
	 * 产品的关注/取消
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param OpType	Follow-设置关注;Delete-取消关注
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	public WebFollow saveWebFollowMess(String relateId,String memberId,String opType,String moduleType);
	
	/**
	 * 得到自选分类信息
	 * @param memberID 网站会员ID
	 */
	public List<WebWatchlistType> getWatchTypeMess(String memberId);
	
	/**
	 * 获取自选预警设置信息
	 * @param productId 产品ID
	 * @param memberID 网站会员ID
	 */
	public List<WebWatchlistAlert> getWatchAlertMess(String productId,String memberId);

	/**
	 * 保存自选预警设置信息
	 * @param productId 产品ID
	 * @param memberID 网站会员ID
	 * @param array 预警设置信息
	 * @return 0:成功  -1:失败
	 */
	public int saveWatchAlertMess(String productId,String memberId,JSONArray array);

	/**
	 * 新增/修改自选分类信息
	 * @param typeId 分类ID
	 * @param memberID 用户ID
	 * @param name 分类名称
	 * @return 0:成功  -1:失败
	 */
	public int saveWatchTypeMess(String typeId,String memberId,String name);
	/**
	 * 删除自选分类信息
	 * @param typeId 分类ID
	 * @return 0:成功  -1:失败
	 */
	public int deleteWatchTypeMess(String typeId);
	
	/**
	 * 根据自选类别得到我自选的产品列表
	 * @param memberId 用户ID
	 * @param typeId 分类ID
	 * @return List 列表数据
	 */
	public List<AppWatchProductVo> findWatchProductList(String typeId,String memberId,String langCode);
	
	/**
	 * 得到我自选的投资策略列表
	 * @param memberId 用户ID
	 * @return List 列表数据
	 */
	public List<AppStrategyMessVo> findWatchStrategyList(String memberId,String langCode);
	/**
	 * 得到我自选的投资组合列表
	 * @param memberId 用户ID
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @return List 列表数据
	 */
	public List<AppPortfolioArenaMessVo> findWatchPortfolioList(String memberId,String periodCode,String langCode);

	
	/**
	 * 得到感兴趣的基金列表
	 * @param memberId
	 * @param periodCode
	 * @param rows
	 * @return
	 */
	public List<AppFundInfoVO> findVisitedFundList(String memberId,String langCode,String periodCode,int rows);
	

	/**
	 * 得到感兴趣的基金列表
	 * @param memberId
	 * @param periodCode
	 * @param rows
	 * @return
	 */
	public List<AppPortfolioInfoVO> findVisitedPortfolioList(String memberId,String langCode,String periodCode,int rows);
	

	/**
	 * 得到感兴趣的策略信息
	 * @param memberId
	 * @param langCode
	 * @param periodCode
	 * @param rows
	 * @return
	 */
	public List<AppStrategyInfoVO> findVisitedStrategyList(String memberId,String langCode,int rows);
	
	/**
	 * 产品的自选/取消
	 * @author zxtan
	 * @date 2017-02-14
	 * @param productId 产品id
	 * @param typeId 自选类型Id
	 * @param memberId 网站会员ID
	 * @param OpType	add-设置自选;Delete-取消自选
	 */
	public WebWatchlist saveWebWatchlistMess(String productId, String typeId, String memberId,String opType);
	
	/**
	 * 得到我自选的基金产品列表
	 * @author zxtan
	 * @date 2017-02-20
	 * @param memberId 用户ID
	 * @param typeId 类型ID
	 * @param langCode 语言
	 * @param toCurrency 货币转换
	 * @return List 列表数据
	 */
	public List<AppWatchFundVO> findWatchFundList(String memberId,String typeId,String langCode,String toCurrency);
	
	/**
	 * 产品的关注
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param moduleType 对应模块,组合，策略关注
	 */
	public String findWebFollowCount(String relateId, String memberId, String moduleType);
}
