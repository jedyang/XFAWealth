/**
 * 
 */
package com.fsll.app.investor.market.service;


import java.util.List;

import com.fsll.app.investor.market.vo.AppIndexChartVO;


/**
 * 首页热门投资接口服务
 * @author zpzhou
 * @date 2016-9-26
 */
public interface AppIndexHotService {
	
	/**
	 * 得到首页组合中用到最多的基金的行情图表数据
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>FundIndexChartVO	基金图表数据
	 */
	public List<AppIndexChartVO> findFundIndexChart(String period, String addiData, String langCode,int num);
	
	/**
	 * 得到首页组合中收益最多的行情图表数据
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>FundIndexChartVO	基金图表数据
	 */
	public List<AppIndexChartVO> findPortfolioIndexChart(String period, String addiData, String langCode,int num);
}
