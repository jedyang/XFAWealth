/**
 * 
 */
package com.fsll.app.investor.me.service;


import java.util.List;

import com.fsll.app.investor.me.vo.AppHoldProductReturnDetailVO;
import com.fsll.app.investor.me.vo.AppHoldProductVO;
import com.fsll.app.investor.me.vo.AppMyAssetsHisMessVo;
import com.fsll.app.investor.me.vo.AppMyAssetsMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioAllocationVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;

/**
 * 我的资产信息接口服务接口类
 * @author zpzhou
 * @date 2016-9-13
 */
public interface AppMyAssetMessService {
	
	/**
	 * 得到我的资产信息
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public AppMyAssetsMessVo findMyAssetMess(String memberId,String toCurrency,String langCode);

	/**
	 * 得到我的历史资产信息列表
	 * @param memberId 用户ID
	 * @param toCurrency 货币转换参数
	 * @return
	 */
	public List<AppMyAssetsHisMessVo> findMyAssetHisMess(String memberId,String toCurrency,String startDate);

	/**
	 * 得到资产分析的产品配置信息
	 * @author zxtan
	 * @date 2017-02-17
	 * @param memberId 会员Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppPortfolioAllocationVO> findProductAllocationList(String memberId,String langCode,String toCurrency);
	
	/**
	 * 得到资产分析的基金配置信息
	 * @author zxtan
	 * @date 2017-02-17
	 * @param memberId 会员Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @param groupType 配置分组类型
	 * @return
	 */
	public List<AppPortfolioAllocationVO> findProductFundAllocationList(String memberId,String langCode,String toCurrency,String groupType);
	
	/**
	 * 得到我的资产的基金产品信息列表
	 * @author zxtan
	 * @date 2017-02-22
	 * @param memberId 会员Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
//	public List<AppPortfolioProductVo> findProductFundList(String memberId,String langCode,String toCurrency);
	
	/**
	 * 得到我的资产的产品信息列表
	 * @author zxtan
	 * @date 2017-03-03
	 * @param memberId 会员Id
	 * @param productType 产品类型
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppHoldProductVO> findHoldProductList(String memberId,String productType,String langCode,String toCurrency);
	
	/**
	 * 得到我的资产的产品回报信息
	 * @author zxtan
	 * @date 2017-03-03
	 * @param holdProductId 持仓产品Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public AppHoldProductReturnDetailVO findHoldProductReturnDetail(String holdProductId,String langCode,String toCurrency);
}
