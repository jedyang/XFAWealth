/**
 * 
 */
package com.fsll.wmes.fund.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.FundReturnEn;
import com.fsll.wmes.entity.FundReturnSc;
import com.fsll.wmes.entity.FundReturnTc;
import com.fsll.wmes.fund.vo.AipVO;
import com.fsll.wmes.fund.vo.ChartDataVO;
import com.fsll.wmes.fund.vo.FundIncomePercentageVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.FundMarketDataVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * 基金净值信息查询服务接口
 * @author michael
 * @date 2016-6-20
 */
public interface FundMarketService {
	
	/**
	 * 获取基金价格行情信息列表
	 * @param fundId 基金id
	 * @param period 时间段类型： day, week, month
	 * @param startDate 开始时间： yyyy-MM-dd
	 * @param endDate 结束时间： yyyy-MM-dd
	 * @param toCurrency 目标货币
	 * @param langCode 语言
	 * @return	GeneralDataVO	价格行情数据
	 */
	public List<FundMarketDataVO> findFundMarketList(String fundId, String period, String startDate, String endDate, String toCurrency, String langCode);

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
	 * @param period 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param addiData 额外返回的数据周期
	 * @param toCurrency 转换目标货币
	 * @param langCode 显示语言
	 * @return	<List>ChartDataVO	基金图表数据
	 */
	public ChartDataVO fundChartData(String fundId, String dataType, String cycle, String period, String addiData, String toCurrency,String langCode);

	/**
	 * 获取基金收益百分比
	 * @author wwluo
	 * @param period 分析时间段编码：1Mth：一个月,3Mth,6Mth,1Yr：一年 ,3Yr,5Yr,YTD
	 * @param type W：FundMarketWeek,M：FundMarketMonth,默认取 FundMarket
	 * @return	
	 */
	public List<FundIncomePercentageVO> getIncomePercentage(String fundId,
			String period,String type,Double weight,boolean ifWeight);

	/**
	 * 获取多只基金总收益百分比
	 * @author wwluo
	 * @param fundMarketDataVOs
	 * @return	
	 */
	public Map<Date, Object> getIncomePercentageTotal(
			List<FundMarketDataVO> fundMarketDataVOs);
	
	/**
	 * 计算周期内每次定投时间
	 * @author wwluo
	 * @param 
	 * @return	
	 */
	public List<String> caluAipTime(AipVO aip);
	
	/**
	 * 定投基金总收益率
	 * @author wwluo
	 * @param 
	 * @return	
	 */
	public Map<Date, Object> getAipIncomePercentageTotal(AipVO aip, Map<Date, Object> incomePercentages);
	
	/**
	 * 根据传入基金获取资产类别
	 * @author wwluo
	 * @param 
	 * @return	
	 */
	public List<Map<String,Object>> getAssetClassByfunds(String funds,String assetClass,String langCode);

}
