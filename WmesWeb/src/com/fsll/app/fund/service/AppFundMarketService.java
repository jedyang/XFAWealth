/**
 * 
 */
package com.fsll.app.fund.service;

import com.fsll.app.fund.vo.AppChartDataVO;
import com.fsll.common.util.JsonPaging;

/**
 * 基金净值信息查询服务接口
 * @author zpzhou
 * @date 2016-6-20
 */
public interface AppFundMarketService {
	
	/**
	 * 获取基金净值分页
	 * @author Michael
	 * @param jsonPaging 分页对象
	 * @param fundId 基金id
	 * @param dataType 数据类型
	 * @param period 时间段类型： day, week, month
	 * @param startDate 开始时间： yyyy-MM-dd
	 * @param endDate 结束时间： yyyy-MM-dd
	 * @param langCode 语言
	 * @param toCurrency 目标货币
	 * @return JsonPaging	基金净值分页数据
	 */
	public JsonPaging findFundMarketList(JsonPaging jsonPaging,String fundId, String period, String startDate, String endDate, String langCode, String toCurrency);

	/**
	 * 获取基金净值图表数据
	 * @author scshi
	 * @param fundId 资金id
	 * @param dataType 数据类型
	 * @param cycle 分析周期：D-日；W-周；M-月
	 * @param periodCode 分析时间段编码：return_period_code_1W：一周，return_period_code_1M：一个月...，return_period_code_1Y：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param toCurrency 转换目标货币
	 * @param langCode 显示语言
	 * @return	<List>ChartDataVO	基金图表数据
	 */
	public AppChartDataVO fundChartData(String fundId, String dataType, String cycle, String periodCode, String addiData, String toCurrency,String langCode);
}
